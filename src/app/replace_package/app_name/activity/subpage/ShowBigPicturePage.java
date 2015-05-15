package app.zhengbang.teme.activity.subpage;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import app.zhengbang.teme.activity.base.BaseSubFragment;
import app.zhengbang.teme.adapter.ShowPicViewPageAdapter;
import com.view.photoview.HackyViewPager;
import com.zhengbang.TeMe.R;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;


public class ShowBigPicturePage extends BaseSubFragment {
    private ViewPager mViewPager;
    private ImageView[] points;
    private int currentIndex = 0;
    private FrameLayout _frameLayout;
    private ArrayList<String> source = new ArrayList<String>();
    private Drawable drawOnlinePoint;
    private Drawable drawInvisiblePoint;
    private int pLength;
    private TextView right_tv;
    private View title_show_pic_left_ll;

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        View ga_show_big_img_page = inflater.inflate(R.layout.show_big_img_page, container, false);
        InitView((LinearLayout) ga_show_big_img_page);
        return ga_show_big_img_page;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private void InitView(LinearLayout linearLayout) {
        mViewPager = new HackyViewPager(mActivity);
        Bundle bundle = getArguments();
        String defaultimage = "";
        // 圆点和距离底部默认值
        int bottomMargin = 5;
        if (bundle != null) {
            if (bundle != null) {
                defaultimage = bundle.getString("defaultimage", "");
                source = bundle.getStringArrayList("source");
                bottomMargin = bundle.getInt("bottomMargin");
                currentIndex = bundle.getInt("index");
            }
        }
        pLength = source.size();
        _frameLayout = new FrameLayout(mActivity);
        _frameLayout.setLayoutParams(new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        _frameLayout.addView(mViewPager);

        linearLayout.addView(_frameLayout);
        // 初始化底部小点
        initPoint(bottomMargin);
        mViewPager.setAdapter(new ShowPicViewPageAdapter(mActivity, source, defaultimage) {
        });
        mViewPager.setOnPageChangeListener(new pageListener());
        setCurDot(currentIndex);
        setCurView(currentIndex);
    }

    @Override
    protected void findViewById(View view) {
        right_tv = (TextView) view.findViewById(R.id.title_show_big_pic_right_tv);
        title_show_pic_left_ll = view.findViewById(R.id.title_show_pic_left_ll);
    }


    @Override
    public void onClick(View view) {
        if (view == title_show_pic_left_ll) {
            popBackStack();
        }
    }

    @Override
    protected void setListener() {
        title_show_pic_left_ll.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        if (right_tv != null) {
            right_tv.setText((currentIndex + 1) + "/" + pLength);
        }
        right_tv.setVisibility(View.GONE);
    }

    /**
     * 初始化底部小点
     */
    private void initPoint(int bottomMargin) {
        drawOnlinePoint = getResources().getDrawable(R.drawable.point_hover);
        drawInvisiblePoint = getResources().getDrawable(R.drawable.point_normal);

        LinearLayout point_linearLayout = new LinearLayout(mActivity);
        LinearLayout.LayoutParams layoutParams_point = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        point_linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        point_linearLayout.setLayoutParams(layoutParams_point);
        point_linearLayout.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

        points = new ImageView[pLength];
        for (int i = 0; points != null & i < points.length; i++) {
            ImageView point = new ImageView(mActivity);
            point.setScaleType(ImageView.ScaleType.CENTER_CROP);
            point.setPadding(0, 0, bottomMargin, bottomMargin);
            point.setImageDrawable((i == currentIndex ? drawOnlinePoint : drawInvisiblePoint));
            points[i] = point;
            point_linearLayout.addView(point);
        }
        _frameLayout.addView(point_linearLayout);

        if (source != null && source.size() <= 1) {
            point_linearLayout.setVisibility(View.INVISIBLE);
        }

        if (source != null && source.size() > 10) {
            point_linearLayout.setVisibility(View.INVISIBLE);
        }
    }

    private class pageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            if (right_tv != null) {
                right_tv.setText((currentIndex + 1) + "/" + pLength);
            }
            setCurDot(position);
        }
    }

    /**
     * 设置当前页面的位置
     */
    private void setCurView(int position) {
        if (position < 0 || position >= pLength) {
            return;
        }
        mViewPager.setCurrentItem(position);
    }

    /**
     * 设置当前的小点的位置
     */
    private void setCurDot(int positon) {
        if (positon < 0 || positon > pLength - 1) {
            return;
        }
        for (ImageView iv : points) {
            iv.setImageDrawable(drawInvisiblePoint);
        }
        currentIndex = positon;
        points[currentIndex].setImageDrawable(drawOnlinePoint);
    }

}
