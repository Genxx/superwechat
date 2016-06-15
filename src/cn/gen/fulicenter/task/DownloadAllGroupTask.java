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
public class DownloadAllGroupTask extends BaseActivity{
    //DownloadAllGroupTask,I.REQUEST_DOWNLOAD_GROUPS,update_group_list
    /** 客户端发送的下载群组请求 */
    private static final String TAG = DownloadAllGroupTask.class.getName();
    Context mContext;
    String username;
    String path;

    public DownloadAllGroupTask(Context mContext, String username) {
        this.mContext = mContext;
        this.username = username;
        initpath();
    }

    private void initpath() {
        try {
            path = new ApiParams()
                    .with(I.Contact.USER_NAME,username)
                    .getRequestUrl(I.REQUEST_DOWNLOAD_GROUPS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void execute(){
        executeRequest(new GsonRequest<Group[]>(path,Group[].class,
                responDownloadAllGroupTaskListener(),errorListener()));
    }

    private Response.Listener<Group[]> responDownloadAllGroupTaskListener() {
        return new Response.Listener<Group[]>() {
            @Override
            public void onResponse(Group[] groups) {
                if(groups!=null && groups.length>0){
                    ArrayList<Group> groupList =
                            SuperWeChatApplication.getInstance().getGroupList();
                    //将数组转化为列表
                    ArrayList<Group> list = Utils.array2List(groups);
                    groupList.clear();
                    groupList.addAll(list);
                    mContext.sendStickyBroadcast(new Intent("update_group_list"));
                }
            }
        };
    }
}
