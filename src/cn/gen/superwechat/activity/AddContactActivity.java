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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.gen.superwechat.I;
import cn.gen.superwechat.R;
import cn.gen.superwechat.applib.controller.HXSDKHelper;

import com.android.volley.Response;
import com.easemob.chat.EMContactManager;

import cn.gen.superwechat.SuperWeChatApplication;
import cn.gen.superwechat.DemoHXSDKHelper;
import cn.gen.superwechat.bean.User;
import cn.gen.superwechat.data.ApiParams;
import cn.gen.superwechat.data.GsonRequest;

public class AddContactActivity extends BaseActivity {
    private EditText editText;
    private LinearLayout searchedUserLayout;
    private TextView nameText, mTextView;
    private Button searchBtn;
    private ImageView avatar;
    private InputMethodManager inputMethodManager;
    private String toAddUsername;
    private ProgressDialog progressDialog;

    TextView mTvNothing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(cn.gen.superwechat.R.layout.activity_add_contact);
        mTextView = (TextView) findViewById(cn.gen.superwechat.R.id.add_list_friends);

        editText = (EditText) findViewById(cn.gen.superwechat.R.id.edit_note);
        String strAdd = getResources().getString(cn.gen.superwechat.R.string.add_friend);
        mTextView.setText(strAdd);
        String strUserName = getResources().getString(cn.gen.superwechat.R.string.user_name);
        editText.setHint(strUserName);
        searchedUserLayout = (LinearLayout) findViewById(cn.gen.superwechat.R.id.ll_user);
        nameText = (TextView) findViewById(cn.gen.superwechat.R.id.name);
        searchBtn = (Button) findViewById(cn.gen.superwechat.R.id.search);
        avatar = (ImageView) findViewById(cn.gen.superwechat.R.id.avatar);
        mTvNothing = (TextView) findViewById(R.id.tv_show_nothing);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }


    /**
     * 查找contact
     * @param v
     */
    public void searchContact(View v) {
        final String name = editText.getText().toString();
        String saveText = searchBtn.getText().toString();
        if (TextUtils.isEmpty(name)) {
            String st = getResources().getString(cn.gen.superwechat.R.string.Please_enter_a_username);
            startActivity(new Intent(this, AlertDialog.class).putExtra("msg", st));
            return;
        }

        //如果添加的是自己就显示自己的页面
        if (SuperWeChatApplication.getInstance().getUserName().equals(nameText.getText().toString())) {
            String str = getString(cn.gen.superwechat.R.string.not_add_myself);
            startActivity(new Intent(this, AlertDialog.class).putExtra("msg", str));
            return;
        }
        toAddUsername = name;
        try {
            String path = new ApiParams()
                    .with(I.User.USER_NAME, toAddUsername)
                    .getRequestUrl(I.REQUEST_FIND_USER);
            executeRequest(new GsonRequest<User>(path,User.class,
                    responseFindUserListener(),errorListener()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Response.Listener<User> responseFindUserListener() {
        return new Response.Listener<User>() {
            @Override
            public void onResponse(User user) {
                if(user!=null){
                    //服务器存在此用户，显示此用户和添加按钮
                    searchedUserLayout.setVisibility(View.VISIBLE);
                    nameText.setText(toAddUsername);
                }else {
                    searchedUserLayout.setVisibility(View.GONE);
                    mTvNothing.setVisibility(View.VISIBLE);
                }
            }
        };
    }


    /**
     *  添加contact
     * @param view
     */
    public void addContact(View view) {


        if (((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList().containsKey(nameText.getText().toString())) {
            //提示已在好友列表中，无需添加
            if (EMContactManager.getInstance().getBlackListUsernames().contains(nameText.getText().toString())) {
                startActivity(new Intent(this, AlertDialog.class).putExtra("msg", "此用户已是你好友(被拉黑状态)，从黑名单列表中移出即可"));
                return;
            }
            String strin = getString(cn.gen.superwechat.R.string.This_user_is_already_your_friend);
            startActivity(new Intent(this, AlertDialog.class).putExtra("msg", strin));
            return;
        }

        progressDialog = new ProgressDialog(this);
        String stri = getResources().getString(cn.gen.superwechat.R.string.Is_sending_a_request);
        progressDialog.setMessage(stri);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {

                try {
                    //demo写死了个reason，实际应该让用户手动填入
                    String s = getResources().getString(cn.gen.superwechat.R.string.Add_a_friend);
                    EMContactManager.getInstance().addContact(toAddUsername, s);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s1 = getResources().getString(cn.gen.superwechat.R.string.send_successful);
                            Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s2 = getResources().getString(cn.gen.superwechat.R.string.Request_add_buddy_failure);
                            Toast.makeText(getApplicationContext(), s2 + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }

    public void back(View v) {
        finish();
    }
}
