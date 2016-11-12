package com.dkl.jmtfps.dao;

/**
 * Tbale of all fingerprint
 * @author cdfinger
 * @since 2015-2-4
 */
public class FpsTable {
	public static final String TABLE_NAME="fp_data_table";
	public static final String COL_ID="_id";
	public static final String COL_FP_NAME="fp_name";
	public static final String COL_FP_DATA_ID="fp_data_id";


	public static final String SQL_CREATE="create table if not exists "+TABLE_NAME
    		+"("
    		+COL_ID+" integer primary key autoincrement,"
    		+COL_FP_NAME+" text,"
    		+COL_FP_DATA_ID+" text"
    		+")";
}
