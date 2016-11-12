package com.dkl.jmtfps.sensor;

import android.content.Context;

import com.dkl.jmtfps.util.Log;
import com.dkl.jmtfps.util.Util;
import com.jmt.fps.JmtFP;

public class JmtSensor extends JmtFP {

	protected static final String TAG = "JmtSensor";
	private final static boolean DEBUG = false;
	private static JmtSensor mSensor;

	private JmtSensor(Context context) {
		super();
		try {
			this.setAppPathSingleton(Util.getPackagePath(context));
			this.setCallbackSingleton(callback);
			this.setDriverPathSingleton("/dev/jmt101");
		} catch (Exception e) {
			Log.e(TAG, "init Fingerprint Sensor is error");
		}
		if (DEBUG) {
			Log.e(TAG, "init Fingerprint Sensor");
		}
	}

	public JmtSensor() {
		super();
	}

	public static JmtSensor getInstance() {
		if (mSensor == null)
			mSensor = new JmtSensor();

		return mSensor;
	}

	public static JmtSensor getInstance(Context context) {
		if (mSensor == null)
			mSensor = new JmtSensor(context);
		return mSensor;
	}

	public int Verify(OnTouchListener sensorVal) {
		if (DEBUG) {
			Log.e(TAG, "Verify Fingerprint Sensor");
			return 100;
		}

		if (sensorVal == null) {
			return -1;
		}

		sensorValue = sensorVal;
		int result = -1;
		try {
			result = mSensor.Verify();
		} catch (Exception e) {
			Log.e(TAG, "Verify Fingerprint Sensor is error");
		}
		return result;
	}

	public int Enroll(OnEnrollProgressListener enrollProgressListener) {
		if (DEBUG) {
			Log.e(TAG, "Enroll Fingerprint Sensor");
			return 100;
		}
		onEnrollProgressListener = enrollProgressListener;
		if (enrollProgressListener == null) {
			return -1;
		}
		int result = -1;
		try {
			result = mSensor.Enrollment();
		} catch (Exception e) {
			Log.e(TAG, "Verify Fingerprint Sensor is error");
		}
		return result;
	}

	public int WaitClick(OnTouchListener sensorVal) {
		if (DEBUG) {
			Log.e(TAG, "WaitClick Fingerprint Sensor");
			return 100;
		}
		if (sensorVal == null) {
			return -1;
		}

		sensorValue = sensorVal;
		int result = -1;
		try {
			result = mSensor.WaitClick();
		} catch (Exception e) {
			Log.e(TAG, "Verify Fingerprint Sensor is error");
		}
		return result;
	}

	public int StartNavigation(OnNavigationListener onNavigation) {
		if (DEBUG) {
			Log.e(TAG, "StartNavigation Fingerprint Sensor");
			return 100;
		}
		if (onNavigation == null) {
			return -1;
		}

		onNavigationListener = onNavigation;
		int result = -1;
		try {
			result = mSensor.StartNavigation();
		} catch (Exception e) {
			Log.e(TAG, "Verify Fingerprint Sensor is error");
		}
		return result;
	}

	public int getOneFinger(OnShowImageListener onShowImage) {
		if (onShowImage == null) {
			return -1;
		}
		onShowImageListener = onShowImage;
		int result = -1;
		try {
			result = mSensor.GetOneFinger();
		} catch (Exception e) {
			Log.e(TAG, "Verify Fingerprint Sensor is error");
		}
		return result;
	}

	static jmtcallback callback = new jmtcallback() {

		@Deprecated
		public int onVerifyCallback(int arg0) {
			// TODO Auto-generated method stub
			sensorValue.value(arg0);
			return 0;
		}

		@Override
		public int onShowImage(byte[] arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			onShowImageListener.onShowImage(arg0, arg1, arg2);
			return 0;
		}

		@Override
		public int onNavigation(int arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			onNavigationListener.onNavigation(arg0, arg1, arg2);
			return 0;
		}

		@Override
		@Deprecated
		public int onEnrollProgress(String arg0, int arg1) {
			// TODO Auto-generated method stub
			//onEnrollProgressListener.onEnrollProgress(gid, fid, per);
			return 0;
		}

		@Override
		public int onClick(int arg0) {
			// TODO Auto-generated method stub
			sensorValue.value(arg0);
			return 0;
		}

		@Override
		public int onAdvice(int arg0) {
			// TODO Auto-generated method stub
			Log.i(TAG, "arg0:" + arg0);
			/**
			 *	amo note
			 *		It seems like the JmtFP.onAdvice() implements are assigned
			 *		 when call JmtSensor.Enroll(enrollProgressListener)
			 *		 and JmtSensor.Verify(onTouchListener).
			 *		I guess that when SDK called onAdvice(), it would execute both of listeners value() work.
			 */
			if (onEnrollProgressListener != null)
				onEnrollProgressListener.value(arg0);
			if (sensorValue != null)
				sensorValue.value(arg0);
			return 0;
		}

		@Override
		public int onEnrollProgress2(int gid, int fid, int per) {
			// TODO Auto-generated method stub
			onEnrollProgressListener.onEnrollProgress(gid, fid, per);
			return 0;
		}

		@Override
		public int onShowData(byte[] arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			return 0;
		}
	};

	static OnTouchListener sensorValue;
	static OnEnrollProgressListener onEnrollProgressListener;
	static OnNavigationListener onNavigationListener;
	static OnShowImageListener onShowImageListener;

	public interface OnTouchListener {
		int value(int val);
	}

	public interface OnEnrollProgressListener {
		int onEnrollProgress(int gid, int fid, int per);
		int value(int val);
	}

	public interface OnNavigationListener {
		int onNavigation(int arg0, int arg1, int arg2);
	}

	public interface OnShowImageListener {
		int onShowImage(byte[] arg0, int arg1, int arg2);
	}

}
