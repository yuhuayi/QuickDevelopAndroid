package com.view.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.zhengbang.TeMe.R;

public class MyDialogFragment extends DialogFragment {
    int mNum;
    String title;
    String message;
    String leftText;
    String rightText; DialogInterface.OnClickListener leftOnClickListener;
    DialogInterface.OnClickListener rightOnClickListener;
    public MyDialogFragment( String title, String message, String leftText, String rightText, DialogInterface.OnClickListener leftOnClickListener, DialogInterface.OnClickListener rightOnClickListener){
        this.title = title;
        this.leftText = leftText;
        this.rightText = rightText;
        this.leftOnClickListener = leftOnClickListener;
        this.rightOnClickListener = rightOnClickListener;
    }
    @Override
    public void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //可以用下面的方法得到参数
//      mNum = getArguments().getInt("num");
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View v = mInflater.inflate(R.layout.dialog_layout,null);
        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setView(v)
                .setPositiveButton(rightText, leftOnClickListener )
                .setNegativeButton(leftText, rightOnClickListener)
                .create();
    }
}
