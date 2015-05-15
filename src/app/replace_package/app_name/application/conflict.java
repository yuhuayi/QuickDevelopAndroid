package app.zhengbang.teme.application;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.Toast;

import java.io.*;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 *
 * @author user
 */

public class mainswitch implements UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";
    // 分支的代码
    
  
    // 这个才是分支代码

    // 主线和分支都有的代码

    // CrashHandler 实例
    private static CrashHandler INSTANCE = new CrashHandler();

    // 程序的 Context 对象
    private Context mContext;

    // 系统默认的 UncaughtException 处理类
    private UncaughtExceptionHandler mDefaultHandler;

    // 用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    // 用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");


    /**
     * 保证只有一个 CrashHandler 实例
     */
    private CrashHandler() {
    }

    /**
     * 获取 CrashHandler 实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        // 获取系mContext统默认的 UncaughtException 处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该 CrashHandler 为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    /**
     * 当 UncaughtException 发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e(TAG, "error : ", e);
            }
            // 退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理，收集错误信息，发送错误报告等操作均在此完成
     *
     * @param ex
     * @return true：如果处理了该异常信息；否则返回 false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 使用 Toast 来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉，程序出现异常，即将退出。", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();

        // 收集设备参数信息
        // collectDeviceInfo(mContext);
        // 保存日志文件
        // saveCrashInfo2File(ex);
        try {
            writeLog(ex);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void writeLog(Throwable ex) throws IOException {
        File file = new File(Environment.getExternalStorageDirectory(), "exception.cache");

        if (!file.exists()) {
            File fileDir = new File(Environment.getExternalStorageDirectory(), "exception");
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            file.createNewFile();
        }

        OutputStream out = new FileOutputStream(file);
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.beginObject();
        writer.name("packageid").value("");
        writer.name("internalver").value("");
        writer.name("submittimestamp").value(System.currentTimeMillis());
        writer.name("logcontent").value(getErrDetail(ex));
        writer.endObject();
        writer.close();
    }

    /**
     * 获取栈信息
     *
     * @param _err
     * @return
     */
    public String getErrDetail(Throwable _err) {
        if (_err == null)
            return "";
        StackTraceElement[] ste = _err.getStackTrace();
        StringBuffer sb = new StringBuffer();
        sb.append(_err.getMessage() + " ");
        for (int i = 0; i < ste.length; i++) {
            sb.append(ste[i].toString() + " ");
        }
        return sb.toString();
    }
    // /**
    // * 收集设备参数信息
    // * @param ctx
    // */
    // public void collectDeviceInfo(Context ctx) {
    // try {
    // PackageManager pm = ctx.getPackageManager();
    // PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
    // PackageManager.GET_ACTIVITIES);
    //
    // if (pi != null) {
    // String versionName = pi.versionName == null ? "null" : pi.versionName;
    // String versionCode = pi.versionCode + "";
    // infos.put("versionName", versionName);
    // infos.put("versionCode", versionCode);
    // }
    // } catch (NameNotFoundException e) {
    // Log.e(TAG, "an error occured when collect package info", e);
    // }
    //
    // Field[] fields = Build.class.getDeclaredFields();
    // for (Field field : fields) {
    // try {
    // field.setAccessible(true);
    // infos.put(field.getName(), field.get(null).toString());
    // Log.d(TAG, field.getName() + " : " + field.get(null));
    // } catch (Exception e) {
    // Log.e(TAG, "an error occured when collect crash info", e);
    // }
    // }
    // }

    // /**
    // * 保存错误信息到文件中
    // *
    // * @param ex
    // * @return 返回文件名称,便于将文件传送到服务器
    // */
    // private String saveCrashInfo2File(Throwable ex) {
    // StringBuffer sb = new StringBuffer();
    // for (Map.Entry<String, String> entry : infos.entrySet()) {
    // String key = entry.getKey();
    // String value = entry.getValue();
    // sb.append(key + "=" + value + "\n");
    // }
    //
    // Writer writer = new StringWriter();
    // PrintWriter printWriter = new PrintWriter(writer);
    // ex.printStackTrace(printWriter);
    // Throwable cause = ex.getCause();
    // while (cause != null) {
    // cause.printStackTrace(printWriter);
    // cause = cause.getCause();
    // }
    // printWriter.close();
    //
    // String result = writer.toString();
    // sb.append(result);
    // try {
    // long timestamp = System.currentTimeMillis();
    // String time = formatter.format(new Date());
    // String fileName = "crash-" + time + "-" + timestamp + ".log";
    //
    // if
    // (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
    // {
    // String path = "/sdcard/crash/";
    // File dir = new File(path);
    // if (!dir.exists()) {
    // dir.mkdirs();
    // }
    // FileOutputStream fos = new FileOutputStream(path + fileName);
    // fos.write(sb.toString().getBytes());
    // fos.close();
    // }
    //
    // return fileName;
    // } catch (Exception e) {
    // Log.e(TAG, "an error occured while writing file...", e);
    // }
    //
    // return null;
    // }
}