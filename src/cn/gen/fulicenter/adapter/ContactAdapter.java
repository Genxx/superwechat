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
package cn.gen.fulicenter.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import cn.gen.fulicenter.Constant;
import cn.gen.fulicenter.DemoHXSDKHelper;
import cn.gen.fulicenter.R;
import cn.gen.fulicenter.applib.controller.HXSDKHelper;
import cn.gen.fulicenter.bean.Contact;
import cn.gen.fulicenter.data.RequestManager;
import cn.gen.fulicenter.utils.UserUtils;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.util.EMLog;

/**
 * 简单的好友Adapter实现
 *
 */
public class ContactAdapter extends BaseAdapter implements SectionIndexer {
    private static final String TAG = "ContactAdapter";
    List<String> list;
    ArrayList<Contact> EMUserList;
    ArrayList<Contact> copyEMUserList;
    private LayoutInflater layoutInflater;
    private SparseIntArray positionOfSection;
    private SparseIntArray sectionOfPosition;
    private int res;
    private MyFilter myFilter;
    private boolean notiyfyByFilter;
    Context mContext;

    public ContactAdapter(Context context, int resource, ArrayList<Contact> objects) {
        mContext = context;
        this.res = resource;
        this.EMUserList = objects;
        copyEMUserList = new ArrayList<Contact>();
        copyEMUserList.addAll(objects);
        layoutInflater = LayoutInflater.from(context);
    }

    public void remove(Contact tobeDeleteEMUser) {
        EMUserList.remove(tobeDeleteEMUser);
    }

    private static class ViewHolder {
        NetworkImageView avatar;
        TextView unreadMsgView;
        TextView nameTextview;
        TextView tvHeader;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(res, null);
            holder.avatar = (NetworkImageView) convertView.findViewById(cn.gen.fulicenter.R.id.avatar);
            holder.unreadMsgView = (TextView) convertView.findViewById(cn.gen.fulicenter.R.id.unread_msg_number);
            holder.nameTextview = (TextView) convertView.findViewById(cn.gen.fulicenter.R.id.name);
            holder.tvHeader = (TextView) convertView.findViewById(cn.gen.fulicenter.R.id.header);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Contact EMUser = getItem(position);
//		if(EMUser == null)

