/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.gen.superwechat.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;

import cn.gen.superwechat.I;
import cn.gen.superwechat.Listener.OnSetAvatarListener;
import cn.gen.superwechat.R;
import cn.gen.superwechat.SuperWeChatApplication;
import cn.gen.superwechat.bean.Contact;
import cn.gen.superwechat.bean.Group;
import cn.gen.superwechat.bean.Message;
import cn.gen.superwechat.bean.User;
import cn.gen.superwechat.data.ApiParams;
import cn.gen.superwechat.data.GsonRequest;
import cn.gen.superwechat.data.OkHttpUtils;
import cn.gen.superwechat.utils.ImageUtils;
import cn.gen.superwechat.utils.Utils;

import com.easemob.exceptions.EaseMobException;

import java.io.File;

public class NewGroupActivity extends BaseActivity {
    private final static String TAG = NewGroupActivity.class.getName();
    private EditText groupNameEditText;
    private ProgressDialog progressDialog;
    private EditText introductionEditText;
    private CheckBox checkBox;
    private CheckBox memberCheckbox;
    private LinearLayout openInviteContainer;

    NewGroupActivity mContext;
    ImageView ivAvatar;
    OnSetAvatarListener mOnSetAvatarListener;
    String avatarName;

    private static final int CREATE_NEW_GROUP = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        mContext = this;
        initView();
        setListener();

    }

    private void setListener() {
        setOnClickchangeListener();
        setSaveGroupClickListener();
        setGroupIconClickListener();
    }

    private void setGroupIconClickListener() {
        findViewById(R.id.layout_group_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("updataUserNick", "++++++++++");
                mOnSetAvatarListener = new OnSetAvatarListener(mContext, R.id.layout_new_group, getAvatarName(), I.AVATAR_TYPE_GROUP_PATH);
            }
        });
    }

    public String getAvatarName() {
        avatarName = System.currentTimeMillis() + "";
        return avatarName;
    }

    private void setOnClickchangeListener() {
        checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    openInviteContainer.setVisibility(View.INVISIBLE);
                } else {
                    openInviteContainer.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initView() {
        groupNameEditText = (EditText) findViewById(R.id.edit_group_name);
        introductionEditText = (EditText) findViewById(R.id.edit_group_introduction);
        checkBox = (CheckBox) findViewById(R.id.cb_public);
        memberCheckbox = (CheckBox) findViewById(R.id.cb_member_inviter);
        openInviteContainer = (LinearLayout) findViewById(R.id.ll_open_invite);
        ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
    }

    public void setSaveGroupClickListener() {
        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str6 = getResources().getString(R.string.Group_name_cannot_be_empty);
                String name = groupNameEditText.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Intent intent = new Intent(mContext, AlertDialog.class);
                    intent.putExtra("msg", str6);
                    startActivity(intent);
                } else {
                    // 进通讯录选人
                    startActivityForResult(new Intent(mContext, GroupPickContactsActivity.class).putExtra("groupName", name), CREATE_NEW_GROUP);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //return;
            mOnSetAvatarListener.setAvatar(requestCode, data, ivAvatar);
        }
        if (requestCode == CREATE_NEW_GROUP) {
//            setProgressDialog();
            //新建群组
            //1.创建环信的群组，为了拿到环信id
            //2.创建远端的群组，并上传群组头像
            //3.添加群主成员到远端
            createNewGroup(data);
        } else {
           // mOnSetAvatarListener.setAvatar(requestCode, data, ivAvatar);
        return;
        }
    }

    private void setProgressDialog() {
        String st1 = getResources().getString(R.string.Is_to_create_a_group_chat);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(st1);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    private void createNewGroup(final Intent data) {
        setProgressDialog();
        final String st2 = getResources().getString(R.string.Failed_to_create_groups);
        //新建群组
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 调用sdk创建群组方法
                String groupName = groupNameEditText.getText().toString().trim();
                String desc = introductionEditText.getText().toString();
                Contact[] contacts = (Contact[]) data.getSerializableExtra("newmembers");
                String[] members = null;
                String[] memberIds = null;
                if (contacts != null) {
                    members = new String[contacts.length];
                    for (int i = 0; i < contacts.length; i++) {
                        members[i] = contacts[i].getMContactCname() + ",";
                    }
                }
                EMGroup emGroup;
                try {
                    if (checkBox.isChecked()) {
                        //创建公开群，此种方式创建的群，可以自由加入
                        //创建公开群，此种方式创建的群，用户需要申请，等群主同意后才能加入此群
                        emGroup=EMGroupManager.getInstance().createPublicGroup(groupName, desc, members, true, 200);
                    } else {
                        //创建不公开群
                        emGroup=EMGroupManager.getInstance().createPrivateGroup(groupName, desc, members, memberCheckbox.isChecked(), 200);
                    }
                   createNewGroupAppServer(emGroup.getGroupId(),groupName,desc,contacts);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
                } catch (final EaseMobException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(NewGroupActivity.this, st2 + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });

    }

    private void createNewGroupAppServer(String hxid, String groupName,
                                         String desc, final Contact[] contacts) {
        User user = SuperWeChatApplication.getInstance().getUser();
        boolean isPublic = checkBox.isChecked();
        boolean isInvites = memberCheckbox.isChecked();
        //注册环信的服务器 registerEMServer
        //先注册本地的服务器并上传头像 REQUEST_REGISTER -->okhttp
        //添加群组成员
        File file  = new File(ImageUtils.getAvatarPath(mContext,I.AVATAR_TYPE_GROUP_PATH),
                avatarName+I.AVATAR_SUFFIX_JPG);
        OkHttpUtils<Group> utils = new OkHttpUtils<Group>();
        utils.url(SuperWeChatApplication.SERVER_ROOT)//设置服务端根地址
                .addParam(I.KEY_REQUEST,I.REQUEST_CREATE_GROUP)//添加上传的请求参数
                .addParam(I.Group.HX_ID,hxid)
                .addParam(I.Group.NAME,groupName)
                .addParam(I.Group.DESCRIPTION,desc)
                .addParam(I.Group.OWNER,user.getMUserName())
                .addParam(I.Group.IS_PUBLIC,isPublic+"")
                .addParam(I.Group.ALLOW_INVITES,isInvites+"")
                .addParam(I.Group.GROUP_ID,user.getMUserId()+"")
                .targetClass(Group.class)//设置服务端返回json数据的解析类型
                .addFile(file)//添加上传的文件
                .execute(new OkHttpUtils.OnCompleteListener<Group>() {
                    @Override
                    public void onSuccess(Group group) {
                        if(group.isResult()){
                            if(contacts!=null){
                             addGruopMembers(group,contacts);
                            }else {
                                addGruopMembers(group,contacts);
                                SuperWeChatApplication.getInstance().getGroupList().add(group);
                                Intent intent = new Intent("update_group_list").putExtra("group",group);
                                setResult(RESULT_OK,intent);
                                progressDialog.dismiss();
                                Utils.showToast(mContext,R.string.Create_groups_Success,Toast.LENGTH_SHORT);
                                finish();
                            }
                        }else {
                            Utils.showToast(mContext,Utils.getResourceString(mContext,group.getMsg()),Toast.LENGTH_SHORT);
                           Log.e(TAG,Utils.getResourceString(mContext,group.getMsg()));
                        }
                    }

                    @Override
                    public void onError(String error) {
                        progressDialog.dismiss();
                        Utils.showToast(mContext,error,Toast.LENGTH_SHORT);
                        Log.e(TAG,"register fail ,error:"+error);
                    }
                });

    }

    private void addGruopMembers(Group group, Contact[] contacts) {
        try {
            String userIds = "";
            String userNames = "";
            for(int i=0;i<contacts.length;i++){
                userIds+=contacts[i].getMContactUserId()+",";
                userNames+=contacts[i].getMContactCname()+",";
            }
            String path = new ApiParams()
                    .with(I.Member.GROUP_HX_ID,group.getMGroupHxid())
                    .with(I.Member.USER_ID,userIds)
                    .with(I.Member.USER_NAME,userNames)
                    .getRequestUrl(I.REQUEST_ADD_GROUP_MEMBERS);
            Log.e(TAG,"path = "+path);
            executeRequest(new GsonRequest<Message>(path,Message.class,
                    responseListener(group),errorListener()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Response.Listener<Message> responseListener(final Group group) {
        return new Response.Listener<Message>() {
            @Override
            public void onResponse(Message message) {
                if(message.isResult()){
                    progressDialog.dismiss();
                    SuperWeChatApplication.getInstance().getGroupList().add(group);
                    Intent intent = new Intent("update_group_list").putExtra("group",group);
                    Utils.showToast(mContext,Utils.getResourceString(mContext,I.MSG_GROUP_CREATE_SCUUESS),Toast.LENGTH_SHORT);
                    setResult(RESULT_OK,intent);
                }else {
                    progressDialog.dismiss();
                    Utils.showToast(mContext,R.string.Failed_to_create_groups,Toast.LENGTH_SHORT);
                }
                finish();
            }
        };
    }

    public void back(View view) {
        finish();
    }


}
