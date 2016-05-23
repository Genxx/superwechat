/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.gen.superwechat.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.easemob.EMCallBack;

import cn.gen.superwechat.R;
import cn.gen.superwechat.SuperWeChatApplication;
import cn.gen.superwechat.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import cn.gen.superwechat.Constant;
import cn.gen.superwechat.DemoHXSDKHelper;
import cn.gen.superwechat.db.EMUserDao;
import cn.gen.superwechat.domain.EMUser;
import cn.gen.superwechat.utils.CommonUtils;

/**
 * 登陆页面
 * 
 */
public class LoginActivity extends BaseActivity {
	Context mContext;
	private static final String TAG = "LoginActivity";
	public static final int REQUEST_CODE_SETNICK = 1;
	private EditText usernameEditText;
	private EditText passwordEditText;

	private boolean progressShow;
	private boolean autoLogin = false;

	private String currentUsername;
	private String currentPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 如果用户名密码都有，直接进入主页面
		if (DemoHXSDKHelper.getInstance().isLogined()) {
			autoLogin = true;
			startActivity(new Intent(LoginActivity.this, MainActivity.class));

			return;
		}
		setContentView(cn.gen.superwechat.R.layout.activity_login);
		mContext=this;

		usernameEditText = (EditText) findViewById(cn.gen.superwechat.R.id.username);
		passwordEditText = (EditText) findViewById(cn.gen.superwechat.R.id.password);


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

				progressShow = true;
				final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
				pd.setCanceledOnTouchOutside(false);
				pd.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						progressShow = false;
					}
				});
				pd.setMessage(getString(cn.gen.superwechat.R.string.Is_landing));
				pd.show();

				final long start = System.currentTimeMillis();
				// 调用sdk登陆方法登陆聊天服务器
				EMChatManager.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

					@Override
					public void onSuccess() {
						if (!progressShow) {
							return;
						}
						// 登陆成功，保存用户名密码
						SuperWeChatApplication.getInstance().setUserName(currentUsername);
						SuperWeChatApplication.getInstance().setPassword(currentPassword);

						try {
							// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
							// ** manually load all local groups and
							EMGroupManager.getInstance().loadAllGroups();
							EMChatManager.getInstance().loadAllConversations();
							// 处理好友和群组
							initializeContacts();
						} catch (Exception e) {
							e.printStackTrace();
							// 取好友或者群聊失败，不让进入主页面
							runOnUiThread(new Runnable() {
								public void run() {
									pd.dismiss();
									DemoHXSDKHelper.getInstance().logout(true,null);
									Toast.makeText(getApplicationContext(), cn.gen.superwechat.R.string.login_failure_failed, Toast.LENGTH_SHORT).show();
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
		EMUser robotEMUser = new EMUser();
		String strRobot = getResources().getString(cn.gen.superwechat.R.string.robot_chat);
		robotEMUser.setUsername(Constant.CHAT_ROBOT);
		robotEMUser.setNick(strRobot);
		robotEMUser.setHeader("");
		userlist.put(Constant.CHAT_ROBOT, robotEMUser);
		
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
