package app.zhengbang.teme.activity.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import app.zhengbang.teme.listener.OnFragmentResultListener;
import com.Constants;
import com.event.EventBus;
import com.event.EventMessage;
import com.umeng.analytics.MobclickAgent;
import com.util.PromptManager;
import com.zhengbang.TeMe.R;

public abstract class BaseFragment extends Fragment implements OnClickListener, OnFragmentResultListener, Parcelable {
    public static BaseActivity mActivity;
    protected BaseFragment backFragment;
    protected LinearLayout title_left_ll;
    protected RelativeLayout ga_title_rl;
    protected ImageView title_left_bt;
    protected TextView title_left_tv;
    protected TextView title_center_tv;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        return init(inflater, container);
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle arguments = getArguments();
        if (arguments != null) {
//			backFragment = (BaseFragment) arguments.getSerializable("currentFragment");
            backFragment = (BaseFragment) arguments.getParcelable("currentFragment");
        }
        MobclickAgent.onPageStart("MainScreen"); // 统计页面
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainScreen");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (BaseActivity) getActivity();
    }

    /**
     * 初始activity方法
     */
    private View init(LayoutInflater inflater, ViewGroup container) {
        View loadViewLayout = loadViewLayout(inflater, container);
        findViewById(loadViewLayout);
        setListener();
        processLogic();
        return loadViewLayout;
    }

    /**
     * 加载页面layout
     */
    protected abstract View loadViewLayout(LayoutInflater inflater, ViewGroup container);

    /**
     * 加载页面元素
     */
    protected abstract void findViewById(View view);

    /**
     * 点击事件
     */
    public abstract void onClick(View view);

    /**
     * 设置各种事件的监听器
     */
    protected abstract void setListener();

    /**
     * 接收发送到的消息
     */
    public abstract void onEventMainThread(EventMessage event);

    /**
     * 业务逻辑处理，主要与后端交互
     */
    protected abstract void processLogic();

    /**
     * 模拟后退键
     */
    public void popBackStack() {
        if (mActivity != null && mActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
            if (Constants.FRAGMENT_SKIP_MODE) {
                FragmentTransaction fragmentTransaction = mActivity.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.push_right_in, R.anim.push_right_out, R.anim.push_left_in, R.anim.push_left_out);
                fragmentTransaction.hide(this);
                if (backFragment != null) {
                    fragmentTransaction.show(backFragment);
                }
                fragmentTransaction.commitAllowingStateLoss();
            }
            if (mActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                mActivity.getSupportFragmentManager().popBackStackImmediate();
            }
        } else {
            if (mActivity != null) {
                mActivity.finish();
                mActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        }
    }

    /**
     * 模拟后退键
     */
    public void popBackStack(boolean showAnimations) {
        if (mActivity != null && mActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
            if (Constants.FRAGMENT_SKIP_MODE) {
                FragmentTransaction fragmentTransaction = mActivity.getSupportFragmentManager().beginTransaction();
                if (showAnimations)
                    fragmentTransaction.setCustomAnimations(R.anim.push_right_in, R.anim.push_right_out, R.anim.push_left_in,
                            R.anim.push_left_out);
                fragmentTransaction.hide(this);
                if (backFragment != null) {
                    fragmentTransaction.show(backFragment);
                }
                fragmentTransaction.commitAllowingStateLoss();
            }
            if (mActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                mActivity.getSupportFragmentManager().popBackStackImmediate();
            }
        } else {
            if (mActivity != null) {
                mActivity.finish();
                mActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        }
    }

    /**
     * 模拟后退键 , 并携带参数
     *
     * @param bundle page回调的参数Pr
     */
    public void popBackStack(Bundle bundle, boolean showAnimations) {
        // if (mActivity != null &&
        // !ListUtils.isEmpty(mActivity.getSupportFragmentManager().getFragments()))
        // {
        if (mActivity != null && mActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
            if (backFragment != null && bundle != null) {
                backFragment.onFragmentResult(this, bundle);
            }
            if (Constants.FRAGMENT_SKIP_MODE) {
                FragmentTransaction fragmentTransaction = mActivity.getSupportFragmentManager().beginTransaction();
                if (showAnimations) {
                    fragmentTransaction.setCustomAnimations(R.anim.push_right_in, R.anim.push_right_out, R.anim.push_left_in,
                            R.anim.push_left_out);
                }
                fragmentTransaction.hide(this);
                if (backFragment != null) {
                    fragmentTransaction.show(backFragment);
                }
                fragmentTransaction.commitAllowingStateLoss();

            }
            if (mActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                mActivity.getSupportFragmentManager().popBackStackImmediate();
            }
        } else {
            if (mActivity != null) {
                mActivity.finish();
                mActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 解注册
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);// 反注册EventBus
        }
    }

    /**
     * 弹出Toast
     *
     * @param text
     */
    public static void showToast(String text) {
        PromptManager.showCustomToast(mActivity, text);
    }

    public void onFragmentResult(Fragment fragment, Bundle bundle) {
    }
}
