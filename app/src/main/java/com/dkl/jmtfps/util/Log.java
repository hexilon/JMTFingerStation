package com.dkl.jmtfps.util;

/**
 * 自定义APP_Debug控制的，log组
 * @see Constants.APP_Debug
 * @author kayle
 *
 */
public class Log {

	public static void i(String tag, String msg) {
		if (Constants.APP_Debug)
			android.util.Log.i(tag, msg);
	}

	public static void e(String tag, String msg) {
		if (Constants.APP_Debug)
			android.util.Log.e(tag, msg);
	}

	public static void v(String tag, String msg) {
		if (Constants.APP_Debug)
			android.util.Log.v(tag, msg);
	}

	public static void d(String tag, String msg) {
		if (Constants.APP_Debug)
			android.util.Log.d(tag, msg);
	}

}
