/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.server;

import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemService;
import android.os.UEventObserver;
import android.util.Slog;
import android.view.KeyEvent;
import com.android.internal.widget.LockPatternUtils;
import com.android.internal.policy.IFingerprintClickCallback;
import com.android.internal.policy.IFingerprintClickInterface;

/**
 * FingerprintObserver monitors for a Fingerprinting station.
 */
final class FingerprintObserver extends SystemService {
	private static final String TAG = "FingerprintObserver";

	private static final String FINGERPRINT_UEVENT_MATCH = "JMT101_STATE=WAKEUP";
	// private static final String FINGERPRINT_STATE_PATH =
	// "/sys/class/switch/dock/state";
	private static final String FINGERPRINT_ACTION = "com.dkl.jmtfps.com.dkl.jmtfps.service.FingerprintLockService_JMTWakeUp";
	private static final String FINGERPRINT_BINDSERVICE = "fingerprint_bind_service";
	private static final String FINGERPRINT_UNBINDSERVICE = "fingerprint_unbind_service";
	private static final String FINGERPRINT_ACTION_SCREEN_OFF = "com.dkl.jmtfps.android.intent.action.SCREEN_OFF";
	private static final String FINGERPRINT_ACTION_SCREEN_ON = "com.dkl.jmtfps.android.intent.action.SCREEN_ON";
	
	private final int MSG_SERVICE_CONNECTED = 0;
	private final int MSG_SERVICE_DISCONNECTED = 1;
	private final int MSG_SINGLE_CLICK = 2;
	private final int MSG_DOUBLE_CLICK = 3;
	private final int MSG_LONG_PRESS = 4;
	private final int MSG_NAVIGATION = 5;
	// private final Object mLock = new Object();
	private IFingerprintClickInterface mFingerprintService;
	private LockPatternUtils mLockPatternUtils;
	private Context mContext;

	public FingerprintObserver(Context context) {
		super(context);
		mContext = context;
		mObserver.startObserving(FINGERPRINT_UEVENT_MATCH);
		// 注册服务广播
		final IntentFilter filter = new IntentFilter();
		filter.addAction(FINGERPRINT_BINDSERVICE);
		filter.addAction(FINGERPRINT_UNBINDSERVICE);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		context.registerReceiver(receiver, filter);
		Slog.v(TAG, "FingerprintObserver is init");
		mLockPatternUtils = new LockPatternUtils(context);
	}
	/**
	 * 解锁方法 尝试
	 */
	public void reportSuccessfulUnlockAttempt() {
            //com.android.keyguard.KeyguardUpdateMonitor.getInstance(mContext).clearFailedUnlockAttempts();
            //mLockPatternUtils.reportSuccessfulPasswordAttempt();
    }

