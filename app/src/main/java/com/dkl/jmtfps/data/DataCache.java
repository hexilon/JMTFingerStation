package com.dkl.jmtfps.data;

import java.util.HashMap;
import java.util.Map;

/** 
 * 应用统一内存缓存类
 */
public class DataCache {

	private static Map<String, Object> cache = null;
	private static DataCache mInstance;
	
	private DataCache() {
		cache = new HashMap<String, Object>();
	}
	
	public static DataCache getInstance() {
		if(mInstance == null) {
			synchronized (DataCache.class) {
				mInstance = new DataCache();
			}
		}
		return mInstance;
	}
	
	public void put(String key, Object value) {
		cache.put(key, value);
	}
	
	public Object get(String key) {
		if(cache.containsKey(key)) {
			return cache.get(key);
		} else {
			return null;
		}
	}
	
	public void remove(String key) {
		if(cache.containsKey(key))
			cache.remove(key);
	}
	
	public void clear() {
		cache.clear();
	}
}
