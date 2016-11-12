package com.dkl.jmtfps.receiver;

import java.util.Arrays;
import java.util.List;

import com.dkl.jmtfps.util.Constants;
import com.dkl.jmtfps.util.Log;
import com.dkl.jmtfps.util.PreferenceUtils;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AppLockBroadcastReceiver extends BroadcastReceiver {

	private String INTENT_ADD_ACTIVITY_ACTION = "com.dkl.jmtfps.com.dkl.jmtfps.receiver.AppLockBootcompleReceiver.AddActivity";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("dkl", "AppLockBroadcastReceiver_onReceive");
		boolean lockState = PreferenceUtils.getInstance().getBoolean(
				Constants.FINGER_OPEN_APP_UNLOCK, false);
		if (!lockState) {
			return;
		}
//		boolean openOne=PreferenceUtils.getInstance().getBoolean(Constants.FINGER_OPEN_APP_UNLOCK_TAG, false);
//		if(!openOne){
//			return ;
//		}
		
		if (intent.getAction().equals(INTENT_ADD_ACTIVITY_ACTION)) {
			int intentnum = intent.getIntExtra("applock_number", 0);
			String intentpkg = intent.getStringExtra("applock_pkgname");
//			intent.putExtra("applock_number", 0);
//			intent.putExtra("applock_pkgname", "");
			Log.i("dkl", "intentnum:" + intentnum);
			switch (intentnum) {
			case 0:
				Log.i("dkl", "intentpkg:" + intentpkg);
				if (intentpkg == null) {
					return;
				}
				String unlockApps = PreferenceUtils.getInstance().getString(
						Constants.SETTINGS_UNLOCK_APP_PACKAGENAMES, "");
				String[] unlockAppArrays = unlockApps.split("\\|");
				List<String> unlockAppList = Arrays.asList(unlockAppArrays);
				if(unlockAppList.contains(intentpkg)){
					return ;
				}
				String needApps = PreferenceUtils.getInstance().getString(
						Constants.SETTINGS_NEEDLOCK_APP_PACKAGENAMES, "");
				String[] needAppArrays = needApps.split("\\|");
				List<String> needAppList = Arrays.asList(needAppArrays);
				try {
					if (needAppList.contains(intentpkg)) {
						Intent intent1 = new Intent();
						intent1.putExtra("packageName", intentpkg);
						intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
						intent1.setClassName("com.dkl.jmtfps",
								"com.dkl.jmtfps.applock.AppLockUIActivity");
						intent1.setAction(Constants.ACTION_APPLOCK_UI);
						intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent1);
					}
				} catch (Exception e) {
					Log.i("dkl", "e:" + e);
				}
				break;
			default:
				break;
			}

		}

	}

}