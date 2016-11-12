package com.dkl.jmtfps.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dkl.jmtfps.R;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.widget.Toast;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告.
 */
public class CrashHandler implements UncaughtExceptionHandler {

	private static CrashHandler INSTANCE = new CrashHandler();// CrashHandler实例
	private Context mContext;// 程序的Context对象

	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {

	}

	/** 获取CrashHandler实例 ,单例模式 */
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
		Thread.setDefaultUncaughtExceptionHandler(this);// 设置该CrashHandler为程序的默认处理器
	}

	/**
	 * 当UncaughtException发生时会转入该重写的方法来处理
	 */
	public void uncaughtException(Thread thread, final Throwable ex) {
		if(ex!=null){
			ex.printStackTrace();
		}
		if(false){//指纹app出现异常不需要重启。
		// 1秒钟后重启应用
		Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		startActivity(i);
//
//		Intent intent = new Intent(mContext, WelcomeActivity.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent restartIntent = PendingIntent.getActivity(mContext, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
		AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1500, restartIntent);
		}
		if (!PreferenceUtils.getInstance().isContains(Constants.IS_SAVE_CRASH_LOG)) {
			new Thread() {
				public void run() {
					// 保存日志文件
					if(ex!=null){
					saveCrashInfo2File(ex);
					Looper.prepare();
					Toast.makeText(mContext, mContext.getString(R.string.txt_sorry_procedure_error_exit), 0).show();
					Looper.loop();
					}
				}
			}.start();
			PreferenceUtils.getInstance().putString(Constants.IS_SAVE_CRASH_LOG, "a");
		}
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
		// 退出程序
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	private String saveCrashInfo2File(Throwable ex) {
		Log.e("error", ex.toString());
		StringBuffer sb = new StringBuffer();

		String errPath = FilePathUtils.getDefaultLogFileName(mContext);
		File f = new File(errPath);
		try {
			if (!f.exists()) {
				f.getParentFile().mkdirs();
				f.createNewFile();

				sb.append(Util.collectDeviceInfo(mContext));
			}
			sb.append("\r\n\r\n\r\n\r\ncrash==================================================\r\n");

			Writer result = new StringWriter();
			PrintWriter printWriter = new PrintWriter(result);
			ex.printStackTrace(printWriter);
			sb.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ":\r\n\r\n");
			sb.append(result.toString().replace("\n", "\r\n"));
			sb.append("\r\n\r\n");

			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			FileWriter writer = new FileWriter(errPath, true);
			writer.write(sb.toString());
			printWriter.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