	private final BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			Slog.v(TAG, "FingerprintObserver BroadcastReceiver is " + action);
			if (FINGERPRINT_BINDSERVICE.equals(action)) {
				fpBindService();
			} else if (FINGERPRINT_UNBINDSERVICE.equals(action)) {
				fpUnBindService();
			}
			if(Intent.ACTION_SCREEN_OFF.equals(action)){
				Intent in = new Intent(FINGERPRINT_ACTION_SCREEN_OFF);
				in.setPackage("com.dkl.jmtfps");
				in.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
				mContext.sendBroadcast(in);
			}
			if(Intent.ACTION_SCREEN_ON.equals(action)){
				Intent in = new Intent(FINGERPRINT_ACTION_SCREEN_ON);
				in.setPackage("com.dkl.jmtfps");
				in.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
				mContext.sendBroadcast(in);
			}
			
		}
	};

	private final Handler mFingerprintHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SERVICE_CONNECTED:
				handleServiceConnected();
				break;
			case MSG_SERVICE_DISCONNECTED:
				handleServiceDisconnected();
				break;
			case MSG_SINGLE_CLICK:
				handleSingleClick();
				break;
			case MSG_DOUBLE_CLICK:
				handleDoubleClick();
				break;
			case MSG_LONG_PRESS:
				handleLongPress();
				break;
			case MSG_NAVIGATION:
				handleNavigation();
				break;
			}
		}
	};

	protected void handleServiceDisconnected() {
		// TODO Auto-generated method stub
		
		mFingerprintService = null;
	}

	protected void handleServiceConnected() {
		// TODO Auto-generated method stub
		try {
			mFingerprintService.registerClick(iFingerprintClickCallback);
		} catch (RemoteException e) {

		}
	}

	protected void handleSingleClick() {
		// TODO Auto-generated method stub

	}

	protected void handleDoubleClick() {
		// TODO Auto-generated method stub
		try {
			Instrumentation inst = new Instrumentation();
			inst.sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	protected void handleLongPress() {
		// TODO Auto-generated method stub
		try {
			Instrumentation inst = new Instrumentation();
			inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	protected void handleNavigation() {
		// TODO Auto-generated method stub

	}

	IFingerprintClickCallback iFingerprintClickCallback = new IFingerprintClickCallback.Stub() {

		@Override
		public void navigation(int x, int y) throws RemoteException {
			// TODO Auto-generated method stub
			Slog.v(TAG, "iFingerprintClickCallback navigation");
			mFingerprintHandler.sendEmptyMessage(MSG_NAVIGATION);
		}

		@Override
		public void longPress() throws RemoteException {
			// TODO Auto-generated method stub
			Slog.v(TAG, "iFingerprintClickCallback longPress");
			mFingerprintHandler.sendEmptyMessage(MSG_LONG_PRESS);
		}

		@Override
		public void doubleClick() throws RemoteException {
			// TODO Auto-generated method stub
			Slog.v(TAG, "iFingerprintClickCallback doubleClick");
			mFingerprintHandler.sendEmptyMessage(MSG_DOUBLE_CLICK);
		}

		@Override
		public void singleClick() throws RemoteException {
			// TODO Auto-generated method stub
			Slog.v(TAG, "iFingerprintClickCallback singleClick");
			mFingerprintHandler.sendEmptyMessage(MSG_SINGLE_CLICK);
		}
	};

	private ServiceConnection mFingerprintConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder iservice) {
			Slog.v(TAG, "Connected to Fingerprint Click service");
			mFingerprintService = IFingerprintClickInterface.Stub
					.asInterface(iservice);
			mFingerprintHandler.sendEmptyMessage(MSG_SERVICE_CONNECTED);
		}

		public void onServiceDisconnected(ComponentName className) {
			Slog.v(TAG, "Unexpected disconnect from Fingerprint Click service");
			mFingerprintHandler.sendEmptyMessage(MSG_SERVICE_DISCONNECTED);
		}
	};

	public boolean fpBindService() {
		Slog.v(TAG, "Fingerprint fpBindService");
		try {
			mContext.bindService(
					new Intent(IFingerprintClickInterface.class.getName())
							.setPackage("com.dkl.jmtfps"),
					mFingerprintConnection, Context.BIND_AUTO_CREATE);
		} catch (Exception e) {
			Slog.v(TAG, "Fingerprint bindService is fail :" + e);
		}
		return true;
	}

	public boolean fpUnBindService() {
		Slog.v(TAG, "Fingerprint fpUnBindService");
		try {
			mFingerprintService.unregisterClick();
		} catch (RemoteException e) {

		}
		mContext.unbindService(mFingerprintConnection);
		return true;
	}

	@Override
	public void onStart() {
	}

	@Override
	public void onBootPhase(int phase) {

	}

	//this is get uevent ,and send broadcast to app.
	private final UEventObserver mObserver = new UEventObserver() {
		@Override
		public void onUEvent(UEventObserver.UEvent event) {
			Slog.v(TAG, "Fingerprint UEVENT: " + event.toString());

			Intent intent = new Intent(FINGERPRINT_ACTION);
			intent.setPackage("com.dkl.jmtfps");
			intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
			mContext.sendBroadcast(intent);
			Slog.v(TAG, "Fingerprint UEVENT: " + event.toString());
		}
	};

}
