<<<<<<< HEAD:src/app/replace_package/app_name/application/TeMeApp.java
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
package app.zhengbang.teme.application;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import app.zhengbang.teme.AppConstants;
import app.zhengbang.teme.activity.MainActivity;
import app.zhengbang.teme.activity.base.BaseFragment;
import app.zhengbang.teme.activity.base.SubActivity;
import app.zhengbang.teme.activity.register_login.StartFragment;
import app.zhengbang.teme.bean.TeMeCategory;
import app.zhengbang.teme.bean.UserBean;
import com.event.EventBus;
import com.event.EventMessage;
import com.lidroid.xutils.DbUtils;
import com.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.universalimageloader.core.ImageLoader;
import com.universalimageloader.core.ImageLoaderConfiguration;
import com.universalimageloader.core.assist.QueueProcessingType;
import com.util.PromptManager;
import com.zhengbang.TeMe.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TeMeApp extends Application {
    
    //感觉很诡异
    // 什么时候是主线, 什么时候是分支
    

    // 退出应用程序,销毁Activity相关
    public static List<Activity> activitys = null;
    public static String project_id;
    public static String LOCATION_TAG;
    public static DbUtils weBusDb;
    // 环信
    public static Context applicationContext;
    private static TeMeApp instance;
    // login user name
    public final String PREF_USERNAME = "username";
    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */
    public static String currentUserNick = "";
    public UserBean currentUser;
    public AlertDialog alertDialog = null;
    public MainActivity mainActivity = null;
    private ArrayList<TeMeCategory> teMeCategories = null;

    public ArrayList<TeMeCategory> getTeMeCategories() {
        return teMeCategories;
    }

    public void setTeMeCategories(ArrayList<TeMeCategory> teMeCategories) {
        this.teMeCategories = teMeCategories;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public UserBean getCurrentUser() {
        if (currentUser==null){
            currentUser = new UserBean();
            currentUser.setUid("");
        }
        return currentUser;
    }

    public void setCurrentUser(UserBean currentUser) {
        this.currentUser = currentUser;
    }

    public void showNeedLoginDialog(final Activity activity, final BaseFragment fragment) {
        alertDialog = PromptManager.showCustomDialog(activity, "提示", "需要登录后才能访问该信息", "登录", "先看看", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog != null && alertDialog.isShowing())
                    alertDialog.dismiss();

                if (activity instanceof MainActivity) {
                    activity.finish();
                    ((MainActivity) activity).gotoSubActivity(SubActivity.class, StartFragment.class.getName(), null);
                } else if (activity instanceof SubActivity) {
                    ((SubActivity) activity).changeSubFragment(fragment, ((SubActivity) activity).fragment_content_id, StartFragment.class.getName(), null);
                } else {
                    activity.finish();
                    Intent intent = new Intent(activity, SubActivity.class);
                    Bundle targetPageBundle = new Bundle();
                    targetPageBundle.putString("targetPage", StartFragment.class.getName());
                    startActivity(intent);
                    activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog != null && alertDialog.isShowing())
                    alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        instance = this;
        // -------------------//
        // 注册到EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        activitys = new LinkedList<Activity>();
        initJPush();
        // 初始化数据库
        // weBusDb = DBManager.getDb(this);
        // ImageLoader初始化
        initImageLoader();
        // CrashHandler.getInstance().init(this);
    }

    public static TeMeApp getInstance() {
        return instance;
    }


    private void initJPush() {
        // JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
        // JPushInterface.init(this); // 初始化 JPush
    }

    public void onEventMainThread(EventMessage event) {
        int i = event.getRequestCode();
        if (i == AppConstants.PromptRequestCode && event.getType().equals(AppConstants.Prompt)) {
            PromptManager.showCustomToast(this, "请检查网络");
        }
    }

    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs() // Remove
                        // for
                        // release
                        // app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        if (activitys != null && activitys.size() > 0) {
            if (!activitys.contains(activity)) {
                activitys.add(activity);
            }
        } else {
            activitys.add(activity);
        }
    }

    public void removeActivity(Activity activity) {
        if (activitys != null) {
            if (activitys.contains(activity)) {
                activitys.remove(activity);
            }
        }
    }

    // 遍历所有Activity并finish
    public void exit() {
        if (activitys != null && activitys.size() > 0) {
            for (Activity activity : activitys) {
                if (activity != null) {
                    activity.finish();
                }
            }
        }
        System.exit(0);
    }
}
=======
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
package app.zhengbang.teme.application;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import app.zhengbang.teme.AppConstants;
import app.zhengbang.teme.activity.MainActivity;
import app.zhengbang.teme.activity.base.BaseFragment;
import app.zhengbang.teme.activity.base.SubActivity;
import app.zhengbang.teme.activity.register_login.StartFragment;
import app.zhengbang.teme.bean.TeMeCategory;
import app.zhengbang.teme.bean.UserBean;
import com.event.EventBus;
import com.event.EventMessage;
import com.lidroid.xutils.DbUtils;
import com.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.universalimageloader.core.ImageLoader;
import com.universalimageloader.core.ImageLoaderConfiguration;
import com.universalimageloader.core.assist.QueueProcessingType;
import com.util.PromptManager;
import com.zhengbang.TeMe.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AppNameApplication extends Application {
    
    // 感觉git 碉堡了
    
    // 退出应用程序,销毁Activity相关
    public static List<Activity> activitys = null;
    public static String project_id;
    public static String LOCATION_TAG;
    public static DbUtils weBusDb;
    // 环信
    public static Context applicationContext;
    private static TeMeApp instance;
    // login user name
    public final String PREF_USERNAME = "username";
    
    public static String currentUserNick = "";
    public UserBean currentUser;
    public AlertDialog alertDialog = null;
    public MainActivity mainActivity = null;
    private ArrayList<TeMeCategory> teMeCategories = null;
    
    
    
    //
    public ArrayList<TeMeCategory> getTeMeCategories() {
        return teMeCategories;
    }
    
    public void setTeMeCategories(ArrayList<TeMeCategory> teMeCategories) {
        this.teMeCategories = teMeCategories;
    }
    
    public MainActivity getMainActivity() {
        return mainActivity;
    }
    
    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
    
    public UserBean getCurrentUser() {
        if (currentUser==null){
            currentUser = new UserBean();
            currentUser.setUid("");
        }
        return currentUser;
    }
    
    public void setCurrentUser(UserBean currentUser) {
        this.currentUser = currentUser;
    }
    
    
    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        instance = this;
        // -------------------//
        // 注册到EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        activitys = new LinkedList<Activity>();
        initJPush();
        // 初始化数据库
        // weBusDb = DBManager.getDb(this);
        // ImageLoader初始化
        initImageLoader();
        // CrashHandler.getInstance().init(this);
    }
    
    public static TeMeApp getInstance() {
        return instance;
    }
    
    
    private void initJPush() {
        // JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
        // JPushInterface.init(this); // 初始化 JPush
    }
    
    public void onEventMainThread(EventMessage event) {
        int i = event.getRequestCode();
        if (i == AppConstants.PromptRequestCode && event.getType().equals(AppConstants.Prompt)) {
            PromptManager.showCustomToast(this, "请检查网络");
        }
    }
    
    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
        .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
        .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs() // Remove
        // for
        // release
        // app
        .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
    
    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        if (activitys != null && activitys.size() > 0) {
            if (!activitys.contains(activity)) {
                activitys.add(activity);
            }
        } else {
            activitys.add(activity);
        }
    }
    
    public void removeActivity(Activity activity) {
        if (activitys != null) {
            if (activitys.contains(activity)) {
                activitys.remove(activity);
            }
        }
    }
    
    // 遍历所有Activity并finish
    public void exit() {
        if (activitys != null && activitys.size() > 0) {
            for (Activity activity : activitys) {
                if (activity != null) {
                    activity.finish();
                }
            }
        }
        System.exit(0);
    }
}
>>>>>>> origin/test:src/app/replace_package/app_name/application/AppNameApplication.java
