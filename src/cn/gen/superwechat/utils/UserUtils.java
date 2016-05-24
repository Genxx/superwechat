package cn.gen.superwechat.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import cn.gen.superwechat.I;
import cn.gen.superwechat.R;
import cn.gen.superwechat.SuperWeChatApplication;
import cn.gen.superwechat.applib.controller.HXSDKHelper;
import cn.gen.superwechat.DemoHXSDKHelper;
import cn.gen.superwechat.bean.Contact;
import cn.gen.superwechat.bean.User;
import cn.gen.superwechat.data.RequestManager;
import cn.gen.superwechat.domain.EMUser;

import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;

public class UserUtils {
    /**
     * 根据username获取相应user，由于demo没有真实的用户数据，这里给的模拟的数据；
     * @param username
     * @return
     */
    public static EMUser getUserInfo(String username){
        EMUser EMUser = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList().get(username);
        if(EMUser == null){
            EMUser = new EMUser(username);
        }
            
        if(EMUser != null){
            //demo没有这些数据，临时填充
        	if(TextUtils.isEmpty(EMUser.getNick()))
        		EMUser.setNick(username);
        }
        return EMUser;
    }

	public static Contact getUserBeanInfo(String username){
		Contact contact = SuperWeChatApplication.getInstance().getUserList().get(username);
		return contact;
	}

    /**
     * 设置用户头像
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView){
    	EMUser EMUser = getUserInfo(username);
        if(EMUser != null && EMUser.getAvatar() != null){
            Picasso.with(context).load(EMUser.getAvatar()).placeholder(cn.gen.superwechat.R.drawable.default_avatar).into(imageView);
        }else{
            Picasso.with(context).load(cn.gen.superwechat.R.drawable.default_avatar).into(imageView);
        }
    }

	public static void setUserBeanAvatar(String username, NetworkImageView imageView){
		Contact contact = getUserBeanInfo(username);
		if(contact!=null && contact.getMContactCname()!=null){
            setUserAvatar(getAvatarPath(username),imageView);
		}
	}

    private static void setUserAvatar(String url,NetworkImageView imageView){
        if(url==null || url.isEmpty()) return;
        imageView.setDefaultImageResId(R.drawable.default_avatar);
        imageView.setImageUrl(url, RequestManager.getImageLoader());
        imageView.setErrorImageResId(R.drawable.default_avatar);
    }
	private static String getAvatarPath(String username) {
        if(username ==null|| username.isEmpty())return null;
		return I.REQUEST_DOWNLOAD_AVATAR_USER+username;
	}

	/**
     * 设置当前用户头像
     */
	public static void setCurrentUserAvatar(Context context, ImageView imageView) {
		EMUser EMUser = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo();
		if (EMUser != null && EMUser.getAvatar() != null) {
			Picasso.with(context).load(EMUser.getAvatar()).placeholder(cn.gen.superwechat.R.drawable.default_avatar).into(imageView);
		} else {
			Picasso.with(context).load(cn.gen.superwechat.R.drawable.default_avatar).into(imageView);
		}
	}

    public static void setCurrentUserAvatar(NetworkImageView imageView){
        User user = SuperWeChatApplication.getInstance().getUser();
        if(user!=null){
            setUserAvatar(getAvatarPath(user.getMUserName()),imageView);
        }
    }
    /**
     * 设置用户昵称
     */
    public static void setUserNick(String username,TextView textView){
    	EMUser EMUser = getUserInfo(username);
    	if(EMUser != null){
    		textView.setText(EMUser.getNick());
    	}else{
    		textView.setText(username);
    	}
    }
    public static void setUserBeanNick (String username,TextView textView){
        Contact contact = getUserBeanInfo(username);
        if(contact!=null){
            if(contact.getMUserNick()!=null){
                textView.setText(contact.getMUserNick());
            } else if(contact.getMContactCname()!=null){
                textView.setText(contact.getMContactCname());
            }
        }else {
            textView.setText(username);
        }
    }
    
    /**
     * 设置当前用户昵称
     */
    public static void setCurrentUserNick(TextView textView){
    	EMUser EMUser = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo();
    	if(textView != null){
    		textView.setText(EMUser.getNick());
    	}
    }
    
    /**
     * 保存或更新某个用户
     * @param newEMUser
     */
	public static void saveUserInfo(EMUser newEMUser) {
		if (newEMUser == null || newEMUser.getUsername() == null) {
			return;
		}
		((DemoHXSDKHelper) HXSDKHelper.getInstance()).saveContact(newEMUser);
	}
    
}
