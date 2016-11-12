package com.dkl.jmtfps;

import com.android.internal.policy.IFingerprintClickCallback;
import com.android.internal.policy.IFingerprintClickInterface;
import com.dkl.jmtfps.sensor.JmtSensor;
import com.dkl.jmtfps.sensor.JmtSensor.OnNavigationListener;
import com.dkl.jmtfps.sensor.JmtSensor.OnTouchListener;
import com.dkl.jmtfps.util.Constants;
import com.dkl.jmtfps.util.IConstants;
import com.dkl.jmtfps.util.Log;
import com.dkl.jmtfps.util.PreferenceUtils;
import com.jmt.fps.JmtFP;

import android.app.Instrumentation;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.view.MotionEvent;

/**
 * used click funcation
 * @author kayle
 *
 */
public class FingerprintClickService extends Service implements IConstants{

	private boolean user_cancel = true;

	private FingerprintClickService instance;
	private IFingerprintClickCallback iFingerprintClickCallback;
	
	public IFingerprintClickInterface.Stub mService = new IFingerprintClickInterface.Stub() {

		@Override
		public void registerClick(IFingerprintClickCallback callback)
				throws RemoteException {
			// TODO Auto-generated method stub
			Log.i("dkl", "registerClick");
			iFingerprintClickCallback = callback;
			choseType();
		}

		@Override
		public void unregisterClick() throws RemoteException {
			// TODO Auto-generated method stub
			Log.i("dkl", "unregisterClick");
			user_cancel = true;
			handler.sendEmptyMessage(MSG_ABORT_FINGER);
		}
	};

	Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_ABORT_FINGER:
				JmtSensor.getInstance(instance).CancelAction();
				break;

