package com.dkl.jmtfps;

import com.dkl.jmtfps.mode.FingerPrintModel;
import com.dkl.jmtfps.sensor.JmtSensor;
import com.dkl.jmtfps.sensor.JmtSensor.OnEnrollProgressListener;
import com.dkl.jmtfps.util.Constants;
import com.dkl.jmtfps.util.Log;
import com.dkl.jmtfps.util.Util;
import com.dkl.jmtfps.view.FingerProcess;
import com.dkl.jmtfps.view.IndexOfFingerProcess;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @category 添加指纹
 * @author dkl
 * 
 */
public class AddFingerPrintActivity extends BaseActivity {
	public static final String TAG = "AddFingerPrintActivity";
	private Button mCancel = null;
	private LinearLayout layoutEnd = null;
	private FingerProcess mFingerProcessView = null;
	private IndexOfFingerProcess mIndexOfFingerProcess = null;
	private ImageView mGuideImageView = null;
	private TextView mGuideTitle = null;
	private TextView mGuideContent = null;
	private TextView mGuideContentNext = null;

	private JmtSensor mSensor;
	private static String NEW_FP_NAME = "新指纹";

	private boolean cancel = false;
	private Handler handler;
	private Thread threadEnroll;
	private AnimationDrawable anim;
	
//	public static boolean ENROLL_ACTION_TAG=false;
	
	/**
	 * 声音池
	 */
	private SoundPool soundPool;
	/**
	 * 成功的声音
	 */
	private int currStreamId1;
	/**
	 * 完成的声音
	 */
	private int currStreamId3;
	/**
	 * 失败的声音
	 */
	private int currStreamId2;
	private Vibrator vibrator = null;

