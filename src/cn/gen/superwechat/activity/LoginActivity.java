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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.easemob.EMCallBack;

import cn.gen.superwechat.I;
import cn.gen.superwechat.Listener.OnSetAvatarListener;
import cn.gen.superwechat.R;
import cn.gen.superwechat.SuperWeChatApplication;
import cn.gen.superwechat.applib.controller.HXSDKHelper;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;

import cn.gen.superwechat.Constant;
import cn.gen.superwechat.DemoHXSDKHelper;
import cn.gen.superwechat.bean.Message;
import cn.gen.superwechat.bean.User;
import cn.gen.superwechat.data.ApiParams;
import cn.gen.superwechat.data.GsonRequest;
import cn.gen.superwechat.data.OkHttpUtils;
import cn.gen.superwechat.db.EMUserDao;
import cn.gen.superwechat.domain.EMUser;
import cn.gen.superwechat.task.DownloadAllGroupTask;
import cn.gen.superwechat.task.DownloadContactList;
import cn.gen.superwechat.task.DownloadPublicGroupTask;
import cn.gen.superwechat.utils.CommonUtils;
import cn.gen.superwechat.utils.MD5;
import cn.gen.superwechat.utils.Utils;
import cn.gen.superwechat.db.UserDao;

/**
 * 登陆页面
 *
 */
public class LoginActivity extends BaseActivity {
    Activity mContext;
    private static final String TAG = "LoginActivity";
    public static final int REQUEST_CODE_SETNICK = 1;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button mbtnUrl;

    private boolean progressShow;
    private boolean autoLogin = false;

