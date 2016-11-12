package com.dkl.jmtfps;

import com.dkl.jmtfps.mode.FingerPrintModel;
import com.dkl.jmtfps.sensor.JmtSensor;
import com.dkl.jmtfps.util.Constants;
import com.dkl.jmtfps.util.PreferenceUtils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ManageFingerPrintActivity extends BaseActivity {
	private TextView textViewName;
//	private JmtSensor mSensor;
	private Button btnOpenApp;
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.id_btn_rename:
			showDialog();
			break;
		case R.id.id_btn_openapp:
			Intent intent=new Intent(Constants.ACTION_APP_OPEN);
			intent.putExtra("fp_id", fingerPrintModel.fp_data_index.trim());
			startActivity(intent);
			break;
		case R.id.id_btn_delete:
			String temp=PreferenceUtils.getInstance().getString(fingerPrintModel.fp_data_index, "no");
			if(temp!=null){
				PreferenceUtils.getInstance().remove(temp);
				PreferenceUtils.getInstance().remove(fingerPrintModel.fp_data_index);
			}
			//Log.e("JMT", "fingerPrintModel.fp_id = " + fingerPrintModel.fp_id);
			//Log.e("JMT", "Integer.parseInt(fingerPrintModel.fp_data_index.trim() = " +Integer.parseInt(fingerPrintModel.fp_data_index.trim()));
			//amo modify
			dbHelper.deleteFpById(String.valueOf(fingerPrintModel.fp_id));
			// +robert
			JmtSensor.getInstance(instance).Erase(Integer.parseInt(fingerPrintModel.fp_data_index.trim()));
			//JmtSensor.getInstance(instance).Erase((fingerPrintModel.fp_id-1));

			instance.sendBroadcast(new Intent(Constants.REFRESH_FP_DATA));
			Toast.makeText(instance, R.string.delete_ok, Toast.LENGTH_LONG).show();
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public
	void initView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_managefp);
		Intent intent = instance.getIntent();
		Object object = intent.getExtras().get(Constants.FINGER_MODEL_KEY);
		if (object instanceof FingerPrintModel) {
			fingerPrintModel = (FingerPrintModel) object;
		} else {
			System.exit(0);
		}
		textViewName=((TextView) findViewById(R.id.id_tv_fp_rename));
		textViewName.setText(fingerPrintModel.fp_name);
		btnOpenApp=(Button) findViewById(R.id.id_btn_openapp);
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		String temp=PreferenceUtils.getInstance().getString(fingerPrintModel.fp_data_index, "no");
		if((temp!=null)&&!"no".equals(temp)){
			btnOpenApp.setText(temp);
		}else{
			btnOpenApp.setText(R.string.openApp);
		}
	}

	public void showDialog() {
		Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.rename);
		builder.setIcon(R.drawable.edit_fpname);
		final EditText editText = new EditText(this);
		editText.setText(fingerPrintModel.fp_name);
		builder.setView(editText);
		builder.setPositiveButton(R.string.confirm,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						String edit = editText.getText().toString();
						if (edit.length() > 0) {
							fingerPrintModel.fp_name = edit;
							textViewName.setText(fingerPrintModel.fp_name);
							dbHelper.updateFpNameById(edit,
									String.valueOf(fingerPrintModel.fp_id));
							instance.sendBroadcast(new Intent(
									Constants.REFRESH_FP_DATA));
							Toast.makeText(instance, R.string.rename_ok, Toast.LENGTH_LONG).show();
						}
					}
				});
		builder.setNegativeButton(R.string.cancel, null);
		builder.show();
	}
}
