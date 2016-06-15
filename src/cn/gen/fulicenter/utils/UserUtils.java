package cn.gen.fulicenter.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import cn.gen.fulicenter.Constant;
import cn.gen.fulicenter.I;
import cn.gen.fulicenter.R;
import cn.gen.fulicenter.SuperWeChatApplication;
import cn.gen.fulicenter.applib.controller.HXSDKHelper;
import cn.gen.fulicenter.DemoHXSDKHelper;
import cn.gen.fulicenter.bean.Contact;
import cn.gen.fulicenter.bean.User;
import cn.gen.fulicenter.data.RequestManager;
import cn.gen.fulicenter.domain.EMUser;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.util.HanziToPinyin;
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
            Picasso.with(context).load(EMUser.getAvatar()).placeholder(cn.gen.fulicenter.R.drawable.default_avatar).into(imageView);
        }else{
            Picasso.with(context).load(cn.gen.fulicenter.R.drawable.default_avatar).into(imageView);
        }
    }

	public static void setUserBeanAvatar(User user, NetworkImageView imageView){
		if(user!=null && user.getMUserName()!=null){
            setUserAvatar(getAvatarPath(user.getMUserName()),imageView);
		}
	}

    public static void setUserBeanAvatar(String username, NetworkImageView imageView){
        Contact contact = getUserBeanInfo(username);
        if(contact!=null && contact.getMContactCname()!=null){
            setUserAvatar(getAvatarPath(username),imageView);
        }else {
            imageView.setDefaultImageResId(R.drawable.default_avatar);
        }
    }
    private static void setUserAvatar(String url,NetworkImageView imageView){
        if(url==null || url.isEmpty()) return;
        imageView.setDefaultImageResId(R.drawable.default_avatar);
        imageView.setImageUrl(url, RequestManager.getImageLoader());
        imageView.setErrorImageResId(R.drawable.default_avatar);
    }
	public static String getAvatarPath(String username) {
        if(username ==null|| username.isEmpty())return null;
		return I.REQUEST_DOWNLOAD_AVATAR_USER+username;
	}

	/**
     * 设置当前用户头像
     */
	public static void setCurrentUserAvatar(Context context, ImageView imageView) {
		EMUser EMUser = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo();
		if (EMUser != null && EMUser.getAvatar() != null) {
			Picasso.with(context).load(EMUser.getAvatar()).placeholder(cn.gen.fulicenter.R.drawable.default_avatar).into(imageView);
		} else {
			Picasso.with(context).load(cn.gen.fulicenter.R.drawable.default_avatar).into(imageView);
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

    public static void setUserBeanNick (User user,TextView textView){
        if(user!=null){
            if(user.getMUserNick()!=null){
                textView.setText(user.getMUserNick());
            } else if(user.getMUserName()!=null){
                textView.setText(user.getMUserName());
            }
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

    public static void setCurrentUserBeanNick(TextView textView){
        User user = SuperWeChatApplication.getInstance().getUser();
        if(user!=null && user.getMUserNick()!=null && textView != null){
            textView.setText(user.getMUserNick());
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

    /**
     * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
     *
     * @param username
     * @param user
     */
    public static void setUserHearder(String username, Contact user) {
        String headerName = null;
        if (!TextUtils.isEmpty(user.getMUserNick())) {
            headerName = user.getMUserNick();
        } else {
            headerName = user.getMUserName();
        }
        if (username.equals(Constant.NEW_FRIENDS_USERNAME)
                ||username.equals(Constant.GROUP_USERNAME)) {
            user.setHeader("");
        } else if (Character.isDigit(headerName.charAt(0))) {
            user.setHeader("#");
        } else {
            user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1)
                    .toUpperCase());
            char header = user.getHeader().toLowerCase().charAt(0);
            if (header < 'a' || header > 'z') {
                user.setHeader("#");
            }
        }
    }

    //设置群组头像
    public static void setGroupBeanAvatar(String mGroupHxid, NetworkImageView imageView) {
        if (mGroupHxid != null && !mGroupHxid.isEmpty()) {
            setGroupAvatar(getGroupAvatarPath(mGroupHxid), imageView);
        }
    }
    public static void setGroupAvatar(String url, NetworkImageView imageView) {
        if (url == null || url.isEmpty()) return;
        imageView.setDefaultImageResId(R.drawable.group_icon);
        imageView.setImageUrl(url, RequestManager.getImageLoader());
        imageView.setErrorImageResId(R.drawable.group_icon);
    }
    public static String getGroupAvatarPath(String hxid) {
        if (hxid == null || hxid.isEmpty()) return null;
        return I.REQUEST_DOWNLOAD_AVATAR_GROUP + hxid;
    }


}
