package cn.gen.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import cn.gen.fulicenter.D;
import cn.gen.fulicenter.FuliCenterApplication;
import cn.gen.fulicenter.I;
import cn.gen.fulicenter.R;
import cn.gen.fulicenter.activity.CollectActivity;
import cn.gen.fulicenter.activity.GoodDetailActivity;
import cn.gen.fulicenter.bean.CollectBean;
import cn.gen.fulicenter.bean.MessageBean;
import cn.gen.fulicenter.data.ApiParams;
import cn.gen.fulicenter.data.GsonRequest;
import cn.gen.fulicenter.task.DownloadCollectCountTask;
import cn.gen.fulicenter.utils.ImageUtils;

/**
 * Created by Administrator on 2016/6/15.
 */
public class CollectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    CollectActivity mContext;
    ArrayList<CollectBean> mCollectList;

    CollectItemViewHolder CollectHolder;
    FooterViewHolder footerViewHolder;

    private String footerText;
    private boolean isMore;

    public boolean isMore() {
        return isMore;
    }

    public void setFooterText(String footerText) {
        this.footerText = footerText;
        notifyDataSetChanged();
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public CollectAdapter(Context mContext, ArrayList<CollectBean> list) {
        this.mContext = (CollectActivity) mContext;
        this.mCollectList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case I.TYPE_ITEM:
                holder = new CollectItemViewHolder(inflater.inflate(R.layout.item_collect, parent, false));
                break;
            case I.TYPE_FOOTER:
                holder = new FooterViewHolder(inflater.inflate(R.layout.item_footer, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterViewHolder) {
            footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.tvFooter.setText(footerText);
            footerViewHolder.tvFooter.setVisibility(View.VISIBLE);
        }
        if (holder instanceof CollectItemViewHolder) {
            CollectHolder = (CollectItemViewHolder) holder;
            final CollectBean collect = mCollectList.get(position);
            CollectHolder.tvCollectName.setText(collect.getGoodsName());
            ImageUtils.setNewGoodThumb(collect.getGoodsThumb(), CollectHolder.nivThumb);

            CollectHolder.layoutCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, GoodDetailActivity.class)
                            .putExtra(D.NewGood.KEY_GOODS_ID, collect.getGoodsId()));
                }
            });
            CollectHolder.ivDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String path = new ApiParams()
                                .with(I.Collect.GOODS_ID, collect.getGoodsId() + "")
                                .with(I.Collect.USER_NAME, FuliCenterApplication.getInstance().getUserName())
                                .getRequestUrl(I.REQUEST_DELETE_COLLECT);
                        mContext.executeRequest(new GsonRequest<MessageBean>(path, MessageBean.class,
                                responseDelCollectListener(collect), mContext.errorListener()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }

    }

    private Response.Listener<MessageBean> responseDelCollectListener(final CollectBean collect) {
        return new Response.Listener<MessageBean>() {
            @Override
            public void onResponse(MessageBean messageBean) {
                if(messageBean.isSuccess()){
                    mCollectList.remove(collect);
                    notifyDataSetChanged();
                    new DownloadCollectCountTask(mContext).execute();
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        return mCollectList == null ? 1 : mCollectList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        } else {
            return I.TYPE_ITEM;
        }
    }

    public void initItems(ArrayList<CollectBean> list) {
        if (mCollectList != null && !mCollectList.isEmpty()) {
            mCollectList.clear();
        }
        mCollectList.addAll(list);
        notifyDataSetChanged();
    }

    public void addItems(ArrayList<CollectBean> list) {
        mCollectList.addAll(list);
        notifyDataSetChanged();
    }

    class CollectItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutCollect;
        NetworkImageView nivThumb;
        TextView tvCollectName;
        ImageView ivDel;

        public CollectItemViewHolder(View itemView) {
            super(itemView);
            layoutCollect = (LinearLayout) itemView.findViewById(R.id.layout_collect);
            nivThumb = (NetworkImageView) itemView.findViewById(R.id.niv_collect_thumb);
            tvCollectName = (TextView) itemView.findViewById(R.id.tv_collect_name);
            ivDel = (ImageView) itemView.findViewById(R.id.iv_del_collect);
        }
    }
}
