package com.util;

import java.io.File;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;

import app.zhengbang.teme.AppConstants;
import app.zhengbang.teme.service.UpdateService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.manager.HttpManager;
import com.net.AsyncHttpClient;
import com.net.AsyncHttpResponseHandler;
import com.net.RequestParams;
import com.zhengbang.TeMe.R;

public class UpdateVersionUtil {
    private static AlertDialog updataVersionDialog;

    //http://123.57.12.122:90/index.php?m=User&a=version
    public static void checkNewAPPVersion(final Context context,final boolean showReminder) {
        if (HttpManager.isNetworkConnected(context)) {
            AsyncHttpClient client = new AsyncHttpClient(context);
            RequestParams requestParams = new RequestParams();
            requestParams.put("m", "User");
            requestParams.put("a", "version");
            requestParams.put("vcode", PackageUtils.getAppVersionCode(context) + "");
            client.get(context, AppConstants.HOST+"", requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onFailure(Throwable error, String content) {
                    super.onFailure(error, content);
                    LogUtil.info("启动检测新版本,接口访问失败");
                }

                @Override
                public void onSuccess(String content) {
                    super.onSuccess(content);
                    try {
                        JSONObject jsonObject = JSON.parseObject(content);
                        if (jsonObject != null && jsonObject.getString("code").equals("100")) {
                            int code = jsonObject.getInteger("version_code");// 版本Code
                            String description = jsonObject.getString("description");// 版本Code
                            final String url = jsonObject.getString("url");// 版本Code
                            if (code > PackageUtils.getAppVersionCode(context) ? false : true) {
                                if (showReminder){
                                    PromptManager.showCustomToast(context,"未检测到新版本");
                                }else{
                                    LogUtil.info("未检测到新版本");
                                }
                            } else {
                                updataVersionDialog = PromptManager.showCustomDialog(context, "提示", "检测到新版本,是否升级?", "暂不升级","确定",new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										updataVersionDialog.dismiss();
									}
								},new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        Intent updateIntent = new Intent(context, UpdateService.class);
                                        updateIntent.putExtra("app_name", context.getResources().getString(R.string.app_name));
                                        updateIntent.putExtra("downurl", url);
                                        context.startService(updateIntent);
                                        updataVersionDialog.dismiss();
                                    }
                                });
                                updataVersionDialog.setCanceledOnTouchOutside(false);
                                updataVersionDialog.show();
                            }
                        } else {
                            if (showReminder){
                                PromptManager.showCustomToast(context,"未检测到新版本");
                            }else{
                                LogUtil.info("未检测到新版本");
                            }
                        }
                    } catch (Exception e) {
                        LogUtil.info(e.getMessage());
                    }
                }
            });

        } else {
            LogUtil.info("启动检测新版本,联网失败");
        }
    }

    /**
     * @param context
     * @param downurl
     * @方法名称:ShowUpAppDialog
     * @描述: 预留 给推送新版本使用
     * @创建人：曹睿翔
     * @创建时间：2014年9月18日 下午12:15:46
     * @备注：
     * @返回类型：void
     */
    public static void ShowUpAppDialog(final Context context, final String downurl) {
        updataVersionDialog = PromptManager.showCustomDialog(context, "提示", "检测到新版本,是否升级?","确定","取消", new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent updateIntent = new Intent(context, UpdateService.class);
                updateIntent.putExtra("app_name", context.getResources().getString(R.string.app_name));
                updateIntent.putExtra("downurl", downurl);
                context.startService(updateIntent);
                updataVersionDialog.dismiss();
            }
        },new OnClickListener() {

            @Override
            public void onClick(View v) {
                
                updataVersionDialog.dismiss();
            }
        });
        updataVersionDialog.setCanceledOnTouchOutside(false);
        updataVersionDialog.show();
    }

    // 下载完成后打开安装apk界面
    public static void installApk(File file, Context context) {
        LogUtil.info("版本更新获取sd卡的安装包的路径=" + file.getAbsolutePath());
        Intent openFile = getFileIntent(file);
        context.startActivity(openFile);

    }

    public static Intent getFileIntent(File file) {
        Uri uri = Uri.fromFile(file);
        String type = getMIMEType(file);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, type);
        return intent;
    }

    public static String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
        // 取得扩展名
        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length());
        if (end.equals("apk")) {
            type = "application/vnd.android.package-archive";
        } else {
            // /*如果无法直接打开，就跳出软件列表给用户选择 */
            type = "*/*";
        }
        return type;
    }
}
