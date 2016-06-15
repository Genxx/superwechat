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
package cn.gen.fulicenter.activity;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import cn.gen.fulicenter.applib.controller.HXSDKHelper;
import cn.gen.fulicenter.Constant;
import cn.gen.fulicenter.DemoHXSDKHelper;
import cn.gen.fulicenter.adapter.NewFriendsMsgAdapter;
import cn.gen.fulicenter.db.InviteMessgeDao;
import cn.gen.fulicenter.domain.InviteMessage;

/**
 * 申请与通知
 *
 */
public class NewFriendsMsgActivity extends BaseActivity {
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(cn.gen.fulicenter.R.layout.activity_new_friends_msg);

		listView = (ListView) findViewById(cn.gen.fulicenter.R.id.list);
		InviteMessgeDao dao = new InviteMessgeDao(this);
		List<InviteMessage> msgs = dao.getMessagesList();
		//设置adapter
		NewFriendsMsgAdapter adapter = new NewFriendsMsgAdapter(this, 1, msgs);
		listView.setAdapter(adapter);
		((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList().get(Constant.NEW_FRIENDS_USERNAME).setUnreadMsgCount(0);
		
	}

	public void back(View view) {
		finish();
	}
	
	
}
