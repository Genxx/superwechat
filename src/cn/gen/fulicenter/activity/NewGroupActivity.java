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
package cn.gen.fulicenter.activity;

import android.app.Activity;
import android.app.ProgressDialog;
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



import com.easemob.exceptions.EaseMobException;

import java.io.File;

import cn.gen.fulicenter.I;
import cn.gen.fulicenter.Listener.OnSetAvatarListener;
import cn.gen.fulicenter.R;
import cn.gen.fulicenter.SuperWeChatApplication;
import cn.gen.fulicenter.bean.Contact;
import cn.gen.fulicenter.bean.Group;
import cn.gen.fulicenter.bean.Message;
import cn.gen.fulicenter.bean.User;
import cn.gen.fulicenter.data.ApiParams;
import cn.gen.fulicenter.data.GsonRequest;
import cn.gen.fulicenter.data.OkHttpUtils;
import cn.gen.fulicenter.utils.ImageUtils;
import cn.gen.fulicenter.utils.Utils;

public class NewGroupActivity extends BaseActivity {
    private EditText groupNameEditText;
    private ProgressDialog progressDialog;
    private EditText introductionEditText;
    private CheckBox checkBox;
    private CheckBox memberCheckbox;
    private LinearLayout openInviteContainer;
    ImageView ivAvatar;
    Activity mContext;
    ProgressDialog pd;
    String GroupName;
    OnSetAvatarListener mOnSetAvatarListener;
    String avatarName;

