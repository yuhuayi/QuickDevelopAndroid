package com.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.view.common.DatePickerView;
import com.view.common.MyDialogFragment;
import com.view.wheel.WheelView;
import com.zhengbang.TeMe.R;

import java.util.Calendar;

/**
 * 提示信息的管理
 */

public class PromptManager {

    private static MyOnClickListener clickListener;
    // 当测试阶段时true
    private static final boolean isShow = true;
    private static Toast mToast;

    /**
     * @param context
     * @方法名称:showNoNetWork
     * @描述: 没网情况, 可以跳转到设置
     * @创建人：曹睿翔
     * @创建时间：2014年9月3日 上午10:24:12
     * @备注：
     * @返回类型：void
     */
    public static void showNoNetWork(final Context context) {
        Builder builder = new Builder(context);
        builder.setIcon(R.drawable.icon)//
                .setTitle(R.string.app_name)//
                .setMessage("当前无网络").setPositiveButton("设置", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 跳转到系统的网络设置界面
                try {
                    Intent intent = new Intent();
                    intent.setAction("android.settings.WIRELESS_SETTINGS");
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    context.startActivity(intent);
                } catch (Exception e) {
                    Intent mIntent = new Intent("/");
                    ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
                    mIntent.setComponent(comp);
                    mIntent.setAction("android.intent.action.VIEW");
                    context.startActivity(mIntent);
                }

            }
        }).setNegativeButton("知道了", null);

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public interface MyOnClickListener {
        void onPositiveBtClick();

        void onNegativeBtClick();
    }

    // 提供一个回调方法
    public static void setMyOnClickListener(MyOnClickListener listener) {
        clickListener = listener;
    }

    /**
     * 得到自定义的progressDialog
     *
     * @param context
     * @param msg
     * @return
     */
    @SuppressLint("InflateParams")
    public static Dialog showLoadDataDialog(Activity context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        if (null != msg) {
            tipTextView.setText(msg);// 设置加载信息
        }
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);

        loadingDialog.setCancelable(true);// 不可以用“返回键”取消
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return loadingDialog;
    }

    /**
     * 得到自定义的progressDialog
     *
     * @param context
     * @return
     */
    @SuppressLint("InflateParams")
    public static Dialog showTMFrameLoadDataDialog(Activity context) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View v = inflater.inflate(R.layout.teme_frame_loading_dialog, null);// 得到加载view
        View v =    View.inflate(context,R.layout.teme_frame_loading_dialog, null);

        final ImageView lodding_img = (ImageView) v.findViewById(R.id.lodding_img);
        final AnimationDrawable anim = (AnimationDrawable) lodding_img.getBackground();
        Dialog loadingDialog = new Dialog(context, R.style.teme_loading_dialog);
        loadingDialog.setCancelable(true);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setContentView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        loadingDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                anim.start();
            }
        });
        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                anim.stop();
            }
        });
        return loadingDialog;
    }


    public static AlertDialog showCustomDialog(Context context, String title, String message, String leftText, String rightText,
                                               View.OnClickListener leftOnClickListener, View.OnClickListener rightOnClickListener) {
        Builder builder = new Builder(context);
        View view = View.inflate(context, R.layout.dialog_layout, null);
        TextView dtitle = (TextView) view.findViewById(R.id.dialog_title);
        TextView dmessage = (TextView) view.findViewById(R.id.dialog_message);
        TextView dcancel = (TextView) view.findViewById(R.id.dialog_cancel);
        TextView dcommit = (TextView) view.findViewById(R.id.dialog_commit);

        dtitle.setText(title);
        dmessage.setText(message);
        dcancel.setText(leftText);
        dcommit.setText(rightText);

        dcancel.setOnClickListener((View.OnClickListener) leftOnClickListener);
        dcommit.setOnClickListener((View.OnClickListener) rightOnClickListener);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(view, 0, 0, 0, 0);
        return dialog;
    }

    public static AlertDialog getDatePickerView(Context context, DatePickerView.OnDateSetListener _onDateSetListener) {
        Calendar calendar = Calendar.getInstance();
        DatePickerView _datePickerDialog = new DatePickerView(context, _onDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        _datePickerDialog.setCancelable(true);
        _datePickerDialog.setCanceledOnTouchOutside(true);
        return _datePickerDialog;
    }

    public static DialogFragment showFragmentCustomDialog(FragmentManager fragmentManager, String title, String message, String leftText,
                                                          String rightText, OnClickListener leftOnClickListener, OnClickListener rightOnClickListener) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        // Create and show the dialog.
        DialogFragment newFragment = new MyDialogFragment(title, message, leftText, rightText, leftOnClickListener, rightOnClickListener);
        Bundle args = new Bundle();
        int mStackLevel = 0;
        args.putInt("num", mStackLevel);
        // 传递参数才用到
        newFragment.setArguments(args);
        newFragment.show(ft, "dialog");
        return newFragment;
    }

    public static AlertDialog showSimpleDialog(Context context, String title, String message, String btText) {
        Builder builder = new Builder(context);
        View view = View.inflate(context, R.layout.simple_dialog_layout, null);
        TextView content = (TextView) view.findViewById(R.id.content);
        TextView dialog_title = (TextView) view.findViewById(R.id.dialog_title);
        TextView dcommit = (TextView) view.findViewById(R.id.dialog_commit);

        content.setText(message);
        dialog_title.setText(title);
        dcommit.setText(btText);

        final AlertDialog simpleDialog = builder.create();
        simpleDialog.setView(view, 0, 0, 0, 0);
        simpleDialog.setCanceledOnTouchOutside(false);
        dcommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleDialog.dismiss();
            }
        });
        simpleDialog.show();
        return simpleDialog;
    }

    public static AlertDialog showSimpleCustomDialog(Context context, String title, String message, String btText,
                                                     View.OnClickListener btOnClickListener) {
        Builder builder = new Builder(context);
        View view = View.inflate(context, R.layout.simple_dialog_layout, null);
        TextView content = (TextView) view.findViewById(R.id.content);
        TextView dialog_title = (TextView) view.findViewById(R.id.dialog_title);
        TextView dcommit = (TextView) view.findViewById(R.id.dialog_commit);

        content.setText(message);
        dialog_title.setText(title);
        dcommit.setText(btText);

        AlertDialog simpleDialog = builder.create();
        simpleDialog.setView(view, 0, 0, 0, 0);
        simpleDialog.setCanceledOnTouchOutside(false);
        dcommit.setOnClickListener(btOnClickListener);
//		simpleDialog.show();
        return simpleDialog;
    }

    public static AlertDialog showMessageDialog(Context context, String message) {
        Builder builder = new Builder(context);
        // 获取LayoutInflater对象
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 由layout文件创建一个View对象
        View layout = inflater.inflate(R.layout.toast_layout, null);

        TextView textView = (TextView) layout.findViewById(R.id.text0);
        textView.setText(message);

        AlertDialog simpleDialog = builder.create();
        simpleDialog.setView(layout, 0, 0, 0, 0);
        simpleDialog.setCanceledOnTouchOutside(true);
        return simpleDialog;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static AlertDialog showCallDialog(final Context context, final String mobile) {
        Builder builder = new Builder(context, R.style.NoBlackDialog);
        View view = View.inflate(context, R.layout.dialog_callphone_layout, null);
        TextView dcancel = (TextView) view.findViewById(R.id.dialog_cancel);
        TextView dcommit = (TextView) view.findViewById(R.id.dialog_commit);

        final AlertDialog calldialog = builder.create();
        calldialog.setView(view, 0, 0, 0, 0);
        dcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calldialog.dismiss();
            }
        });

        dcommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyIntent.CallPhone(context, mobile);
                calldialog.dismiss();
            }
        });

        return calldialog;
    }

    /**
     * @return
     * @方法名称:logOutDialog
     * @描述: TODO
     * @创建人：曹睿翔
     * @创建时间：2014年9月9日 上午11:15:05
     * @备注：
     * @返回类型：AlertDialog
     */
    public static AlertDialog logOutDialog(Context context, String title, String message, String commit, String cancel,
                                           final MyOnClickListener clickListener) {
        Builder builder = new Builder(context);
        View view = View.inflate(context, R.layout.dialog_layout, null);
        TextView dtitle = (TextView) view.findViewById(R.id.dialog_title);
        TextView dmessage = (TextView) view.findViewById(R.id.dialog_message);
        TextView dcancel = (TextView) view.findViewById(R.id.dialog_cancel);
        TextView dcommit = (TextView) view.findViewById(R.id.dialog_commit);

        dtitle.setText(title);
        dmessage.setText(message);
        dcommit.setText(commit);
        dcancel.setText(cancel);

        final AlertDialog dialog = builder.create();
        dcommit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (clickListener != null)
                    clickListener.onPositiveBtClick();
            }
        });
        dcancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (clickListener != null)
                    clickListener.onNegativeBtClick();
            }
        });
        dialog.setView(view, 0, 0, 0, 0);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    /**
     * @param context
     * @return
     * @方法名称:showDefaultDialog
     * @描述: 用于回调一个PositiveButton回调
     * @创建人：曹睿翔
     * @创建时间：2014年9月3日 上午10:23:39
     * @备注：
     * @返回类型：AlertDialog
     */
    public static AlertDialog showDefaultDialog(final Context context, final String dtitle, final String message, final String commit,
                                                final String cancel) {
        Builder builder = new Builder(context);
        View view = View.inflate(context, R.layout.dialog_layout, null);

        builder.setTitle(dtitle).setMessage(message).setPositiveButton(commit, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (clickListener == null) {
                    return;
                }
                clickListener.onPositiveBtClick();

            }
        }).setNegativeButton(cancel, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (clickListener == null) {
                    return;
                }
                clickListener.onNegativeBtClick();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(view, 0, 0, 0, 0);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    /**
     * @param context
     * @param msg
     * @return
     * @方法名称:showConfirmAlert
     * @描述: 确定框, 用于禁止用户其他操作, closeConfirmAlert()用于关闭Dialog
     * @创建人：曹睿翔
     * @创建时间：2014年9月3日 上午10:25:08
     * @备注：
     * @返回类型：AlertDialog
     */
    public static AlertDialog showConfirmAlert(final Context context, String msg) {
        Builder builder = new Builder(context);
        builder.setTitle(R.string.app_name).setMessage(msg).setPositiveButton("确定", null);
        AlertDialog confirmAlertDialog = builder.create();
        confirmAlertDialog.setCanceledOnTouchOutside(false);
        confirmAlertDialog.show();
        return confirmAlertDialog;
    }


    /**
     * @param context
     * @param msg
     * @方法名称:showToastTest
     * @描述: 测试用 在正式投入市场：删
     * @创建人：曹睿翔
     * @创建时间：2014年9月3日 上午10:31:26
     * @备注：
     * @返回类型：void
     */
    public static void showToastTest(Context context, String msg) {
        if (isShow) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showCustomToast(Context context, CharSequence text) {
        if (mToast == null) {
            mToast = new Toast(context);
            // 获取LayoutInflater对象
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 由layout文件创建一个View对象
            View layout = inflater.inflate(R.layout.toast_layout, null);
            mToast.setView(layout);
            mToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        }
        mToast.setDuration(Toast.LENGTH_SHORT);
        TextView textView = (TextView) mToast.getView().findViewById(R.id.text0);
        textView.setText(text);
        mToast.show();
    }

    /**
     * 自定义toast 位置
     *
     * @param context
     * @param msg
     */
    public static void showToastAtPostion(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
