package com.dkl.jmtfps.util;

import android.os.Environment;

public final class Constants {

	public final static String ACTION_LIST_FP 	= "android.intent.action.MAIN";
	public final static String ACTION_ADD_FP 	= "android.intent.action.com.dkl.jmtfps.AddFingerPrintActivity";
	public final static String ACTION_MANAGE_FP = "android.intent.action.com.dkl.jmtfps.ManageFingerPrintActivity";
	public final static String ACTION_ABOUT_FP 		= "android.intent.action.com.dkl.jmtfps.AboutFingerprintActivity";
	public final static String ACTION_APPLOCK_LIST 	= "android.intent.action.com.dkl.jmtfps.applock.AppLockListActivity";
	public final static String ACTION_APPLOCK_UI = "android.intent.action.com.dkl.jmtfps.applock.AppLockUIActivity";
	public final static String ACTION_MATCH_RECEIVER = "android.intent.action.com.dkl.jmtfps.receiver.MatchFingerprintReceiver";
	public final static String ACTION_FINGER_LOCK_SERVICE = "com.android.internal.policy.IFingerprintLockInterface";
	public final static String ACTION_FINGER_TOUCH_SERVICE = "android.intent.action.com.dkl.jmtfps.service.TouchService";
	public final static String ACTION_APPLOCK_RECEIVER = "android.intent.action.com.dkl.jmtfps.receiver.AppLockBroadcastReceiver";
	public static final String REFRESH_FP_DATA = "refresh_fingerprint_data";
	public static final String ACTION_MATCH_APPLOCK_UI = "match_fingerprint_data_applock_ui";
	public static final String ACTION_APP_OPEN ="android.intent.action.com.dkl.jmtfps.AppListSelectActivity";
	public static final String FINGERPRINT_ACTION_SCREEN_OFF = "com.dkl.jmtfps.android.intent.action.SCREEN_OFF";
	public static final String FINGERPRINT_ACTION_SCREEN_ON = "com.dkl.jmtfps.android.intent.action.SCREEN_ON";;
	public static final String IS_SAVE_CRASH_LOG = "is_save_crash_log";
	public static final String LOGIN_NAME_KEY = "login_name_key";
	public static final String FINGERPRINT_UEVENT_MATCH = "JMT101_STATE=WAKEUP";
	public static final String BRK_FINGER_UNLOCK_ACTION = "com.dkl.jmtfps.unlock.action";
	public static final boolean APP_Debug=true;
	public static String PACKAGE_PATH = DKLApplication.getInstance().getPackageName();
	public static String FILE_PATH = Environment.getDataDirectory().getPath() + "/data/" + PACKAGE_PATH + "/";
	public static String FINGER_MODEL_KEY = "fingerPrintModel_key";
	public static String FINGER_OPEN_UNLOCK = "fingerPrintModel_open_unlock";
//	public static String FINGER_SLEEP_UNLOCK = "fingerPrintModel_sleep_unlock";
	public static String FINGER_DOUBLE_CLICK = "fingerPrintModel_double_click";
	public static String FINGER_NAV_CLICK = "fingerPrintModel_nav_click";
	public static String FINGERPRINT_BINDSERVICE = "fingerprint_bind_service";
	public static String FINGERPRINT_UNBINDSERVICE = "fingerprint_unbind_service";
	public static String FINGERPRINT_CLICK_BINDSERVICE = "fingerprint_click_bind_service";
	public static String FINGER_SLEEP_UNLOCK_ISTURE = "fingerPrintModel_sleep_unlock_is_true";
	public static String FINGER_OPEN_APP_UNLOCK = "fingerPrintModel_open_app_unlock";
	public static String FINGER_OPEN_APP_UNLOCK_TAG = "fingerPrintModel_open_app_unlock_tag";
	public static String SETTINGS_NEEDLOCK_APP_PACKAGENAMES = "setting_need_lock_app_package_name";
	public static String SETTINGS_UNLOCK_APP_PACKAGENAMES = "setting_unlock_app_package_name";
	public static String KEY_PWD = "setting_password_";
	public static boolean MATCH_FINGER = false;
	public static boolean ENROLL_ACTION_TAG = false;
	
}
