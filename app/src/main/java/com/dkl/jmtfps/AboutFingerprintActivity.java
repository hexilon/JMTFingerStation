package com.dkl.jmtfps;

import com.dkl.jmtfps.sensor.JmtSensor;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutFingerprintActivity extends BaseActivity{

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void initView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_aboutfp);
		
		TextView versionName=(TextView) findViewById(R.id.textView1);
		TextView sdkversionName=(TextView) findViewById(R.id.textView4);
		String string=getResources().getString(R.string.about_version_name);
		try {
			versionName.setText(String.format(string,this.getPackageManager().getPackageInfo(this.getPackageName(),0).versionName));
		} catch (NameNotFoundException e)  {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		string=getResources().getString(R.string.about_sdk_version_name);
		sdkversionName.setText(String.format(string,JmtSensor.getInstance(instance).GetSDKVersion()));
//		String JMT_ACTION="com.dkl.jmtfps.com.dkl.jmtfps.service.FingerprintLockService_JMTWakeUp";
//		Intent intent=new Intent(JMT_ACTION);
//		intent.setPackage("com.dkl.jmtfps");
//		instance.sendBroadcast(intent);
	}

}
