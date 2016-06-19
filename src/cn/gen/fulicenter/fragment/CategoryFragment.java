package cn.gen.fulicenter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.android.volley.Response;

import java.util.ArrayList;

import cn.gen.fulicenter.I;
import cn.gen.fulicenter.R;
import cn.gen.fulicenter.activity.FuliCenterMainActivity;
import cn.gen.fulicenter.adapter.CategoryAdapter;
import cn.gen.fulicenter.bean.CategoryChildBean;
import cn.gen.fulicenter.bean.CategoryGroupBean;
import cn.gen.fulicenter.data.ApiParams;
import cn.gen.fulicenter.data.GsonRequest;
import cn.gen.fulicenter.utils.Utils;

/**
 * Created by Administrator on 2016/6/19.
 */
public class CategoryFragment extends Fragment {
    FuliCenterMainActivity mContext;
    ArrayList<CategoryGroupBean> mGroupList;
    ArrayList<ArrayList<CategoryChildBean>> mChildList;
    ExpandableListView melvCategory;
    CategoryAdapter mAdapter;

    /*小类匹配大类的辅助*/
    int groupCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = (FuliCenterMainActivity) getActivity();
        View layout = inflater.inflate(R.layout.fragment_category, container, false);
        initView(layout);
        initData();
        return layout;
    }

    private void initData() {
        mGroupList = new ArrayList<CategoryGroupBean>();
        mChildList = new ArrayList<ArrayList<CategoryChildBean>>();
        try {
            String path = new ApiParams()
                    .getRequestUrl(I.REQUEST_FIND_CATEGORY_GROUP);
            mContext.executeRequest(new GsonRequest<CategoryGroupBean[]>(path,
                    CategoryGroupBean[].class,
                    responDownCategoryListListener(), mContext.errorListener()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Response.Listener<CategoryGroupBean[]> responDownCategoryListListener() {
        return new Response.Listener<CategoryGroupBean[]>() {
            @Override
            public void onResponse(CategoryGroupBean[] categoryGroupBeen) {
                if (categoryGroupBeen != null) {
                    try {
                        mGroupList = Utils.array2List(categoryGroupBeen);
                        int i = 0;
                        for (CategoryGroupBean group : mGroupList) {
                            mChildList.add(i, new ArrayList<CategoryChildBean>());
                            String path = new ApiParams()
                                    .with(I.CategoryChild.PARENT_ID, group.getId() + "")
                                    .with(I.PAGE_ID, "0")
                                    .with(I.PAGE_SIZE, I.PAGE_SIZE_DEFAULT + "")
                                    .getRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN);
                            mContext.executeRequest(new GsonRequest<CategoryChildBean[]>(path,
                                    CategoryChildBean[].class,
                                    responDownCategoryChildListListener(i),
                                    mContext.errorListener()));
                            i++;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };

    }

    private Response.Listener<CategoryChildBean[]> responDownCategoryChildListListener(final int i) {
        return new Response.Listener<CategoryChildBean[]>() {
            @Override
            public void onResponse(CategoryChildBean[] categoryGroupBeen) {
                groupCount++;
                if(categoryGroupBeen!=null){
                    ArrayList<CategoryChildBean> childList = Utils.array2List(categoryGroupBeen);
                   if(childList!=null){
                       mChildList.set(i,childList);
                   }
                }
                if (mGroupList.size() == groupCount){
                    mAdapter.addItems(mGroupList,mChildList);
                }
            }
        };
    }


    private void initView(View layout) {
        melvCategory = (ExpandableListView) layout.findViewById(R.id.elvcategory);
        melvCategory.setGroupIndicator(null);
        mGroupList = new ArrayList<CategoryGroupBean>();
        mChildList = new ArrayList<ArrayList<CategoryChildBean>>();
        mAdapter = new CategoryAdapter(mContext, mGroupList, mChildList);
        melvCategory.setAdapter(mAdapter);
    }
}
