package cn.gen.fulicenter.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.toolbox.NetworkImageView;

import cn.gen.fulicenter.D;
import cn.gen.fulicenter.FuliCenterApplication;
import cn.gen.fulicenter.I;
import cn.gen.fulicenter.R;
import cn.gen.fulicenter.bean.AlbumBean;
import cn.gen.fulicenter.bean.GoodDetailsBean;
import cn.gen.fulicenter.bean.MessageBean;
import cn.gen.fulicenter.bean.User;
import cn.gen.fulicenter.data.ApiParams;
import cn.gen.fulicenter.data.GsonRequest;
import cn.gen.fulicenter.task.DownloadCollectCountTask;
import cn.gen.fulicenter.utils.DisplayUtils;
import cn.gen.fulicenter.utils.ImageUtils;
import cn.gen.fulicenter.utils.Utils;
import cn.gen.fulicenter.view.FlowIndicator;
import cn.gen.fulicenter.view.SlideAutoLoopView;


/**
 * Created by Administrator on 2016/6/16.
 */
public class GoodDetailActivity extends BaseActivity {
    Context mContext;
    GoodDetailsBean mGood;
    int mGoodsId;


    SlideAutoLoopView mSlideAutoLoopView;
    FlowIndicator mFlowIndicator;
    /*  * 显示颜色的容器布局
      */
    LinearLayout mLayoutColors;
    ImageView mivCollect;
    ImageView mivAddCart;
    ImageView mivShare;
    TextView mtvCartCount;
    TextView tvGoodName;
    TextView tvGoodEnglishName;
    TextView tvShopPrice;
    TextView tvCurrencyPrice;
    WebView wvGoodBrief;

    /**
     * 当前颜色值
     **/
    int mCurrentColor;