    private static final int CREATE_NEW_GROUP = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        mContext=this;
        initView();
        setListener();
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
        openInviteContainer = (LinearLayout) findViewById(R.id.ll_open_invite);
        ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
        groupNameEditText = (EditText) findViewById(R.id.edit_group_name);
        introductionEditText = (EditText) findViewById(R.id.edit_group_introduction);
        checkBox = (CheckBox) findViewById(R.id.cb_public);
        memberCheckbox = (CheckBox) findViewById(R.id.cb_member_inviter);
    }

    private void setListener() {
        setOnCheckchangedListener();
        setSaveGroupClickListener();
        setGroupIconClickListener();
    }

    private void setGroupIconClickListener() {
//        Log.e("Login","---11111-----");
        findViewById(R.id.layout_group_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e("Login","--2222222-----");
                mOnSetAvatarListener = new OnSetAvatarListener(mContext,R.id.layout_new_group,getavatarName(),I.AVATAR_TYPE_GROUP_PATH);
//                Log.e("Login","++++++++++");
            }
        });

    }

    private String getavatarName() {
        avatarName = System.currentTimeMillis() + "";//毫秒数
        return avatarName;
    }

    private void setSaveGroupClickListener() {
        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log.e("Login", "-----000000000000--------");
                String str6 = getResources().getString(R.string.Group_name_cannot_be_empty);
                String name = groupNameEditText.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Intent intent = new Intent(mContext, AlertDialog.class);
                    intent.putExtra("msg", str6);
                    /// Log.e("Login", "-----111111111111--------");
                    startActivity(intent);

                } else {
                    // 进通讯录选人
                    ///  Log.e("Login", "-----222222222222--------");
                    startActivityForResult(new Intent(NewGroupActivity.this, GroupPickContactsActivity.class)
                            .putExtra("groupName", name), CREATE_NEW_GROUP);
                }
            }
        });


    }

    private void setOnCheckchangedListener() {
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


    /**
     * @param v
     */
    public void save(View v) {
        String str6 = getResources().getString(R.string.Group_name_cannot_be_empty);
        String name = groupNameEditText.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Log.e("Login","name-----------------"+name);
            Intent intent = new Intent(this, AlertDialog.class);
            intent.putExtra("msg", str6);
            startActivity(intent);
        } else {
            // 进通讯录选人
            startActivityForResult(new Intent(this, GroupPickContactsActivity.class).putExtra("groupName", name), 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == CREATE_NEW_GROUP) {
            setProgressDialog();
            //mOnSetAvatarListener.setAvatar(requestCode,data,ivAvatar);
            //新建群组
            creatNewGroup(data);
        } else {
            mOnSetAvatarListener.setAvatar(requestCode, data, ivAvatar);
        }
    }

    private void setProgressDialog() {
        String st1 = getResources().getString(R.string.Is_to_create_a_group_chat);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(st1);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

    }

    private void creatNewGroup(final Intent data) {
        final String st2 = getResources().getString(R.string.Failed_to_create_groups);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用sdk创建群组方法
                // Log.e("Login","------------111111------");
                String groupName = groupNameEditText.getText().toString().trim();
                String desc = introductionEditText.getText().toString();
                Contact[] contacts = (Contact[]) data.getSerializableExtra("newmembers");

                String[] members = null;
                String[] memberIds = null;
                if (contacts != null) {
                    members = new String[contacts.length];
                    memberIds = new String[contacts.length];
                    for (int i = 0; i < contacts.length; i++) {
                        members[i] = contacts[i].getMContactCname() + ",";
                        memberIds[i] = contacts[i].getMContactId() + ",";
                        Log.e("Login", "+++++" + memberIds);
                    }

                }
                EMGroup emGroup;
                try {
                    if (checkBox.isChecked()) {
                        //创建公开群，此种方式创建的群，可以自由加入
                        //创建公开群，此种方式创建的群，用户需要申请，等群主同意后才能加入此群
                        emGroup= EMGroupManager.getInstance().createPublicGroup(groupName, desc, members, true, 200);
                    } else {
                        //创建不公开群
                        emGroup=EMGroupManager.getInstance().createPrivateGroup(groupName, desc, members, memberCheckbox.isChecked(), 200);
                    }
                    creatNewGroupAppServer(emGroup.getGroupId(),groupName,desc,contacts);
                    String hxid = emGroup.getGroupId();
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
                            Toast.makeText(NewGroupActivity.this, st2 + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }


        }).start();
    }

    private void creatNewGroupAppServer(String hxid, String groupName,
                                        String desc, final Contact[] contacts) {
        // Log.e("Login","++++++++++222222++++++++++++"+hxid);
        User user = SuperWeChatApplication.getInstance().getUser();
        boolean ispublic = checkBox.isChecked();
        boolean isInvites = memberCheckbox.isChecked();
        //注册环信的账号
        //首先注册远端服务器账号，并上传头像----OKHttp上传
        //如果环信注册失败，调用取消注册的方法，删除远端服务器账号和图片
        //request=register&m
        //  Log.e("Login", "------------33333333333----------");
        File file=new File(ImageUtils.getAvatarPath(mContext,I.AVATAR_TYPE_GROUP_PATH),
                avatarName + I.AVATAR_SUFFIX_JPG);//获取文件名
        //  Log.e("Login", "+++++++++++++++" + file);
        final OkHttpUtils<Group> utils = new OkHttpUtils<Group>();
        utils.url(SuperWeChatApplication.SERVER_ROOT)
                .addParam(I.KEY_REQUEST,I.REQUEST_CREATE_GROUP)
                .addParam(I.Group.HX_ID,hxid)
                .addParam(I.Group.NAME,groupName)
                .addParam(I.Group.DESCRIPTION,desc)
                .addParam(I.Group.OWNER,user.getMUserName())
                .addParam(I.Group.IS_PUBLIC,ispublic+"")
                .addParam(I.Group.ALLOW_INVITES,isInvites+"")
                .addParam(I.User.USER_ID,user.getMUserId()+"")
                .targetClass(Group.class)
                .addFile(file)
                .execute(new OkHttpUtils.OnCompleteListener<Group>() {
                    @Override
                    public void onSuccess(Group group) {
                        if (group.isResult()){
                            if (contacts!=null){
                                addGroupMembers(group,contacts);
                            }else {
                                SuperWeChatApplication.getInstance().getGroupList().add(group);
                                Intent intent = new Intent("update_group_list").putExtra("group", group);
                                progressDialog.dismiss();
                                setResult(RESULT_OK,intent);
                                finish();
                            }
                        }else {
                            pd.dismiss();
                            Utils.showToast(mContext,Utils.getResourceString(mContext,group.getMsg()),Toast.LENGTH_SHORT);
                        }
                    }
                    @Override
                    public void onError(String error) {
                        pd.dismiss();
                        Utils.showToast(mContext,error,Toast.LENGTH_SHORT);
                    }
                });
    }
    private void addGroupMembers(Group group, Contact[] members) {
        String userIds = "";
        String userNames = "";
        for (int i=0;i<members.length;i++) {
            userIds += members[i].getMContactCid() + ",";
            userNames += members[i].getMContactCname() + ",";

        }
        try {
            Log.e("Login", "++++++++++++" + userIds+"++++++"+userNames);
            String path = new ApiParams()
                    .with(I.Member.GROUP_HX_ID, group.getMGroupHxid())
                    .with(I.Member.USER_ID, userIds)
                    .with(I.Member.USER_NAME, userNames)
                    .getRequestUrl(I.REQUEST_ADD_GROUP_MEMBERS);
            executeRequest(new GsonRequest<Message>(path,Message.class,
                    responserListener(group),errorListener()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Response.Listener<Message> responserListener(final Group group) {

        return new Response.Listener<Message>() {
            @Override
            public void onResponse(Message message) {
                if (message.isResult()) {
                    progressDialog.dismiss();
                    SuperWeChatApplication.getInstance().getGroupList().add(group);
                    // Log.e("Login", "group"+group);
                    Intent intent = new Intent("update_group_list").putExtra("group", group);
                    setResult(RESULT_OK, intent);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(mContext,R.string.Failed_to_create_groups,Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        };
    }

    public void back(View view) {
        finish();
    }
}
