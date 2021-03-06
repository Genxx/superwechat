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
package cn.gen.superwechat;

import java.util.List;
import java.util.Map;

import android.content.Context;

import cn.gen.superwechat.applib.model.DefaultHXSDKModel;
import cn.gen.superwechat.db.DemoDBManager;
import cn.gen.superwechat.db.EMUserDao;
import cn.gen.superwechat.domain.RobotUser;
import cn.gen.superwechat.domain.EMUser;

public class DemoHXSDKModel extends DefaultHXSDKModel{

    public DemoHXSDKModel(Context ctx) {
        super(ctx);
    }

    public boolean getUseHXRoster() {
        return true;
    }
    
    // demo will switch on debug mode
    public boolean isDebugMode(){
        return true;
    }
    
    public boolean saveContactList(List<EMUser> contactList) {
        EMUserDao dao = new EMUserDao(context);
        dao.saveContactList(contactList);
        return true;
    }

    public Map<String, EMUser> getContactList() {
        EMUserDao dao = new EMUserDao(context);
        return dao.getContactList();
    }
    
    public void saveContact(EMUser EMUser){
    	EMUserDao dao = new EMUserDao(context);
    	dao.saveContact(EMUser);
    }
    
    public Map<String, RobotUser> getRobotList(){
    	EMUserDao dao = new EMUserDao(context);
    	return dao.getRobotUser();
    }

    public boolean saveRobotList(List<RobotUser> robotList){
    	EMUserDao dao = new EMUserDao(context);
    	dao.saveRobotUser(robotList);
    	return true;
    }
    
    public void closeDB() {
        DemoDBManager.getInstance().closeDB();
    }
    
    @Override
    public String getAppProcessName() {
        return context.getPackageName();
    }
}
