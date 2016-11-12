package com.dkl.jmtfps.applock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.ant.liao.GifView;
import com.dkl.jmtfps.BaseActivity;
import com.dkl.jmtfps.R;
import com.dkl.jmtfps.mode.AppInfoBean;
import com.dkl.jmtfps.util.Constants;
import com.dkl.jmtfps.util.Log;


public class AppLockListActivity extends BaseActivity implements
		OnItemClickListener {
	private static ListView listApp;
	private Switch switch1;
	private String string;
	private TextView textView;

	private List<AppInfoBean> appInfoBeans = new ArrayList<AppInfoBean>();
	private List<AppInfoBean> appLockInfoBeans = new ArrayList<AppInfoBean>();
	boolean OpenKey = false;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		default:
			break;
		}
	}

	final class Hooker {
		LinearLayout linearLayout;
		ImageView imageView_icon;
		TextView textViewName;
		Switch switch1;
	}

	BaseAdapter baseAdapter = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			Hooker hooker = null;
			if (convertView == null) {
				hooker = new Hooker();
				convertView = LayoutInflater.from(instance).inflate(
						R.layout.app_item_list, null);
				hooker.linearLayout = (LinearLayout) convertView
						.findViewById(R.id.LinearLayout1);
				hooker.imageView_icon = (ImageView) convertView
						.findViewById(R.id.imageView_icon);
				hooker.textViewName = (TextView) convertView
						.findViewById(R.id.textView_appName);
				hooker.switch1 = (Switch) convertView
						.findViewById(R.id.switch_item);
				convertView.setTag(hooker);
			} else {
				hooker = (Hooker) convertView.getTag();
			}
			AppInfoBean appInfoBean = appInfoBeans.get(position);
			hooker.imageView_icon.setImageDrawable(appInfoBean.app_icon);
			hooker.textViewName.setText(appInfoBean.app_name);
			hooker.switch1.setChecked(appInfoBean.app_check);
			hooker.switch1.setEnabled(OpenKey);
			hooker.linearLayout.setEnabled(OpenKey);
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return appInfoBeans.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return appInfoBeans.size();
		}
	};

	public void initView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_list_app);
		((GifView) findViewById(R.id.list_app_gi))
				.setGifImage(R.drawable.fingerprintloading);
		listApp = (ListView) findViewById(R.id.list_app);
		textView = (TextView) findViewById(R.id.textView);
		switch1 = (Switch) findViewById(R.id.switch1);

		listApp.setOnItemClickListener(this);

		switch1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				open(isChecked);
			}
		});
		OpenKey = preferenceUtils.getBoolean(Constants.FINGER_OPEN_APP_UNLOCK,
				false);
		switch1.setChecked(OpenKey);

		string = getResources().getString(R.string.app_use_lock);

		textView.setText(String.format(string, 0));
		textView.setVisibility(OpenKey ? View.VISIBLE : View.GONE);
		new Thread(new LoadApps()).start();
	}

	private void open(boolean isChecked) {
		// TODO Auto-generated method stub
		OpenKey = isChecked;
		switch1.setChecked(OpenKey);
		baseAdapter.notifyDataSetChanged();
		if (listApp != null)
			listApp.setClickable(OpenKey);
		if (textView != null)
			textView.setVisibility(OpenKey ? View.VISIBLE : View.GONE);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		preferenceUtils.putBoolean(Constants.FINGER_OPEN_APP_UNLOCK, OpenKey);
		new Thread(new SaveLockAppsThread()).start();
		super.onStop();
	}

	private class SaveLockAppsThread implements Runnable {
		@Override
		public void run() {
			if (appInfoBeans != null) {
				// Settings.System.putString(instance.getContentResolver(),
				// Constants.SETTINGS_NEEDLOCK_APP_PACKAGENAMES, null);
				preferenceUtils.putString(
						Constants.SETTINGS_NEEDLOCK_APP_PACKAGENAMES, "");
				StringBuilder stringBuilder = new StringBuilder();
				for (int i = 0; i < appInfoBeans.size(); i++) {
					if (appInfoBeans.get(i).app_check) {
						stringBuilder.append(appInfoBeans.get(i).app_package
								+ "|");
						Log.i("dkl", "appInfoBeans.get(i).app_package:" + appInfoBeans.get(i).app_package);
						// Settings.System.putString(
						// instance.getContentResolver(),
						// Constants.SETTINGS_NEEDLOCK_APP_PACKAGENAMES,
						// stringBuilder.toString());
						preferenceUtils.putString(
								Constants.SETTINGS_NEEDLOCK_APP_PACKAGENAMES,
								stringBuilder.toString());
					}
				}
			}

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (!OpenKey) {
			return;
		}
		Switch switch_item = (Switch) view.findViewById(R.id.switch_item);
		AppInfoBean appInfoBean = appInfoBeans.get(position);
		appInfoBean.app_check = !appInfoBean.app_check;
		if (appInfoBean.app_check) {
			appLockInfoBeans.add(appInfoBean);
		} else {
			appLockInfoBeans.remove(appInfoBean);
		}
		switch_item.setChecked(appInfoBean.app_check);
		textView.setText(String.format(string, appLockInfoBeans.size()));
	}

	/**
	 * 异步加载应用程序列表数据
	 * 
	 * @author 丁凯林
	 * 
	 */
	class LoadApps implements Runnable {
		@Override
		public void run() {
			// String needApps = Settings.System.getString(getContentResolver(),
			// Constants.SETTINGS_NEEDLOCK_APP_PACKAGENAMES);

			String[] needLockApps = getResources().getStringArray(
					R.array.needlock_systemapp);// 需要加锁的app
			String[] exclueApps = getResources().getStringArray(
					R.array.exclude_app);// 需要过滤的app

			List<String> needLockAppsList = Arrays.asList(needLockApps);
			List<String> exclueAppsList = Arrays.asList(exclueApps);
			String needApps = preferenceUtils.getString(
					Constants.SETTINGS_NEEDLOCK_APP_PACKAGENAMES, "");
			// Log.i("dkl", "needApps:" + needApps);
			String[] needAppArrays = needApps.split("\\|");
			List<String> needAppList = Arrays.asList(needAppArrays);

			PackageManager packageManager = instance.getPackageManager();
			Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
			mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			List<ResolveInfo> listAppsTemp = packageManager
					.queryIntentActivities(mainIntent, 0);
			for (ResolveInfo info : listAppsTemp) {
				if (!exclueAppsList.contains(info.activityInfo.packageName)) {
					AppInfoBean appInfoBean = new AppInfoBean();
					appInfoBean.app_name = info.loadLabel(packageManager)
							.toString();
					appInfoBean.app_package = info.activityInfo.packageName;
					appInfoBean.app_icon = info.loadIcon(packageManager);
					appInfoBean.app_check = false;
					if (needLockAppsList
							.contains(info.activityInfo.packageName)
							|| needAppList
									.contains(info.activityInfo.packageName)) {
						appInfoBean.app_check = true;
						appLockInfoBeans.add(appInfoBean);
					}
					appInfoBeans.add(appInfoBean);
				}
			}
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					listApp.setAdapter(baseAdapter);
					textView.setText(String.format(string,
							appLockInfoBeans.size()));
				}
			});

		}
	}
}
