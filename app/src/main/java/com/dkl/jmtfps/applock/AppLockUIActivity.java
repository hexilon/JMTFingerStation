package com.dkl.jmtfps.applock;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ant.liao.GifView;
import com.dkl.jmtfps.BaseActivity;
import com.dkl.jmtfps.R;
import com.dkl.jmtfps.sensor.JmtSensor;
import com.dkl.jmtfps.sensor.JmtSensor.OnTouchListener;
import com.dkl.jmtfps.util.Constants;
import com.dkl.jmtfps.util.IConstants;
import com.dkl.jmtfps.util.Log;
import com.dkl.jmtfps.util.Util;
import com.dkl.jmtfps.view.NumberPad;
import com.dkl.jmtfps.view.ProcessIndication;
import com.dkl.jmtfps.view.NumberPad.OnNumberClickListener;

public class AppLockUIActivity extends BaseActivity implements
		OnNumberClickListener, IConstants {
	private LinearLayout fingerlayout;
	private RelativeLayout digitallayout;

	private StringBuilder mPassword = null;// 输入的密码

	private NumberPad numberPad;
	private ProcessIndication processIndication;

	private boolean MatchCancel = false;
	private boolean VERIFY_RUNNING = false;
	private Thread tid_match;
	private Handler uiUpdateHandler;
	private String packageName;

	// private LockPatternUtils lockPatternUtils;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.toDigital:
			fingerlayout.setVisibility(View.GONE);
			digitallayout.setVisibility(View.VISIBLE);
			stopMatch();
			break;

		default:
			break;
		}
	}

	@Override
	public void initView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_lock_ui);
		fingerlayout = (LinearLayout) findViewById(R.id.fingerlayout);
		digitallayout = (RelativeLayout) findViewById(R.id.digitallayout);
		((GifView) findViewById(R.id.id_lockui_fp_gi))
				.setGifImage(R.drawable.fingerprint_gif);
		numberPad = (NumberPad) findViewById(R.id.id_lockui_numberpad);
		numberPad.setOnNumberClickListener(this);
		processIndication = (ProcessIndication) findViewById(R.id.id_lockui_indication);
		mPassword = new StringBuilder();
		packageName = getIntent().getStringExtra("packageName");
		// lockPatternUtils=new LockPatternUtils(instance);
		// IntentFilter filter = new IntentFilter();
		// filter.addAction(Constants.ACTION_MATCH_APPLOCK_UI);
		// instance.registerReceiver(receiver, filter);
		uiUpdateHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case MSG_SUCCESSFUL:
					new Thread(new SaveLockAppsThread()).start();
