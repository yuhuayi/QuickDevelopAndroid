package app.zhengbang.teme.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.zhengbang.teme.AppConstants;
import app.zhengbang.teme.activity.base.BaseMainFragment;
import com.event.EventBus;
import com.event.EventMessage;
import com.util.L;
import com.util.LogUtil;

/**
 * Created by victor on 2014/11/10 0010.
 */
public class TemplatePage extends BaseMainFragment {
    private int requestCode = 001;

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
//        View view = inflater.inflate(R.layout.register_layout, null);
        View view = null;
        return view;
    }

    @Override
    protected void findViewById(View view) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.main_title_right_bt:
//                mActivity.hideSoftInputMethod(mActivity, view);
//                popBackStack();
//                break;
            default:
                break;
        }
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic() {
        initPage();
    }

    private void initPage() {
        // 初始化标题
        try {
            String title = getArguments().getString("title");
//            mActivity.sub_title_center_tv.setVisibility(View.VISIBLE);
//            mActivity.sub_title_center_tv.setText(title + "");
        } catch (Exception e) {
            LogUtil.info(e.getMessage()+"");
        }
        // 注册到EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onEventMainThread(EventMessage event) {
        int i = event.getRequestCode();
        int j =  AppConstants.requestCode;
        Bundle bundle = new Bundle();
        bundle.putString("title","");
        bundle.putString("url","");
        bundle.putBoolean("",true);
        bundle.putInt("",1);
        if (i == requestCode && event.getType().equals(AppConstants.TemplateEngine)) {
            boolean success = event.getBundle().getBoolean("success");
            if (success) {
            } else {
            }
        }

        try {

        } catch (Exception e) {
            L.e(String.valueOf(e.getMessage()));
        }
//        showToast("刷新成功");
//        showToast("已加载"+trades.size()+"条数据");
//        mActivity.finish();
//        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
//        mActivity.startActivity()
//        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);;
        
//        if (!RegexTool.checkUserName(username)) {
//            showToast("请用字母、数字、下划线,至少6个字符");
//            return;
//        }
//        
//        if (!RegexTool.checkPassword(psd_1)) {
//            showToast("密码只能为 8 - 32 位数字，字母及常用符号组成\n");
//            return;
//        }
    }
}
