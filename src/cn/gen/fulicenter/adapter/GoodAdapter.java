package cn.gen.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import cn.gen.fulicenter.I;
import cn.gen.fulicenter.R;
import cn.gen.fulicenter.bean.NewGoodBean;
import cn.gen.fulicenter.utils.ImageUtils;

/**
 * Created by Administrator on 2016/6/15.
 */
public class GoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    ArrayList<NewGoodBean> mGoodList;

    GoodItemViewHolder goodHolder;
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

    public void setMore(boolean more){
        isMore=more;
    }

    public GoodAdapter(Context mContext, ArrayList<NewGoodBean> mGoodList) {
        this.mContext = mContext;
        this.mGoodList = mGoodList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case I.TYPE_ITEM:
                holder = new GoodItemViewHolder(inflater.inflate(R.layout.item_new_good, parent, false));
                break;
            case I.TYPE_FOOTER:
                holder = new FooterViewHolder(inflater.inflate(R.layout.item_footer,parent,false));
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
            goodHolder = (GoodItemViewHolder) holder;
            final NewGoodBean good = mGoodList.get(position);
            goodHolder.tvGoodName.setText(good.getGoodsName());
            goodHolder.tvGoodPrice.setText(good.getCurrencyPrice());
            ImageUtils.setNewGoodThumb(good.getGoodsThumb(),goodHolder.nivThumb);
        }

    }

    @Override
    public int getItemCount() {
        return mGoodList == null?1:mGoodList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        } else {
            return I.TYPE_ITEM;
        }
    }

    public void initItems(ArrayList<NewGoodBean> list){
        if(mGoodList!=null && !mGoodList.isEmpty()){
            mGoodList.clear();
        }
        mGoodList.addAll(list);
        notifyDataSetChanged();
    }
    public void addItems(ArrayList<NewGoodBean> list){
        mGoodList.addAll(list);
        notifyDataSetChanged();
    }

    class GoodItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutGood;
        NetworkImageView nivThumb;
        TextView tvGoodName;
        TextView tvGoodPrice;

        public GoodItemViewHolder(View itemView) {
            super(itemView);
            layoutGood = (LinearLayout) itemView.findViewById(R.id.layout_good);
            nivThumb = (NetworkImageView) itemView.findViewById(R.id.niv_good_thumb);
            tvGoodName = (TextView) itemView.findViewById(R.id.tv_good_name);
            tvGoodPrice = (TextView) itemView.findViewById(R.id.tv_good_price);
        }
    }
}
