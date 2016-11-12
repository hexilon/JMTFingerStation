package com.dkl.jmtfps.receiver;

import com.dkl.jmtfps.FingerprintLockService;
import com.dkl.jmtfps.sensor.JmtSensor;
import com.dkl.jmtfps.sensor.JmtSensor.OnTouchListener;
import com.dkl.jmtfps.util.Constants;
import com.dkl.jmtfps.util.IConstants;
import com.dkl.jmtfps.util.Log;
import com.dkl.jmtfps.util.PreferenceUtils;
import com.jmt.fps.JmtFP;
import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.Vibrator;

public class JMTWakeUpReceiver extends BroadcastReceiver implements IConstants {
    private static final String TAG = "JMTWakeUpReceiver";
    private static final String JMT_ACTION = "com.dkl.jmtfps.com.dkl.jmtfps.service.FingerprintLockService_JMTWakeUp";
    private static boolean wakeUpIng = false;
    private Vibrator vibrator;
    public static Handler uiUpdateHandler;
    private static Context instance;
    private Thread matchThread;
    private VerifyDownTimer verifyDownTimer;
    private String fp_id = null;

    /**
     *  amo add, for verify notify works
     */
    private static final int _ACQUIRED_LOW_COVER        = 1107;
    private static final int _ACQUIRED_BAD_IMAGE        = 1108;
    private static final int ACQUIRED_VERIFY_MATCH      = 1109;
    private static final int ACQUIRED_VERIFY_NOT_MATCH  = 1110;

