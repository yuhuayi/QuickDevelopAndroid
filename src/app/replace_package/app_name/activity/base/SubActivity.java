package app.zhengbang.teme.activity.base;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import app.zhengbang.teme.AppConstants;
import app.zhengbang.teme.activity.subpage.AddVideoPage;
import app.zhengbang.teme.activity.subpage.myself.PersonSpaceEditDataPage;
import com.event.EventBus;
import com.event.EventMessage;
import com.util.*;
import com.zhengbang.TeMe.R;

import java.util.List;

public class SubActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "SubActivity";
    public FragmentManager subFragmentManager;
    public int fragment_content_id;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.sub_activity_layout);
    }

    @Override
    protected void findViewById() {
        fragment_content_id = R.id.sub_page_container;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic() {
        eventBus = EventBus.getDefault();
        subFragmentManager = getSupportFragmentManager();
        showTargetPage();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getSupportFragmentManager() != null) {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            if (!ListUtils.isEmpty(fragments)) {
                for (int i = 0; i < fragments.size() - 1; i++) {
                    fragments.get(i).getFragmentManager().popBackStackImmediate();
                }
            }
        }
    }

    @Override
    public void showTargetPage() {
        try {
            Intent intent = getIntent();
            Bundle targetPageBundle = intent.getBundleExtra("targetPageBundle");
            String targetPage = targetPageBundle.getString("targetPage");
            Class<?> targetPageClazz = Class.forName(targetPage);
            FragmentTransaction ft = subFragmentManager.beginTransaction();
            ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_right_in, R.anim.push_right_out); // 左右推进推出
            Fragment targetPageFragment = (Fragment) targetPageClazz.newInstance();
            ft.add(fragment_content_id, targetPageFragment, targetPage);
            if (intent.getExtras() != null) {
                targetPageFragment.setArguments(intent.getExtras());
            }
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            LogUtil.info(e.getMessage() + "");
        }
    }

    public void onEventMainThread(EventMessage event) {
        int i = event.getRequestCode();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public EventBus eventBus;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode > 0) {
            Bundle bundle = new Bundle();
            Uri uri = null;
            if (requestCode == AppConstants.photo_code && data != null) {// 相册
                uri = data.getData();
                String upLoadFilePath = FileUtils.getImagePathFromUri(this, uri);
                bundle.clear();
                bundle.putString("path", AppConstants.FILE_SCHEME + upLoadFilePath);
            } else {
                if (requestCode == AppConstants.carmer_code) {// 相机
                    bundle.clear();
                    // 获取相机返回的数据，并转换为图片格式
                    if (data != null && data.getData() != null) {
                        uri = data.getData();
                    }
                    if (uri == null) {
                        if (ImageUtils.photoUri != null) { // 很恶心的问题, 有的手机拿不到Url
                            uri = ImageUtils.photoUri;
                        }
                    }
                    String upLoadFilePath = FileUtils.getRealPathFromURI(this, uri);
                    bundle.putString("path", AppConstants.FILE_SCHEME + upLoadFilePath);
                }
            }
            String path = bundle.getString("path");
            if (StringUtils.isEmpty(path)) {
                return;
            } else {
                if (!FileUtils.isExist(Tools.getRealPath(path))) {
                    return;
                }
            }
            switch (AppConstants.current_page_take_pic) {
                case 20004:// PersonSpaceEditDataPage.request_CODE
                    eventBus.post(new EventMessage(requestCode, PersonSpaceEditDataPage.TAG, bundle));
                    break;
                case 20005:// PersonSpaceEditDataPage.request_CODE
                    eventBus.post(new EventMessage(requestCode, AddVideoPage.TAG, bundle));
                    break;
            }
        }
    }
}
