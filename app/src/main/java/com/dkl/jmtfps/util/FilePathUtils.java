package com.dkl.jmtfps.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

/**
 * 文件路径工具类
 */
public class FilePathUtils {
	
	
	/**
	 * 根据传入的uniqueName获取硬盘缓存的路径地址。
	 */
	@SuppressLint("NewApi")
	private static File getDiskCacheDir(Context context, String uniqueName) {
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			cachePath = context.getExternalCacheDir().getPath();
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + uniqueName);
	}
	
	/**
	 * 获得当前应用默认的解压路径
	 * 
	 * @param context
	 * @return
	 */
	public static String getDefaultUnzipFile(Context mContext) {
		File cacheDir = getDiskCacheDir(mContext, "unZip");
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
		return cacheDir.getAbsolutePath();
	}

	/**
	 * 获取SD卡目录下相对应包名程序下的数据库的路径
	 * 
	 * @param context
	 * @return
	 */
	public static String getDefaultDataBasePath(Context mContext) {
		File cacheDir = getDiskCacheDir(mContext, "database");
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
		return cacheDir.getAbsolutePath();
	}

	public static File getDefaultDataBasePath_XMPP(Context mContext) {
		File cacheDir = getDiskCacheDir(mContext, "xmpp_database");
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
		return cacheDir;
	}

	/**
	 * 获取SD卡目录下相对应包名程序下的文件保存的图片的路径
	 * 
	 * @param context
	 * @return
	 */
	public static String getDefaultFilePath(Context mContext) {
		File cacheDir = getDiskCacheDir(mContext, "file");
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
		return cacheDir.getAbsolutePath();
	}
	
	public static String getRecvFilePath() {
		try {
			String path = Environment.getExternalStorageDirectory().getAbsolutePath();
			File file = new File(path + "/kingnode/recvFile");
			if (!file.exists()) {
				file.mkdirs();
			}
			return file.getAbsolutePath();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 获取SD卡目录下相对应包名程序下的拍照保存的图片的路径
	 * 
	 * @param context
	 * @return
	 */
	public static String getDefaultImageFilePath(Context mContext) {
		File cacheDir = getDiskCacheDir(mContext, "image");
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
		return cacheDir.getAbsolutePath();
	}
	
	/**
	 * 获取SD卡目录下相对应包名程序下的录音保存的图片的路径
	 * 
	 * @param context
	 * @return
	 */
	public static String getDefaultRecordPath(Context mContext) {
		File cacheDir = getDiskCacheDir(mContext, "record");
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
		return cacheDir.getAbsolutePath();
	}

	/**
	 * 获取SD卡目录下相对应包名程序下的视频保存的图片的路径
	 * 
	 * @param context
	 * @return
	 */
	public static String getDefaultVideoPath(Context mContext) {
		File cacheDir = getDiskCacheDir(mContext, "video");
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
		return cacheDir.getAbsolutePath();
	}

	@SuppressLint("NewApi")
	public static void deleteAllCacel(Context context) {
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			cachePath = context.getExternalCacheDir().getPath();
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		try {
			deletefile(new File(cachePath + File.separator).getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除某个文件夹下的所有文件夹和文件
	 * 
	 * @param delpath
	 *            String
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @return boolean
	 */
	public static boolean deletefile(String delpath) throws Exception {
		try {
			File file = new File(delpath);
			// 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
			if (!file.isDirectory()) {
				file.delete();
			} else if (file.isDirectory()) {
				String[] filelist = file.list();
				for (int i = 0; i < filelist.length; i++) {
					File delfile = new File(delpath + "/" + filelist[i]);
					if (!delfile.isDirectory()) {
						delfile.delete();
						System.out.println(delfile.getAbsolutePath() + "删除文件成功");
					} else if (delfile.isDirectory()) {
						deletefile(delpath + "/" + filelist[i]);
					}
				}
				System.out.println(file.getAbsolutePath() + "删除成功");
				file.delete();
			}

		} catch (FileNotFoundException e) {
		}
		return true;
	}
	
	/**
	 * 日志保存目录
	 * @return
	 */
	public static String getDefaultLogPath(Context mContext) {
		File cacheDir = getDiskCacheDir(mContext, "log");
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
		return cacheDir.getAbsolutePath();
	}
	
	/**
	 * 日志保存目录
	 * 
	 * @return
	 */
	public static String getDefaultLogFileName(Context mContext) {
		return getDefaultLogPath(mContext) + "/" + mContext.getPackageName() + "-"
				+ new SimpleDateFormat("yyyyMMdd").format(new Date()) + "-"
				+ "alanqiu" + ".txt";

	}

	public static String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection,null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }

        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

	public static void openFile(Context ct, File file) {
		try {
			if (null == file || !file.exists()) {
				Toast.makeText(ct, "文件不存在！", Toast.LENGTH_SHORT).show();
				return;
			}
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// 设置intent的Action属性
			intent.setAction(Intent.ACTION_VIEW);
			// 获取文件file的MIME类型
			String type = getMIMEType(file);
			// 设置intent的data和Type属性。
			intent.setDataAndType(/* uri */Uri.fromFile(file), type);
			// 跳转
			ct.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(ct, "找不到可以打开该文件类型的应用\r\n文件存放于:\r\n" + file.getPath(),Toast.LENGTH_SHORT).show();
		}
	}
	/**
	 * 根据文件后缀名获得对应的MIME类型。
	 * 
	 * @param file
	 */
	private static String getMIMEType(File file) {
		String type = "*/*";
		String fName = file.getName();
		// 获取后缀名前的分隔符"."在fName中的位置。
		int dotIndex = fName.lastIndexOf(".");
		if (dotIndex < 0) {
			return type;
		}
		/* 获取文件的后缀名 */
		String end = fName.substring(dotIndex, fName.length()).toLowerCase();
		if ("".equals(end)) {
			return type;
		}
		// 在MIME和文件类型的匹配表中找到对应的MIME类型。
		for (int i = 0; i < MIME_MapTable.length; i++) {
			if (end.equals(MIME_MapTable[i][0]))
				type = MIME_MapTable[i][1];
		}
		return type;
	}
	private final static String[][] MIME_MapTable = {
			// {后缀名，MIME类型}
			{ ".3gp", "video/3gpp" },
			{ ".apk", "application/vnd.android.package-archive" },
			{ ".asf", "video/x-ms-asf" },
			{ ".avi", "video/x-msvideo" },
			{ ".bin", "application/octet-stream" },
			{ ".bmp", "image/bmp" },
			{ ".c", "text/plain" },
			{ ".class", "application/octet-stream" },
			{ ".conf", "text/plain" },
			{ ".cpp", "text/plain" },
			{ ".doc", "application/msword" },
			{ ".docx",
					"application/vnd.openxmlformats-officedocument.wordprocessingml.document" },
			{ ".xls", "application/vnd.ms-excel" },
			{ ".xlsx",
					"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" },
			{ ".exe", "application/octet-stream" },
			{ ".gif", "image/gif" },
			{ ".gtar", "application/x-gtar" },
			{ ".gz", "application/x-gzip" },
			{ ".h", "text/plain" },
			{ ".htm", "text/html" },
			{ ".html", "text/html" },
			{ ".jar", "application/java-archive" },
			{ ".java", "text/plain" },
			{ ".jpeg", "image/jpeg" },
			{ ".jpg", "image/jpeg" },
			{ ".js", "application/x-javascript" },
			{ ".log", "text/plain" },
			{ ".m3u", "audio/x-mpegurl" },
			{ ".m4a", "audio/mp4a-latm" },
			{ ".m4b", "audio/mp4a-latm" },
			{ ".m4p", "audio/mp4a-latm" },
			{ ".m4u", "video/vnd.mpegurl" },
			{ ".m4v", "video/x-m4v" },
			{ ".mov", "video/quicktime" },
			{ ".mp2", "audio/x-mpeg" },
			{ ".mp3", "audio/x-mpeg" },
			{ ".mp4", "video/mp4" },
			{ ".mpc", "application/vnd.mpohun.certificate" },
			{ ".mpe", "video/mpeg" },
			{ ".mpeg", "video/mpeg" },
			{ ".mpg", "video/mpeg" },
			{ ".mpg4", "video/mp4" },
			{ ".mpga", "audio/mpeg" },
			{ ".msg", "application/vnd.ms-outlook" },
			{ ".ogg", "audio/ogg" },
			{ ".pdf", "application/pdf" },
			{ ".png", "image/png" },
			{ ".pps", "application/vnd.ms-powerpoint" },
			{ ".ppt", "application/vnd.ms-powerpoint" },
			{ ".pptx","application/vnd.openxmlformats-officedocument.presentationml.presentation" },
			{ ".prop", "text/plain" }, { ".rc", "text/plain" },
			{ ".rmvb", "audio/x-pn-realaudio" }, { ".rtf", "application/rtf" },
			{ ".sh", "text/plain" }, { ".tar", "application/x-tar" },
			{ ".tgz", "application/x-compressed" }, { ".txt", "text/plain" },
			{ ".wav", "audio/x-wav" }, { ".wma", "audio/x-ms-wma" },
			{ ".wmv", "audio/x-ms-wmv" },
			{ ".wps", "application/vnd.ms-works" }, { ".xml", "text/plain" },
			{ ".z", "application/x-compress" },
			{ ".zip", "application/x-zip-compressed" }, { "", "*/*" } };


//	public static int getFileIcon(String fileName) {
//		String ext = "";
//		if (fileName.contains(".")) {
//			ext = fileName.substring(fileName.lastIndexOf(".") + 1);
//		}
//		if ("doc".equals(ext) || "docx".equals(ext) || "rtf".equals(ext) || "wps".equalsIgnoreCase(ext)) {
//			return R.drawable.fdq;
//		} else if ("xls".equalsIgnoreCase(ext) || "xlsx".equalsIgnoreCase(ext)) {
//			return R.drawable.fcg;
//		} else if ("ppt".equalsIgnoreCase(ext) || "pptx".equalsIgnoreCase(ext)) {
//			return R.drawable.fcz;
//		} else if ("xml".equalsIgnoreCase(ext) || "html".equalsIgnoreCase(ext)) {
//			return R.drawable.fdb;
//		} else if ("pdf".equalsIgnoreCase(ext)) {
//			return R.drawable.fda;
//		} else if ("txt".equalsIgnoreCase(ext)) {
//			return R.drawable.fcv;
//		} else if ("png".equalsIgnoreCase(ext) || "jpg".equalsIgnoreCase(ext) || "gif".equalsIgnoreCase(ext) || "bmp".equalsIgnoreCase(ext) || "jpeg".equalsIgnoreCase(ext)) {
//			return R.drawable.fdd;
//		} else if ("swf".equalsIgnoreCase(ext) || "rmvb".equalsIgnoreCase(ext) || "avi".equalsIgnoreCase(ext) || "mp4".equalsIgnoreCase(ext) || "rm".equalsIgnoreCase(ext) || "mkv".equalsIgnoreCase(ext)) {
//			return R.drawable.fch;
//		} else if ("amr".equalsIgnoreCase(ext) || "wav".equalsIgnoreCase(ext) || "mp3".equalsIgnoreCase(ext)) {
//			return R.drawable.fdc;
//		} else if ("rar".equalsIgnoreCase(ext) || "zip".equalsIgnoreCase(ext) || "7z".equalsIgnoreCase(ext)) {
//			return R.drawable.fcf;
//		} else {
//			return R.drawable.fco;
//		}
//	}
}
