package cn.gen.fulicenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Response;

import cn.gen.fulicenter.FuliCenterApplication;
import cn.gen.fulicenter.I;
import cn.gen.fulicenter.activity.BaseActivity;
import cn.gen.fulicenter.bean.MessageBean;
import cn.gen.fulicenter.bean.User;
import cn.gen.fulicenter.data.ApiParams;
import cn.gen.fulicenter.data.GsonRequest;

/**
 * Created by Administrator on 2016/6/20.
 */
public class DownloadCollectCountTask extends BaseActivity{
    Context mContext;
    String path;

    public DownloadCollectCountTask(Context mContext) {
        this.mContext = mContext;
        initPath();
    }

    private void initPath() {
        try {
            User user = FuliCenterApplication.getInstance().getUser();
            if (user != null) {
                path = new ApiParams()
                        .with(I.User.USER_NAME, user.getMUserName())
                        .getRequestUrl(I.REQUEST_FIND_COLLECT_COUNT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void execute() {
        if (path == null || path.isEmpty()) return;
        executeRequest(new GsonRequest<MessageBean>(path,MessageBean.class,
                responseDownloadCollectCountListener(),errorListener()));
    }

    private Response.Listener<MessageBean> responseDownloadCollectCountListener() {
        return new Response.Listener<MessageBean>() {
            @Override
            public void onResponse(MessageBean messageBean) {
                if (messageBean.isSuccess()) {
                    String count = messageBean.getMsg();
                    Log.e("main", "count:" + count);
                    FuliCenterApplication.getInstance().setCollectCount(Integer.parseInt(count));

                } else {
                    Log.e("main", "count=0");
                    FuliCenterApplication.getInstance().setCollectCount(0);
                }
                Intent intent = new Intent("update_collect_count");
                mContext.sendStickyBroadcast(intent);
            }
        };
    }
}
