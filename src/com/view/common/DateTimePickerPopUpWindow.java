package com.view.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.util.ScreenUtils;
import com.view.wheel.WheelMain;
import com.zhengbang.TeMe.R;

import java.util.Calendar;

public class DateTimePickerPopUpWindow extends PopupWindow {

	private View mMenuView;
	private Activity mContext;
	private Button button_ok;
	private Button button_cancel;
	private WheelMain wheelMain;
	private onOkButtonClickListener buttonClickListener;

	public DateTimePickerPopUpWindow(Activity context, String _wheel_title, onOkButtonClickListener buttonClickListener) {
		super(context);
		this.mContext = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.wheel_select_date, null);
		button_cancel = (Button) mMenuView.findViewById(R.id.wheel_button_cancel);
		button_ok = (Button) mMenuView.findViewById(R.id.wheel_button_ok);
		TextView wheel_title = (TextView) mMenuView.findViewById(R.id.wheel_title_ok);
		wheel_title.setText(_wheel_title + "");
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});
		setListener();

		wheelMain = new WheelMain(mContext, mMenuView);
		wheelMain.screenheight = ScreenUtils.getScreenWidth(mContext);
		Calendar calendar = Calendar.getInstance();
		// calendar.setTime(dateFormat.parse(time));设置指定时间
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		wheelMain.setTime(year, month, day);
		this.buttonClickListener = buttonClickListener;

	}

	public void show(View parent, int gravity, int x, int y) {
		showAtLocation(parent, gravity, x, y);
	}

	protected void setListener() {
		button_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				buttonClickListener.onOkButtonClick(wheelMain.getY_MTime());
				dismiss();
			}
		});

		button_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	public interface onOkButtonClickListener {
		void onOkButtonClick(String time);
	}
}
