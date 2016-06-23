package cn.gen.fulicenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Response;

import java.util.ArrayList;
import java.util.HashMap;

import cn.gen.fulicenter.FuliCenterApplication;
import cn.gen.fulicenter.I;
import cn.gen.fulicenter.activity.BaseActivity;
import cn.gen.fulicenter.activity.GoodDetailActivity;
import cn.gen.fulicenter.bean.CartBean;
import cn.gen.fulicenter.bean.GoodDetailsBean;
import cn.gen.fulicenter.data.ApiParams;
import cn.gen.fulicenter.data.GsonRequest;
import cn.gen.fulicenter.utils.Utils;

/**
 * Created by Administrator on 2016/5/23.
 */
public class DownloadCartList extends BaseActivity {
    //DownloadCartBeanList,I.REQUEST_DOWNLOAD_CartBean_ALL_LIST,update_CartBean_list
    /**
     * 客户端发送的下载联系人所有集合请求
     */
    Context mContext;
    String username;
    int pageId;
    int pageSize;
    String path;
    int listSize;
    ArrayList<CartBean> list;

    public DownloadCartList(Context mContext, String username, int pageId, int pageSize) {
        this.mContext = mContext;
        this.username = username;
        this.pageId = pageId;
        this.pageSize = pageSize;
        initpath();
    }

    private void initpath() {
        try {
            path = new ApiParams()
                    .with(I.Cart.USER_NAME, username)
                    .with(I.PAGE_ID, pageId + "")
                    .with(I.PAGE_SIZE, pageSize + "")
                    .getRequestUrl(I.REQUEST_FIND_CARTS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void execute() {
        executeRequest(new GsonRequest<CartBean[]>(path, CartBean[].class,
                responDownloadCartListTaskListener(), errorListener()));
    }

    private Response.Listener<CartBean[]> responDownloadCartListTaskListener() {
        return new Response.Listener<CartBean[]>() {
            @Override
            public void onResponse(CartBean[] response) {
                if (response != null) {
                    list = Utils.array2List(response);
                    try {
                        for (CartBean cart : list) {
                            path = new ApiParams()
                                    .with(I.CategoryGood.GOODS_ID, cart.getGoodsId() + "")
                                    .getRequestUrl(I.REQUEST_FIND_GOOD_DETAILS);
                            Log.e("main", "responDownloadCartListTaskListener:path=" + path);
                            executeRequest(new GsonRequest<GoodDetailsBean>(path, GoodDetailsBean.class,
                                    responDownloadGoodDetailListener(cart), errorListener()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    private Response.Listener<GoodDetailsBean> responDownloadGoodDetailListener(final CartBean cart) {
        return new Response.Listener<GoodDetailsBean>() {
            @Override
            public void onResponse(GoodDetailsBean goodDetailsBean) {
                listSize++;
                if(goodDetailsBean!=null){
                    cart.setGoods(goodDetailsBean);
                    ArrayList<CartBean> cartList = FuliCenterApplication.getInstance().getCartList();
                    if(!cartList.contains(cart)){
                        cartList.add(cart);
                    }
                }
                if(listSize==list.size()){
                    mContext.sendStickyBroadcast(new Intent("update_Cart_list"));
                }
            }
        };
    }
}