    private String currentUsername;
    private String currentPassword;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 如果用户名密码都有，直接进入主页面
        if (DemoHXSDKHelper.getInstance().isLogined()) {
            autoLogin = true;
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            return;
        }
        setContentView(R.layout.activity_login);
        mContext = this;

        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);
        mbtnUrl = (Button) findViewById(R.id.btnLogin);


        if (SuperWeChatApplication.getInstance().getUserName() != null) {
            usernameEditText.setText(SuperWeChatApplication.getInstance().getUserName());
        }
        setListener();
    }

    private void setListener() {
        onLoginListener();
        onUserNameChangedListener();
        onRegisterListener();

    }


    private void onUserNameChangedListener() {
        // 如果用户名改变，清空密码
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordEditText.setText(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 登录
     */
    private void onLoginListener() {
        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonUtils.isNetWorkConnected(mContext)) {
                    Toast.makeText(mContext, cn.gen.superwechat.R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
                    return;
                }
                currentUsername = usernameEditText.getText().toString().trim();
                currentPassword = passwordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(currentUsername)) {
                    Toast.makeText(mContext, cn.gen.superwechat.R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(currentPassword)) {
                    Toast.makeText(mContext, cn.gen.superwechat.R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                showProgressShow();
//                final long start = System.currentTimeMillis();
                // 调用sdk登陆方法登陆聊天服务器
                EMChatManager.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        if (!progressShow) {
                            return;
                        }
                        loginAppServer();
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }

                    @Override
                    public void onError(final int code, final String message) {
                        if (!progressShow) {
                            return;
                        }
                        runOnUiThread(new Runnable() {
                            public void run() {
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(), getString(cn.gen.superwechat.R.string.Login_failed) + message,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

    }
//登录本地
    private void loginAppServer() {
        UserDao dao = new UserDao(mContext);
        User user = dao.findUserByUserName(currentUsername);
        if (user != null) {
            if (user.getMUserPassword().equals(MD5.getData(currentPassword))) {
                saveUser(user);
                loginSuccess();
            } else {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), cn.gen.superwechat.R.string.login_failure_failed, Toast.LENGTH_SHORT).show();
            }
        } else {
            //http://10.0.2.2:8080/SuperWeChatServer/Server?
            // request=login&m_user_name=&m_user_password=
            //volley login server
            try {
                String path = new ApiParams()
                        .with(I.User.USER_NAME,currentUsername)
                        .with(I.User.PASSWORD,currentPassword)
                        .getRequestUrl(I.REQUEST_LOGIN);
                executeRequest(new GsonRequest<User>(path,User.class,
                        responseListener(),errorListener()));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private Response.Listener<User> responseListener() {
        return new Response.Listener<User>() {
            @Override
            public void onResponse(User user) {
                if(user.isResult()){
                    saveUser(user);
                    user.setMUserPassword(MD5.getData(user.getMUserPassword()));
                    UserDao dao = new UserDao(mContext);
                    dao.addUser(user);
                    loginSuccess();
                }else {
                    pd.dismiss();
                    Utils.showToast(mContext,Utils.getResourceString(mContext,user.getMsg()),Toast.LENGTH_SHORT);
                }
            }
        };
    }

    /**保存当前登录的用户到全局变量**/
    private void saveUser(User user) {
        SuperWeChatApplication instance = SuperWeChatApplication.getInstance();
        instance.setUser(user);
        //登录成功，保存用户名,密码
        instance.setUserName(currentUsername);
        instance.setPassword(currentPassword);
        SuperWeChatApplication.currentUserNick = user.getMUserNick();
    }

    private void loginSuccess() {
        try {
            // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
            // ** manually load all local groups and
            EMGroupManager.getInstance().loadAllGroups();
            EMChatManager.getInstance().loadAllConversations();
            // 处理好友和群组
            initializeContacts();
            //下载用户头像到sd卡里面
            //http://10.0.2.2:8080/SuperWeChatServer/Server?
            // request=download_avatar&avatarType=
            final OkHttpUtils<Message> utils = new OkHttpUtils<Message>();
            utils.url(SuperWeChatApplication.SERVER_ROOT)//设置服务端根地址
            .addParam(I.KEY_REQUEST,I.REQUEST_DOWNLOAD_AVATAR)//添加上传的请求参数
            .addParam(I.AVATAR_TYPE,currentUsername)//添加用户的账号
            .doInBackground(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
//                    Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                    String avatarPath = I.AVATAR_TYPE_USER_PATH + I.BACKSLASH
                            + currentUsername + I.AVATAR_SUFFIX_JPG;
                    File file = OnSetAvatarListener.getAvatarFile(mContext,avatarPath);
                    FileOutputStream out  = null;
                    out = new FileOutputStream(file);
                    utils.downloadFile(response,file,false);
                }
            }).execute(null);
            //登录成功后下载联系人，群组等资料
            Log.e("main","start dowmload arraylist");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new DownloadContactList(mContext,currentUsername).execute();
                    new DownloadAllGroupTask(mContext,currentUsername).execute();
                    new DownloadPublicGroupTask(mContext,currentUsername,
                            I.PAGE_ID_DEFAULT,I.PAGE_SIZE_DEFAULT).execute();
                }
            });
            //处理好友和群组
            initializeContacts();
        } catch (Exception e) {
            e.printStackTrace();
            // 取好友或者群聊失败，不让进入主页面
            runOnUiThread(new Runnable() {
                public void run() {
                    pd.dismiss();
                    DemoHXSDKHelper.getInstance().logout(true, null);
                    Toast.makeText(getApplicationContext(), R.string.login_failure_failed, Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }

        // 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
        boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(
                SuperWeChatApplication.currentUserNick.trim());
        if (!updatenick) {
            Log.e("LoginActivity", "update current user nick fail");
        }
        if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
            pd.dismiss();
        }
        // 进入主页面
        Intent intent = new Intent(LoginActivity.this,
                MainActivity.class);
        startActivity(intent);

        finish();
    }

    private void showProgressShow() {
        progressShow = true;
        pd = new ProgressDialog(LoginActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressShow = false;
            }
        });
        pd.setMessage(getString(R.string.Is_landing));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pd.show();
            }
        });
    }

    private void initializeContacts() {
        Map<String, EMUser> userlist = new HashMap<String, EMUser>();
        // 添加user"申请与通知"
        EMUser newFriends = new EMUser();
        newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
        String strChat = getResources().getString(
                cn.gen.superwechat.R.string.Application_and_notify);
        newFriends.setNick(strChat);

        userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
        // 添加"群聊"
        EMUser groupEMUser = new EMUser();
        String strGroup = getResources().getString(cn.gen.superwechat.R.string.group_chat);
        groupEMUser.setUsername(Constant.GROUP_USERNAME);
        groupEMUser.setNick(strGroup);
        groupEMUser.setHeader("");
        userlist.put(Constant.GROUP_USERNAME, groupEMUser);

        // 添加"Robot"
//        EMUser robotEMUser = new EMUser();
//        String strRobot = getResources().getString(cn.gen.superwechat.R.string.robot_chat);
//        robotEMUser.setUsername(Constant.CHAT_ROBOT);
//        robotEMUser.setNick(strRobot);
//        robotEMUser.setHeader("");
//        userlist.put(Constant.CHAT_ROBOT, robotEMUser);

        // 存入内存
        ((DemoHXSDKHelper) HXSDKHelper.getInstance()).setContactList(userlist);
        // 存入db
        EMUserDao dao = new EMUserDao(LoginActivity.this);
        List<EMUser> EMUsers = new ArrayList<EMUser>(userlist.values());
        dao.saveContactList(EMUsers);
    }

    /**
     * 注册
     *
     */
    public void onRegisterListener() {
        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, RegisterActivity.class), 0);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (autoLogin) {
            return;
        }
    }
}
