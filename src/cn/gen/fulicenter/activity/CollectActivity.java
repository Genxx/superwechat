package cn.gen.fulicenter.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;

import java.util.ArrayList;

import cn.gen.fulicenter.FuliCenterApplication;
import cn.gen.fulicenter.I;
import cn.gen.fulicenter.R;
import cn.gen.fulicenter.adapter.CollectAdapter;
import cn.gen.fulicenter.bean.CollectBean;
import cn.gen.fulicenter.data.ApiParams;
import cn.gen.fulicenter.data.GsonRequest;
import cn.gen.fulicenter.utils.DisplayUtils;
import cn.gen.fulicenter.utils.Utils;

/**
 * Created by Administrator on 2016/6/15.
 */
public class CollectActivity extends BaseActivity {
    public static final String TAG = CollectActivity.class.getName();

    CollectActivity mContext;
    ArrayList<CollectBean> mCollectList;
    CollectAdapter mAdapter;
    private int pageId=0;
    private int actioin=I.ACTION_DOWNLOAD;
    String path;
    
    /*下拉刷新控件*/
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    TextView mtvHint;
    GridLayoutManager mGidLayoutManager;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_collect);
        mContext = this;
        mCollectList = new ArrayList<CollectBean>();
        initView();
        setListener();
        initData();
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
                                mContext.executeRequest(new GsonRequest<CollectBean[]>(path,
                                        CollectBean[].class,responseDownloadCollectListener(),
                                        mContext.errorListener()));
                            }
                        }
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        //获取最后列表项的下标
                        lastItemPosition = mGidLayoutManager.findLastVisibleItemPosition();
                        //解决RecyclerView和SeipeRefreshLayout公用存在的bug
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
                        mContext.executeRequest(new GsonRequest<CollectBean[]>(path,
                                CollectBean[].class,responseDownloadCollectListener(),
                                mContext.errorListener()));
                    }
                }
        );
    }


    private void initData() {
        try {
            getPath(pageId);
            mContext.executeRequest(new GsonRequest<CollectBean[]>(path,
                    CollectBean[].class,responseDownloadCollectListener(),
                    mContext.errorListener()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Response.Listener<CollectBean[]> responseDownloadCollectListener() {
        return new Response.Listener<CollectBean[]>(){
            @Override
            public void onResponse(CollectBean[] collects) {
                if(collects!=null){
                    mAdapter.setMore(true);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mtvHint.setVisibility(View.GONE);
                    mAdapter.setFooterText(getResources().getString(R.string.load_more));
                    //将数组转换为集合
                    ArrayList<CollectBean> list = Utils.array2List(collects);
                    if(actioin==I.ACTION_DOWNLOAD||actioin==I.ACTION_PULL_DOWN){
                        mAdapter.initItems(list);
                    }else if(actioin == I.ACTION_PULL_UP){
                        mAdapter.addItems(list);
                    }
                    if(collects.length<I.PAGE_SIZE_DEFAULT){
                        mAdapter.setMore(false);
                        mAdapter.setFooterText(getResources().getString(R.string.no_more));
                    }
                }
            }
        };
    }

    private String getPath(int pageId){
        try {
            String userName = FuliCenterApplication.getInstance().getUserName();
            path = new ApiParams()
                    .with(I.Collect.USER_NAME, userName)
                    .with(I.PAGE_ID, pageId + "")
                    .with(I.PAGE_SIZE, I.PAGE_SIZE_DEFAULT +"")
                    .getRequestUrl(I.REQUEST_FIND_COLLECTS);
            Log.i("main",path);
            return path;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private void initView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.sfl_collect);
        mSwipeRefreshLayout.setColorSchemeColors(
                R.color.google_blue,
                R.color.google_green,
                R.color.google_red,
                R.color.google_yellow
        );
        mtvHint = (TextView) findViewById(R.id.tv_refresh_hint);
        mGidLayoutManager = new GridLayoutManager(mContext,I.COLUM_NUM);
        mGidLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_collect);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mGidLayoutManager);
        mAdapter = new CollectAdapter(mContext,mCollectList);
        mRecyclerView.setAdapter(mAdapter);
        DisplayUtils.initBackWithTitle(mContext,"收藏的宝贝");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
