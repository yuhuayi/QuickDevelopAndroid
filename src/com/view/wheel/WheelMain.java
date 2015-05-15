package com.view.wheel;

import android.content.Context;
import android.util.Log;
import android.view.View;
import com.zhengbang.TeMe.R;

import java.util.Arrays;
import java.util.List;

public class WheelMain {

	private View view;
	private WheelView wv_year;
	private WheelView wv_month;
	private WheelView wv_day;
	private WheelView wv_hours;
	private WheelView wv_mins;
	private Context mContext;
	public int screenheight;

	private static int START_YEAR = 1942, END_YEAR = 2049;

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public static int getSTART_YEAR() {
		return START_YEAR;
	}

	public static void setSTART_YEAR(int sTART_YEAR) {
		START_YEAR = sTART_YEAR;
	}

	public static int getEND_YEAR() {
		return END_YEAR;
	}

	public static void setEND_YEAR(int eND_YEAR) {
		END_YEAR = eND_YEAR;
	}

	public WheelMain(Context mContext, View view) {
		super();
		this.view = view;
		this.mContext = mContext;
		setView(view);
		viewInit();
	}

	public void viewInit() {
		wv_year = (WheelView) view.findViewById(R.id.year);
		wv_month = (WheelView) view.findViewById(R.id.month);
		wv_day = (WheelView) view.findViewById(R.id.day);
		wv_hours = (WheelView) view.findViewById(R.id.hours);
		wv_mins = (WheelView) view.findViewById(R.id.min);
	}

	/**
	 * 时分选择器
	 */
	public void showHours(int hours, int min) {
		wv_year.setVisibility(View.GONE);
		wv_month.setVisibility(View.GONE);
		wv_day.setVisibility(View.GONE);
		wv_hours.setVisibility(View.VISIBLE);
		wv_mins.setVisibility(View.VISIBLE);
		// 时

		wv_hours.setViewAdapter(new NumericWheelAdapter(mContext, 0, 23, "时"));// 设置"年"的显示数据
		wv_hours.setCyclic(true);// 可循环滚动
		wv_hours.setCurrentItem(hours);// 初始化时显示的数据

		// 分

		wv_mins.setViewAdapter(new NumericWheelAdapter(mContext, 0, 59, "分"));
		wv_mins.setCyclic(true);
		wv_mins.setCurrentItem(min);

	}

	public void setTime(final int year, final int month, final int day) {
		initDateTimePicker(year, month, day);
	}

	/**
	 * @Description: TODO 弹出阳历日期时间选择器
	 */
	public void initDateTimePicker(int year, int month, int day) {

		wv_year.setVisibility(View.VISIBLE);
		wv_month.setVisibility(View.VISIBLE);
		wv_day.setVisibility(View.GONE);
		wv_hours.setVisibility(View.GONE);
		wv_mins.setVisibility(View.GONE);

		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };

		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);
		// 年
		wv_year.setViewAdapter(new NumericWheelAdapter(mContext, START_YEAR, END_YEAR, "年"));// 设置"年"的显示数据
		wv_year.setCyclic(true);// 可循环滚动
		wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据
		wv_year.setVisibleItems(5);
		// 月
		wv_month.setViewAdapter(new NumericWheelAdapter(mContext, 1, 12, "月"));
		wv_month.setCyclic(true);
		wv_month.setCurrentItem(month);
		wv_month.setVisibleItems(5);

		// 日

		wv_day.setCyclic(true);
		// 判断大小月及是否闰年,用来确定"日"的数据
		if (list_big.contains(String.valueOf(month + 1))) {
			wv_day.setViewAdapter(new NumericWheelAdapter(mContext, 1, 31, "日"));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			wv_day.setViewAdapter(new NumericWheelAdapter(mContext, 1, 30, "日"));
		} else {
			// 闰年
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				wv_day.setViewAdapter(new NumericWheelAdapter(mContext, 1, 29, "日"));
			else
				wv_day.setViewAdapter(new NumericWheelAdapter(mContext, 1, 28, "日"));
		}
		wv_day.setCurrentItem(day - 1);
		wv_day.setVisibleItems(5);

		// 添加"年"监听
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int year_num = newValue + START_YEAR;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
					wv_day.setViewAdapter(new NumericWheelAdapter(mContext, 1, 31, "日"));
				} else if (list_little.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
					wv_day.setViewAdapter(new NumericWheelAdapter(mContext, 1, 30, "日"));
				} else {
					if ((year_num % 4 == 0 && year_num % 100 != 0) || year_num % 400 == 0)
						wv_day.setViewAdapter(new NumericWheelAdapter(mContext, 1, 29, "日"));
					else
						wv_day.setViewAdapter(new NumericWheelAdapter(mContext, 1, 28, "日"));
				}
			}
		};
		// 添加"月"监听
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(month_num))) {
					wv_day.setViewAdapter(new NumericWheelAdapter(mContext, 1, 31, "日"));
				} else if (list_little.contains(String.valueOf(month_num))) {
					wv_day.setViewAdapter(new NumericWheelAdapter(mContext, 1, 30, "日"));
				} else {
					if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year.getCurrentItem() + START_YEAR) % 100 != 0)
							|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
						wv_day.setViewAdapter(new NumericWheelAdapter(mContext, 1, 29, "日"));
					else
						wv_day.setViewAdapter(new NumericWheelAdapter(mContext, 1, 28, "日"));
				}
			}
		};
		wv_year.addChangingListener(wheelListener_year);
		wv_month.addChangingListener(wheelListener_month);
	}

	public String getTime() {
		StringBuffer sb = new StringBuffer();
		sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-").append((wv_month.getCurrentItem() + 1)).append("-")
				.append((wv_day.getCurrentItem() + 1));
		return sb.toString();
	}

	public String getY_MTime() {
		StringBuffer sb = new StringBuffer();
		sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-").append((wv_month.getCurrentItem() + 1));
		return sb.toString();
	}

	public int getYear() {

		return (wv_year.getCurrentItem() + START_YEAR);
	}

	public int getMonth() {
		return (wv_month.getCurrentItem() + 1);
	}

	public int getDay() {
		return (wv_day.getCurrentItem() + 1);
	}

	public int getHours() {
		return (wv_hours.getCurrentItem());
	}

	public int getMin() {
		return (wv_mins.getCurrentItem());
	}

}
