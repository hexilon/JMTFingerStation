package com.dkl.jmtfps;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FingerprintMainActivity extends BaseActivity{

	private Button[] mTabs;
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		setContentView(R.layout.activity_main_fp);
//		
//		mTabs = new Button[2];
//		mTabs[0] = (Button) this.findViewById(R.id.main_finger);
//		mTabs[1] = (Button) this.findViewById(R.id.main_use);
//		// 把第一个tab设为选中状态
//		mTabs[0].setSelected(true);
		
	}
	
	public void onTabClicked(View view){
		for (Button mTab : mTabs) {
			if(view.getId()==mTab.getId()){
				mTab.setSelected(true);
			}else{
				mTab.setSelected(false);
			}
		}
	}

}