        //设置nick，demo里不涉及到完整user，用username代替nick显示
        String username = EMUser.getMContactCname();
        String header = EMUser.getHeader();
        if (position == 0 || header != null && !header.equals(getItem(position - 1).getHeader())) {
            if (TextUtils.isEmpty(header)) {
                holder.tvHeader.setVisibility(View.GONE);
            } else {
                holder.tvHeader.setVisibility(View.VISIBLE);
                holder.tvHeader.setText(header);
            }
        } else {
            holder.tvHeader.setVisibility(View.GONE);
        }
        //显示申请与通知item
        if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
            holder.nameTextview.setText(EMUser.getMUserNick());
            holder.avatar.setDefaultImageResId(R.drawable.new_friends_icon);
            holder.avatar.setImageUrl("", RequestManager.getImageLoader());
            int unreadMsgCount = ((DemoHXSDKHelper)HXSDKHelper.getInstance())
                    .getContactList().get(Constant.NEW_FRIENDS_USERNAME).getUnreadMsgCount();
            if (EMUser.getMUserUnreadMsgCount() > 0||unreadMsgCount>0) {
                holder.unreadMsgView.setVisibility(View.VISIBLE);
//			    holder.unreadMsgView.setText(EMUser.getUnreadMsgCount()+"");
            } else {
                holder.unreadMsgView.setVisibility(View.INVISIBLE);
            }
        } else if (username.equals(Constant.GROUP_USERNAME)) {
            //群聊item
            holder.nameTextview.setText(EMUser.getMUserNick());
            holder.avatar.setDefaultImageResId(R.drawable.groups_icon);
            holder.avatar.setImageUrl("", RequestManager.getImageLoader());
            holder.avatar.setErrorImageResId(R.drawable.groups_icon);
        } else if (username.equals(Constant.CHAT_ROOM)) {
            //群聊item
            holder.nameTextview.setText(EMUser.getMUserNick());
            holder.avatar.setImageResource(cn.gen.fulicenter.R.drawable.groups_icon);
        } else if (username.equals(Constant.CHAT_ROBOT)) {
            //Robot item
            holder.nameTextview.setText(EMUser.getMUserNick());
            holder.avatar.setImageResource(cn.gen.fulicenter.R.drawable.groups_icon);
        } else {
//		    holder.nameTextview.setText(EMUser.getNick());
            //设置用户头像
//			UserUtils.setUserAvatar(getContext(), username, holder.avatar);
            UserUtils.setUserBeanAvatar(username, holder.avatar);
            UserUtils.setUserBeanNick(username, holder.nameTextview);
            if (holder.unreadMsgView != null)
                holder.unreadMsgView.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    @Override
    public Contact getItem(int position) {
        return EMUserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {

        return EMUserList == null ? 0 : EMUserList.size();
    }

    public int getPositionForSection(int section) {
        return positionOfSection.get(section);
    }

    public int getSectionForPosition(int position) {
        return sectionOfPosition.get(position);
    }

    @Override
    public Object[] getSections() {
        positionOfSection = new SparseIntArray();
        sectionOfPosition = new SparseIntArray();
        int count = getCount();
        list = new ArrayList<String>();
        list.add(mContext.getString(cn.gen.fulicenter.R.string.search_header));
        positionOfSection.put(0, 0);
        sectionOfPosition.put(0, 0);
        for (int i = 1; i < count; i++) {

            String letter = getItem(i).getHeader();
            EMLog.d(TAG, "contactadapter getsection getHeader:" + letter + " name:" + getItem(i).getMContactCname());
            int section = list.size() - 1;
            if (list.get(section) != null && !list.get(section).equals(letter)) {
                list.add(letter);
                section++;
                positionOfSection.put(section, i);
            }
            sectionOfPosition.put(i, section);
        }
        return list.toArray(new String[list.size()]);
    }

    public Filter getFilter() {
        if (myFilter == null) {
            myFilter = new MyFilter(EMUserList);
        }
        return myFilter;
    }

    private class MyFilter extends Filter {
        List<Contact> mOriginalList = null;

        public MyFilter(List<Contact> myList) {
            this.mOriginalList = myList;
        }

        @Override
        protected synchronized FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (mOriginalList == null) {
                mOriginalList = new ArrayList<Contact>();
            }
            EMLog.d(TAG, "contacts original size: " + mOriginalList.size());
            EMLog.d(TAG, "contacts copy size: " + copyEMUserList.size());

            if (prefix == null || prefix.length() == 0) {
                results.values = copyEMUserList;
                results.count = copyEMUserList.size();
            } else {
                String prefixString = prefix.toString();
                final int count = mOriginalList.size();
                final ArrayList<Contact> newValues = new ArrayList<Contact>();
                for (int i = 0; i < count; i++) {
                    final Contact EMUser = mOriginalList.get(i);
                    String username = EMUser.getMContactCname();

                    if (username.startsWith(prefixString)) {
                        newValues.add(EMUser);
                    } else {
                        final String[] words = username.split(" ");
                        final int wordCount = words.length;

                        // Start at index 0, in case valueText starts with space(s)
                        for (int k = 0; k < wordCount; k++) {
                            if (words[k].startsWith(prefixString)) {
                                newValues.add(EMUser);
                                break;
                            }
                        }
                    }
                }
                results.values = newValues;
                results.count = newValues.size();
            }
            EMLog.d(TAG, "contacts filter results size: " + results.count);
            return results;
        }

        @Override
        protected synchronized void publishResults(CharSequence constraint,
                                                   FilterResults results) {
            EMUserList.clear();
            EMUserList.addAll((List<Contact>) results.values);
            EMLog.d(TAG, "publish contacts filter results size: " + results.count);
            if (results.count > 0) {
                notiyfyByFilter = true;
                notifyDataSetChanged();
                notiyfyByFilter = false;
            } else {
                notifyDataSetInvalidated();
            }
        }
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (!notiyfyByFilter) {
            copyEMUserList.clear();
            copyEMUserList.addAll(EMUserList);
        }
    }


}
