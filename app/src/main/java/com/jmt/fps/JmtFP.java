package com.jmt.fps;

/**
 * Created by Amo on 2016/9/5.
 */

import android.util.Log;

public class JmtFP {
    public static final int ERR_OK = 0;
    public static final int ERR_FAILED = -1;
    public static final int ERR_ABORT = -2;
    public static final int ERR_NO_DEV = -3;
    public static final int ERR_NOMATCH = -4;
    public static final int ERR_NODEVICE = -5;
    public static final int ERR_TIMEOUT = -6;
    public static final int ERR_INVALIDARG = -7;
    public static final int ERR_NO_MEMORY = -8;
    public static final int ERR_ACCESS = -9;
    public static final int ERR_SDK_INTERNAL = -10;
    public static final int ERR_NO_UUID = -11;
    public static final int ERR_NO_DATABASE = -12;
    public static final int ERR_TOUCH = -13;
    public static final int ERR_JAVA_ALLOC = -14;
    public static final int ERR_SENSOR_IN_OPERATION = -15;
    public static final int ERR_NO_MATCH_PATTERN = -16;
    public static final int ERR_DATA_BASE_IS_FULL = -17;
    public static final int _10X_ = 0;
    public static final int _30X_ = 1;
    public static final int _303_ = 2;
    public static final int _305_ = 3;
    public static final int _307_ = 4;
    public static final int NOTIFY_LEAVE_FINGER = 100;
    public static final int TOO_WET = 101;
    public static final int TOO_SMALL = 102;
    public static final int ADJUST = 103;
    public static final int WAIT_FINGER = 104;
    public static final int CHANGE_FINGER = 105;
    /**
     *	amo add to merge +robert
     */
    public static final int _ACQUIRED_GOOD = 0;
    public static final int _ACQUIRED_PARTIAL = 1;
    public static final int _ACQUIRED_INSUFFICIENT = 2;
    public static final int _ACQUIRED_IMAGER_DIRTY = 3;
    public static final int _ACQUIRED_TOO_SLOW = 4;
    public static final int _ACQUIRED_TOO_FAST = 5;
    public static final int _ACQUIRED_VENDOR_BASE = 1000;
    public static final int _ACQUIRED_VENDOR_BASE_ALI = 1100;
    public static final int _ACQUIRED_WAIT_FINGER_INPUT = 1101;
    public static final int _ACQUIRED_FINGER_DOWN = 1102;
    public static final int _ACQUIRED_FINGER_UP = 1103;
    public static final int _ACQUIRED_INPUT_TOO_LONG = 1104;
    public static final int _ACQUIRED_DUPLICATE_FINGER = 1105;
    public static final int _ACQUIRED_DUPLICATE_AREA = 1106;
    public static final int _ACQUIRED_LOW_COVER = 1107;
    public static final int _ACQUIRED_BAD_IMAGE = 1108;
    public static final int _ERROR_HW_UNAVAILABLE = 1;
    public static final int _ERROR_UNABLE_TO_PROCESS = 2;
    public static final int _ERROR_TIMEOUT = 3;
    public static final int _ERROR_NO_SPACE = 4;
    public static final int _ERROR_CANCELED = 5;
    public static final int _ERROR_UNABLE_TO_REMOVE = 6;
    public static final int _ERROR_VENDOR_BASE = 1000;
    private JmtFP.jmtcallback mjc;
    private String app_path;
    private String driver_path;
    private static volatile JmtFP instance = null;

    static {
        try {
            Log.e("JMTFP", "LoadLibrary ..... ");
            System.loadLibrary("jni_jmtfps");
        } catch (Throwable var1) {
            System.err.println("Unable to load jni_jmtfps: " + var1);
        }

    }

    public int onClick(int type) {
        if(this.mjc != null) {
            try {
                this.mjc.onClick(type);
            } catch (Exception var3) {
                Log.e("JMT", "Module CALLBACK onClick something wrong");
            }
        }

        return 0;
    }

    public int onNavigation(int result, int ver, int hor) {
        if(this.mjc != null) {
            try {
                this.mjc.onNavigation(result, ver, hor);
            } catch (Exception var5) {
                Log.e("JMT", "Module CALLBACK onNavigation something wrong");
            }
        }

        return 0;
    }

    public int onEnrollProgress2(int gid, int fid, int percentage) {
        if(this.mjc != null) {
            try {
                this.mjc.onEnrollProgress2(gid, fid, percentage);
            } catch (Exception var5) {
                Log.e("JMT", "Module CALLBACK onEnrollProgress2 something wrong");
            }
        }

        return 0;
    }

    public int onEnrollProgress(String finger_id, int percentage) {
        if(this.mjc != null) {
            try {
                this.mjc.onEnrollProgress(finger_id, percentage);
            } catch (Exception var4) {
                Log.e("JMT", "Module CALLBACK onEnrollProgress something wrong");
            }
        }

        return 0;
    }

    public int onAdvice(int event) {
        if(this.mjc != null) {
            try {
                this.mjc.onAdvice(event);
            } catch (Exception var3) {
                Log.e("JMT", "Module CALLBACK onAdvice something wrong");
            }
        }

        return 0;
    }

    public int onShowData(byte[] pfpimage, int ifpwidth, int ifpheight) {
        int ret = 0;
        if(this.mjc != null) {
            try {
                ret = this.mjc.onShowData(pfpimage, ifpwidth, ifpheight);
            } catch (Exception var6) {
                Log.e("JMT", "Module CALLBACK onShowData something wrong");
            }
        }

        return ret;
    }

