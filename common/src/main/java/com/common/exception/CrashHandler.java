
package com.common.exception;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.Toast;

import com.common.utils.AppLog;
import com.common.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * 作者：gxj on 2017/9/5 08:12
 * 邮箱：jun18735177413@sina.com
 *
 * TODO UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告.
 */
public class CrashHandler implements UncaughtExceptionHandler {
    public static final String TAG = CrashHandler.class.getSimpleName();

    // 系统默认的UncaughtException处理类
    private UncaughtExceptionHandler mDefaultHandler;

    // CrashHandler实例
    private static CrashHandler instance;

    // 程序的Context对象
    private Context mContext;

    // 用来存储设备信息和异常信息
    private Map<String, String> infos = new TreeMap<String, String>();

    // 用于格式化日期,作为日志文件名的一部分
    private static DateFormat formatter = new SimpleDateFormat("MMdd-HH:mm:ss");

    private CrashHandler() {
        
    }

    public synchronized static CrashHandler getInstance() {
        if (instance == null) {
            instance = new CrashHandler();
        }
        return instance;
    }

    /**
     * 初始化
     * 
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // 使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, R.string.sorry_app_will_exit, Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        if (ex == null && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            ex.printStackTrace();
            // 处理异常
            handleException(ex);
            SystemClock.sleep(5000);
            // 退出程序
            exitApp();
        }
    }

    private void exitApp() {
        System.exit(0);
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     * 
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 收集设备参数信息
        collectDeviceInfo(mContext);
        // 保存日志文件
        try {
            saveCrashInfo2File(ex);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 收集设备参数信息
     * 
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            AppLog.e(TAG, "an error occured when collect package info"+ e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                AppLog.e(TAG, "an error occured when collect crash info"+ e);
            }
        }
    }

    /**
     * 保存错误信息到文件中
     * 
     * @param ex
     * @return 返回文件名称,便于将文件传送到服务器
     * @throws IOException 
     */
    private String saveCrashInfo2File(Throwable ex) throws IOException {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\r\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        FileOutputStream fos = null;
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = time + "-" + timestamp + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = "/"+Environment.getExternalStorageDirectory()+"/leshare-log/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
            }
            return fileName;
        } catch (Exception e) {
            AppLog.e(TAG, "an error occured while writing file..."+ e);
        }finally{
            if(printWriter != null){
                printWriter.close();
            }
            if(fos != null){
                fos.close();
            }
        }
        return null;
    }
}
