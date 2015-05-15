package app.zhengbang.teme.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.event.EventBus;
import com.event.EventMessage;
import com.util.PromptManager;

public abstract class BaseSubFragment extends BaseFragment implements OnClickListener {
    public static SubActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return init(inflater, container);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (SubActivity) getActivity();
    }

    public void onEventMainThread(EventMessage event) {

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
     * 业务逻辑处理，主要与后端交互
     */
    protected abstract void processLogic();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);// 反注册EventBus
        }
    }
}
