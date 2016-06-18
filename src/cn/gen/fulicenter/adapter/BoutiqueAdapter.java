package cn.gen.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import cn.gen.fulicenter.I;
import cn.gen.fulicenter.R;
import cn.gen.fulicenter.bean.BoutiqueBean;
import cn.gen.fulicenter.utils.ImageUtils;

/**
 * Created by Administrator on 2016/6/15.
 */
public class BoutiqueAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    ArrayList<BoutiqueBean> mBoutiqueList;

    GoodItemViewHolder boutiqueViewHolder;
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

    public BoutiqueAdapter(Context mContext, ArrayList<BoutiqueBean> list) {
        this.mContext = mContext;
        this.mBoutiqueList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case I.TYPE_ITEM:
                holder = new GoodItemViewHolder(inflater.inflate(R.layout.item_boutique, parent, false));
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
        if (holder instanceof GoodItemViewHolder) {
            boutiqueViewHolder = (GoodItemViewHolder) holder;
            final BoutiqueBean boutiqueBean = mBoutiqueList.get(position);
            boutiqueViewHolder.tvDescription.setText(boutiqueBean.getDescription());
            boutiqueViewHolder.tvName.setText(boutiqueBean.getName());
            boutiqueViewHolder.tvPrice.setText(boutiqueBean.getTitle());
            ImageUtils.setNewGoodThumb(boutiqueBean.getImageurl(), boutiqueViewHolder.nivThumb);
        }
    }

    @Override
    public int getItemCount() {
        return mBoutiqueList == null ? 1 : mBoutiqueList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        } else {
            return I.TYPE_ITEM;
        }
    }

    public void initItems(ArrayList<BoutiqueBean> list) {
        if (mBoutiqueList != null && !mBoutiqueList.isEmpty()) {
            mBoutiqueList.clear();
        }
        mBoutiqueList.addAll(list);
        notifyDataSetChanged();
    }

    public void addItems(ArrayList<BoutiqueBean> list) {
        mBoutiqueList.addAll(list);
        notifyDataSetChanged();
    }


    class GoodItemViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layoutItem;
        NetworkImageView nivThumb;
        TextView tvDescription;
        TextView tvName;
        TextView tvPrice;

        public GoodItemViewHolder(View itemView) {
            super(itemView);
            layoutItem = (RelativeLayout) itemView.findViewById(R.id.layout_boutique_item);
            nivThumb = (NetworkImageView) itemView.findViewById(R.id.ivBoutqueImg);
            tvDescription = (TextView) itemView.findViewById(R.id.tvBoutiqueDescription);
            tvName = (TextView) itemView.findViewById(R.id.tvBoutiqueName);
            tvPrice = (TextView) itemView.findViewById(R.id.tvBoutiqueTitle);
        }
    }
}
