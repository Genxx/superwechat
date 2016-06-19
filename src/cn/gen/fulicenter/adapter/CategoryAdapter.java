package cn.gen.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import cn.gen.fulicenter.D;
import cn.gen.fulicenter.I;
import cn.gen.fulicenter.R;
import cn.gen.fulicenter.activity.CategoryChildActivity;
import cn.gen.fulicenter.bean.CategoryChildBean;
import cn.gen.fulicenter.bean.CategoryGroupBean;
import cn.gen.fulicenter.utils.ImageUtils;

/**
 * Created by Administrator on 2016/6/19.
 */
public class CategoryAdapter extends BaseExpandableListAdapter{
    Context mContext;
    ArrayList<CategoryGroupBean> mGroupList;
    ArrayList<ArrayList<CategoryChildBean>> mChildList;

    public CategoryAdapter(Context mContext, ArrayList<CategoryGroupBean> mGroupList,
                           ArrayList<ArrayList<CategoryChildBean>> mChildList) {
        this.mContext = mContext;
        this.mGroupList = mGroupList;
        this.mChildList = mChildList;
    }

    @Override
    public int getGroupCount() {
        return mGroupList==null?0:mGroupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildList==null||mChildList.get(groupPosition)==null?0:mChildList.get(groupPosition).size();
    }

    @Override
    public CategoryGroupBean getGroup(int groupPosition) {
        return mGroupList.get(groupPosition);
    }

    @Override
    public CategoryChildBean getChild(int groupPosition, int childPosition) {
        return mChildList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View layout, ViewGroup parent) {
        ViewGroupHolder holder = null;
        if (layout==null){
            layout = View.inflate(mContext, R.layout.item_category_group,null);
            holder = new ViewGroupHolder();
            holder.ivIndicator = (ImageView) layout.findViewById(R.id.ivIndicator);
            holder.ivGroupThumb = (NetworkImageView) layout.findViewById(R.id.ivGroupThumb);
            holder.tvGroupName = (TextView) layout.findViewById(R.id.tvgroupName);
            layout.setTag(holder);
        }else {
            holder = (ViewGroupHolder) layout.getTag();
        }
        CategoryGroupBean group = getGroup(groupPosition);
        holder.tvGroupName.setText(group.getName());
        String imgUrl = group.getImageUrl();
        String url = I.DOWNLOAD_DOWNLOAD_CATEGORY_GROUP_IMAGE_URL+imgUrl;
        ImageUtils.setThumb(url,holder.ivGroupThumb);
        if (isExpanded){
            holder.ivIndicator.setImageResource(R.drawable.expand_off);
        }else {
            holder.ivIndicator.setImageResource(R.drawable.expand_on);
        }
        return layout;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isExpanded, View layout, ViewGroup parent) {
        ViewChildHolder holder=null;
        if(layout==null){
            holder = new ViewChildHolder();
            layout = View.inflate(mContext,R.layout.item_category_child,null);
            holder.layoutChild = (RelativeLayout) layout.findViewById(R.id.layout_category_child);
            holder.ivCategoryCHildThumb = (NetworkImageView) layout.findViewById(R.id.ivCategoryChildThumb);
            holder.tvCategoryChildName = (TextView) layout.findViewById(R.id.tvCategoryChildName);
            layout.setTag(holder);
        }else {
            holder = (ViewChildHolder) layout.getTag();
        }
        final CategoryChildBean child = getChild(groupPosition,childPosition);
        String name = child.getName();
        holder.tvCategoryChildName.setText(name);

        String imgUrl =child.getImageUrl();
        String url = I.DOWNLOAD_DOWNLOAD_CATEGORY_CHILD_IMAGE_URL+imgUrl;
        ImageUtils.setThumb(url,holder.ivCategoryCHildThumb);
        holder.layoutChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, CategoryChildActivity.class)
                        .putExtra(I.CategoryChild.CAT_ID,child.getId())
                        .putExtra(I.CategoryGroup.NAME,mGroupList.get(groupPosition).getName())
                        .putExtra("childList",(ArrayList<CategoryChildBean>)mChildList.get(groupPosition)));
            }
        });
        return layout;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class ViewGroupHolder {
        NetworkImageView ivGroupThumb;
        TextView tvGroupName;
        ImageView ivIndicator;
    }
    class ViewChildHolder{
        RelativeLayout layoutChild;
        NetworkImageView ivCategoryCHildThumb;
        TextView tvCategoryChildName;
    }

    public void addItems(ArrayList<CategoryGroupBean> groupList,
                         ArrayList<ArrayList<CategoryChildBean>> childList){
        this.mGroupList.addAll(groupList);
        this.mChildList.addAll(childList);
        notifyDataSetChanged();
    }
}
