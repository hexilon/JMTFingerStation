package com.dkl.jmtfps.util;

import com.dkl.jmtfps.sensor.JmtSensor;

import android.app.Application;

public class DKLApplication extends Application {
	private static Application mInstance;

	public static Application getInstance() {
		return mInstance;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mInstance = this;
		// 封装程序异常
//		CrashHandler.getInstance().init(this);
	}
	
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		JmtSensor.getInstance(mInstance).SetWaitTouch();
	}
}
