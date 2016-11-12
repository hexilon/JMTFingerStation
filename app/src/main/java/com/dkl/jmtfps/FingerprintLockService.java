package com.dkl.jmtfps;

import com.android.internal.policy.IFingerprintLockCallback;
import com.android.internal.policy.IFingerprintLockInterface;
import com.dkl.jmtfps.sensor.JmtSensor;
import com.dkl.jmtfps.util.Constants;
import com.dkl.jmtfps.util.IConstants;
import com.dkl.jmtfps.util.Log;
import com.dkl.jmtfps.util.PreferenceUtils;
import com.jmt.fps.JmtFP;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;

public class FingerprintLockService extends Service implements IConstants {
    private static final String TAG = "dkl";
    public static Context instance;
    private String fp_id = null;
    /**
     *	amo add @2016/09/07
     */
    private PowerManager mPowerManager;
    private KeyguardManager mKeyguardManager;
    public static IFingerprintLockCallback mCallback;
    private IFingerprintLockInterface.Stub mService = new IFingerprintLockInterface.Stub() {

        @Override
        public synchronized void unregisterCallback(IFingerprintLockCallback cb) {
            mCallback = cb;
            Log.i(TAG, "unregisterCallback");
            if (Constants.MATCH_FINGER) {
                /**
                 *	amo modify
                 *		if screen unlocks, KeyguardViewMediator will call unregisterCallback()
                 *			implemented by FingerprintLockService here.
                 */
                int ret_cancel = JmtSensor.getInstance(instance).CancelAction();
                Log.i(TAG, "CancelAction:" + ret_cancel);
            }
            PreferenceUtils.getInstance().putBoolean(Constants.FINGER_SLEEP_UNLOCK_ISTURE, false);
        }

        public synchronized void stopUi() {
        }

        public synchronized void startUi(IBinder containingWindowToken, int x,
                                         int y, int width, int height, boolean useLiveliness) {
        }

        @Override
        public synchronized void registerCallback(IFingerprintLockCallback cb) {
            Log.e(TAG, "registerCallback");
            mCallback = cb;
            /**
             *	amo add @2016/09/07
             *		Unfortunately if screen is on and screen is unlock, but SDK verify routine is running such as certain while loop
             *		, the unlock screen using pin or pattern will be blocked and can NOT appear until SDK verify routine is done.
             *
             *		Based on frameworks code modified that KeyguardViewMediator calls jmtBindService()
             *		when its onScreenTurnedOn() AND onScreenTurnedOFF() is executed.
             *		, then if our jmtBindService() is successful, it will invoke mJmtService.registerCallback(cb) through handler mechanism.
             *		and jobs of registerCallback() is implemented by FingerprintLockService here.
             */
            /*
            boolean isNeedtoCancelAction = false;
            isNeedtoCancelAction = ((mPowerManager.isScreenOn()) && Constants.MATCH_FINGER);
            if(isNeedtoCancelAction) {
                if(JmtSensor.getInstance(instance).CancelAction() == JmtFP.ERR_OK){
                    Log.e(TAG, "CancelAction is sucessful");
                }else{
                    Log.e(TAG, "CancelAction is NOT sucessful");
                }
                if(JmtSensor.getInstance(instance).SetWaitTouch() == JmtFP.ERR_OK){
                    Log.e(TAG, "SetWaitTouch is sucessful");
                }else{
                    Log.e(TAG, "SetWaitTouch is NOT sucessful");
                }
            }
            */
        }

        public void onScreenTurnedOn(IFingerprintLockCallback cb)
                throws RemoteException {
            Log.e(TAG, "onScreenTurnedOn");
            mCallback = cb;
            if (JmtSensor.getInstance(instance) != null) {
                int r = JmtSensor.getInstance(instance).SetWaitTouch();
                if (r != 0) {
                    r = JmtSensor.getInstance(instance).SetWaitTouch();
                }
                Log.e("dkl", "SetWaitTouch:" + r);
            }
        }

        /**
         * 关闭屏幕
         */
        public void onScreenTurnedOff(IFingerprintLockCallback cb)
                throws RemoteException {
            Log.e(TAG, "onScreenTurnedOff");
            mCallback = cb;
            if (JmtSensor.getInstance(instance) != null) {
                int r = JmtSensor.getInstance(instance).SetWaitTouch();
                if (r != 0) {
                    r = JmtSensor.getInstance(instance).SetWaitTouch();
                }
                Log.e("dkl", "SetWaitTouch:" + r);
            }

        }
    };

    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate ");
        instance = this;
        // final IntentFilter filter = new IntentFilter();
        // filter.addAction(Intent.ACTION_SCREEN_ON);
        // filter.addAction(Intent.ACTION_SCREEN_OFF);
        // registerReceiver(receiver, filter);
        // mObserver.startObserving(Constants.FINGERPRINT_UEVENT_MATCH);
        /**
         *	amo add @2016/09/07
         */
        mPowerManager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        mKeyguardManager = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
    }

    /**
     *	 amo modify
     *
     *	 <service
     *		 android:name=".FingerprintLockService"
     *		 android:exported="true"
     *		 android:theme="@android:style/Theme.NoTitleBar" >
     *		 <intent-filter>
     *			 <action android:name="com.android.internal.policy.IFingerprintLockInterface" />
     *		 </intent-filter>
     *	 </service>
     *
     */
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return mService;
    }

}