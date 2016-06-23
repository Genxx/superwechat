package cn.gen.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import cn.gen.fulicenter.I;
import cn.gen.fulicenter.R;
import cn.gen.fulicenter.activity.BoutiqueChildActivity;
import cn.gen.fulicenter.bean.CartBean;
import cn.gen.fulicenter.bean.GoodDetailsBean;
import cn.gen.fulicenter.utils.ImageUtils;

/**
 * Created by Administrator on 2016/6/15.
 */
public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    ArrayList<CartBean> mCartList;

    CartItemViewHolder cartViewHolder;
    private boolean isMore;


    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public CartAdapter(Context mContext, ArrayList<CartBean> list) {
        this.mContext = mContext;
        this.mCartList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder holder = null;
        holder = new FooterViewHolder(inflater.inflate(R.layout.item_footer, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        cartViewHolder = (CartItemViewHolder) holder;
        final CartBean cart = mCartList.get(position);
        GoodDetailsBean goods = cart.getGoods();
        if(goods==null){
            return;
        }
        cartViewHolder.tvGoodName.setText(goods.getGoodsName());
        cartViewHolder.tvCartCount.setText(""+cart.getCount());
        cartViewHolder.mchkCart.setChecked(cart.isChecked());

        cartViewHolder.tvPrice.setText(goods.getRankPrice());
        ImageUtils.setNewGoodThumb(goods.getGoodsThumb(), cartViewHolder.iv);
    }

    @Override
    public int getItemCount() {
        return mCartList == null ? 0 : mCartList.size();
    }

    public void initItems(ArrayList<CartBean> list) {
        if (mCartList != null && !mCartList.isEmpty()) {
            mCartList.clear();
        }
        mCartList.addAll(list);
        notifyDataSetChanged();
    }

    public void addItems(ArrayList<CartBean> list) {
        mCartList.addAll(list);
        notifyDataSetChanged();
    }


    class CartItemViewHolder extends RecyclerView.ViewHolder {
        NetworkImageView iv;
        TextView tvCartCount;
        TextView tvGoodName;
        TextView tvPrice;
        CheckBox mchkCart;
        ImageView mivAdd;
        ImageView mivReduce;

        public CartItemViewHolder(View itemView) {
            super(itemView);
            iv = (NetworkImageView) itemView.findViewById(R.id.ivGoodThumb);
            tvCartCount = (TextView) itemView.findViewById(R.id.tvCartCount);
            tvGoodName = (TextView) itemView.findViewById(R.id.tvGoodsName);
            tvPrice = (TextView) itemView.findViewById(R.id.tvGoodprice);
            mchkCart = (CheckBox) itemView.findViewById(R.id.cheSelect);
            mivAdd = (ImageView) itemView.findViewById(R.id.ivAddCart);
            mivReduce = (ImageView) itemView.findViewById(R.id.ivReduceCart);
        }
    }
}
