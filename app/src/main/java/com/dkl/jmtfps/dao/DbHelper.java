package com.dkl.jmtfps.dao;

import java.util.ArrayList;
import java.util.List;

import com.dkl.jmtfps.mode.FingerPrintModel;
import com.dkl.jmtfps.util.Constants;
import com.dkl.jmtfps.util.Log;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * 数据库操作
 * 
 * @author dkl
 * @since 2015-2-4
 */
public class DbHelper {
	private String TAG = "DbHelper";
	private Context mContext = null;
	public static SQLiteDatabase dbWrite;
	private ContentResolver mContentResolver = null;
	private static final UriMatcher MATCHER = new UriMatcher(
			UriMatcher.NO_MATCH);

	/*
	 * 按 fp_data_index 分组
	 */
	public static String GROUPFP = "content://" + Constants.PACKAGE_PATH
			+ ".dao/" + FpsTable.TABLE_NAME;
	/*
	 * 不分组，也就是查询所有记录
	 */
	public static String NOTGROUPFP = "content://" + Constants.PACKAGE_PATH
			+ ".dao/nogroup";

	public static Uri NOTGROUPFPURI = Uri.parse(NOTGROUPFP);
	public static Uri FPURI = Uri.parse(GROUPFP);

	public DbHelper(Context context) {
		mContext = context;
		dbWrite = FpProvider.database;
		mContentResolver = mContext.getContentResolver();
	}

	/******************** 插入数据begin ********************/

	public void insertNewFpData(String fp_name, String fp_data_index) {
		ContentValues values = new ContentValues();
		values.put(FpsTable.COL_FP_NAME, fp_name);
		values.put(FpsTable.COL_FP_DATA_ID, fp_data_index);
		mContentResolver.insert(FPURI, values);
		Log.e(TAG, "insertNewFpData success...");
	}

	public void insertNewFpData(FingerPrintModel finger) {
		insertNewFpData(finger.fp_name, finger.fp_data_index);
		Log.e(TAG, "insertNewFpData success...");
	}

	public void insertNewFpData(ArrayList<FingerPrintModel> fingers) {
		dbWrite.beginTransaction();
		try {
			for (int i = 0; i < fingers.size(); i++) {
				insertNewFpData(fingers.get(i));
			}
			dbWrite.setTransactionSuccessful();
		} finally {
			dbWrite.endTransaction();
		}

		Log.e(TAG, "insertNewFpDataList success...");
	}

	/******************** 插入数据end ********************/

	/******************** 查询begin ********************/

	/**
	 * Group by FpsTable.COL_FP_DATA_INDEX;因为命名可能相同，但是指纹对应的index不同
	 */
	public Cursor queryAllFinger() {
		Log.e(TAG, "queryAllFinger success...");
		Cursor cursor = mContentResolver.query(FPURI, new String[] {
				FpsTable.COL_ID, FpsTable.COL_FP_NAME,
				FpsTable.COL_FP_DATA_ID }, null, null, null);
		return cursor;
	}

	/*
	 * 得到指纹的个数
	 */
	public int getFingerCount() {
		Cursor cursor = mContentResolver.query(FPURI, new String[] {
				FpsTable.COL_ID, FpsTable.COL_FP_NAME,
				FpsTable.COL_FP_DATA_ID }, null, null, null);
		int count = cursor.getCount();
		cursor.close();
		return count;
	}

	/********************* 查询end ********************/

	/******************** 更新begin ********************/

	/**
	 * 重命名
	 */
	public void updateFpNameById(String fp_new_name, String fp_id_string) {
		ContentValues values = new ContentValues();
		values.put(FpsTable.COL_FP_NAME, fp_new_name);
		mContentResolver.update(FPURI, values, "_id=?",
				new String[] { fp_id_string });
		Log.e(TAG, "updateFpNameById success...");
	}
	

	public void updateFpNameByIndex(String fp_new_name, String fp_index_string) {
		ContentValues values = new ContentValues();
		values.put(FpsTable.COL_FP_NAME, fp_new_name);
		mContentResolver.update(FPURI, values, "fp_data_index=?",
				new String[] { fp_index_string });
		Log.e(TAG, "updateFpNameByIndex success...");
	}

	/******************** 更新end ********************/

