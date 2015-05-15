package com.view.common;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TextView;
import com.zhengbang.TeMe.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DatePickerView extends AlertDialog implements
        OnDateChangedListener {

    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String DAY = "day";

    private final DatePicker mDatePicker;
    private final OnDateSetListener mCallBack;
    private View view;
    private final Calendar mCalendar;
    private Context mContext;
    private TextView tvTitle;

    /**
     * The callback used to indicate the user is done filling in the date.
     */
    public interface OnDateSetListener {
        void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth);
    }

    public DatePickerView(Context context, OnDateSetListener callBack,int year, int monthOfYear, int dayOfMonth) {
        this(context, 0, callBack, year, monthOfYear, dayOfMonth);
    }

    public DatePickerView(Context context, int theme, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        super(context, theme);
        this.mContext = context;

        mCallBack = callBack;

        mCalendar = Calendar.getInstance();

        Context themeContext = getContext();

        LayoutInflater inflater = (LayoutInflater) themeContext .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 然后把DatePicker嵌套进去就可以了。
        view = inflater.inflate(R.layout.dialog_date_picker, null);
        tvTitle = (TextView) view.findViewById(R.id.date_picker_title);
        // setView(view);
        mDatePicker = (DatePicker) view.findViewById(R.id.datePicker);
        mDatePicker.init(year, monthOfYear, dayOfMonth, this);
        updateTitle(year, monthOfYear, dayOfMonth);

        // 实现自己的标题和ok按钮
        setButton();
    }

    public void myShow() {
        // 自己实现show方法，主要是为了把setContentView方法放到show方法后面，否则会报错。
        show();
        setContentView(view);
    }

    private void setTitle(String title) {
        // 获取自己定义的title布局并赋值。
        tvTitle.setText(title);
    }

    private void setButton() {
        // 获取自己定义的响应按钮并设置监听，直接调用构造时传进来的CallBack接口（为了省劲，没有自己写接口，直接用之前本类定义好的）同时关闭对话框。
        view.findViewById(R.id.date_picker_ok).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCallBack != null) {
                            mDatePicker.clearFocus();
                            mCallBack.onDateSet(mDatePicker,
                                    mDatePicker.getYear(),
                                    mDatePicker.getMonth(),
                                    mDatePicker.getDayOfMonth());
                        }
                        dismiss();
                    }
                });
    }


    private void updateTitle(int year, int month, int day) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, day);
//	            String title = DateUtils.formatDateTime(mContext,
//	            		mCalendar.getTimeInMillis(),
//	                    DateUtils.FORMAT_SHOW_DATE
//	                    | DateUtils.FORMAT_SHOW_WEEKDAY
//	                    | DateUtils.FORMAT_SHOW_YEAR
//	                    | DateUtils.FORMAT_ABBREV_MONTH
//	                    | DateUtils.FORMAT_ABBREV_WEEKDAY);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        String title = formatter.format(mCalendar.getTime());
        mCalendar.getTime();
        setTitle(title);
    }

    /**
     * Gets the {@link android.widget.DatePicker} contained in this dialog.
     *
     * @return The calendar view.
     */
    public DatePicker getDatePicker() {
        return mDatePicker;
    }

    public void updateDate(int year, int monthOfYear, int dayOfMonth) {
        mDatePicker.updateDate(year, monthOfYear, dayOfMonth);
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(YEAR, mDatePicker.getYear());
        state.putInt(MONTH, mDatePicker.getMonth());
        state.putInt(DAY, mDatePicker.getDayOfMonth());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int year = savedInstanceState.getInt(YEAR);
        int month = savedInstanceState.getInt(MONTH);
        int day = savedInstanceState.getInt(DAY);
        mDatePicker.init(year, month, day, this);
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mDatePicker.init(year, monthOfYear, dayOfMonth, this);
        updateTitle(year, monthOfYear, dayOfMonth);
    }
}