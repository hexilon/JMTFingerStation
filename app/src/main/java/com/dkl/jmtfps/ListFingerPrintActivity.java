package com.dkl.jmtfps;

import java.util.ArrayList;
import java.util.List;
import com.dkl.jmtfps.mode.FingerPrintModel;
import com.dkl.jmtfps.sensor.JmtSensor;
import com.dkl.jmtfps.util.Constants;
import com.dkl.jmtfps.util.Log;
import com.jmt.fps.JmtFP;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @category 指纹列表
 * @author dkl
 * 
 */
public class ListFingerPrintActivity extends BaseActivity implements OnItemClickListener {
	private static final String TAG = "JMT";
    private Switch switch1, switch3;
	private Button buttonAdd, btnTest;
	private ListView listView = null;
	private List<FingerPrintModel> fingerPrintModels = new ArrayList<FingerPrintModel>();
    private JmtSensor mSensor;

	final class Hooker {
		TextView textViewName;
	}

	BaseAdapter baseAdapter = new BaseAdapter() {

		@Override
		public View getView(int arg0, View view, ViewGroup arg2) {
			// TODO Auto-generated method stub
			Hooker hooker;
			if (view == null) {
				hooker = new Hooker();
				view = LayoutInflater.from(instance).inflate(
						R.layout.fp_item_list, null);
				hooker.textViewName = (TextView) view
						.findViewById(R.id.textviewName);
				view.setTag(hooker);
			} else {
				hooker = (Hooker) view.getTag();
			}
			FingerPrintModel fingerPrintModel = fingerPrintModels.get(arg0);

			hooker.textViewName.setText(fingerPrintModel.fp_name);
			return view;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return fingerPrintModels.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return fingerPrintModels.size();
		}
	};

