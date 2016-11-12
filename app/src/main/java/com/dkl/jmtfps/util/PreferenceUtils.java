/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dkl.jmtfps.util;


import android.content.Context;
import android.content.SharedPreferences;


/**
 * shaPreference 工具类
 * 
 * @author: dkl
 * @remark:
 */
public class PreferenceUtils {

	/**
	 * 保存Preference的name
	 */
	public static String PREFERENCE_NAME = DKLApplication.getInstance()
			.getPackageName();
	private static SharedPreferences mSharedPreferences;
	private static PreferenceUtils mPreferenceUtils;
	private static SharedPreferences.Editor editor;

	private PreferenceUtils() {
		mSharedPreferences = DKLApplication.getInstance().getSharedPreferences(
				PREFERENCE_NAME, Context.MODE_PRIVATE);
	}

	/**
	 * 单例模式，获取instance实例
	 * 
	 * @param cxt
	 * @return
	 */
	public static PreferenceUtils getInstance() {
		if (mPreferenceUtils == null) {
			mPreferenceUtils = new PreferenceUtils();
		}
		editor = mSharedPreferences.edit();
		return mPreferenceUtils;
	}

	public void putString(String key, String value) {
		editor.putString(key, value);
		editor.commit();
	}

	public void putBoolean(String key, boolean value) {
		editor.putBoolean(key, value);
		editor.commit();
	}

	public void putInt(String key, int value) {
		editor.putInt(key, value);
		editor.commit();
	}

	public String getString(String key, String defValue) {
		return mSharedPreferences.getString(key, defValue);
	}

	public boolean getBoolean(String key, boolean defValue) {
		return mSharedPreferences.getBoolean(key, defValue);
	}

	public int getInt(String key, int defValue) {
		return mSharedPreferences.getInt(key, defValue);
	}

	public void remove(String key) {
		if (mSharedPreferences.contains(key)) {
			editor.remove(key);
			editor.commit();
		}
	}
	public void putPwd(String pwd) {
		putString(Constants.KEY_PWD, pwd);
	}
	
	public String getPwd(){
		return getString(Constants.KEY_PWD, "");
	}

	public boolean isContains(String key) {
		return mSharedPreferences.contains(key);
	}

	public void clear() {
		editor.clear().commit();
	}

}
