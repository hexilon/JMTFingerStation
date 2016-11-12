package com.dkl.jmtfps;

import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ScrollView;
import android.widget.HorizontalScrollView;

import com.dkl.jmtfps.sensor.JmtSensor;
import com.dkl.jmtfps.sensor.JmtSensor.OnShowImageListener;
import com.jmt.fps.JmtFP;
import com.jmt.fps.JmtFP.jmtcallback;

import java.nio.ByteBuffer;

public class TestActivity extends Activity implements SurfaceHolder.Callback,
		jmtcallback {

	private static final String TAG = "JMTDemoAPP";

	private static String Ver;

	private JmtSensor mSensor;

	private Activity instance;

	private boolean user_cancel;

	private int iFpImageWidth;
	private int iFpImageHeight;
	private int iFpSize;
	private int iFpChipID;

	private byte[] mRGBFinger;

	private TextView tvSDK;
	private TextView tvChipID;
	private TextView tvPercentage;
	private TextView tvMessage;
	private TextView tvScroll;

	private ScrollView svScroll;
	private HorizontalScrollView hsvScroll;

	private SurfaceView mSurfaceView;
	private static SurfaceHolder mHolder;

	private static Toast mToast;

	private static boolean bmpReady;
	private static BitmapPool mBitmap_Finger;
	private static Paint mPaint;
	private static Canvas mCanvas;
	private static Matrix mMatrix;

	private Thread tid_draw;
	private HandlerThread tid_image;
	private Thread tid_stress;

	private Handler hid_image;

	private Runnable rid_image;

	private Handler uiUpdateHandler;

	private boolean onStress;

	private boolean onGetFinger;

	// private Vibrator mVibrator;

	static final int BITMAP_POOL_SIZE = 5;

	static final int MSG_NATIVE_CALLBACK = 1;
	static final int MSG_THREAD_TOGGLE_BTN = 2;
	static final int MSG_THREAD_GET_FIG = 3;
	static final int MSG_THREAD_ENROLL_FIG = 4;
	static final int MSG_THREAD_ENROLL_OK = 5;
	static final int MSG_THREAD_VERIFY_FIG = 6;
	static final int MSG_THREAD_VERIFY_Fail = 7;
	static final int MSG_THREAD_OUTOFMEMORY = 10;
	static final int MSG_NO_SENSOR = 11;
	static final int MSG_THREAD_ERASE_FULL = 12;
	static final int MSG_POOR_IMG = 13;
	static final int MSG_THREAD_GET_FIG_FAIL = 14;
	static final int MSG_NOTIFY_LEAVE_FINGER = 15;
	static final int MSG_ADJUST = 16;
	static final int MSG_WAIT_FINGER = 17;
	static final int MSG_CHANGE_FINGER = 18;

	static final int MSG_CLICK_SENSOR = 20;

	private int wait_finger = 0;

	public int onClick(int event) {
		return 0;
	}

	public int onNavigation(int result, int ver, int hor) {
		return 0;
	}

	public int onEnrollProgress(String finger_id, int percentage) {
		return 0;
	}

	public int onEnrollProgress2(int gid, int fid, int percentage) {
		return 0;
	}

	public int onAdvice(int event) {
		Message uiMessage;
		switch (event) {
		case JmtFP.NOTIFY_LEAVE_FINGER:
			Log.e(TAG, "Leave your finger.....");
			uiMessage = new Message();
			uiMessage.what = MSG_NOTIFY_LEAVE_FINGER;
			uiUpdateHandler.sendMessage(uiMessage);
			break;
		case JmtFP.TOO_WET:
			/*
			 * uiMessage = new Message(); uiMessage.what = JmtFP.TOO_WET;
			 * Log.e(TAG, "Please adjust your finger");
			 * tvMessage.setText("Your finger is too wet !!");
			 */
			break;
		case JmtFP.TOO_SMALL:
			/*
			 * uiMessage = new Message(); uiMessage.what = JmtFP.TOO_SMALL;
			 * tvMessage.setText("Pressed area is too small !!");
			 */
			break;
		case JmtFP.ADJUST:
			Log.e(TAG, "Please adjust your finger");
			wait_finger = 0;
			uiMessage = new Message();
			uiMessage.what = MSG_ADJUST;
			uiUpdateHandler.sendMessage(uiMessage);
			break;
		case JmtFP.WAIT_FINGER:
			// Log.e(TAG, "Wait Finger on.....");
			wait_finger++;
			uiMessage = new Message();
			uiMessage.what = MSG_WAIT_FINGER;
			uiMessage.arg1 = wait_finger;
			uiUpdateHandler.sendMessage(uiMessage);
			break;
		case JmtFP.CHANGE_FINGER:
			Log.e(TAG, "[105] Change Finger....");
			wait_finger = 0;
			uiMessage = new Message();
			uiMessage.what = MSG_CHANGE_FINGER;
			uiUpdateHandler.sendMessage(uiMessage);
			break;
		default:
			break;
		}
		// uiUpdateHandler.sendMessage(uiMessage);
		return 0;
	}

	public int onShowImage(byte[] pfpimage, int ifpwidth, int ifpheight) {
		int RawIdx;
		int RgbIdx;
		int Pixel;
		byte[] pRGBFinger;

		iFpImageWidth = ifpwidth;
		iFpImageHeight = ifpheight;

		pRGBFinger = mRGBFinger;
		RgbIdx = 0;

		for (RawIdx = 0; RawIdx < ifpwidth * ifpheight; RawIdx++) {
			Pixel = (pfpimage[RawIdx] & 0xFF);
			// 8 bits to RGB8888
			pRGBFinger[RgbIdx++] = (byte) Pixel;
			pRGBFinger[RgbIdx++] = (byte) Pixel;
			pRGBFinger[RgbIdx++] = (byte) Pixel;
			pRGBFinger[RgbIdx++] = (byte) 0xFF;
		}

		if (uiUpdateHandler != null) {
			Message m = uiUpdateHandler
					.obtainMessage(MSG_NATIVE_CALLBACK, 0, 0);
			uiUpdateHandler.sendMessage(m);
		}

		return 0;
	}

	public int onShowData(byte[] pfpimage, int ifpwidth, int ifpheight) {
		// TODO Auto-generated method stub
		int RawIdx;
		int RgbIdx;
		int Pixel;
		byte[] pRGBFinger;

		iFpImageWidth = ifpwidth;
		iFpImageHeight = ifpheight;

		pRGBFinger = mRGBFinger;
		RgbIdx = 0;

		for (RawIdx = 0; RawIdx < ifpwidth * ifpheight; RawIdx++) {
			Pixel = (pfpimage[RawIdx] & 0xFF);
			// 8 bits to RGB8888
			pRGBFinger[RgbIdx++] = (byte) Pixel;
			pRGBFinger[RgbIdx++] = (byte) Pixel;
			pRGBFinger[RgbIdx++] = (byte) Pixel;
			pRGBFinger[RgbIdx++] = (byte) 0xFF;
		}

		if (uiUpdateHandler != null) {
			Message m = uiUpdateHandler
					.obtainMessage(MSG_NATIVE_CALLBACK, 0, 0);
			uiUpdateHandler.sendMessage(m);
		}
		return 0;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		instance = this;
		PackageManager m = getPackageManager();
		String s = getPackageName();
		try {
			PackageInfo p = m.getPackageInfo(s, 0);
			s = p.applicationInfo.dataDir;
		} catch (PackageManager.NameNotFoundException e) {
			Log.e(TAG, "Error Package name not found");
		}
		Log.e(TAG, "Package name = " + s);

		mSensor = JmtSensor.getInstance();
		mSensor.SetCallback(this);
		// mSensor.SetJmtSingleton(this, s, "/dev/jmt101");

		int Module_Result;
		int width[] = new int[1];
		int height[] = new int[1];
		int chipid[] = new int[1];
		int type = mSensor.GetSensorType();
		Log.e(TAG, "SensorType = " + Integer.toString(type));

		if (type == JmtFP._10X_)
			mSensor.SetEnrollCounts(6);
		else if (type == JmtFP._30X_)
			mSensor.SetEnrollCounts(6);
		else if (type == JmtFP._303_)
			mSensor.SetEnrollCounts(10);
		else
			mSensor.SetEnrollCounts(10);

		mSensor.GetFingerAttr(type, width, height);

		iFpImageHeight = height[0];
		iFpImageWidth = width[0];
		iFpSize = iFpImageHeight * iFpImageWidth;

		Ver = mSensor.GetSDKVersion();
		user_cancel = false;

		mRGBFinger = new byte[iFpSize * 4];
		Log.e(TAG, "FINGER_SIZE = " + Integer.toString(iFpSize));

		mSurfaceView = (SurfaceView) this.findViewById(R.id.surfaceView1);
		mHolder = mSurfaceView.getHolder();
		mHolder.addCallback(this);

		mPaint = new Paint();
		mPaint.setAntiAlias(true);

		mMatrix = new Matrix();
		mMatrix.setScale(6, 6);

		tvSDK = (TextView) findViewById(R.id.tvSDK);
		tvSDK.setText("SDK Version : " + Ver);

		try {
			Module_Result = mSensor.ReadChipID(chipid);
		} catch (Exception e) {
			Log.i(TAG, "ReadChipID is error");
			Module_Result = -1;
		}
		iFpChipID = chipid[0];
		tvChipID = (TextView) findViewById(R.id.tv_chipid);
		if (Module_Result != JmtFP.ERR_FAILED) {
			tvChipID.setText("Chip ID : " + Integer.toHexString(iFpChipID));
		} else {
			tvChipID.setText("Chip ID : ERROR ID !!");
		}

		tvPercentage = (TextView) findViewById(R.id.tvPercentage);
		tvPercentage.setText("");

		tvMessage = (TextView) findViewById(R.id.tvMessage);
		tvMessage.setText("");

		bmpReady = false;
		mBitmap_Finger = new BitmapPool(BITMAP_POOL_SIZE);

		uiUpdateHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Bitmap bitmap;

				switch (msg.what) {
				case MSG_NO_SENSOR:
					Log.i("Error", "No fingerprint reader !!");
					break;

				case MSG_THREAD_TOGGLE_BTN:
					/* clean up DrawThread */
					user_cancel = true;
					if (tid_draw != null) {
						if (tid_draw.isAlive()) {
							tid_draw.interrupt();
						}

						try {
							tid_draw.join(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					user_cancel = false;
					break;

				case MSG_NATIVE_CALLBACK:
					/* prepare picture data */
					Log.e(TAG,
							">>>>>>>>>>>>> MSG_NATIVE_CALLBACK..."
									+ Integer.toString(msg.arg1));
					if (iFpImageWidth > 0 && iFpImageHeight > 0) {
						// Log.e(TAG,
						// ">>>>> MSG >>>>>>>>>> width >0, height>0");
						Log.e(TAG, "Width : " + iFpImageWidth);
						Log.e(TAG, "Height : " + iFpImageHeight);
						bitmap = mBitmap_Finger.getfirstBitmap();
						if (bitmap == null) {
							bitmap = Bitmap.createBitmap(iFpImageWidth,
									iFpImageHeight, Bitmap.Config.ARGB_8888);
						}

						bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(mRGBFinger));
						mBitmap_Finger.putlastBitmap(bitmap);
						bmpReady = true;
					}
					// tvMessage.setText ("Sensor advice " + msg.arg1);
					break;

				case MSG_THREAD_GET_FIG:
					tvMessage.setText("Get fingerprint image OK !!");
					// mVibrator.vibrate(new long[] { 100, 300 }, -1);
					break;

				case MSG_POOR_IMG:
					tvMessage.setText("Get fingerprint is poor !!");
					break;

				case MSG_NOTIFY_LEAVE_FINGER:
					tvMessage.setText("Please leave your finger !!");
					break;

				case MSG_ADJUST:
					tvMessage
							.setText("Get finger failed. Please adjust your finger !! ");
					break;

				case MSG_WAIT_FINGER:
					if (onGetFinger) {
						tvMessage
								.setText("Press finger on sensor to get fingerprint !!");
					}
					break;
				case MSG_CHANGE_FINGER:
					tvMessage
							.setText("Match fail, please adjust your finger !!");
					break;

				case JmtFP.TOO_SMALL:
				case JmtFP.TOO_WET:
					tvMessage.setText("Please adjust your finger !!");
					break;
				case MSG_THREAD_VERIFY_FIG:
					tvMessage.setText("Fingerprint verify OK !!");
					break;

				case MSG_THREAD_VERIFY_Fail:
					tvMessage.setText("Fingerprint verify fail, try again !!");
					break;

				default:
					break;
				}
				super.handleMessage(msg);
			}
		};

	}

	@Override
	protected synchronized void onPause() {
		user_cancel = true;
		if (mSensor != null) {
			// mSensor.CancelAction();
		}
		super.onPause();
	}

	@Override
	protected synchronized void onResume() {
		user_cancel = false;

		super.onResume();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		onStress();
	}

	public void onStress() {
		mCanvas = mHolder.lockCanvas();
		mCanvas.drawColor(Color.WHITE);
		mHolder.unlockCanvasAndPost(mCanvas);

		tvPercentage.setText("");

		onStress = true;
		user_cancel = false;

		tvMessage.setText("Place finger on sensor !!");

		bmpReady = false;
		tid_draw = new Thread(new DrawThread());
		tid_draw.setPriority(Thread.MAX_PRIORITY);
		tid_draw.start();

		tid_stress = new Thread(new MatchFingerThread());
		tid_stress.setPriority(Thread.MAX_PRIORITY);
		tid_stress.start();
	}

	private class GetFingerStressTask implements Runnable {
		@Override
		public void run() {

			boolean keep_going = true;
			int Module_Result;
			Message uiMessage;
			int count = 0;
			Log.i(TAG,
					"GetFingerStressTask START *****************************!");

			while (keep_going) {
				if (Thread.interrupted()) {
					Log.i(TAG, "GetFingerStressTask interrupted!");
					uiUpdateHandler.sendEmptyMessage(MSG_THREAD_GET_FIG);
					break;
				}

				if (user_cancel) {
					Log.i(TAG, "GetFingerStressTask user cancel!");
					break;
				}

				count++;
				// Module_Result=mSensor.getOneFinger(new OnShowImageListener()
				// {
				//
				// @Override
				// public int onShowImage(byte[] arg0, int arg1, int arg2) {
				// // TODO Auto-generated method stub
				// return onShowImaged(arg0, arg1, arg2);
				// }
				// });
				Module_Result = mSensor.GetOneFinger();
				Log.i(TAG, "Module_Result:" + Module_Result);
				switch (Module_Result) {
				case JmtFP.ERR_OK:
					// Log.i (TAG, "Get FP Image OK");
					uiUpdateHandler.sendEmptyMessage(MSG_THREAD_GET_FIG);
					break;

				case JmtFP.ERR_ABORT:
					Log.i(TAG, "GetFingerStressTask User Cancel");
					keep_going = false;
					break;

				default:
					Log.i(TAG, "Something wrong here");
					keep_going = false;
					break;
				}
			}
			uiMessage = new Message();
			uiMessage.what = MSG_THREAD_TOGGLE_BTN;
			uiUpdateHandler.sendMessage(uiMessage);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private class MatchFingerThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.e(TAG, "Begin VerifyFingerprint !!");
			boolean keep_going = true;
			JmtSensor.getInstance(instance).CancelAction();

			while (keep_going) {
				int result = mSensor.Verify();
				if (result >= 0) {
					keep_going = false;
					uiUpdateHandler.sendEmptyMessage(MSG_THREAD_VERIFY_FIG);
				} else {
					uiUpdateHandler.sendEmptyMessage(MSG_THREAD_VERIFY_Fail);
				}
			}
		}

	}

	private class DrawThread implements Runnable {
		@Override
		public void run() {
			Bitmap bitmap;

			while (true) {
				if (Thread.interrupted()) {
					Log.i(TAG, "DrawThread interrupted!");
					break;
				}

				if (user_cancel) {
					Log.i(TAG, "DrawThread user cancel!");
					break;
				}

				if (bmpReady) {
					Log.i(TAG, "DrawThread one finger!");
					Rect mRect_Frame = new Rect(5, 10, iFpImageWidth * 3,
							iFpImageHeight * 3);

					mCanvas = mHolder.lockCanvas(mRect_Frame);
					bitmap = mBitmap_Finger.getlastBitmap();

					mCanvas.drawColor(Color.WHITE);
					// mCanvas.drawBitmap(bitmap, 64, 64, mPaint);
					mCanvas.drawBitmap(bitmap, new Rect(0, 0, iFpImageWidth,
							iFpImageHeight - 1), mRect_Frame, mPaint);
					mBitmap_Finger.putfirstBitmap(bitmap);

					mHolder.unlockCanvasAndPost(mCanvas);

					bmpReady = false;
				}
			}
		}
	}

	public void onBackPressed() {
		user_cancel = true;
		System.exit(0);
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		user_cancel = true;

		if (mSensor != null) {
			mSensor.CancelAction();
		}

		if (tid_draw.isAlive()) {
			tid_draw.interrupt();
		}

		if (tid_image.isAlive()) {
			tid_image.interrupt();
		}

		if (tid_stress.isAlive()) {
			tid_stress.interrupt();
		}

		super.onDestroy();
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		mCanvas = mHolder.lockCanvas();
		mCanvas.drawColor(Color.WHITE);
		mHolder.unlockCanvasAndPost(mCanvas);
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		mCanvas = mHolder.lockCanvas();
		mCanvas.drawColor(Color.WHITE);
		mHolder.unlockCanvasAndPost(mCanvas);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
	}
}
