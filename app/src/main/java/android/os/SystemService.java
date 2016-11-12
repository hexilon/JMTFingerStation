package android.os;

import android.content.Context;


public class SystemService {
	public SystemService(Context context) {
		// TODO Auto-generated constructor stub
	}
	public void onStart() {
	}

	
	public void onBootPhase(int phase) {

	}
	private void startOtherServices() {
//		if (!engModeFlag && !disableNonCoreServices) {
//            try {
//                Slog.i(TAG, "UpdateLock Service");
//                ServiceManager.addService(Context.UPDATE_LOCK_SERVICE,
//                        new UpdateLockService(context));
//            } catch (Throwable e) {
//                reportWtf("starting UpdateLockService", e);
//            }
//        }
		
//		try {
//            Slog.i(TAG, "fingerprint Service");
//            ServiceManager.addService("fingerManager", new FPService(0, context));
//        }
//        catch (Throwable e) {
//            reportWtf("starting fingerprint Service", e);
//        }
	}
}
