package com.android.server;

import com.jmt.fps.JmtFP;

import android.os.IFingerprintCallBack;
import android.os.IFingerprintService;
import android.os.RemoteException;
import android.util.Slog;

public class FingerprintService extends IFingerprintService.Stub {
	private IFingerprintCallBack fingerprintCallBack;
	private final String TAG = "FingerprintService";
	private final String DEV = "dev/jmt101";
	private final String FP_DATA = "fp_data";
	private final int IFP_CALLBACK_IS_NULL = -1;

	public FingerprintService() {
		Slog.v(TAG, "FingerprintService is init");
	}

	@Override
	public int initSensor(IFingerprintCallBack CallBack) throws RemoteException {
		// TODO Auto-generated method stub
		fingerprintCallBack = CallBack;
		JmtFP.getInstance().setAppPathSingleton(FP_DATA);
		JmtFP.getInstance().setDriverPathSingleton(DEV);
		JmtFP.getInstance().setCallbackSingleton(callback);
		JmtFP.getInstance().CancelWaitTouch();
		JmtFP.getInstance().CancelAction();
		return 0;
	}

	@Override
	public int GetSensorType() throws RemoteException {
		// TODO Auto-generated method stub
		return JmtFP.getInstance().GetSensorType();
	}

	@Override
	public int CancelAction() throws RemoteException {
		// TODO Auto-generated method stub
		return JmtFP.getInstance().CancelAction();
	}

	@Override
	public int Verify() throws RemoteException {
		// TODO Auto-generated method stub
		return JmtFP.getInstance().Verify();
	}

	@Override
	public int SetEnrollCounts(int cnt) throws RemoteException {
		// TODO Auto-generated method stub
		return JmtFP.getInstance().SetEnrollCounts(cnt);
	}

	@Override
	public int Enrollment() throws RemoteException {
		// TODO Auto-generated method stub
		return JmtFP.getInstance().Enrollment();
	}

	@Override
	public int Erase(int index) throws RemoteException {
		// TODO Auto-generated method stub
		return JmtFP.getInstance().Erase(index);
	}

	@Override
	public int EraseFull() throws RemoteException {
		// TODO Auto-generated method stub
		return JmtFP.getInstance().EraseFull();
	}

	@Override
	public int SetWaitTouch() throws RemoteException {
		// TODO Auto-generated method stub
		return JmtFP.getInstance().SetWaitTouch();
	}

	@Override
	public int CancelWaitTouch() throws RemoteException {
		// TODO Auto-generated method stub
		return JmtFP.getInstance().CancelWaitTouch();
	}

	@Override
	public int WaitClick() throws RemoteException {
		// TODO Auto-generated method stub
		return JmtFP.getInstance().WaitClick();
	}

	@Override
	public int StartNavigation() throws RemoteException {
		// TODO Auto-generated method stub
		return JmtFP.getInstance().StartNavigation();
	}

	@Override
	public String GetSDKVersion() throws RemoteException {
		// TODO Auto-generated method stub
		return JmtFP.getInstance().GetSDKVersion();
	}

	@Override
	public int GetOneFinger() throws RemoteException {
		// TODO Auto-generated method stub
		return JmtFP.getInstance().GetOneFinger();
	}

	@Override
	public int GetFingerLoop() throws RemoteException {
		// TODO Auto-generated method stub
		return JmtFP.getInstance().GetFingerLoop();
	}

	@Override
	public int SystemWakeup() throws RemoteException {
		// TODO Auto-generated method stub
		return JmtFP.getInstance().SystemWakeup();
	}

	@Override
	public int SetInputKey(int key) throws RemoteException {
		// TODO Auto-generated method stub
		return JmtFP.getInstance().SetInputKey(key);
	}

	JmtFP.jmtcallback callback = new JmtFP.jmtcallback() {

		@Override
		public int onAdvice(int advice) {
			// TODO Auto-generated method stub
			if (fingerprintCallBack == null)
				return IFP_CALLBACK_IS_NULL;
			try {
				return fingerprintCallBack.onAdvice(advice);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 0;
		}

		@Override
		public int onClick(int type) {
			// TODO Auto-generated method stub
			if (fingerprintCallBack == null)
				return IFP_CALLBACK_IS_NULL;
			try {
				return fingerprintCallBack.onClick(type);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 0;
		}

		@Override
		public int onEnrollProgress(String finger_id, int percentage) {
			// TODO Auto-generated method stub
			if (fingerprintCallBack == null)
				return IFP_CALLBACK_IS_NULL;
			try {
				return fingerprintCallBack.onEnrollProgress(finger_id,
						percentage);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 0;
		}

		@Override
		public int onEnrollProgress2(int gid, int fid, int percentage) {
			// TODO Auto-generated method stub
			if (fingerprintCallBack == null)
				return IFP_CALLBACK_IS_NULL;
			try {
				return fingerprintCallBack.onEnrollProgress2(gid, fid,
						percentage);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 0;
		}

		@Override
		public int onNavigation(int result, int ver, int hor) {
			// TODO Auto-generated method stub
			if (fingerprintCallBack == null)
				return IFP_CALLBACK_IS_NULL;
			try {
				return fingerprintCallBack.onNavigation(result, ver, hor);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 0;
		}

		@Override
		public int onShowData(byte[] pfpimage, int ifpwidth, int ifpheight) {
			// TODO Auto-generated method stub
			if (fingerprintCallBack == null)
				return IFP_CALLBACK_IS_NULL;
			try {
				return fingerprintCallBack.onShowData(pfpimage, ifpwidth,
						ifpheight);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 0;
		}

		@Override
		public int onShowImage(byte[] pfpimage, int ifpwidth, int ifpheight) {
			// TODO Auto-generated method stub
			if (fingerprintCallBack == null)
				return IFP_CALLBACK_IS_NULL;
			try {
				return fingerprintCallBack.onShowImage(pfpimage, ifpwidth,
						ifpheight);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 0;
		}

	};

}
