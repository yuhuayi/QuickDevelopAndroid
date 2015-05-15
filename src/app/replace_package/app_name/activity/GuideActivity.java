package app.zhengbang.teme.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import app.zhengbang.teme.activity.base.BaseActivity;
import app.zhengbang.teme.activity.base.BaseFragment;
import app.zhengbang.teme.activity.base.SubActivity;
import app.zhengbang.teme.activity.register_login.StartFragment;
import app.zhengbang.teme.activity.subpage.GuideBasePage;
import com.zhengbang.TeMe.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能综述：引导用户熟悉软件
 *
 * @author Administrator
 */
public class GuideActivity extends SubActivity {
    private ViewPager viewPager;

    private List<BaseFragment> fragments;         // Tab页面列表

    private int[] imageIds = new int[]{R.drawable.help_one, R.drawable.help_two, R.drawable.help_three};

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_guide);
    }

    @Override
    protected void findViewById() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic() {
        fragments = new ArrayList<BaseFragment>();
        for (int i = 0; i < imageIds.length; i++) {
            GuideBasePage guideBasePage = new GuideBasePage(imageIds[i]);
            fragments.add(guideBasePage);
        }
        fragments.add(new StartFragment());
        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {


            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };

        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onClick(View view) {

    }
}
