package com.dkl.jmtfps;

import java.util.Locale;

import com.dkl.jmtfps.dao.DbHelper;
import com.dkl.jmtfps.mode.FingerPrintModel;
import com.dkl.jmtfps.sensor.JmtSensor;
import com.dkl.jmtfps.util.AppManager;
import com.dkl.jmtfps.util.Constants;
import com.dkl.jmtfps.util.Log;
import com.dkl.jmtfps.util.PreferenceUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public abstract class BaseActivity extends Activity implements OnClickListener {
	protected Activity instance;
	protected PreferenceUtils preferenceUtils;
	protected static DbHelper dbHelper;
	protected FingerPrintModel fingerPrintModel;
	private static final String TAG = "BaseActivity";

	public abstract void initView(Bundle savedInstanceState);

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Constants.ENROLL_ACTION_TAG = false;
		instance = this;
		JmtSensor.getInstance(instance);
		preferenceUtils = PreferenceUtils.getInstance();
		/**
         *   amo modify
         *   For KXD, if open this line, default is "doubleClick(true)", and click function for back is default usable.
         */
		//preferenceUtils.putBoolean(Constants.FINGER_DOUBLE_CLICK, true);
		if (dbHelper == null) {
			dbHelper = new DbHelper(instance);
		}
		//Log.e(TAG, "preferenceUtils.hashCode()" + preferenceUtils.hashCode());
		Log.e(TAG, "preferenceUtils.getBoolean(Constants.FINGER_DOUBLE_CLICK, false)" + preferenceUtils.getBoolean(Constants.FINGER_DOUBLE_CLICK, false));
		hideTitle(true);
		setFullScreen(false);
		setLandscape(false);
		setTranslucentStatusBar(false);

		AppManager.getAppManager().addActivity(this);

		initView(savedInstanceState);

	}

	/**
	 * 是否有标题栏
	 * 
	 * @param flag
	 */
	private void hideTitle(boolean flag) {
		if (flag)
			requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
	}

	/**
	 * 是否全屏
	 * 
	 * @param flag
	 */
	private void setFullScreen(boolean flag) {
		if (flag)
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
	}

	/**
	 * 是否为横屏，不是横屏就是竖屏
	 * 
	 * @param flag
	 */
	private void setLandscape(boolean flag) {
		if (flag) {
			if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
		} else {
			if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
		}
	}

	/**
	 * 设置沉浸式的状态栏
	 * 
	 * @param flag
	 */
	private void setTranslucentStatusBar(boolean flag) {
		if (android.os.Build.VERSION.SDK_INT <= 18) {
			return;
		}
		if (!flag) {
			return;
		}
		Window window = getWindow();
		window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		// 设置根布局的内边距
		// RelativeLayout relativeLayout = (RelativeLayout)
		// findViewById(R.id.layout);
		// relativeLayout.setPadding(0,
		// getActionBarHeight()+getStatusBarHeight(), 0, 0);
	}
	
	private void switchLanguage(String language){
		Resources resources=getResources();
		Configuration config=resources.getConfiguration();
		DisplayMetrics dm=resources.getDisplayMetrics();
		if(language.equals("zh")){
			config.locale=Locale.SIMPLIFIED_CHINESE;
		}else{
			config.locale=Locale.ENGLISH;
		}
		resources.updateConfiguration(config, dm);
		preferenceUtils.putString("language", language);
	}

	public void StartActivity(String action) {
		StartActivity(new Intent(action));
	}

	public void StartActivity(Intent intent) {
		instance.startActivity(intent);
	}

	@Override
	protected void onRestart() {
		Constants.ENROLL_ACTION_TAG = false;
		super.onRestart();

	}

	@Override
	protected void onResume() {
		Constants.ENROLL_ACTION_TAG = false;
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		Constants.ENROLL_ACTION_TAG = false;
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Constants.ENROLL_ACTION_TAG = false;
		AppManager.getAppManager().finishActivity(this);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}
	

}
