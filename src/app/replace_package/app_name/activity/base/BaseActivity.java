//package app.yitobe.workingchat.activity.base;
package app.zhengbang.teme.activity.base;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import app.zhengbang.teme.application.TeMeApp;
import com.Constants;
import com.event.EventBus;
import com.event.EventMessage;
import com.umeng.analytics.MobclickAgent;
import com.util.PromptManager;
import com.zhengbang.TeMe.R;

public abstract class BaseActivity extends FragmentActivity implements OnClickListener {
    protected Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        TeMeApp.getInstance().addActivity(this);
        initView();
    }

    protected abstract void showTargetPage();

    public void onEventMainThread(EventMessage event) {

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);       //统计时长
    }

    public void onPause() {
        super.onPause();
        dismissLoadingDialog();
        MobclickAgent.onPause(this);
    }

    /**
     * 初始activity方法
     */
    private void initView() {
        loadViewLayout();
        findViewById();
        setListener();
        processLogic();
    }

    /**
     * 加载页面layout
     */
    protected abstract void loadViewLayout();

    /**
     * 加载页面元素
     */
    protected abstract void findViewById();

    /**
     * 设置各种事件的监听器
     */
    protected abstract void setListener();

    /**
     * 业务逻辑处理，主要与后端交互
     */
    protected abstract void processLogic();

    /**
     * 点击事件
     */
    public abstract void onClick(View view);

    /**
     * 弹出Toast
     *
     * @param text
     */
    public void showToast(String text) {
        PromptManager.showCustomToast(this, text);
    }

    public void initBaseProgressDialog() {
        loadingDialog =  PromptManager.showTMFrameLoadDataDialog(this);
    }

    public void showLoadingDialog(String msg) {
        if (loadingDialog == null) {
            initBaseProgressDialog();
        }
        TextView tipTextView = (TextView) loadingDialog.findViewById(R.id.tipTextView);// 提示文字
        if (null != msg) {
            tipTextView.setText(msg);// 设置加载信息
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    public void dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 添加动画效果
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }

    protected void onDestroy() {
        super.onDestroy();
        TeMeApp.getInstance().removeActivity(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 收起软键盘
     */
    public static void colseSoftInputMethod(Context context, View v) {
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public void gotoSubActivity(Class targetActivity, String targetPage, Bundle bundle) {
        Intent intent = new Intent(this, targetActivity);
        Bundle targetPageBundle = new Bundle();
        targetPageBundle.putString("targetPage", targetPage);
        intent.putExtra("targetPageBundle", targetPageBundle);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void gotoSubActivity(Class targetActivity, Bundle bundle) {
        gotoSubActivity(targetActivity, null, bundle, false);
    }

    public void gotoSubActivity(Class targetActivity, String targetPage, Bundle bundle, boolean finishActivity) {
        gotoSubActivity(targetActivity, targetPage, bundle);
        if (finishActivity) {
            finish();
        }
    }

    /**
     * 切换Fragment
     *
     * @param _bundle 界面间传递数据 切换目标Fragment
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void changeSubFragment(BaseFragment currentFragment, int content_id, String targetFragmentString, Bundle _bundle) {
        changeSubFragmentWithAnim(currentFragment, content_id, targetFragmentString, _bundle, true);
    }

    private void changeSubFragmentWithAnim(BaseFragment currentFragment, int content_id, String targetFragmentString, Bundle _bundle, boolean isShowAnimation) {
        Bundle bundle = new Bundle();
        if (_bundle != null)
            bundle.putAll(_bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (isShowAnimation)
            ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_right_in, R.anim.push_right_out); // 左右推进推出
        if (Constants.FRAGMENT_SKIP_MODE) {
            if (currentFragment != null) {
                ft.hide(currentFragment);
            }
        }
        Fragment targetFragment = getSupportFragmentManager().findFragmentByTag(targetFragmentString);
        if (targetFragment == null) {
            try {
                Class<?> targetPageClazz = Class.forName(targetFragmentString);
                targetFragment = (Fragment) targetPageClazz.newInstance();
                if (Constants.FRAGMENT_SKIP_MODE) {
                    ft.add(content_id, targetFragment, targetFragmentString);
                } else {
                    ft.replace(content_id, targetFragment);
                }
               
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (Constants.FRAGMENT_SKIP_MODE) {
                ft.show(targetFragment);
            } else {
                if (!targetFragment.isVisible())
                    ft.replace(content_id, targetFragment);
            }
        }
        ft.addToBackStack(null);
        if (currentFragment != null) {
//            bundle.putSerializable("currentFragment", currentFragment);
            bundle.putParcelable("currentFragment", currentFragment);
        }
        if (targetFragment != null) {
            if (targetFragment.isAdded()) {
                Bundle arguments = targetFragment.getArguments();
                if (arguments != null) {
                    arguments.clear();
                    arguments.putAll(bundle);
                }
            } else {
                targetFragment.setArguments(bundle);
            }
        }

        ft.commitAllowingStateLoss();
    }

    /**
     * 切换Fragment
     *
     * @param _bundle 界面间传递数据 切换目标Fragment
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void changeSubFragment(BaseFragment currentFragment, int content_id, String targetFragmentString,
                                  Bundle _bundle, boolean isShowAnimation) {
        changeSubFragmentWithAnim(currentFragment, content_id, targetFragmentString, _bundle, isShowAnimation);
    }
}
