package com.dkl.jmtfps;

import com.dkl.jmtfps.sensor.JmtSensor;
import com.dkl.jmtfps.sensor.JmtSensor.OnTouchListener;
import com.jmt.fps.JmtFP;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * CameraActivity start this
 * 通过Intent，可以控制不单单进行拍照操作
 * @author kayle
 * 
 */
public class SensorTakeCameraService extends Service {

	private static final String TAG = "SensorTakeCameraService";

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	Service instance;
	boolean user_cancel = false;
	private Thread tid_waitclick;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i(TAG, "onCreate");
		instance = this;
		waitClick();
	}

	// 如果是startService 就用这个，否则bindService就用onRebind
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		waitClick();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub
		super.onRebind(intent);
		waitClick();
	}

	private void waitClick() {
		user_cancel = false;
		if (tid_waitclick == null) {
			tid_waitclick = new Thread(new WaitClickTask());
			tid_waitclick.setPriority(Thread.MAX_PRIORITY);
			tid_waitclick.start();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		user_cancel = true;

		if (JmtSensor.getInstance(instance) != null) {
			JmtSensor.getInstance(instance).CancelAction();
		}
		if (tid_waitclick != null && tid_waitclick.isAlive()) {
			tid_waitclick.interrupt();
		}
		tid_waitclick = null;
	}

	private class WaitClickTask implements Runnable {
		@Override
		public void run() {
			boolean keep_going = true;
			int Module_Result;
			Log.i(TAG,
					"WaitClickTask Thread START *****************************!");
			while (keep_going) {
				if (Thread.interrupted()) {
					Log.i(TAG, "WaitClickTask interrupted!");
					break;
				}

				if (user_cancel) {
					Log.i(TAG, "WaitClickTask user cancel!");
					break;
				}
				// 获取指纹单例 ，进行等待点击操作
				// Module_Result 返回函数调用结果，
				Module_Result = JmtSensor.getInstance(instance).WaitClick(
						new OnTouchListener() {
							@Override
							public int value(int val) {// 回调返回是点击次数
								// TODO Auto-generated method stub
								switch (val) {
								case 0:
									Log.e(TAG,
											"Click callback, single click.....");
									takePicture();//单击拍照
									break;
								case 1:
									Log.e(TAG,
											"Click callback, Double click.....");
									break;
								case 2:
									Log.e(TAG,
											"Click callback, Long click.....");
									break;
								default:
									break;
								}
								return 0;
							}
						});
				switch (Module_Result) {
				case JmtFP.ERR_OK:
					// Log.i (TAG, "Get FP Image OK");
					break;
				case JmtFP.ERR_ABORT:
					Log.i(TAG, "WaitClickTask User Cancel");
					keep_going = false;
					break;

				default:
					Log.i(TAG, "Something wrong here");
					keep_going = false;
					break;
				}
			}
		}
	}

	// 拍照
	private void takePicture() {
		//140===>拍照
		int r=JmtSensor.getInstance(instance).SetInputKey(140);
		Log.e(TAG, "SetInputKey:"+r);
	}

}
