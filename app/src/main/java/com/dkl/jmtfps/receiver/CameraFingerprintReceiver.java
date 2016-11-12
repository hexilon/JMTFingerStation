package com.dkl.jmtfps.receiver;

import com.dkl.jmtfps.SensorTakeCameraService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CameraFingerprintReceiver extends BroadcastReceiver {
	String resumeStr = "com.dkl.jmtfps.com.dkl.jmtfps.receiver.camera.onResumeTasks";
	String stopStr = "com.dkl.jmtfps.com.dkl.jmtfps.receiver.camera.onStopTasks";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		Log.i("CameraFingerprint","action:"+action);
		if (action.equals(resumeStr)) {
			context.startService(new Intent(context,SensorTakeCameraService.class));
			return;
		}
		if (action.equals(stopStr)) {
			context.stopService(new Intent(context,SensorTakeCameraService.class));
			return;
		}
	}

}