    @SuppressLint("NewApi")
    @Override
    public void onReceive(final Context context, Intent intent) {
        // TODO Auto-generated method stub

        Log.e(TAG, "JMTWakeUpReceiver");
        instance = context;
        mPowerManager = (PowerManager) instance.getSystemService(Context.POWER_SERVICE);
        KeyguardManager mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if(Constants.ENROLL_ACTION_TAG){
            Log.e(TAG, "ENROLL_ACTION_TAG == true");
            return;
        }
        /**
         *   amo modify
         *   if user is using our apk except adding fingerprint templates, they can also use click function.
         */
        Log.e(TAG, "PreferenceUtils.getInstance().getBoolean(Constants.FINGER_DOUBLE_CLICK, false)" + PreferenceUtils.getInstance().getBoolean(Constants.FINGER_DOUBLE_CLICK, false));
        boolean isOpenClickFunction = false;
        isOpenClickFunction = (!mKeyguardManager.isKeyguardLocked()) && (mPowerManager.isScreenOn())
                && PreferenceUtils.getInstance().getBoolean(Constants.FINGER_DOUBLE_CLICK, false);
        if(isOpenClickFunction){
            Log.e(TAG, ">>>>>FINGER_CLICK");
            if(JmtSensor.getInstance(instance).SetInputKey(1) == JmtFP.ERR_OK){
                Log.e(TAG, "SetInputKey is sucessful");
            }else{
                Log.e(TAG, "SetInputKey is NOT sucessful");
            }
            /**
             *   amo note
             *   whenever consume a uevent came from irq,
             *       run "JmtSensor.SetWaitTouch()" to make sensor waitting for the next one.
             */
            if(JmtSensor.getInstance(instance).SetWaitTouch() == JmtFP.ERR_OK){
                Log.e(TAG, "SetWaitTouch is sucessful");
            }else{
                Log.e(TAG, "SetWaitTouch is NOT sucessful");
            }
            return;
        }
        //verifyDownTimer = new VerifyDownTimer(5000, 1000);
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        uiUpdateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {

                case MSG_SUCCESSFUL:
                    Log.e(TAG, "handleMessage(): MSG_SUCCESSFUL.");
                    //JmtSensor.getInstance(instance).SystemWakeup();
                    if (FingerprintLockService.mCallback != null) {
                        synchronized (this){

                            /**
                             *	amo modify
                             *       if open this code block, acquire wakelock and system could keep awake.
                             */
                            acquireWakeLock();
                            /**
                             *  amo
                             *    FingerprintLockService.mCallback.pokeWakelock() will invoke
                             *      the PowerManager.wakeUp(SystemClock.uptimeMillis() in KeyguardViewMediator
                             *      and wake up device on.
                             *    , so I open this code block and wake up system after executing WakeLock.acquire().
                             */
                            try {
                                FingerprintLockService.mCallback.pokeWakelock(1000);
                            } catch (RemoteException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            try {
                                FingerprintLockService.mCallback.unlock();

                            } catch (RemoteException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            /**
            	             *	amo modify
             	             *       if open this code block, release wakelock and system could fall asleep.
             	             */
                            releaseWakeLock();
                        }
                    }else{
                        Log.i(TAG, "FingerprintLockService.mCallback is null");
                        //JmtSensor.getInstance(instance).SystemWakeup();
                        PreferenceUtils.getInstance().putBoolean(Constants.FINGER_SLEEP_UNLOCK_ISTURE, true);
                    }
                    if (fp_id != null) {
                        String pg = PreferenceUtils.getInstance().getString(fp_id, "no");
                        if (pg != null && !pg.equals("no")) {
                            String pa = PreferenceUtils.getInstance()
                                    .getString(pg, "no");
                            Intent intent = new Intent();
                            intent.setClassName(pg, pa);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            instance.startActivity(intent);
                        }
                    }
                    end = SystemClock.currentThreadTimeMillis();
                    Log.i(TAG, "Verify_Time1:" + (end - start));
                    break;

                case MSG_FAILURE:
                    Log.e(TAG, "handleMessage(): MSG_FAILURE.");
                    vibrator.vibrate(new long[] { 100, 300 }, -1);
                    /**
                     * 	amo modify @2016/09/07
                     *  	Unfortunately if screen is on and screen is unlock, but SDK verify routine is running such as certain while loop
                     *		, the unlock screen using pin or pattern will be blocked and can NOT appear until SDK verify routine is done.
                     *		To open this code block, it's workable way to deal with that by stopping SDK running works immediately
                     *		and set sensor waiting for next touch.
                     *		Otherwise the "failed try times limit" will be useless after opening this code block.
                     */
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
                    break;

                case MSG_ADJUST:
                    Log.e(TAG, "handleMessage(): MSG_ADJUST.");
                    vibrator.vibrate(new long[] { 100, 300 }, -1);
                    break;

                case MSG_WAIT_FINGER:
                    //Log.e(TAG, "handleMessage(): MSG_ADJUST.");
                    //vibrator.vibrate(new long[] { 100, 300 }, -1);
                    break;

                case MSG_CHANGE_FINGER:
                    Log.e(TAG, "handleMessage(): MSG_CHANGE_FINGER.");
                    vibrator.vibrate(new long[] { 100, 300 }, -1);
                    break;
                }
                super.handleMessage(msg);
            }
        };
        Log.e(TAG, "JMTWakeUpReceiver1");
        if(intent.getAction().equals(JMT_ACTION)){
            Log.e(TAG, "JMTWakeUpReceiver2");
            if(mKeyguardManager.isKeyguardLocked() || !(mPowerManager.isScreenOn())){
                Log.e(TAG, "JMTWakeUpReceiver ---> going to call VerifyFingerprint()");
                VerifyFingerprint();
            }
        }
        /*
        if (intent.getAction().equals(JMT_ACTION)) {
			if (mKeyguardManager.inKeyguardRestrictedInputMode() && mKeyguardManager.isKeyguardLocked()){//判断是否在锁屏界面
				if (!wakeUpIng) {
					wakeUpIng = true;
                    // acquireWakeLock();
                    // releaseWakeLock();
                    // JmtSensor.getInstance(instance).SystemWakeup();
                    // vibrator.vibrate(new long[] { 100, 600 }, -1);
                    Log.e(TAG, "JMTWakeUpReceiver2");
                    // +robert
                    if(mKeyguardManager.isKeyguardLocked()||!(mPowerManager.isScreenOn())){
                        Log.e(TAG, "JMTWakeUpReceiver ---> going to call VerifyFingerprint()");
                        VerifyFingerprint();
                    }
				}
            }
		}
		*/
    }

    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock = null;

