package app.zhengbang.teme.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import app.zhengbang.teme.AppConstants;
import app.zhengbang.teme.activity.base.BaseActivity;
import app.zhengbang.teme.activity.mainpage.CircleOfFriendsPage;
import app.zhengbang.teme.activity.mainpage.DiscoverPage;
import app.zhengbang.teme.activity.mainpage.MessagePage;
import app.zhengbang.teme.activity.mainpage.MySelfPage;
import app.zhengbang.teme.activity.mainpage.publish.PublishProductPage;
import app.zhengbang.teme.application.TeMeApp;

import com.event.EventBus;
import com.event.EventMessage;
import com.util.*;
import com.zhengbang.TeMe.R;

import java.util.HashMap;

/**
 * 主页面
 */
public class MainActivity extends BaseActivity {
	private static final String TAG = "MainActivity";
	public View teme_bottom_layout; // 底部导航
	public Fragment tab0Page;
	public Fragment tab1Page;
	public Fragment tab2Page;
	public boolean tab2PageIsShow; // 不能删
	public int currentPageIndex = 0;
	public Fragment tab3Page;
	public Fragment tab4Page;
	private View tab0_View, tab1_View, tab2_View, tab3_View, tab4_View;
	public View main_content;
	private HashMap<String, Fragment> fragmentMap = new HashMap<String, Fragment>();
	private ImageView tab0_IMG, tab1_IMG, tab2_IMG, tab4_IMG, tab3_IMG;

	private TextView tab0_TV, tab1_TV, tab2_TV, tab4_TV, tab3_TV;