			default:
				break;
			}
		};
	};
	
	public void onCreate() {
		instance = this;
		Log.i("dkl", "Click========start=====");
		
	};
	
	

	protected void choseType() {
		Log.i("dkl", "choseType");
		if(user_cancel == false){
			return ;
		}
		user_cancel = false;
//		startClick();
//		startNav();
//		if(true){return ;}
		if (PreferenceUtils.getInstance().getBoolean(
				Constants.FINGER_DOUBLE_CLICK, false)) {
			Log.i("dkl", "startClick");
			startClick();
			return;
		}
		if (PreferenceUtils.getInstance().getBoolean(
				Constants.FINGER_NAV_CLICK, false)) {
			Log.i("dkl", "startNav");
			startNav();
			return;
		}
	}

	protected void startNav() {
		tid_waitclick = new Thread(new WaitNavTask());
		tid_waitclick.setPriority(Thread.MAX_PRIORITY);
		tid_waitclick.start();
	}

	Thread tid_waitclick;

	protected void startClick() {
		// TODO Auto-generated method stub
		tid_waitclick = new Thread(new WaitClickTask());
		tid_waitclick.setPriority(Thread.MAX_PRIORITY);
		tid_waitclick.start();
	}

	private class WaitClickTask implements Runnable {
		@Override
		public void run() {

			boolean keep_going = true;
			int Module_Result;
			Message uiMessage;
			// int count =0;
			Log.i("dkl",
					"WaitClickTask Thread START *****************************!");

			if (keep_going) {
				if (Thread.interrupted()) {
					Log.i("dkl", "WaitClickTask interrupted!");
					return ;
				}

				if (user_cancel) {
					Log.i("dkl", "WaitClickTask user cancel!");
					return ;
				}

				// count++;
				Module_Result = JmtSensor.getInstance(instance).WaitClick(
						onTouchListener);

				switch (Module_Result) {
				case JmtFP.ERR_OK:
					// Log.i (TAG, "Get FP Image OK");
					keep_going = false;
					user_cancel = true;
					break;

				case JmtFP.ERR_ABORT:
					Log.i("dkl", "WaitClickTask User Cancel");
					keep_going = false;
					user_cancel = true;
					break;

				default:
					Log.i("dkl", "Something wrong here");
					keep_going = false;
					user_cancel = true;
					break;
				}
			}
		}
	}

	OnTouchListener onTouchListener = new OnTouchListener() {

		@Override
		public int value(int val) {
			// TODO Auto-generated method stub
			Log.i("dkl", "WaitClick========val=====" + val);
			if (iFingerprintClickCallback == null) {
				Log.i("dkl", "iFingerprintClickCallback is null");
				return 0;
			}
			switch (val) {
			case 0:
//				try {
//					iFingerprintClickCallback.singleClick();
//				} catch (RemoteException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				break;
			case 1:
//				JmtSensor.getInstance(instance).SetInputKey(0);//驱动方式
				try {
					iFingerprintClickCallback.doubleClick();
					Log.i("dkl", "doubleClick");
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 2:
//				try {
//					iFingerprintClickCallback.longPress();
//				} catch (RemoteException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				break;

			default:
				break;
			}
			return 1;
		}
	};

	private class WaitNavTask implements Runnable {
		@Override
		public void run() {

			boolean keep_going = true;
			int Module_Result;
			Message uiMessage;
			// int count =0;
			Log.i("dkl",
					"WaitNavTask Thread START *****************************!");

			if (keep_going) {
				if (Thread.interrupted()) {
					Log.i("dkl", "WaitNavTask interrupted!");
					return;
				}

				if (user_cancel) {
					Log.i("dkl", "WaitNavTask user cancel!");
					return;
				}

				// count++;
				Module_Result = JmtSensor.getInstance(instance)
						.StartNavigation(onNavigationListener);

				switch (Module_Result) {
				case JmtFP.ERR_OK:
					Log.i ("dkl", "Get FP Image OK");
					keep_going = false;
					user_cancel = true;
					break;

				case JmtFP.ERR_ABORT:
					Log.i("dkl", "WaitClickTask User Cancel");
					keep_going = false;
					user_cancel = true;
					break;

				default:
					Log.i("dkl", "Something wrong here");
					keep_going = false;
					user_cancel = true;
					break;
				}
			}
		}
	}
	boolean flag = true;
	boolean flag1 = true;
	int n=1;int m=0;
	OnNavigationListener onNavigationListener = new OnNavigationListener() {

		@Override
		public int onNavigation(int arg0, final int x, final int y) {
			// TODO Auto-generated method stub
			Log.i("dkl", "arg0:" + arg0 + "arg1:" + x + "arg2:"
					+ y);
			if (iFingerprintClickCallback == null) {
				Log.i("dkl", "iFingerprintClickCallback is null");
				return 0;
			}
			if(x>0){
				n++;
				m=0;
			}
			if(x<0){
				m++;
				n=0;
			}
			
			if(m>3||n>3){
				flag=true;
			}else{
				flag=false;
			}
			if (flag&&flag1) {
				flag1 = false;
				try {
					iFingerprintClickCallback.navigation(x, y);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				flag1 = true;
			}
//			if (flag) {
//				flag = false;
//				if (x > 0) {
//					moveRight();
//				}
//				if (x < 0) {
//					moveLeft();
//				}
//				flag = true;
//			}
//			new Handler().postDelayed(new Runnable() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					
//				}
//			}, 1000);
//			moveLeft();
//			Log.i("dkl", "moveLeft END");
			return 0;
		}

	};
	Instrumentation inst=new Instrumentation();
	void moveLeft(){
//		new Thread(){
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				super.run();
				try {
					Log.e("dkl", "moveLeft0");
					inst.sendPointerSync(MotionEvent.obtain(
							SystemClock.uptimeMillis(),
							SystemClock.uptimeMillis(),
							MotionEvent.ACTION_DOWN, 240, 260, 0));
					inst.sendPointerSync(MotionEvent.obtain(
							SystemClock.uptimeMillis(),
							SystemClock.uptimeMillis(),
							MotionEvent.ACTION_MOVE, 100, 260, 0));
					inst.sendPointerSync(MotionEvent.obtain(
							SystemClock.uptimeMillis(),
							SystemClock.uptimeMillis(), MotionEvent.ACTION_UP,
							100, 260, 0));
					Log.e("dkl", "moveLeft");
				} catch (Exception e) {
					e.printStackTrace();
					Log.e("dkl", "e:"+e.toString());
				}
//			}
//		}.start();
	}
	
	void moveRight(){
//		new Thread(){
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				super.run();
				try {
					Log.e("dkl", "moveRight0");
					inst.sendPointerSync(MotionEvent.obtain(
							SystemClock.uptimeMillis(),
							SystemClock.uptimeMillis(),
							MotionEvent.ACTION_DOWN, 240, 260, 0));
					inst.sendPointerSync(MotionEvent.obtain(
							SystemClock.uptimeMillis(),
							SystemClock.uptimeMillis(),
							MotionEvent.ACTION_MOVE, 300, 260, 0));
					inst.sendPointerSync(MotionEvent.obtain(
							SystemClock.uptimeMillis(),
							SystemClock.uptimeMillis(), MotionEvent.ACTION_UP,
							300, 260, 0));
					Log.e("dkl", "moveRight");
				} catch (Exception e) {
					e.printStackTrace();
					Log.e("dkl", "e:"+e.toString());
				}
//			}
//		};
	}
	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub
		super.onRebind(intent);
		Log.e("dkl", "onRebind");
//		choseType();
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Log.e("dkl", "onBind");
		return mService;
	}

}