    private void acquireWakeLock() {
        if (null == mWakeLock) {
            Log.e(TAG, "Acquiring wakelock");
            mPowerManager = (PowerManager) instance
                    .getSystemService(Context.POWER_SERVICE);
            mWakeLock = mPowerManager.newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK
                            | PowerManager.ON_AFTER_RELEASE, this.getClass()
                            .getCanonicalName());
            mWakeLock.acquire();
        }
    }

    private void releaseWakeLock() {
        if (null != mWakeLock && mWakeLock.isHeld()) {
            Log.e(TAG, "call releaseWakeLock");
            mWakeLock.release();
            mWakeLock = null;
        }
    }

    class VerifyDownTimer extends CountDownTimer {

        public VerifyDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
            /*
            JmtSensor.getInstance(instance).CancelAction();
            Log.e(TAG, "SetWaitTouch");
            int r = JmtSensor.getInstance(instance).SetWaitTouch();
            Log.e(TAG, "SetWaitTouch:" + r);
            */
        }

        @Override
        public void onTick(long arg0) {
            // TODO Auto-generated method stub
        }

    }

    @Override
    public IBinder peekService(Context myContext, Intent service) {
        // TODO Auto-generated method stub
        return super.peekService(myContext, service);
    }

    private void resetTimer() {
        // if (verifyDownTimer != null) {
        verifyDownTimer.cancel();
        // verifyDownTimer.start();
        // }
    }

    long start, end, stime;

    private void VerifyFingerprint() {
        if (!PreferenceUtils.getInstance().getBoolean(
                Constants.FINGER_OPEN_UNLOCK, false)) {
            return;
        }
        if (matchThread == null) {
            matchThread = new Thread(new MatchFingerThread());
            matchThread.start();
        }
    }

    private class MatchFingerThread implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Log.e(TAG, "Begin VerifyFingerprint !!");
            //acquireWakeLock();
            JmtSensor.getInstance(instance).CancelAction();
            int result = -1;
            Constants.MATCH_FINGER = true;
            Log.e(TAG, "CAll verify jni");
            synchronized (this){
                result = JmtSensor.getInstance(instance).Verify(new OnTouchListener() {
                    @Override
                    public int value(int val) {
                        // TODO Auto-generated method stub
                        callBack(val);
                        return 0;
                    }
                });
            }
            /*
            ***************************************************************************/
            /**
             *   amo modify
             *   "Constants.MATCH_FINGER" means if the program of "JmtSensor.getInstance(instance).Verify()" is completed or not.
             *   , I guess that I should set "Constants.MATCH_FINGER = false" before turn-on the screen as dkl's flow!?
             *   --> I move "Constants.MATCH_FINGER = false" to (IFingerprintLockInterface.Stub) mService.unregisterCallback()
             */
            Constants.MATCH_FINGER= false;
            /*
            ***************************************************************************/
            /**
             *  amo modidy
             *	    move these jobs to notify and handler mechanism.
             */
            /*
            if (result >= 0) {
                mWakeLock = mPowerManager.newWakeLock(
                        PowerManager.ACQUIRE_CAUSES_WAKEUP
                                | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
                if (!mWakeLock.isHeld()) {
                    Log.e(TAG, "Acquiring wakelock");
                    mWakeLock.acquire();
                }
                fp_id = result + "";
                moduleResult(0);
            } else {
                moduleResult(result);
            }
            */
            wakeUpIng = false;
            //releaseWakeLock();
        }
    }

    public void callBack(int val) {
        // TODO Auto-generated method stub
        if (flag != 0) {
            return;
        }
        Message uiMessage;
        switch (val) {
			case JmtFP.NOTIFY_LEAVE_FINGER:
				Log.e(TAG, ">>>>>NOTIFY_LEAVE_FINGERET");
				uiMessage = new Message();
				uiMessage.what = MSG_NOTIFY_LEAVE_FINGER;
				uiUpdateHandler.sendMessage(uiMessage);
				break;
			case JmtFP.TOO_WET:
				Log.e(TAG, ">>>>>TOO_WET");
				uiMessage = new Message();
				uiMessage.what = MSG_TOO_WET;
				uiUpdateHandler.sendMessage(uiMessage);
				break;
			case JmtFP.TOO_SMALL:
				Log.e(TAG, ">>>>>TOO_SMALL");
				uiMessage = new Message();
				uiMessage.what = MSG_TOO_SMALL;
				uiUpdateHandler.sendMessage(uiMessage);
				break;
			case JmtFP.ADJUST:
				Log.e(TAG, ">>>>>ADJUST");
				Log.e(TAG, "Please adjust your finger");
				// uiMessage = new Message();
				// uiMessage.what = MSG_ADJUST;
				// uiUpdateHandler.sendMessage(uiMessage);
				uiMessage = new Message();
				uiMessage.what = MSG_FAILURE;
				uiUpdateHandler.sendMessage(uiMessage);
				//resetTimer();
				//vibrator.vibrate(new long[] { 100, 300 }, -1);
				break;
			case JmtFP.WAIT_FINGER:
				Log.e(TAG, ">>>>>WAIT_FINGER");
				uiMessage = new Message();
				uiMessage.what = MSG_WAIT_FINGER;
				uiUpdateHandler.sendMessage(uiMessage);
				break;
			case JmtFP.CHANGE_FINGER:
				Log.e(TAG, ">>>>>CHANGE_FINGER");
				uiMessage = new Message();
				uiMessage.what = MSG_FAILURE;
				uiUpdateHandler.sendMessage(uiMessage);
				//resetTimer();
				//vibrator.vibrate(new long[] { 100, 300 }, -1);
				break;
			/**
             * amo note
			 *	Now, SDK has been modified that whenever fingerprint and templates match
			 *	, the advice result code(ACQUIRED_VERIFY_MATCH) will be passed from mgr_verify() of SDK.
			 */
			case ACQUIRED_VERIFY_MATCH:
				Log.e(TAG, ">>>>>ACQUIRED_VERIFY_MATCH");
				/**
            	 *	amo modidy
            	 *	    move light screen and keyguard unlock jobs to this position of notify and handler mechanism
            	 *	    if open this code block, works first screen on, second unlock.
             	 */
                /*
				synchronized (this){
                    mWakeLock = mPowerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
                            | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
                    if (!mWakeLock.isHeld()) {
                        Log.e(TAG, "Acquiring wakelock");
                        mWakeLock.acquire();
                    }
                }
                releaseWakeLock();
                */
                moduleResult(JmtSensor.ERR_OK);
				break;
            /**amo note
			 *	Now, SDK has been modified that whenever fingerprint and templates NOT match
			 *	, the advice result code(ACQUIRED_VERIFY_NOT_MATCH) will be passed from mgr_verify() of SDK.
			 */
            case ACQUIRED_VERIFY_NOT_MATCH:
                Log.e(TAG, ">>>>>ACQUIRED_VERIFY_NOT_MATCH");
				/**
            	 *	amo modidy
            	 *	move these jobs to notify and handler mechanism, because context is passed from onReceive()
             	 */
                uiMessage = new Message();
                uiMessage.what = MSG_FAILURE;
                uiUpdateHandler.sendMessage(uiMessage);
                break;
            /**amo note
			 *	Now, SDK has been modified that whenever fingerprint and templates LOW COVER
			 *	, the advice result code(_ACQUIRED_LOW_COVER) will be passed from mgr_verify() of SDK.
			 */
            case _ACQUIRED_LOW_COVER:
                Log.e(TAG, ">>>>>_ACQUIRED_LOW_COVER");
				/**
            	 *	amo modidy
            	 *	move these jobs to notify and handler mechanism, because context is passed from onReceive()
             	 */
                uiMessage = new Message();
                uiMessage.what = MSG_FAILURE;
                uiUpdateHandler.sendMessage(uiMessage);
                break;
            /**amo note
			 *	Now, SDK has been modified that whenever fingerprint and templates BAD IMAGE
			 *	, the advice result code(_ACQUIRED_BAD_IMAGE) will be passed from mgr_verify() of SDK.
			 */
            case _ACQUIRED_BAD_IMAGE:
                Log.e(TAG, ">>>>>_ACQUIRED_BAD_IMAGE");
				/**
            	 *	amo modidy
            	 *	move these jobs to notify and handler mechanism, because context is passed from onReceive()
             	 */
                uiMessage = new Message();
                uiMessage.what = MSG_FAILURE;
                uiUpdateHandler.sendMessage(uiMessage);
                break;

        }
    }

    private int flag = 0;


    public void moduleResult(int result) {
        // TODO Auto-generated method stub
        Log.e(TAG, "mSensor.Verify end -------");
        flag = result;

        Message uiMessage;
        switch (result) {
        case JmtSensor.ERR_OK:
            Log.e(TAG, "mSensor.Verify ok -------");
            uiMessage = new Message();
            uiMessage.what = MSG_SUCCESSFUL;
            uiUpdateHandler.sendMessage(uiMessage);
            break;

        case JmtSensor.ERR_NOMATCH:
            Log.e(TAG, "FP Verify Fail, Try again");
            uiMessage = new Message();
            uiMessage.what = MSG_FAILURE;
            uiUpdateHandler.sendMessage(uiMessage);
            break;

        case JmtSensor.ERR_TIMEOUT:
            /* notify user to swipe finger */
            Log.i(TAG, "ERR_TIMEOUT");
            break;

        case JmtSensor.ERR_ABORT:

            break;
        default:
            Log.i(TAG, "Something wrong here");
            break;
        }
    }

}