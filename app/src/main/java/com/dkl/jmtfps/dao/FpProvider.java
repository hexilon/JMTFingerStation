package com.dkl.jmtfps.dao;

import com.dkl.jmtfps.dao.DbHelper;
import com.dkl.jmtfps.dao.FpsTable;
import com.dkl.jmtfps.util.Log;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * ContentProvider of fingerprint
 * @author cdfinger
 * @since 2015-2-4
 */
public class FpProvider extends ContentProvider {
	public static final String TAG = "FpProvider";
	public static SQLiteDatabase database;
	/*
	 * Database Name
	 */
	public static final String DATABASE_NAME = "cdfinger_fp";
	/*
	 * database Version
	 */
	public static final int DATABASE_VERSION = 1;

	@Override
	public boolean onCreate() {
		database = getContext().openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
                database.execSQL(FpsTable.SQL_CREATE);
                Log.e(TAG, "onCreate success...");
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,String[] selectionArgs, String sortOrder) {
		
                if (uri.toString().equals(DbHelper.GROUPFP)){
                    //group by FpsTable.COL_FP_DATA_INDEX
                    return database.query(FpsTable.TABLE_NAME, projection, selection,selectionArgs, FpsTable.COL_FP_NAME, null, sortOrder);
                } else if (uri.toString().equals(DbHelper.NOTGROUPFP)){
                    return database.query(FpsTable.TABLE_NAME, projection, selection,selectionArgs, null, null, sortOrder);
                }
                Log.e(TAG, "query success...");
                return database.query(FpsTable.TABLE_NAME, projection, selection,selectionArgs, FpsTable.COL_FP_NAME, null, sortOrder);
		
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long rowid = database.insert(FpsTable.TABLE_NAME, FpsTable.COL_ID,values);
		Uri insertUri = ContentUris.withAppendedId(uri, rowid);// 得到代表新增记录的Uri
		this.getContext().getContentResolver().notifyChange(uri, null);
		Log.e(TAG, "insert success...");
		return insertUri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		Log.e(TAG, "delete success...");
		return database.delete(FpsTable.TABLE_NAME, selection, selectionArgs);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,String[] selectionArgs) {
		Log.e(TAG, "update success...");
		return database.update(FpsTable.TABLE_NAME, values, selection,selectionArgs);
	}

}
