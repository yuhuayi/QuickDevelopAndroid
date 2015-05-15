package com.manager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.universalimageloader.core.DisplayImageOptions;
import com.universalimageloader.core.assist.ImageScaleType;
import com.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.zhengbang.TeMe.R;

/**
 * Created by victor on 2014/10/13 0013.
 */
public class ImageLoaderManager {

    public static DisplayImageOptions getDisplayImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(R.color.default_color)
                .showImageForEmptyUri(R.color.default_color)
                .showImageOnFail(R.color.default_color)
                .cacheInMemory()
                .cacheOnDisc()
                .decodingOptions(getOptions())
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        // 图片圆角处理，慎用，会增大内存使用
//                .displayer(new RoundedBitmapDisplayer(30)).
//                build();
        return options;
    }

    public static DisplayImageOptions getDisplayImageOptions1() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.color.default_color)
                .showImageOnFail(R.color.default_color)
                .resetViewBeforeLoading()
                .cacheOnDisc()
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
        return options;
    }

    public static DisplayImageOptions getHeadPortraitDisplayImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.head_portrait)
                .showImageOnFail(R.drawable.head_portrait)
                .resetViewBeforeLoading()
                .cacheOnDisc()
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
        return options;
    }


    public static BitmapFactory.Options getOptions() {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        return opt;
    }
}
