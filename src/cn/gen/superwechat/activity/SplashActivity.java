package cn.gen.superwechat.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import cn.gen.superwechat.DemoHXSDKHelper;
import cn.gen.superwechat.I;
import cn.gen.superwechat.SuperWeChatApplication;
import cn.gen.superwechat.bean.Contact;
import cn.gen.superwechat.bean.User;
import cn.gen.superwechat.task.DownloadAllGroupTask;
import cn.gen.superwechat.task.DownloadContactList;
import cn.gen.superwechat.task.DownloadPublicGroupTask;
import cn.ucai.superwechat.db.UserDao;

/**
 * 开屏页
 *
 */
public class SplashActivity extends BaseActivity {
	private RelativeLayout rootLayout;
	private TextView versionText;
	Context mContext;
	
	private static final int sleepTime = 2000;

	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(cn.gen.superwechat.R.layout.activity_splash);
		super.onCreate(arg0);
		mContext = this;

		rootLayout = (RelativeLayout) findViewById(cn.gen.superwechat.R.id.splash_root);
		versionText = (TextView) findViewById(cn.gen.superwechat.R.id.tv_version);

		versionText.setText(getVersion());
		AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
		animation.setDuration(1500);
		rootLayout.startAnimation(animation);
	}

	@Override
	protected void onStart() {
		super.onStart();
		if(DemoHXSDKHelper.getInstance().isLogined()){
			Log.e("main","start dowmload arraylist--------SplashAcitivity");
			String username = SuperWeChatApplication.getInstance().getUserName();
			UserDao dao = new UserDao(mContext);

			User user = dao.findUserByUserName(username);
			SuperWeChatApplication.getInstance().setUser(user);
//			SuperWeChatApplication.currentUserNick = user.getMUserNick();

//			User user = SuperWeChatApplication.getInstance().getUser();
//			SuperWeChatApplication instance = SuperWeChatApplication.getInstance();
//			instance.setUser(user);
//
//			//登录成功，保存用户名密码
//			instance.setUserName(user.getMUserName());
//			instance.setPassword(user.getMUserPassword());
//			SuperWeChatApplication.currentUserNick = user.getMUserNick();
			if(user!=null){
			new DownloadContactList(mContext,user.getMUserName()).execute();
			new DownloadAllGroupTask(mContext,user.getMUserName()).execute();
			new DownloadPublicGroupTask(mContext,user.getMUserName(),
					I.PAGE_ID_DEFAULT,I.PAGE_SIZE_DEFAULT).execute();
			}
		}

		new Thread(new Runnable() {
			public void run() {
				if (DemoHXSDKHelper.getInstance().isLogined()) {
					// ** 免登陆情况 加载所有本地群和会话
					//不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
					//加上的话保证进了主页面会话和群组都已经load完毕
					long start = System.currentTimeMillis();
					EMGroupManager.getInstance().loadAllGroups();
					EMChatManager.getInstance().loadAllConversations();
					long costTime = System.currentTimeMillis() - start;
					//等待sleeptime时长
					if (sleepTime - costTime > 0) {
						try {
							Thread.sleep(sleepTime - costTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					//进入主页面
					startActivity(new Intent(SplashActivity.this, MainActivity.class));
					finish();
				}else {
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
					}
					startActivity(new Intent(SplashActivity.this, LoginActivity.class));
					finish();
				}
			}
		}).start();

	}
	
	/**
	 * 获取当前应用程序的版本号
	 */
	private String getVersion() {
		String st = getResources().getString(cn.gen.superwechat.R.string.Version_number_is_wrong);
		PackageManager pm = getPackageManager();
		try {
			PackageInfo packinfo = pm.getPackageInfo(getPackageName(), 0);
			String version = packinfo.versionName;
			return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return st;
		}
	}
}