	/******************** 删除begin ********************/
	/**
	 * 删除数据库中的指纹
	 * 
	 * @param fp_id
	 */
	public void deleteFpById(String fp_id) {
		mContentResolver.delete(FPURI, "_id=?", new String[] { fp_id });
		Log.e(TAG, "deleteFpById success...");
	}

	public void deleteFpByIndex(String fp_index_string) {
		mContentResolver.delete(FPURI, "fp_data_index=?",
				new String[] { fp_index_string });
		Log.e(TAG, "deleteFpByIndex success...");
	}

	/******************** 删除end ********************/

	/**
	 * 获取新指纹名称后缀
	 * 
	 * @return
	 */
	public int getNewFpNameIndex() {
		int _id = 1;
		int _index = 0;
		String title = "";
		String title_pre = "新指纹";// mContext.getString(R.string.app_name);

		Cursor cursor = mContentResolver.query(FPURI, new String[] {
				FpsTable.COL_FP_DATA_ID, FpsTable.COL_FP_NAME }, null, null,
				FpsTable.COL_FP_DATA_ID + " asc");

		List<String> idList = new ArrayList<String>();

		while (cursor.moveToNext()) {
			_id = cursor.getInt(cursor
					.getColumnIndex(FpsTable.COL_FP_DATA_ID));
			title = cursor.getString(cursor
					.getColumnIndex(FpsTable.COL_FP_NAME));
			Log.e(TAG, "_id : " + _id + " | title : " + title);
			idList.add(title);
		}
		cursor.close();

		Log.e(TAG, "当前最大_id : " + _id);

		for (int i = 1; i <= _id; i++) {
			String title_string = title_pre + i;
			if (!idList.contains(title_string)) {
				Log.e(TAG, "不包含笔记 : " + title_string);
				return i;
			}
		}

		// +robert
		_index = idList.size() + 1;
		Log.e(TAG, "所有笔记都已存在，只能取当前_id最大值 : " + _index);
		return _index;
	}

	/*
	 * 获取当前指纹的最大索引
	 */
	public int getCurrentMaxFpIndex() {
		int maxindex = 0;
		Cursor cursor = mContentResolver.query(FPURI,
				new String[] { FpsTable.COL_ID }, null, null,
				FpsTable.COL_ID + " desc");

		if (cursor.moveToFirst()) {
			maxindex = cursor.getInt(cursor
					.getColumnIndex(FpsTable.COL_ID));
		}
		cursor.close();

		Log.e(TAG, "getCurrentMaxFpIndex success..." + maxindex);
		return maxindex;
	}

	/*
	 * 获取当前指纹的最大ID
	 */
	public int getCurrentMaxFpId() {
		int maxid = 0;
		Cursor cursor = mContentResolver.query(FPURI,
				new String[] { FpsTable.COL_ID }, null, null,
				FpsTable.COL_FP_DATA_ID + " desc");
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				maxid = cursor.getInt(cursor.getColumnIndex(FpsTable.COL_ID));
			}
			cursor.close();
		}
		Log.e(TAG, "getCurrentMaxFpId success..." + maxid);
		return maxid;
	}

	/*
	 * 通过游标，初始化FingerPrintModel列表
	 */
	private List<FingerPrintModel> getFingerPrintFromCursor(Cursor cursor) {
		Log.e(TAG, "getFingerPrintFromCursor...begin");
		List<FingerPrintModel> fingerList = new ArrayList<FingerPrintModel>();
		while (cursor.moveToNext()) {
			FingerPrintModel finger = new FingerPrintModel();
			finger.fp_id = cursor
					.getInt(cursor.getColumnIndex(FpsTable.COL_ID));
			finger.fp_name = cursor.getString(cursor
					.getColumnIndex(FpsTable.COL_FP_NAME));
			finger.fp_data_index = cursor.getString(cursor
					.getColumnIndex(FpsTable.COL_FP_DATA_ID));
			fingerList.add(finger);
		}
		Log.e(TAG, "getFingerPrintFromCursor success...");
		return fingerList;
	}

	public List<FingerPrintModel> getFingerPrintList() {
		List<FingerPrintModel> arrayList = new ArrayList<FingerPrintModel>();
		Cursor cursor = queryAllFinger();
		if (cursor != null) {
			arrayList = getFingerPrintFromCursor(cursor);
		}
		return arrayList;
	}
}
