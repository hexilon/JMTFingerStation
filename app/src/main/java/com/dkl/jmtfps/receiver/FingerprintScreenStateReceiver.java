package com.dkl.jmtfps.receiver;

import com.dkl.jmtfps.FingerprintLockService;
import com.dkl.jmtfps.sensor.JmtSensor;
import com.dkl.jmtfps.util.Constants;
import com.dkl.jmtfps.util.PreferenceUtils;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.RemoteException;
import android.util.Log;

public class FingerprintScreenStateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent!=null){
			String action = intent.getAction();
			if(Constants.FINGERPRINT_ACTION_SCREEN_OFF.equals(action)){
                Log.e("JMT", "FINGERPRINT_ACTION_SCREEN_OFF");
                if(JmtSensor.getInstance(context).CancelAction() == 0){
                    Log.e("JMT", "CancelAction is called and done");
                }
                if(JmtSensor.getInstance(context).SetWaitTouch() == 0){
                    Log.e("JMT", "SetWaitTouch is called and done");
                }
			}
            else if(Constants.FINGERPRINT_ACTION_SCREEN_ON.equals(action)){
                Log.e("JMT", "FINGERPRINT_ACTION_SCREEN_ON");
			}
            else if(Intent.ACTION_USER_PRESENT.equals(action)){
				Log.e("JMT", ">>>>>ACTION_USER_PRESENT");
				//Log.e("JMT", "Intent.ACTION_USER_PRESENT.equals(action)");

				if(Constants.MATCH_FINGER){
					Log.e("JMT", ">>>>>ACTION_USER_PRESENT and Constants.MATCH_FINGER is true");
                    if(JmtSensor.getInstance(context).SetWaitTouch() == 0){
                        Log.e("JMT", "SetWaitTouch is called and done");
                    }
					Constants.MATCH_FINGER= false;
				}else if(!Constants.MATCH_FINGER){
                    Log.e("JMT", ">>>>>ACTION_USER_PRESENT and Constants.MATCH_FINGER is true");
                    if(JmtSensor.getInstance(context).SetWaitTouch() == 0){
                        Log.e("JMT", "SetWaitTouch is called and done");
                    }
                    //Constants.MATCH_FINGER= false;
                }

                Constants.MATCH_FINGER= false;
				//Constants.MATCH_FINGER= false;
				//int result = JmtSensor.getInstance(context).SetWaitTouch();
				//Log.e("JMT", "SetWaitTouch:"+result);
			}
            else if(Intent.ACTION_BOOT_COMPLETED.equals(action)){
				Log.e("JMT", ">>>>>ACTION_BOOT_COMPLETED");
			}
		}
	}

}
