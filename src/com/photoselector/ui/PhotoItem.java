package com.photoselector.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.photoselector.model.PhotoModel;
import com.universalimageloader.core.DisplayImageOptions;
import com.universalimageloader.core.ImageLoader;
import com.util.PromptManager;
import com.zhengbang.TeMe.R;

/**
 * @author Aizaz AZ
 */

public class PhotoItem extends LinearLayout implements OnCheckedChangeListener,
        OnLongClickListener {

    private ImageView ivPhoto;
    private CheckBox cbPhoto;
    private onPhotoItemCheckedListener listener;
    private PhotoModel photo;
    private boolean isCheckAll;
    private onItemClickListener l;
    private int position;
    private Context context;

    public static DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.ic_picture_loading)
            .showImageOnFail(R.drawable.ic_picture_loadfailed)
            .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
            .bitmapConfig(Bitmap.Config.RGB_565).build();

    public PhotoItem(Context context) {
        super(context);
    }

    public PhotoItem(Context context, onPhotoItemCheckedListener listener) {
        this(context);
        LayoutInflater.from(context).inflate(R.layout.layout_photoitem, this, true);
        this.listener = listener;
        this.context = context;

        setOnLongClickListener(this);

        ivPhoto = (ImageView) findViewById(R.id.iv_photo_lpsi);
        cbPhoto = (CheckBox) findViewById(R.id.cb_photo_lpsi);

        cbPhoto.setOnCheckedChangeListener(this); //
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isCheckAll) {
            if (!photo.isChecked() && PhotoSelectorActivity.selected != null && PhotoSelectorActivity.selected.size() >= PhotoSelectorActivity.MAX_IMAGE) {
                PromptManager.showCustomToast(context, "最多选取" + PhotoSelectorActivity.MAX_IMAGE + "张");
                cbPhoto.setChecked(false);
                photo.setChecked(false);
                setSelected(false);
                ivPhoto.clearColorFilter();
                return;
            }
            listener.onCheckedChanged(photo, buttonView, isChecked); //
        }
        if (isChecked) {
            setDrawingable();
            ivPhoto.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        } else {
            ivPhoto.clearColorFilter();
        }
        photo.setChecked(isChecked);
    }

    public void setImageDrawable(final PhotoModel photo) {
        this.photo = photo;
        // You may need this setting form some custom ROM(s)
      /*
       * new Handler().postDelayed(new Runnable() {
		 *
		 * @Override public void run() { ImageLoader.getInstance().displayImage(
		 * "file://" + photo.getOriginalPath(), ivPhoto); } }, new
		 * Random().nextInt(10));
		 */

        ImageLoader.getInstance().displayImage(
                "file://" + photo.getOriginalPath(), ivPhoto, imageOptions);
    }

    private void setDrawingable() {
        ivPhoto.setDrawingCacheEnabled(true);
        ivPhoto.buildDrawingCache();
    }

    @Override
    public void setSelected(boolean selected) {
        if (photo == null) {
            return;
        }
        isCheckAll = true;
        cbPhoto.setChecked(selected);
        isCheckAll = false;
    }

    public void setOnClickListener(onItemClickListener l, int position) {
        this.l = l;
        this.position = position;
    }

    public static interface onPhotoItemCheckedListener {
        public void onCheckedChanged(PhotoModel photoModel,
                                     CompoundButton buttonView, boolean isChecked);
    }

    public interface onItemClickListener {
        public void onItemClick(int position);
    }

    @Override
    public boolean onLongClick(View v) {
        if (l != null)
            l.onItemClick(position);
        return true;
    }

}