	public FragmentManager fragmentManager;
	public Bundle mBundle;
	public EventBus eventBus;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.activity_main_activity);
	}

	@Override
	protected void findViewById() {
		main_content = findViewById(R.id.main_content);
		teme_bottom_layout = findViewById(R.id.webus_bottom_layout);

		tab0_View = findViewById(R.id.tab0_View);
		tab1_View = findViewById(R.id.tab1_View);
		tab2_View = findViewById(R.id.tab2_View);
		tab3_View = findViewById(R.id.tab3_View);
		tab4_View = findViewById(R.id.tab4_View);

		tab0_IMG = (ImageView) findViewById(R.id.tab0_IMG);
		tab1_IMG = (ImageView) findViewById(R.id.tab1_IMG);
		tab2_IMG = (ImageView) findViewById(R.id.tab2_IMG);
		tab3_IMG = (ImageView) findViewById(R.id.tab3_IMG);
		tab4_IMG = (ImageView) findViewById(R.id.tab4_IMG);

		tab0_TV = (TextView) findViewById(R.id.tab0_TV);
		tab1_TV = (TextView) findViewById(R.id.tab1_TV);
		tab2_TV = (TextView) findViewById(R.id.tab2_TV);
		tab3_TV = (TextView) findViewById(R.id.tab3_TV);
		tab4_TV = (TextView) findViewById(R.id.tab4_TV);
	}

	@Override
	protected void setListener() {
		tab0_View.setOnClickListener(this);
		tab1_View.setOnClickListener(this);
		tab2_View.setOnClickListener(this);
		tab3_View.setOnClickListener(this);
		tab4_View.setOnClickListener(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setTabSelection(0);
		// if (TeMeApp.getInstance().getCurrentUser() == null) {
		// setTabSelection(0);
		// } else {
		// setTabSelection(1);
		// }
	}

	@Override
	protected void processLogic() {
		if (!EventBus.getDefault().isRegistered(this)) {
			EventBus.getDefault().register(this);
		}
		fragmentManager = getSupportFragmentManager();
		mBundle = new Bundle();
		eventBus = EventBus.getDefault();
		TeMeApp.getInstance().setMainActivity(this);

		setTabSelection(0);
		// if (TeMeApp.getInstance().getCurrentUser() == null) {
		// setTabSelection(0);
		// } else {
		// setTabSelection(1);
		// }
		Intent intent = getIntent();
		if (intent != null) {
		}
		// UpdateVersionUtil.checkNewAPPVersion(this, false);
	}

	@Override
	public void showTargetPage() {
		try {
			Intent intent = getIntent();
			Bundle targetPageBundle = intent.getBundleExtra("targetPageBundle");
			String targetPage = targetPageBundle.getString("targetPage");
			Class<?> targetPageClazz = Class.forName(targetPage);
			Fragment targetPageFragment = fragmentMap.get(targetPage);
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_right_in, R.anim.push_right_out); // 左右推进推出
			if (targetPageFragment == null) {
				targetPageFragment = (Fragment) targetPageClazz.newInstance();
				fragmentMap.put(targetPage, targetPageFragment);
				ft.add(R.id.main_content, targetPageFragment);
			} else {
				ft.show(targetPageFragment);
			}
			if (intent.getExtras() != null) {
				targetPageFragment.setArguments(intent.getExtras());
			}
			ft.commitAllowingStateLoss();
		} catch (Exception e) {
			LogUtil.info(e.getMessage() + "");
		}
	}

	/**
	 * 切换Fragment
	 * 
	 * @param isAddToBackStackAndShowBack
	 *            是否添加到返回栈
	 * @param bundle
	 *            界面间传递数据
	 * @param targetFragment
	 *            切换目标Fragment
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void changeSubFragment(Fragment currentFragment, Fragment targetFragment, Bundle bundle, boolean isAddToBackStackAndShowBack) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_right_in, R.anim.push_right_out); // 左右推进推出
		if (currentFragment != null) {
			ft.hide(currentFragment);
		}
		if (bundle != null) {
			targetFragment.setArguments(bundle);
		}
		ft.show(targetFragment);
		ft.commitAllowingStateLoss();
		if (isAddToBackStackAndShowBack) {
			ft.addToBackStack(null);
		}
		ft.commitAllowingStateLoss();
	}

	public void onEventMainThread(EventMessage event) {
		int i = event.getRequestCode();
		String type = event.getType();
		if (i == AppConstants.DEAL_PublishActivity_Back && type.equals(PublishActivity.class.getSimpleName())) {
			teme_bottom_layout.setVisibility(View.VISIBLE);
			setTabSelection(currentPageIndex);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab0_View:
			// 当点击了消息tab时，选中第1个tab
			setTabSelection(0);
			currentPageIndex = 0;
			tab2PageIsShow = false;
			break;
		case R.id.tab1_View:
			if (StringUtils.isEmpty(TeMeApp.getInstance().getCurrentUser().getUid())) {
				TeMeApp.getInstance().showNeedLoginDialog(this, null);
				return;
			}
			// 当点击了消息tab时，选中第1个tab
			setTabSelection(1);
			currentPageIndex = 1;
			tab2PageIsShow = false;
			break;
		case R.id.tab2_View:
			if (StringUtils.isEmpty(TeMeApp.getInstance().getCurrentUser().getUid())) {
				TeMeApp.getInstance().showNeedLoginDialog(this, null);
				return;
			}
			// 当点击了消息tab时，选中第1个tab
			setTabSelection(2);
			tab2PageIsShow = true;
			break;
		case R.id.tab3_View:
			if (StringUtils.isEmpty(TeMeApp.getInstance().getCurrentUser().getUid())) {
				TeMeApp.getInstance().showNeedLoginDialog(this, null);
				return;
			}
			// 当点击了消息tab时，选中第1个tab
			setTabSelection(3);
			currentPageIndex = 3;
			tab2PageIsShow = false;
			break;
		case R.id.tab4_View:
			if (StringUtils.isEmpty(TeMeApp.getInstance().getCurrentUser().getUid())) {
				TeMeApp.getInstance().showNeedLoginDialog(this, null);
				return;
			}
			// 当点击了消息tab时，选中第1个tab
			setTabSelection(4);
			currentPageIndex = 4;
			tab2PageIsShow = false;
			break;
		default:
			break;
		}
	}

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
			case 001:
				break;

			}
		}
	}

	/**
	 * 根据传入的index参数来设置选中的tab页。
	 * 
	 * @param index
	 */
	public void setTabSelection(int index) {
		// 每次选中之前先清楚掉上次的选中状态
		// UserCenterPage.user_head_img
		main_content.setVisibility(View.VISIBLE);
		teme_bottom_layout.setVisibility(View.VISIBLE);
		clearSelection();
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);
		main_content.setVisibility(View.VISIBLE);
		switch (index) {
		case 0:
			teme_bottom_layout.setVisibility(View.VISIBLE);
			tab0_IMG.setImageResource(R.drawable.icon_maintab_transfer_press);
			tab0_TV.setTextColor(getResources().getColor(R.color.tv_selectt_tab));

			tab0_View.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_0_selected));

			if (tab0Page == null) {
				// 如果MessageFragment为空，则创建一个并添加到界面上
				tab0Page = new DiscoverPage();
				transaction.add(R.id.main_content, tab0Page);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(tab0Page);
			}
			break;
		case 1:
			tab1_View.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_1_selected));

			teme_bottom_layout.setVisibility(View.VISIBLE);
			tab1_IMG.setImageResource(R.drawable.icon_maintab_wherebus_press);
			tab1_TV.setTextColor(getResources().getColor(R.color.tv_selectt_tab));
			if (tab1Page == null) {
				// 如果ContactsFragment为空，则创建一个并添加到界面上
				tab1Page = new CircleOfFriendsPage();
				transaction.add(R.id.main_content, tab1Page);
			} else {
				// 如果ContactsFragment不为空，则直接将它显示出来
				transaction.show(tab1Page);
			}
			break;
		case 2:
			// teme_bottom_layout.setVisibility(View.GONE);
			// tab2_IMG.setImageResource(R.drawable.icon_maintab_line_press);
			// tab2_TV.setTextColor(getResources().getColor(R.color.tv_selectt_tab));
			// if (tab2Page == null) {
			// // 如果ContactsFragment为空，则创建一个并添加到界面上
			// tab2Page = new PublishPage();
			// transaction.add(R.id.main_content, tab2Page);
			// } else {
			// // 如果ContactsFragment不为空，则直接将它显示出来
			// transaction.show(tab2Page);
			// }
			startActivity(new Intent(this, PublishActivity.class));
			break;
		case 3:
			tab3_View.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_3_selected));
			teme_bottom_layout.setVisibility(View.VISIBLE);
			tab3_IMG.setImageResource(R.drawable.icon_maintab_station_press);
			tab3_TV.setTextColor(getResources().getColor(R.color.tv_selectt_tab));
			if (tab3Page == null) {
				// 如果ContactsFragment为空，则创建一个并添加到界面上
				tab3Page = new MessagePage();
				transaction.add(R.id.main_content, tab3Page);
			} else {
				// 如果ContactsFragment不为空，则直接将它显示出来
				transaction.show(tab3Page);
			}
			break;
		case 4:
			tab4_View.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_4_selected));

			teme_bottom_layout.setVisibility(View.VISIBLE);
			tab4_IMG.setImageResource(R.drawable.icon_maintab_self_press);
			tab4_TV.setTextColor(getResources().getColor(R.color.tv_selectt_tab));
			if (tab4Page == null) {
				// 如果ContactsFragment为空，则创建一个并添加到界面上
				tab4Page = new MySelfPage();
				transaction.add(R.id.main_content, tab4Page);
			} else {
				// 如果ContactsFragment不为空，则直接将它显示出来
				transaction.show(tab4Page);
			}
			break;

		default:
			break;
		}
		transaction.commitAllowingStateLoss();
	}

	/**
	 * 清除掉所有的选中状态。
	 */
	private void clearSelection() {

		tab0_View.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_0_unselected));
		tab1_View.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_1_unselected));
		tab3_View.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_3_unselected));
		tab4_View.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_4_unselected));

		tab0_IMG.setImageResource(R.drawable.icon_maintab_transfer_normal);
		tab0_TV.setTextColor(Color.parseColor("#82858b"));

		tab1_IMG.setImageResource(R.drawable.icon_maintab_wherebus_normal);
		tab1_TV.setTextColor(Color.parseColor("#82858b"));

		tab2_IMG.setImageResource(R.drawable.icon_maintab_line_normal);
		tab2_TV.setTextColor(Color.parseColor("#82858b"));

		tab4_IMG.setImageResource(R.drawable.icon_maintab_self_normal);
		tab4_TV.setTextColor(Color.parseColor("#82858b"));

		tab3_IMG.setImageResource(R.drawable.icon_maintab_station_normal);
		tab3_TV.setTextColor(Color.parseColor("#82858b"));
	}

	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (tab0Page != null) {
			transaction.hide(tab0Page);
		}
		if (tab1Page != null) {
			transaction.hide(tab1Page);
		}
		if (tab2Page != null) {
			transaction.hide(tab2Page);
		}
		if (tab3Page != null) {
			transaction.hide(tab3Page);
		}
		if (tab4Page != null) {
			transaction.hide(tab4Page);
		}
	}

	/**
	 * 捕捉返回事件按钮 因为此 Activity 继承 TabActivity 用 onKeyDown 无响应，所以改用
	 * dispatchKeyEvent 一般的 Activity 用 onKeyDown 就可以了
	 */
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (fragmentManager.getBackStackEntryCount() > 1) {
				colseSoftInputMethod(this, teme_bottom_layout);
				fragmentManager.popBackStackImmediate();
				return true;
			} else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
				if (tab2PageIsShow) {
					teme_bottom_layout.setVisibility(View.VISIBLE);
					setTabSelection(currentPageIndex);
					tab2PageIsShow = false;
				} else {
					exitApp();
				}
				return true;
			} else {
				return super.onKeyDown(keyCode, event);
			}
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	/**
	 * 退出程序
	 */
	private void exitApp() {
		// 判断2次点击事件时间
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			showToast("再按一次退出程序");
			exitTime = System.currentTimeMillis();
		} else {
			TeMeApp.getInstance().exit();
			finish();
			overridePendingTransition(0, R.anim.goldav_app_exit);
		}
	}

}