//					preferenceUtils.putBoolean(
//							Constants.FINGER_OPEN_APP_UNLOCK_TAG, false);
//					Toast.makeText(instance, "比对成功!", Toast.LENGTH_SHORT)
//							.show();
					instance.finish();
					break;
				case MSG_FAILURE:
					Toast.makeText(instance, R.string.fingerprint_unlock_failure, Toast.LENGTH_SHORT)
							.show();
					break;

				default:
					break;
				}
			}
		};
	}

	private void startMatch() {
		// if (true) {
		// return;
		// }
		if (VERIFY_RUNNING) {
			return;
		}
		MatchCancel = false;
//		Util.unBindClickService(instance, true);
		JmtSensor.getInstance(instance).CancelAction();
		tid_match = new Thread(new MatchFingerThread());
		tid_match.setPriority(Thread.MAX_PRIORITY);
		tid_match.start();
		// Log.i("dkl", "Verify:"+result);
	}

	private class MatchFingerThread implements Runnable {
		@Override
		public void run() {
			VERIFY_RUNNING = true;
			boolean keep_going = true;
			int Module_Result;
			Message uiMessage;

			while (keep_going) {
				if (Thread.interrupted()) {
					Log.i("dkl", "MatchFingerThread interrupted!");
					break;
				}

				if (MatchCancel) {
					Log.i("dkl", "MatchFingerThread user cancel!");
					break;
				}

				Module_Result = JmtSensor.getInstance(instance).Verify(
						new OnTouchListener() {

							@Override
							public int value(int val) {
								// TODO Auto-generated method stub
								callBack(val);
								return 0;
							}
						});
				if (Module_Result >= JmtSensor.ERR_OK) {
					Log.e("dkl", "FP Verify Successfully");
					uiMessage = new Message();
					uiMessage.what = MSG_SUCCESSFUL;
					uiUpdateHandler.sendMessage(uiMessage);
					keep_going = false;
				} else
					switch (Module_Result) {

					case JmtSensor.ERR_TIMEOUT:
						/* notify user to swipe finger */
						keep_going = false;
						break;

					case JmtSensor.ERR_NOMATCH:
						Log.e("dkl", "FP Verify Fail, Try again");

						uiMessage = new Message();
						uiMessage.what = MSG_FAILURE;
						uiUpdateHandler.sendMessage(uiMessage);

						// keep_going = false;
						break;

					default:
						Log.i("dkl", "Something wrong here");
						keep_going = false;
						break;
					}
			}
			VERIFY_RUNNING = false;
		}
	}

	public void callBack(int val) {
		// TODO Auto-generated method stub
		Message uiMessage;
		switch (val) {
		case JmtSensor.NOTIFY_LEAVE_FINGER:
			uiMessage = new Message();
			uiMessage.what = MSG_NOTIFY_LEAVE_FINGER;
			uiUpdateHandler.sendMessage(uiMessage);
			break;
		case JmtSensor.TOO_WET:
			uiMessage = new Message();
			uiMessage.what = MSG_TOO_WET;
			uiUpdateHandler.sendMessage(uiMessage);
			break;
		case JmtSensor.TOO_SMALL:
			uiMessage = new Message();
			uiMessage.what = MSG_TOO_SMALL;
			uiUpdateHandler.sendMessage(uiMessage);
			break;
		case JmtSensor.ADJUST:
			Log.e("dkl", "Please adjust your finger");
			uiMessage = new Message();
			uiMessage.what = MSG_ADJUST;
			uiUpdateHandler.sendMessage(uiMessage);
			break;
		case JmtSensor.WAIT_FINGER:
			Log.e("dkl", "Wait Finger on.....");
			uiMessage = new Message();
			uiMessage.what = MSG_WAIT_FINGER;
			uiUpdateHandler.sendMessage(uiMessage);
			break;
		case JmtSensor.CHANGE_FINGER:
			Log.e("dkl", ".............105");
			uiMessage = new Message();
			uiMessage.what = MSG_CHANGE_FINGER;
			uiUpdateHandler.sendMessage(uiMessage);
			break;
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		if (dbHelper.getFingerCount() <= 0) {
			fingerlayout.setVisibility(View.GONE);
			digitallayout.setVisibility(View.VISIBLE);
		} else {
			startMatch();
		}
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		stopMatch();
		super.onStop();
	}

	private void stopMatch() {
		// TODO Auto-generated method stub
		MatchCancel = true;
		JmtSensor.getInstance(instance).CancelAction();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// instance.unregisterReceiver(receiver);
		if (tid_match.isAlive()) {
			tid_match.interrupt();
		}
//		Util.unBindClickService(instance, false);
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(Intent.ACTION_MAIN);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.addCategory(Intent.CATEGORY_HOME);
			startActivity(i);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void clickNumer(int number) {
		// TODO Auto-generated method stub
		switch (number) {
		case -1:// 删除
			if (mPassword.length() > 0) {
				mPassword.deleteCharAt(mPassword.length() - 1);
			}
			break;
		case -2:// 取消
			if (dbHelper.getFingerCount() <= 0) {
				Toast.makeText(instance, R.string.no_finger_about_unlock, Toast.LENGTH_SHORT)
						.show();
				return;
			}
			fingerlayout.setVisibility(View.VISIBLE);
			digitallayout.setVisibility(View.GONE);
			startMatch();
			break;
		default:
			String pwd = mPassword.append(number).toString();

			// if (pwd.equals(preferenceUtils.getPwd())) {
			if (pwd.equals("1234")) {// mLockPatternUtils.checkPassword(pin)
//				preferenceUtils.putBoolean(
//						Constants.FINGER_OPEN_APP_UNLOCK_TAG, false);
				new Thread(new SaveLockAppsThread()).start();
//				Toast.makeText(instance, "密码验证成功!", 0).show();
				finish();
				return;
			}
			if (pwd.length() == 4) {
				Animation shake = AnimationUtils.loadAnimation(instance,
						R.anim.shake);// 加载动画资源文件
				processIndication.startAnimation(shake); // 给组件播放动画效果
				mPassword.delete(0, mPassword.length());
				// return ;
			}
			break;
		}
		processIndication.setIndex(mPassword.length());
	}

	private class SaveLockAppsThread implements Runnable {
		@Override
		public void run() {
			if (packageName != null) {
//				StringBuilder stringBuilder = new StringBuilder();
				StringBuilder stringBuilder = new StringBuilder(preferenceUtils.getString(
						Constants.SETTINGS_UNLOCK_APP_PACKAGENAMES, ""));
				stringBuilder.append(packageName + "|");
				Log.i("dkl", "appInfoBeans.get(i).app_package:" + packageName);
				preferenceUtils.putString(
						Constants.SETTINGS_UNLOCK_APP_PACKAGENAMES,
						stringBuilder.toString());
			}

		}
	}

}
