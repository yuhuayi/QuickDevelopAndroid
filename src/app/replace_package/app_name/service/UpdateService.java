package app.zhengbang.teme.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;
import com.Constants;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.util.FileUtils;
import com.util.UpdateVersionUtil;
import com.zhengbang.TeMe.R;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;


public class UpdateService extends Service {
    private static String down_url; // = "http://192.168.1.112:8080/360.apk";
    private static final int DOWN_OK = 1; // 下载完成
    private static final int DOWN_ERROR = 0;

    private String app_name;

    private NotificationManager notificationManager;
    private Notification notification;

    private Intent updateIntent;
    private PendingIntent pendingIntent;
    private String updateFile;

    private int notification_id = 0;
    long totalSize = 0;// 文件总大小
    /**
     * 更新UI
     */
    final Handler handler = new Handler() {
        @SuppressWarnings("deprecation")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_OK:
                    // 下载完成，点击安装
                    Intent installApkIntent = UpdateVersionUtil.getFileIntent(new File(updateFile));
                    pendingIntent = PendingIntent.getActivity(UpdateService.this, 0, installApkIntent, 0);
                    notification.contentIntent = pendingIntent;
                    notification.flags |= Notification.FLAG_AUTO_CANCEL;
                    notification.setLatestEventInfo(UpdateService.this, app_name, "下载成功，点击安装", pendingIntent);
                    notificationManager.notify(notification_id, notification);
                    stopService(updateIntent);
                    break;
                case DOWN_ERROR:
                    notification.setLatestEventInfo(UpdateService.this, app_name, "下载失败", pendingIntent);
                    break;
                default:
                    stopService(updateIntent);
                    break;
            }
        }
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            try {
                app_name = intent.getStringExtra("app_name");
                down_url = intent.getStringExtra("downurl");
                // 创建文件
                File updateFile = FileUtils.getDiskCacheFile(getApplicationContext(), Constants.DOWNLOAD_APP_NAME);
                if (!updateFile.exists()) {
                    try {
                        updateFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                // 创建通知
                createNotification();
                // 开始下载
                downloadUpdateFile(down_url, updateFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 创建通知栏
     */
    RemoteViews contentView;

    @SuppressWarnings("deprecation")
    public void createNotification() {

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification = new Notification();
        notification.icon = R.drawable.icon;
        // 这个参数是通知提示闪出来的值.
        notification.tickerText = "开始下载";

//        updateIntent = new Intent(this, HomeActivity.class);
        updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);

        // 这里面的参数是通知栏view显示的内容
        notification.setLatestEventInfo(this, app_name, "下载：0%", pendingIntent);
        notificationManager.notify(notification_id, notification);

        /***
         * 在这里我们用自定的view来显示Notification
         */
        contentView = new RemoteViews(getPackageName(), R.layout.notification_item);
        contentView.setTextViewText(R.id.notificationTitle, "正在下载");
        contentView.setTextViewText(R.id.notificationPercent, "0%");
        contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);

        notification.contentView = contentView;

        notification.contentIntent = pendingIntent;
        notificationManager.notify(notification_id, notification);
    }

    /**
     * 下载文件
     */
    public void downloadUpdateFile(String down_url, String file) throws Exception {
        updateFile = file;
        HttpUtils httpUtils = new HttpUtils();
        HttpUtils.download(down_url, file, new RequestCallBack<File>() {

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                // 下载成功
                Message message = handler.obtainMessage();
                message.what = DOWN_OK;
                handler.sendMessage(message);
                UpdateVersionUtil.installApk(new File(updateFile), UpdateService.this);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Message message = handler.obtainMessage();
                message.what = DOWN_ERROR;
                handler.sendMessage(message);
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                double x_double = current * 1.0;
                double tempresult = x_double / total;
                DecimalFormat df1 = new DecimalFormat("0.00"); // ##.00%
                // 百分比格式，后面不足2位的用0补齐
                String result = df1.format(tempresult);
                contentView.setTextViewText(R.id.notificationPercent, (int) (Float.parseFloat(result) * 100) + "%");
                contentView.setProgressBar(R.id.notificationProgress, 100, (int) (Float.parseFloat(result) * 100), false);
                notificationManager.notify(notification_id, notification);
            }
        });
    }
}