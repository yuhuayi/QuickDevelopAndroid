package app.zhengbang.teme.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import app.zhengbang.teme.activity.base.BaseActivity;
import app.zhengbang.teme.activity.base.SubActivity;
import app.zhengbang.teme.activity.register_login.StartActivity;
import app.zhengbang.teme.activity.register_login.StartFragment;
import app.zhengbang.teme.service.LoginService;

import com.event.EventMessage;
import com.util.PreferencesUtils;
import com.zhengbang.TeMe.R;

/**
 * 欢迎界面
 */
public class SplashActivity extends BaseActivity {

    private int firstSplash = -1;
    private boolean login_state = false;

    private void start() {
        AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
        aa.setDuration(2000);
        ImageView v = (ImageView) findViewById(R.id.splash_image);
        v.startAnimation(aa);
        aa.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {

            }

            public void onAnimationEnd(Animation animation) {
                boolean firstIn = PreferencesUtils.getBoolean(SplashActivity.this, "firstIn", true);
                Intent intent = null;
                if (firstIn) {
                    // 第一次运行,打开引导页
                    intent = new Intent(SplashActivity.this, GuideActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // 切换到登录页
                    if (login_state) {
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    } else {
                        gotoSubActivity(StartActivity.class, StartFragment.class.getName(), null,true);
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
        });
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.splash_activity);
    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic() {
        LoginService autoLogin = new LoginService(this);
        autoLogin.autoUserLogin(new LoginService.LoginSuccess() {
            @Override
            public void onLoginSuccess() {
                login_state = true;
                start();
            }

            @Override
            public void onLoginFail() {
                login_state = false;
                start();
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void showTargetPage() {

    }

    public void onEventMainThread(EventMessage event) {

    }
}
