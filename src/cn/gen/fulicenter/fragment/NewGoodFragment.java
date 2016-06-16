package cn.gen.fulicenter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;

import java.util.ArrayList;

import cn.gen.fulicenter.D;
import cn.gen.fulicenter.I;
import cn.gen.fulicenter.R;
import cn.gen.fulicenter.activity.FuliCenterMainActivity;
import cn.gen.fulicenter.adapter.GoodAdapter;
import cn.gen.fulicenter.bean.NewGoodBean;
import cn.gen.fulicenter.data.ApiParams;
import cn.gen.fulicenter.data.GsonRequest;
import cn.gen.fulicenter.utils.Utils;

/**
 * Created by Administrator on 2016/6/15.
 */
public class NewGoodFragment extends Fragment {
    public static final String TAG = NewGoodFragment.class.getName();

    FuliCenterMainActivity mContext;
    ArrayList<NewGoodBean> mGoodList;
    GoodAdapter mAdapter;
    private int pageId=0;
    private int actioin=I.ACTION_DOWNLOAD;
    String path;
    
    /*下拉刷新控件*/
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    TextView mtvHint;
    GridLayoutManager mGidLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = (FuliCenterMainActivity) getActivity();
        View layout = View.inflate(mContext, R.layout.fragment_new_good,null);
        mGoodList = new ArrayList<NewGoodBean>();
        initView(layout);
        setListener();
        initData();
        return layout;
    }

    private void setListener() {
        setPullDownRefreshListener();
        setPullUpRefreshListener();
    }

    /**
     * 上拉刷新事件监听
     */
    private void setPullUpRefreshListener() {
        mRecyclerView.setOnScrollListener(
                new RecyclerView.OnScrollListener(){
                    int lastItemPosition;

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if(newState ==RecyclerView.SCROLL_STATE_IDLE&&
                                lastItemPosition == mAdapter.getItemCount()-1){
                            if(mAdapter.isMore()){
                                mSwipeRefreshLayout.setRefreshing(true);
                                actioin=I.ACTION_PULL_UP;
                                pageId+=I.PAGE_SIZE_DEFAULT;
                                getPath(pageId);
                                mContext.executeRequest(new GsonRequest<NewGoodBean[]>(path,
                                        NewGoodBean[].class,responseDownloadNewGoodListener(),
                                        mContext.errorListener()));
                            }
                        }
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        //获取最后列表项的下标
                        lastItemPosition = mGidLayoutManager.findLastVisibleItemPosition();
                        mSwipeRefreshLayout.setEnabled(mGidLayoutManager
                        .findFirstCompletelyVisibleItemPosition() == 0);
                    }
                }
        );
    }

    /**
     * 下拉刷新事件的监听
     */
    private void setPullDownRefreshListener() {
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener(){
                    @Override
                    public void onRefresh() {
                        mtvHint.setVisibility(View.VISIBLE);
                        pageId=0;
                        actioin = I.ACTION_PULL_DOWN;
                        getPath(pageId);
                        mContext.executeRequest(new GsonRequest<NewGoodBean[]>(path,
                                NewGoodBean[].class,responseDownloadNewGoodListener(),
                                mContext.errorListener()));
                    }
                }
        );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void initData() {
       getPath(pageId);
        mContext.executeRequest(new GsonRequest<NewGoodBean[]>(path,
                NewGoodBean[].class,responseDownloadNewGoodListener(),
                mContext.errorListener()));
    }

    private Response.Listener<NewGoodBean[]> responseDownloadNewGoodListener() {
        return new Response.Listener<NewGoodBean[]>(){

            @Override
            public void onResponse(NewGoodBean[] newGoodBeen) {
                if(newGoodBeen!=null){
                    mAdapter.setMore(true);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mtvHint.setVisibility(View.GONE);
                    mAdapter.setFooterText(getResources().getString(R.string.load_more));
                    //将数组转换为集合
                    ArrayList<NewGoodBean> list = Utils.array2List(newGoodBeen);
                    if(actioin==I.ACTION_DOWNLOAD||actioin==I.ACTION_PULL_DOWN){
                        mAdapter.initItems(list);
                    }else if(actioin == I.ACTION_PULL_UP){
                        mAdapter.addItems(list);
                    }
                    if(newGoodBeen.length<I.PAGE_SIZE_DEFAULT){
                        mAdapter.setMore(false);
                        mAdapter.setFooterText(getResources().getString(R.string.no_more));
                    }
                }
            }
        };
    }

    private String getPath(int pageId){
        try {
            String path = new ApiParams()
                    .with(I.NewAndBoutiqueGood.CAT_ID, I.CAT_ID +"")
                    .with(I.PAGE_ID, pageId + "")
                    .with(I.PAGE_SIZE, I.PAGE_SIZE_DEFAULT +"")
                    .getRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS);
            return path;
        } catch (Exception e) {
            e.printStackTrace();
        }  
        return null;
    }
    
    private void initView(View layout) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.sfl_newgood);
        mSwipeRefreshLayout.setColorSchemeColors(
                R.color.google_blue,
                R.color.google_green,
                R.color.google_red,
                R.color.google_yellow
        );
        mtvHint = (TextView) layout.findViewById(R.id.tv_refresh_hint);
        mGidLayoutManager = new GridLayoutManager(mContext,I.COLUM_NUM);
        mGidLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.rv_newgood);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mGidLayoutManager);
        mAdapter = new GoodAdapter(mContext,mGoodList);
        mRecyclerView.setAdapter(mAdapter);
    }
}
