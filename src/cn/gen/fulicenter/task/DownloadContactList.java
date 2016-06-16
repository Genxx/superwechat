package cn.gen.fulicenter.task;

import android.content.Context;
import android.content.Intent;

import com.android.volley.Response;

import java.util.ArrayList;
import java.util.HashMap;

import cn.gen.fulicenter.I;
import cn.gen.fulicenter.FuliCenterApplication;
import cn.gen.fulicenter.activity.BaseActivity;
import cn.gen.fulicenter.bean.Contact;
import cn.gen.fulicenter.data.ApiParams;
import cn.gen.fulicenter.data.GsonRequest;
import cn.gen.fulicenter.utils.Utils;

/**
 * Created by Administrator on 2016/5/23.
 */
public class DownloadContactList extends BaseActivity{
    //DownloadContactList,I.REQUEST_DOWNLOAD_CONTACT_ALL_LIST,update_contact_list
    /** 客户端发送的下载联系人所有集合请求 */
    Context mContext;
    String username;
    String path;

    public DownloadContactList(Context mContext, String username) {
        this.mContext = mContext;
        this.username = username;
        initpath();
    }

    private void initpath() {
        try {
            path = new ApiParams()
                    .with(I.Contact.USER_NAME,username)
                    .getRequestUrl(I.REQUEST_DOWNLOAD_CONTACT_ALL_LIST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void execute(){
        executeRequest(new GsonRequest<Contact[]>(path,Contact[].class,
                responDownloadContactListTaskListener(),errorListener()));
    }

    private Response.Listener<Contact[]> responDownloadContactListTaskListener() {
        return new Response.Listener<Contact[]>() {
            @Override
            public void onResponse(Contact[] response) {
                if(response!=null){
                    ArrayList<Contact> contactList =
                            FuliCenterApplication.getInstance().getContactList();
                    //将数组转化为列表
                    ArrayList<Contact> list = Utils.array2List(response);
                    contactList.clear();
                    contactList.addAll(list);
                    HashMap<String, Contact> userList =
                            FuliCenterApplication.getInstance().getUserList();
                    userList.clear();
                    for(Contact c:list){
                        userList.put(c.getMContactCname(),c);
                    }
                    mContext.sendStickyBroadcast(new Intent("update_contact_list"));
                }
            }
        };
    }
}
