package com.dkl.jmtfps.util;

import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
/**
 * 应用程序Activity管理类：用于Activity管理和应用程序
 */

public class AppManager {

	private static Stack<Activity> activityStack;

	private static AppManager instance;

	private AppManager() {
	}

	/**
	 * 单一实例
	 */
	public static AppManager getAppManager() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}

	/**
	 * 添加Activity到堆
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 获取当前Activity
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 结束当前Activity
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;

		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 结束Activity
	 */
	public void finishAllActivity(boolean isContainLogin) {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			Activity activity = activityStack.get(i);
			if(null != activity) {
				activity.finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * activity 启动
	 * 
	 * @param activity
	 * @param appIntent
	 */
	public void startActivity(Context activity, Intent appIntent) {
		appIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		activity.startActivity(appIntent);
		// activity.overridePendingTransition(android.R.anim.slide_in_left,
		// android.R.anim.slide_in_left);
	}

	/**
	 * * 应用程序
	 */
	public void AppExit(Context context, boolean isContainLogin) {
		try {
			finishAllActivity(isContainLogin);
			if(isContainLogin) {
				ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
				activityMgr.restartPackage(context.getPackageName());
				//退出程序   
				android.os.Process.killProcess(android.os.Process.myPid());  
			}
		} catch (Exception e) {
		}
	}
	
}