package com.dkl.jmtfps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SetupIntro extends BaseActivity{

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		startActivity(new Intent(instance, ListFingerPrintActivity.class));
		finish();
	}

}
