package app.zhengbang.teme.listener;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.view.ViewPager;
import android.widget.TextView;
import com.zhengbang.TeMe.R;

/**
 * Created by victor on 2014/11/27 0027.
 */
public   class PageListener implements ViewPager.OnPageChangeListener {
    private Activity mActivity;
    private TextView mTextView1;
    private TextView mTextView2;

    public PageListener(Activity activity, TextView textView1, TextView textView2) {
        this.mActivity = activity;
        this.mTextView1 = textView1;
        this.mTextView2 = textView2;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            mTextView1.setTextColor(mActivity.getResources().getColor(R.color.black));
            mTextView2.setTextColor(mActivity.getResources().getColor(R.color.gray));
        } else {
            mTextView1.setTextColor(mActivity.getResources().getColor(R.color.gray));
            mTextView2.setTextColor(mActivity.getResources().getColor(R.color.black));
        }
    }

}