    public int onShowImage(byte[] pfpimage, int ifpwidth, int ifpheight) {
        int ret = 0;
        if(this.mjc != null) {
            try {
                ret = this.mjc.onShowImage(pfpimage, ifpwidth, ifpheight);
            } catch (Exception var6) {
                Log.e("JMT", "Module CALLBACK onShowImage something wrong");
            }
        }

        return ret;
    }

    public void setCallbackSingleton(JmtFP.jmtcallback jmc) {
        this.mjc = jmc;
        this.SetCallback(jmc);
    }

    public void setDriverPathSingleton(String driver_path) {
        this.driver_path = driver_path;
        this.SetDir(this.app_path, driver_path);
    }

    public void setAppPathSingleton(String app_path) {
        this.app_path = app_path;
    }

    public static synchronized JmtFP getInstance() {
        if(instance == null) {
            Class var0 = JmtFP.class;
            synchronized(JmtFP.class) {
                instance = new JmtFP();
            }
        }

        return instance;
    }

    public JmtFP() {
    }

    public void SetCallback(JmtFP.jmtcallback jmc) {
        this.mjc = jmc;
        this.native_SetCallbackObject(this.mjc);
    }

    public int GetFingerAttr(int type, int[] width, int[] height) {
        byte result = 0;
        switch(type) {
            case 0:
                width[0] = 256;
                height[0] = 400;
                break;
            case 1:
                width[0] = 128;
                height[0] = 128;
                break;
            case 2:
                width[0] = 176;
                height[0] = 80;
                break;
            case 3:
                width[0] = 120;
                height[0] = 120;
                break;
            case 4:
                width[0] = 184;
                height[0] = 56;
                break;
            default:
                width[0] = 0;
                height[0] = 0;
                result = -7;
        }

        return result;
    }

    public int Enrollment() {
        return this.native_Enrollment();
    }

    public int Verify() {
        return this.native_Verify();
    }

    public int CancelAction() {
        return this.native_CancelAction();
    }

    public int SetDir(String app_path, String driver_path) {
        return this.native_SetDir(app_path, driver_path);
    }

    public String GetSDKVersion() {
        return this.native_GetSDKVersion();
    }

    public int ReadRegister(byte address) {
        return this.native_ReadRegister(address);
    }

    public int WriteRegister(byte address, byte value) {
        return this.native_WriteRegister(address, value);
    }

    public int GetSwipeFrame(int isSetSwipeRegister) {
        return this.native_GetSwipeFrame(isSetSwipeRegister);
    }

    public int GetSwipeFrameLoop(int isSetSwipeRegister) {
        return this.native_GetSwipeFrameLoop(isSetSwipeRegister);
    }

    public int GetRawData(int mode) {
        return this.native_GetRawData(mode);
    }

    public int GetOneFinger() {
        return this.native_GetOneFinger();
    }

    public int GetFingerLoop() {
        return this.native_GetFingerLoop();
    }

    public int SetWaitTouch() {
        return this.native_SetWaitTouch();
    }

    public int CancelWaitTouch() {
        return this.native_CancelWaitTouch();
    }

    public int SetEnrollCounts(int cnt) {
        return this.native_SetEnrollCounts(cnt);
    }

    public int GetSensorType() {
        return this.native_GetSensorType();
    }

    public int StartNavigation() {
        return this.native_StartNavigation();
    }

    public int WaitClick() {
        return this.native_WaitClick();
    }

    public int ReadChipID(int[] chip_id) {
        return this.native_ReadChipID(chip_id);
    }

    public int SystemWakeup() {
        return this.native_SystemWakeup();
    }

    public int VerifyAsync() {
        return this.native_VerifyAsync();
    }

    public int Erase(int index) {
        return this.native_Erase(index);
    }

    public int EraseFull() {
        return this.native_EraseFull();
    }

    public int SetInputKey(int key) {
        return this.native_SetInputKey(key);
    }

    private native int native_Enrollment();

    private native int native_Verify();

    private native int native_CancelAction();

    private native int native_SetDir(String var1, String var2);

    private native String native_GetSDKVersion();

    private native int native_GetOneFinger();

    private native int native_GetRawData(int var1);

    private native int native_ReadRegister(byte var1);

    private native int native_GetSwipeFrame(int var1);

    private native int native_GetSwipeFrameLoop(int var1);

    private native int native_WriteRegister(byte var1, byte var2);

    private native int native_GetFingerLoop();

    private native void native_SetCallbackObject(Object var1);

    private native int native_SetWaitTouch();

    private native int native_CancelWaitTouch();

    private native int native_GetSensorType();

    private native int native_SetEnrollCounts(int var1);

    private native int native_StartNavigation();

    private native int native_WaitClick();

    private native int native_ReadChipID(int[] var1);

    private native int native_SystemWakeup();

    private native int native_VerifyAsync();

    private native int native_Erase(int var1);

    private native int native_EraseFull();

    private native int native_SetInputKey(int var1);

    public interface jmtcallback {
        int onEnrollProgress(String var1, int var2);

        int onEnrollProgress2(int var1, int var2, int var3);

        int onAdvice(int var1);

        int onShowImage(byte[] var1, int var2, int var3);

        int onNavigation(int var1, int var2, int var3);

        int onClick(int var1);

        int onShowData(byte[] var1, int var2, int var3);
    }
}
