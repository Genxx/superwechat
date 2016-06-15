package cn.gen.fulicenter.task;

import android.content.Context;
import android.content.Intent;

import com.android.volley.Response;

import java.util.ArrayList;

import cn.gen.fulicenter.I;
import cn.gen.fulicenter.SuperWeChatApplication;
import cn.gen.fulicenter.activity.BaseActivity;
import cn.gen.fulicenter.bean.Group;
import cn.gen.fulicenter.data.ApiParams;
import cn.gen.fulicenter.data.GsonRequest;
import cn.gen.fulicenter.utils.Utils;

/**
 * Created by Administrator on 2016/5/23.
 */
public class DownloadPublicGroupTask extends BaseActivity {
    //DownloadPublicGroupTask,I.REQUEST_FIND_PUBLIC_GROUPS,update_public_group
    /**
     * 客户端发送的下载公开裙请求
     */
    private static final String TAG = DownloadPublicGroupTask.class.getName();
    Context mContext;
    String username;
    String path;
    int pageId;
    int pageSize;

    public DownloadPublicGroupTask(Context mContext, String username,int pageId,int pageSize) {
        this.mContext = mContext;
        this.username = username;
        this.pageId = pageId;
        this.pageSize = pageSize;
        initPath();
    }

    private void initPath() {
        try {
            path = new ApiParams()
                    .with(I.Contact.USER_NAME, username)
                    .with(I.PAGE_ID,pageId+"")
                    .with(I.PAGE_SIZE,pageSize+"")
                    .getRequestUrl(I.REQUEST_FIND_PUBLIC_GROUPS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void execute() {
        executeRequest(new GsonRequest<Group[]>(path, Group[].class,
                responDownloadPublicGroupTaskListener(), errorListener()));
    }

    private Response.Listener<Group[]> responDownloadPublicGroupTaskListener() {
        return new Response.Listener<Group[]>() {
            @Override
            public void onResponse(Group[] groups) {
                if (groups != null) {
                    ArrayList<Group> publicGroupList =
                            SuperWeChatApplication.getInstance().getPublicGroupList();
                    //将数组转化为列表
                    ArrayList<Group> list = Utils.array2List(groups);
                    for (Group g : list) {
                        if (!publicGroupList.contains(g)) {
                            publicGroupList.add(g);
                        }
                    }
                    mContext.sendStickyBroadcast(new Intent("update_public_group"));
                }
            }
        };
    }
}
