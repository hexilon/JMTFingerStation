package com.dkl.jmtfps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dkl.jmtfps.mode.AppInfoBean;
import com.dkl.jmtfps.util.PreferenceUtils;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
/**
 * quick open app used fingerprint
 * @author kayle
 *
 */
public class AppListSelectActivity extends BaseActivity implements
		OnItemClickListener {

	String fp_id;
	ListView listOpenApp;
	private List<AppInfoBean> appInfoBeans = new ArrayList<AppInfoBean>();

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_open_list_app);
		fp_id=getIntent().getStringExtra("fp_id");
		
		Log.i("dkl", "fp_id"+fp_id);
		listOpenApp = (ListView) this.findViewById(R.id.list_app);
		listOpenApp.setOnItemClickListener(this);
		
		new Thread(new LoadApps()).start();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		AppInfoBean appInfoBean = appInfoBeans.get(position);
		PreferenceUtils.getInstance().putString(fp_id, appInfoBean.app_package);
		PreferenceUtils.getInstance().putString(appInfoBean.app_package, appInfoBean.app_activity);
		this.finish();
	}

	final class Hooker {
		LinearLayout linearLayout;
		ImageView imageView_icon;
		TextView textViewName;
	}

	BaseAdapter baseAdapter = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			Hooker hooker = null;
			if (convertView == null) {
				hooker = new Hooker();
				convertView = LayoutInflater.from(instance).inflate(
						R.layout.app_item_open_list, null);
				hooker.imageView_icon = (ImageView) convertView
						.findViewById(R.id.imageView_icon);
				hooker.textViewName = (TextView) convertView
						.findViewById(R.id.textView_appName);
				convertView.setTag(hooker);
			} else {
				hooker = (Hooker) convertView.getTag();
			}
			AppInfoBean appInfoBean = appInfoBeans.get(position);
			hooker.imageView_icon.setImageDrawable(appInfoBean.app_icon);
			hooker.textViewName.setText(appInfoBean.app_name);

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

			String[] exclueApps = getResources().getStringArray(
					R.array.exclude_app);// 需要过滤的app

			List<String> exclueAppsList = Arrays.asList(exclueApps);
			// String needApps = preferenceUtils.getString(
			// Constants.SETTINGS_NEEDLOCK_APP_PACKAGENAMES, "");
			// Log.i("dkl", "needApps:" + needApps);
			// String[] needAppArrays = needApps.split("\\|");
			// List<String> needAppList = Arrays.asList(needAppArrays);

			PackageManager packageManager = instance.getPackageManager();
			Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
			mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			List<ResolveInfo> listAppsTemp = packageManager
					.queryIntentActivities(mainIntent, 0);
			AppInfoBean appInfoBean = new AppInfoBean();
			appInfoBean.app_name = "无";
			appInfoBean.app_package = "no";
			appInfoBean.app_activity= "no";
			appInfoBeans.add(appInfoBean);
			for (ResolveInfo info : listAppsTemp) {
				if (!exclueAppsList.contains(info.activityInfo.packageName)) {
					appInfoBean = new AppInfoBean();
					appInfoBean.app_name = info.loadLabel(packageManager)
							.toString();
					appInfoBean.app_package = info.activityInfo.packageName;
					Log.i("dkl", "appInfoBean.app_package："+appInfoBean.app_package);
					appInfoBean.app_activity= info.activityInfo.name;
					Log.i("dkl", "appInfoBean.app_activity："+appInfoBean.app_activity);
					appInfoBean.app_icon = info.loadIcon(packageManager);
					appInfoBean.app_check = false;

					appInfoBeans.add(appInfoBean);
				}
			}
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					listOpenApp.setAdapter(baseAdapter);
				}
			});

		}
	}
}