    boolean isCollect;
    int actionCollect;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_good_details);
        mContext = this;
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        setCollectClickListener();
    }

    private void setCollectClickListener() {
        mivCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = FuliCenterApplication.getInstance().getUser();
                if(user==null){
                    startActivity(new Intent(GoodDetailActivity.this,LoginActivity.class));
                }else {
                    try {
                        String path;
                        if(isCollect){
                            actionCollect = I.ACTION_DEL_COLECT;
                            path = new ApiParams()
                                    .with(I.Collect.USER_NAME,user.getMUserName())
                                    .with(I.Collect.GOODS_ID,mGoodsId+"")
                                    .getRequestUrl(I.REQUEST_DELETE_COLLECT);
                        }else {
                            actionCollect = I.ACTION_ADD_CONLLECT;
                            path = new ApiParams()
                                    .with(I.Collect.USER_NAME,user.getMUserName())
                                    .with(I.Collect.GOODS_ID,mGoodsId+"")
                                    .with(I.Collect.GOODS_NAME,mGood.getGoodsName())
                                    .with(I.Collect.GOODS_ENGLISH_NAME,mGood.getGoodsEnglishName())
                                    .with(I.Collect.GOODS_THUMB,mGood.getGoodsThumb())
                                    .with(I.Collect.ADD_TIME,mGood.getAddTime()+"")
                                    .getRequestUrl(I.REQUEST_ADD_COLLECT);
                        }
                        Log.e("main", "OnClickListener Path:" + path);
                        executeRequest(new GsonRequest<MessageBean>(path,MessageBean.class,
                                responseSetCollectListener(),errorListener()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private Response.Listener<MessageBean> responseSetCollectListener() {
        return new Response.Listener<MessageBean>() {
            @Override
            public void onResponse(MessageBean messageBean) {
                 if(messageBean.isSuccess()){
                     if(actionCollect==I.ACTION_ADD_CONLLECT){
                         isCollect = true;
                         mivCollect.setImageResource(R.drawable.bg_collect_out);
                     }else {
                         isCollect = false;
                         mivCollect.setImageResource(R.drawable.bg_collect_in);
                     }
                     new DownloadCollectCountTask(mContext).execute();

                 }
                Utils.showToast(mContext,messageBean.getMsg(),Toast.LENGTH_LONG);
            }
        };
    }

    private void initData() {
        mGoodsId = getIntent().getIntExtra(D.GoodDetails.KEY_GOODS_ID, 0);
        try {
            String path = new ApiParams()
                    .with(I.CategoryGood.GOODS_ID, mGoodsId + "")
                    .getRequestUrl(I.REQUEST_FIND_GOOD_DETAILS);
            executeRequest(new GsonRequest<GoodDetailsBean>(path, GoodDetailsBean.class,
                    responseDownloadGoodDetailsListener(), errorListener()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Response.Listener<GoodDetailsBean> responseDownloadGoodDetailsListener() {
        return new Response.Listener<GoodDetailsBean>() {
            @Override
            public void onResponse(GoodDetailsBean goodDetailsBean) {
                if (goodDetailsBean != null) {
                    mGood = goodDetailsBean;
                    //设置商品名称，价格，webVIEW的简介
                    DisplayUtils.initBackWithTitle(GoodDetailActivity.this, getResources().getString(R.string.title_good_details));
                    tvCurrencyPrice.setText(mGood.getCurrencyPrice());
                    tvGoodEnglishName.setText(mGood.getGoodsEnglishName());
                    tvGoodName.setText(mGood.getGoodsName());
                    wvGoodBrief.loadDataWithBaseURL(null, mGood.getGoodsBrief().trim(), D.TEXT_HTML, D.UTF_8, null);

                    //初始化颜色面板
                    initColorBanner();
                } else {
                    Utils.showToast(mContext, "商品详情下载失败", Toast.LENGTH_SHORT);
                    finish();
                }
            }
        };
    }

    private void initColorBanner() {
        updateColor(0);
        for (int i = 0; i < mGood.getProperties().length; i++) {
            mCurrentColor = i;
            View layout = View.inflate(mContext, R.layout.layout_property_color, null);
            final NetworkImageView ivColor = (NetworkImageView) layout.findViewById(R.id.ivColorItem);
            String colorImg = mGood.getProperties()[i].getColorImg();
            if (colorImg.isEmpty()) {
                continue;
            }
            ImageUtils.setGoodDetailThumb(colorImg, ivColor);
            mLayoutColors.addView(layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateColor(mCurrentColor);
                }
            });
        }
    }

    /**
     * 设置指定属性的图片轮播
     *
     * @param i
     */
    private void updateColor(int i) {
        AlbumBean[] albums = mGood.getProperties()[i].getAlbums();
        String[] albumImgUrl = new String[albums.length];
        for (int j = 0; j < albumImgUrl.length; j++) {
            albumImgUrl[j] = albums[j].getImgUrl();

        }
        mSlideAutoLoopView.startPlayLoop(mFlowIndicator, albumImgUrl, albumImgUrl.length);
    }


    private void initView() {
        mivCollect = (ImageView) findViewById(R.id.ivCollect);
        mivAddCart = (ImageView) findViewById(R.id.ivAddCart);
        mivShare = (ImageView) findViewById(R.id.ivShare);
        mtvCartCount = (TextView) findViewById(R.id.tvCartCount);

        mSlideAutoLoopView = (SlideAutoLoopView) findViewById(R.id.salv);
        mFlowIndicator = (FlowIndicator) findViewById(R.id.indicator);
        mLayoutColors = (LinearLayout) findViewById(R.id.layoutColorSelector);
        tvCurrencyPrice = (TextView) findViewById(R.id.tvCurrencyPrice);
        tvGoodEnglishName = (TextView) findViewById(R.id.tvGoodEnglishName);
        tvGoodName = (TextView) findViewById(R.id.tvGoodName);
        wvGoodBrief = (WebView) findViewById(R.id.wvGoodBrief);
        WebSettings settings = wvGoodBrief.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setBuiltInZoomControls(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCollectStatus();
    }

    private void initCollectStatus() {
        User user = FuliCenterApplication.getInstance().getUser();
        if (user != null) {
            try {
                String path = new ApiParams()
                        .with(I.Collect.USER_NAME, FuliCenterApplication.getInstance().getUserName())
                        .with(I.Collect.GOODS_ID, mGoodsId + "")
                        .getRequestUrl(I.REQUEST_IS_COLLECT);
                executeRequest(new GsonRequest<MessageBean>(path, MessageBean.class,
                        responseIsCollectListener(), errorListener()));
                Log.e("main", "InitCollectStatus path:" + path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            isCollect = false;
            mivCollect.setImageResource(R.drawable.bg_collect_in);
        }
    }

    private Response.Listener<MessageBean> responseIsCollectListener() {
        return new Response.Listener<MessageBean>() {
            @Override
            public void onResponse(MessageBean messageBean) {
                if (messageBean.isSuccess()) {
                    isCollect =true;
                    mivCollect.setImageResource(R.drawable.bg_collect_out);
                } else {
                    isCollect=false;
                    mivCollect.setImageResource(R.drawable.bg_collect_in);
                }
            }
        };
    }
}
