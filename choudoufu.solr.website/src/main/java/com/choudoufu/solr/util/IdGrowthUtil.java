package com.choudoufu.solr.util;

import com.choudoufu.solr.constants.CacheConsts;


public class IdGrowthUtil {

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 50; i++) {
			System.out.println(getIncrId("test1"));
		}
		
		Thread.sleep(1000);
		EhcacheUtil.getInstance().shutdown();
	}
	/**
	 * 获得 自增ID
	 * @param module
	 * @return
	 */
	public static long getIncrId(String module){
		long interval = 200L;
		//取内存
		Long id = getCacheIncrId(module, false);
		if(id == null){
			//取磁盘
			id = getCacheIncrId(module, true);
			if(id == null)
				id = 0L;
		}
		//写入 磁盘
		if(id%interval == 0){
			putCacheIncrId(module, id+interval, true);
		}
		id++;
		
		//写入 内存
		putCacheIncrId(module, id, false);
		return id;
	}
	
	private static Long getCacheIncrId(String moduleKey, boolean isDisk){
		return (Long)EhcacheUtil.getInstance().get(CacheConsts.CACHE_TO_DISK, moduleKey);
	}
	
	private static void putCacheIncrId(String moduleKey, Long id, boolean isDisk){
		if(isDisk)
			EhcacheUtil.getInstance().putDisk(CacheConsts.CACHE_TO_DISK, moduleKey, id);
		else
			EhcacheUtil.getInstance().put(CacheConsts.CACHE_TO_DISK, moduleKey, id);
	}
}
