package com.dkl.jmtfps.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.dkl.jmtfps.R;
import com.dkl.jmtfps.data.DataCache;
import com.dkl.jmtfps.view.TitleExitAnimListener;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class Util {
	/**
	 * can bind FingerprintClickService
	 * 
	 * @param context
	 * @param flag
	 */
//	public static void unBindClickService(final Context context, boolean flag) {
//		if (PreferenceUtils.getInstance().getBoolean(
//				Constants.FINGER_DOUBLE_CLICK, false)
//				|| PreferenceUtils.getInstance().getBoolean(
//						Constants.FINGER_NAV_CLICK, false)) {
//			if (flag) {
//				if (PreferenceUtils.getInstance().getBoolean(
//						Constants.FINGERPRINT_CLICK_BINDSERVICE, false)) {
//					PreferenceUtils.getInstance().putBoolean(
//							Constants.FINGERPRINT_CLICK_BINDSERVICE, false);
//					Intent intent = new Intent();
//					intent.setAction(Constants.FINGERPRINT_UNBINDSERVICE);
//					context.sendBroadcast(intent);
//					Log.i("dkl", "sendBroadcast unbind");
//				}
//			} else {
//				if (!PreferenceUtils.getInstance().getBoolean(
//						Constants.FINGERPRINT_CLICK_BINDSERVICE, false)) {
//					PreferenceUtils.getInstance().putBoolean(
//							Constants.FINGERPRINT_CLICK_BINDSERVICE, true);
//
//					// new Handler().postDelayed(new Runnable() {
//					//
//					// @Override
//					// public void run() {
//					// TODO Auto-generated method stub
//					Intent intent = new Intent();
//					intent.setAction(Constants.FINGERPRINT_BINDSERVICE);
//					context.sendBroadcast(intent);
//					// }
//					// }, 1000);
//					Log.i("dkl", "sendBroadcast bind");
//				}
//			}
//		}
//	}

	public static void showGuideContentAnim(TextView enterView,
			TextView exitView) {
		Animation exitanimation = AnimationUtils.loadAnimation(
				DKLApplication.getInstance(), R.anim.register_title_text_exit);
		Animation enteranimation = AnimationUtils.loadAnimation(
				DKLApplication.getInstance(), R.anim.register_title_text_enter);
		exitanimation.setAnimationListener(new TitleExitAnimListener(exitView,
				View.GONE));
		enteranimation.setAnimationListener(new TitleExitAnimListener(
				enterView, View.VISIBLE));
		exitView.startAnimation(exitanimation);
		enterView.startAnimation(enteranimation);
	}

	public static void chmodFile(String filenameString) {
		String command = "chmod 777 " + filenameString;// 全部权限
		Runtime runtime = Runtime.getRuntime();
		try {
			Process proc = runtime.exec(command);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * @获取安装包
	 * */
	public static String getPackagePath(Context ctx) {

		PackageManager m = ctx.getPackageManager();
		String s = ctx.getPackageName();
		try {
			PackageInfo p = m.getPackageInfo(s, 0);
			s = p.applicationInfo.dataDir;
		} catch (PackageManager.NameNotFoundException e) {

		}
		return s;
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param context
	 */
	public static String collectDeviceInfo(Context context) {
		Map<String, String> info = new HashMap<String, String>();// 用来存储设备信息和异常信息
		try {
			PackageManager pm = context.getPackageManager();// 获得包管理器
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_ACTIVITIES);// 得到该应用的信息，即主Activity
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				info.put("versionName", versionName);
				info.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		Field[] fields = Build.class.getDeclaredFields();// 反射机制
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				info.put(field.getName(), field.get("").toString());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : info.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\r\n");
		}
		sb.append("\r\n");
		sb.append("*********user="
				+ DataCache.getInstance().get(Constants.LOGIN_NAME_KEY));
		sb.append("*********\r\n");
		return sb.toString();
	}

}
