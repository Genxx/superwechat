package cn.gen.fulicenter.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cn.gen.fulicenter.R;

/**
 * Created by Administrator on 2016/6/16.
 */
public class FooterViewHolder extends RecyclerView.ViewHolder{
    public TextView tvFooter;
    public FooterViewHolder(View itemView) {
        super(itemView);
        tvFooter = (TextView) itemView.findViewById(R.id.tv_footer);
    }
}