	@Override
	public void initView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_addfp);
		mFingerProcessView = (FingerProcess) findViewById(R.id.id_fp_process);
		mIndexOfFingerProcess = (IndexOfFingerProcess) findViewById(R.id.id_fp_index_process);
		mCancel = (Button) findViewById(R.id.id_btn_stop);
		layoutEnd = (LinearLayout) findViewById(R.id.id_ll_setup_end);
		mGuideTitle = (TextView) findViewById(R.id.id_add_guide_title);
		mGuideContent = (TextView) findViewById(R.id.id_add_guide_content);
		mGuideContentNext = (TextView) findViewById(R.id.id_add_guide_content_next);
		mGuideImageView = (ImageView) findViewById(R.id.id_add_guide);
		//mGuideImageView.setBackgroundResource(R.drawable.guide_anim);
		//anim = (AnimationDrawable) mGuideImageView.getBackground();
		//anim.stop();
		//anim.start();
		mGuideImageView.setVisibility(View.GONE);// 按压式可用
		setCurrentStep(0);
		mFingerProcessView.setVisibility(View.VISIBLE);
		mIndexOfFingerProcess.setVisibility(View.VISIBLE);
		mGuideContentNext.setText(R.string.record_fp_content_remove);
		Util.showGuideContentAnim(mGuideContent, mGuideContentNext);

		soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 5);
		currStreamId1 = soundPool.load(this, R.raw.success, 1);
		currStreamId3 = soundPool.load(this, R.raw.finalok, 1);
		currStreamId2 = soundPool.load(this, R.raw.fail, 1);
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

		initSensor();

		handler = new Handler() {
			@SuppressLint("ResourceAsColor")
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case 0:
					mGuideTitle.setText(R.string.fingerprint_openfail);
					//+robert
					//mGuideTitle.setTextColor(R.color.red);
					//Toast.makeText(instance, "指纹无权限！打开失败！", 1).show();
					break;
				case 1:
					break;
				case 2:
					/**
					 *	amo modify to merge +robert
					 */
					//setCurrentStep(msg.arg1 - 1);
					setCurrentStep(msg.arg1);
					Log.i("dkl", "msg.arg1:" + msg.arg1);
					if (msg.arg1 == FingerPrintModel.FP_DATA_ITEM) {
						fingerprintFinish();
						soundPool.play(currStreamId3, 1.0F, 1.0F, 0, 0, 1.0F);
						/**
						 *	amo add to merge +robert
						 */
						vibrator.vibrate(new long[] { 100, 300 }, -1);
					} else {
						soundPool.play(currStreamId1, 1.0F, 1.0F, 0, 0, 1.0F);
						/**
						 *	amo add to merge +robert
						 */
						vibrator.vibrate(new long[] { 100, 300 }, -1);
						mFingerProcessView.setVisibility(View.VISIBLE);
						mIndexOfFingerProcess.setVisibility(View.VISIBLE);
						mGuideContentNext.setText(R.string.record_fp_content_remove);
						Util.showGuideContentAnim(mGuideContent, mGuideContentNext);
					}
					break;
				case 3:
					soundPool.play(currStreamId2, 1.0F, 1.0F, 0, 0, 1.0F);
					// +robert
					Toast.makeText(instance, "录入失败,错误：" + msg.arg1, Toast.LENGTH_SHORT).show();
					break;
				case 4:
					// Toast.makeText(instance, "放置手指", 0).show();
					// Util.showGuideContentAnim(mGuideContent,
					// mGuideContentNext);
					vibrator.vibrate(new long[] { 100, 300 }, -1);
					mGuideContentNext.setText(R.string.record_fp_content_remove);
					Util.showGuideContentAnim(mGuideContentNext, mGuideContent);
					break;
				/**
				 *	amo add to merge +robert
				 */
				case 5:
					// Toast.makeText(instance, "松开手指", 0).show();
					break;
					//+ robert
					case JmtSensor._ACQUIRED_DUPLICATE_FINGER:
						vibrator.vibrate(new long[] { 100, 300 }, -1);
						mGuideContentNext.setText(R.string.record_fp_duplicate);
						Util.showGuideContentAnim(mGuideContentNext,
								mGuideContent);
						break;
				default:
					break;
				}
			}
		};

	}

	private void initSensor() {
		mSensor = JmtSensor.getInstance(this);
		int result = mSensor.CancelWaitTouch();
		Log.i(TAG, "CancelWaitTouch:"+result);
		result = JmtSensor.getInstance(instance).CancelAction();
		Log.i(TAG, "CancelWaitTouch:"+result);
		mSensor.SetEnrollCounts(10);
		Constants.ENROLL_ACTION_TAG = true;
	}

	/**
	 *	amo close this code block to merge +robert
	 *		save() is deprecated and its jobs is moved to updateDB()
	 */
	/*
	private void save() {
		dbHelper.insertNewFpData(fingerPrintModel);
		instance.sendBroadcast(new Intent(Constants.REFRESH_FP_DATA));
		preferenceUtils.putBoolean(Constants.FINGER_OPEN_UNLOCK, true);
	}
	*/

	/**
	 *	amo note
	 *		initMode() is deprecated and its jobs is moved to updateDB().
	 */
	/*
	private void initMode(String fp_id) {
		fingerPrintModel = new FingerPrintModel();
		int index = dbHelper.getNewFpNameIndex();
		fingerPrintModel.fp_id = index;
		fingerPrintModel.fp_name = NEW_FP_NAME + index;
		fingerPrintModel.fp_data_index = fp_id;
	}
	*/

	/**
	 *	amo add to merge +robert
	 *		save() and initMode() are deprecated and their jobs is moved to updateDB() here.
	 */
	//+robert
	private void updateDB(String fp_id) {
		fingerPrintModel = new FingerPrintModel();
		int index = dbHelper.getNewFpNameIndex();
		fingerPrintModel.fp_id = index;
		fingerPrintModel.fp_name = NEW_FP_NAME + index;
		fingerPrintModel.fp_data_index = fp_id;
		//+robert
		dbHelper.insertNewFpData(fingerPrintModel);
		instance.sendBroadcast(new Intent(Constants.REFRESH_FP_DATA));
	}

	private void fingerprintFinish() {
		// TODO Auto-generated method stub
		cancel = true;
		layoutEnd.setVisibility(View.VISIBLE);
		mCancel.setVisibility(View.GONE);
		mGuideTitle.setText(R.string.finish);
		mGuideContentNext.setText(R.string.record_successfully);
		/**
		 *	amo add to merge +robert
		 */
		// + robert
		Util.showGuideContentAnim(mGuideContentNext, mGuideContent);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		// 开启录入线程
		threadEnroll = new Thread(new enrollFingerThread());
		threadEnroll.start();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		cancel();
		int result = mSensor.SetWaitTouch();
		Log.i(TAG, "SetWaitTouch:"+result);
		
		super.onStop();
		this.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		int result = mSensor.SetWaitTouch();
		Log.i(TAG, "SetWaitTouch:"+result);
		Constants.ENROLL_ACTION_TAG = false;
		//Util.unBindClickService(instance, false);
	}

	void cancel() {
		//ENROLL_ACTION_TAG=false;
		cancel = true;
		int result = mSensor.CancelAction();
		Log.i(TAG, "CancelAction:"+result);
		if (threadEnroll != null) {
			threadEnroll.interrupt();
		}
	}

	boolean touchLeave = false;

	class enrollFingerThread implements Runnable {
		// @Override
		public void run() {
			//Util.unBindClickService(instance, true);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//int count = 0;
			int Module_Result = 0;
			while (!cancel) {
				if (Thread.interrupted()) {
					//Log.e(TAG, "EnrollPieceThread interrupted!");
					break;
				}

				if (cancel) {
					Log.i(TAG, "User cancel");
					return;
				}
				//Log.i(TAG, "mSensor.Enrollment");

				touchLeave = false;
				Module_Result = mSensor.Enroll(new OnEnrollProgressListener() {
					@Override
					public int onEnrollProgress(int gid, int fid, int percentage) {
						// TODO Auto-generated method stub
						Log.i("dkl", "percentage:" + percentage);

						Message message = Message.obtain();
						message.what = 2;
						message.arg1 = percentage / 10;
						handler.sendMessage(message);
						if (percentage == 100) {
							//initMode(gid + "");
							/**
							 * amo note
							 *		gid is corresponding to FingerPrintModel.fp_data_index.
							 */
							// +robert
							updateDB(Integer.toString(gid));
							cancel = true;
						}
						touchLeave = false;
						return 0;
					}
					/**
					 *	amo modify to merge +robert
					 *		value(val) is a JmtFP.onAdvice() implement called by SDK through JNI.
					 */
					@Override
					public int value(int val) {
						// TODO Auto-generated method stub
						/**
						 *	amo close this code block to merge +robert
						 */
						/*
						if (val == JmtSensor.NOTIFY_LEAVE_FINGER) {
							if (!touchLeave) {
								touchLeave = true;
								handler.sendEmptyMessage(4);
								//Log.i(TAG, "val:" + val);
							}
						}
						*/
						/**
						 *	amo add to merge +robert
						 */
						// +robert
						switch (val) {
							case JmtSensor._ACQUIRED_GOOD:
								Log.i(TAG, "JmtSensor._ACQUIRED_GOOD...");
								break;
							case JmtSensor._ACQUIRED_PARTIAL:
								Log.i(TAG, "JmtSensor._ACQUIRED_PARTIAL...");
								break;
							case JmtSensor._ACQUIRED_INSUFFICIENT:
								Log.i(TAG, "JmtSensor._ACQUIRED_INSUFFICIENT...");
								break;
							case JmtSensor._ACQUIRED_IMAGER_DIRTY:
								Log.i(TAG, "JmtSensor._ACQUIRED_IMAGER_DIRTY...");
								break;
							case JmtSensor._ACQUIRED_TOO_SLOW:
								Log.i(TAG, "JmtSensor._ACQUIRED_TOO_SLOW...");
								break;
							case JmtSensor._ACQUIRED_TOO_FAST:
								Log.i(TAG, "JmtSensor._ACQUIRED_TOO_FAST...");
								break;
							case JmtSensor._ACQUIRED_VENDOR_BASE:
								Log.i(TAG, "JmtSensor._ACQUIRED_VENDOR_BASE...");
								break;
							case JmtSensor._ACQUIRED_VENDOR_BASE_ALI:
								Log.i(TAG, "JmtSensor._ACQUIRED_VENDOR_BASE_ALI...");
								break;
							case JmtSensor._ACQUIRED_WAIT_FINGER_INPUT:
								Log.i(TAG, "JmtSensor._ACQUIRED_WAIT_FINGER_INPUT...");
								break;
							case JmtSensor._ACQUIRED_FINGER_DOWN:
								Log.i(TAG, "JmtSensor._ACQUIRED_FINGER_DOWN...");
								break;
							case JmtSensor._ACQUIRED_FINGER_UP:
								Log.i(TAG, "JmtSensor._ACQUIRED_FINGER_UP...");
								break;
							case JmtSensor._ACQUIRED_INPUT_TOO_LONG:
								Log.i(TAG, "JmtSensor._ACQUIRED_INPUT_TOO_LONG...");
								break;
							case JmtSensor._ACQUIRED_DUPLICATE_FINGER:
								Log.i(TAG, "JmtSensor._ACQUIRED_DUPLICATE_FINGER...");
								Message message = Message.obtain();
								message.what = JmtSensor._ACQUIRED_DUPLICATE_FINGER;
								handler.sendMessage(message);
								break;
							case JmtSensor._ACQUIRED_DUPLICATE_AREA:
								Log.i(TAG, "JmtSensor._ACQUIRED_DUPLICATE_AREA...");
								break;
							case JmtSensor._ACQUIRED_LOW_COVER:
								Log.i(TAG, "JmtSensor._ACQUIRED_LOW_COVER...");
								break;
							case JmtSensor._ACQUIRED_BAD_IMAGE:
								Log.i(TAG, "JmtSensor._ACQUIRED_BAD_IMAGE...");
								break;
							default:
								break;
						}
						return 0;
					}
				});

				//Log.i(TAG, "Module_Result：" + Module_Result);
				if (cancel) {
					return;
				}
				if (Module_Result != -2) {
					if (Module_Result != JmtSensor.ERR_OK) {
						cancel=true;
						handler.sendEmptyMessage(3);
					} else {
						//count++;
					}
				}
			}
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.id_btn_stop:
			// 清楚之前录入数据
			cancel = true;
			cancel();
			instance.finish();// 提前终止
			break;
		case R.id.id_btn_rename:
			rename();// 重命名
			break;
		case R.id.id_btn_finish:
			// 保存数据，刷新退出
			/**
			 *	amo modify to merge +robert
			 *		It seems like this btn_finish is nothing used but to finish activity now
			 *  	, the new fingerPrintModel has been created and SQLite database has been updated
			 *  	, whenever the enroll progress is completed to 100%.
			 */
			//save();
			instance.finish();// 完成
			break;
		case R.id.id_btn_test:
			test();
			break;
		default:
			break;
		}
	}

	private void rename() {
		// TODO Auto-generated method stub
		showDialog();
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
						}
					}
				});
		builder.setNegativeButton(R.string.cancel, null);
		builder.show();
	}

	public void setCurrentStep(int step) {
		mFingerProcessView.mCurrentStep = 0;
		mFingerProcessView.twikcleImg(this, step);
		mIndexOfFingerProcess.mCurrentStep = 0;
		mIndexOfFingerProcess.twikcleImg(this, step);
	}

	int i = 0;
	private void test() {
		// TODO Auto-generated method stub
		Message message = new Message();
		message.what = 2;
		message.arg1 = ++i;
		handler.sendMessage(message);
	}
}
