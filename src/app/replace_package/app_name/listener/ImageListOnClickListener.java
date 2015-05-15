package app.zhengbang.teme.listener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import app.zhengbang.teme.activity.base.BaseFragment;
import app.zhengbang.teme.activity.base.SubActivity;
import app.zhengbang.teme.activity.subpage.ShowBigPicturePage;

import java.util.ArrayList;

/**
 * Created by victor on 2014/11/27 0027.
 */
public class ImageListOnClickListener implements ViewPager.OnClickListener {
    private SubActivity mActivity;
    private ArrayList<String> mPaths;
    private BaseFragment currentFragment;
    private int index;

    public ImageListOnClickListener(SubActivity activity, BaseFragment currentFragment, int index , ArrayList<String> paths) {
        this.mActivity = activity;
        this.mPaths = paths;
        this.index = index;
        this.currentFragment = currentFragment;
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("source", mPaths);
        bundle.putInt("bottomMargin", 5);//间距
        bundle.putInt("index", index);
        mActivity.changeSubFragment(currentFragment, mActivity.fragment_content_id,
                ShowBigPicturePage.class.getName(), bundle);
    }
}