	@Override
	public void initView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_listfp);
        //Log.e(TAG, ">>>>>initView()");
        //Log.e(TAG, "preferenceUtils.hashCode()" + preferenceUtils.hashCode());
        //Log.e(TAG, "preferenceUtils.getBoolean(Constants.FINGER_OPEN_UNLOCK, false)" + preferenceUtils.getBoolean(Constants.FINGER_OPEN_UNLOCK, false));
        //Log.e(TAG, "preferenceUtils.getBoolean(Constants.FINGER_DOUBLE_CLICK, false)" + preferenceUtils.getBoolean(Constants.FINGER_DOUBLE_CLICK, false));
        //Log.e(TAG, ">>>>>switch 1 set up ...");
        switch1 = (Switch) findViewById(R.id.switch1);
		switch1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
                preferenceUtils.putBoolean(Constants.FINGER_OPEN_UNLOCK, switch1.isChecked());
                Log.e(TAG, "preferenceUtils.getBoolean(Constants.FINGER_OPEN_UNLOCK, false)" + preferenceUtils.getBoolean(Constants.FINGER_OPEN_UNLOCK, false));
                open(isChecked);
			}
		});
        //Log.e(TAG, "preferenceUtils.hashCode()" + preferenceUtils.hashCode());
        //Log.e(TAG, "preferenceUtils.getBoolean(Constants.FINGER_OPEN_UNLOCK, false)" + preferenceUtils.getBoolean(Constants.FINGER_OPEN_UNLOCK, false));
        //Log.e(TAG, "preferenceUtils.getBoolean(Constants.FINGER_DOUBLE_CLICK, false)" + preferenceUtils.getBoolean(Constants.FINGER_DOUBLE_CLICK, false));
        //Log.e(TAG, ">>>>>switch 2 set up ...");
        switch3 = (Switch) findViewById(R.id.switch3);
		switch3.setChecked(preferenceUtils.getBoolean(Constants.FINGER_DOUBLE_CLICK, true));
        preferenceUtils.putBoolean(Constants.FINGER_DOUBLE_CLICK, switch3.isChecked());
        doubleClick(switch3.isChecked());
        //Log.e(TAG, "preferenceUtils.hashCode()" + preferenceUtils.hashCode());
        //Log.e(TAG, "preferenceUtils.getBoolean(Constants.FINGER_OPEN_UNLOCK, false)" + preferenceUtils.getBoolean(Constants.FINGER_OPEN_UNLOCK, false));
        //Log.e(TAG, "preferenceUtils.getBoolean(Constants.FINGER_DOUBLE_CLICK, false)" + preferenceUtils.getBoolean(Constants.FINGER_DOUBLE_CLICK, false));
		switch3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
                preferenceUtils.putBoolean(Constants.FINGER_DOUBLE_CLICK, switch3.isChecked());
                Log.e(TAG, "preferenceUtils.getBoolean(Constants.FINGER_DOUBLE_CLICK, false)" + preferenceUtils.getBoolean(Constants.FINGER_DOUBLE_CLICK, false));
                doubleClick(isChecked);
			}
		});
		buttonAdd = (Button) findViewById(R.id.btn_add);
		buttonAdd.setVisibility(View.VISIBLE);
		listView = (ListView) findViewById(R.id.list_fp);
		listView.setOnItemClickListener(this);
		listView.setAdapter(baseAdapter);
        /**
         *   amo add
         *       To open or close click function by JmtFP.SetWaitTouch() and JmtFP.CancelWaitTouch()
         */
        initSensor();
	}

    private void initSensor(){
        mSensor = JmtSensor.getInstance();
        if(mSensor.CancelAction() == JmtFP.ERR_OK){
            Log.e(TAG, "CancelAction is sucessful");
        }else{
            Log.e(TAG, "CancelAction is NOT sucessful");
        }
        if(mSensor.SetWaitTouch() == JmtFP.ERR_OK){
            Log.e(TAG, "SetWaitTouch is sucessful");
        }else{
            Log.e(TAG, "SetWaitTouch is NOT sucessful");
        }
    }

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		fingerPrintModels.clear();
		fingerPrintModels.addAll(dbHelper.getFingerPrintList());
		baseAdapter.notifyDataSetChanged();
		int count = fingerPrintModels.size();
		buttonAdd.setVisibility(View.VISIBLE);
		if (count<=0) {
			switch1.setChecked(false);
			switch1.setEnabled(false);
		}
        /*
        * user
         */
        else{
            //switch1.setChecked(true);
			switch1.setEnabled(true);
			if(count>=5){
				buttonAdd.setVisibility(View.GONE);
			}
		}
        switch1.setChecked(preferenceUtils.getBoolean(
                Constants.FINGER_OPEN_UNLOCK, false));

	}

	protected void doubleClick(boolean isChecked) {
		// TODO Auto-generated method stub
		if(isChecked){
			JmtSensor.getInstance(instance).SetWaitTouch();
		}
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_add:
			startAddFingerActivity();
			break;
		case R.id.btn_about:
			StartActivity(Constants.ACTION_ABOUT_FP);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		preferenceUtils.putBoolean(Constants.FINGER_OPEN_UNLOCK, switch1.isChecked());
		preferenceUtils.putBoolean(Constants.FINGER_DOUBLE_CLICK, switch3.isChecked());
		super.onStop();
	}

	private void open(boolean isChecked) {
		// TODO Auto-generated method stub
		if (fingerPrintModels.size() <= 0) {
			Toast.makeText(instance, R.string.no_finger_about_unlock,
					Toast.LENGTH_SHORT).show();
			return;
		}
	}

	public void startAddFingerActivity() {
		StartActivity(Constants.ACTION_ADD_FP);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		preferenceUtils.putBoolean(Constants.FINGER_OPEN_UNLOCK, switch1.isChecked());
		preferenceUtils.putBoolean(Constants.FINGER_DOUBLE_CLICK, switch3.isChecked());
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Constants.ACTION_MANAGE_FP);
		Bundle bundle = new Bundle();
		bundle.putParcelable(Constants.FINGER_MODEL_KEY, fingerPrintModels.get(position));
		intent.putExtras(bundle);
		instance.startActivity(intent);
	}
}
