package com.choudoufu.solr.util;

import java.util.Date;

import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequestBase;
import org.apache.solr.response.SolrQueryResponse;

import com.choudoufu.solr.constants.CacheConsts;
import com.choudoufu.solr.model.SysTable;


public class IdGrowthUtil {

	private static volatile SolrCore core;
	private static volatile SolrQueryRequestBase solrReq;
	private static volatile SolrQueryResponse solrResp;
	
	
	public static void init(SolrCore core){
		IdGrowthUtil.core = core;
		solrReq = new SolrQueryRequestBase(core, null) { };
		solrResp = new SolrQueryResponse();
	}
	
	private static long getGrowthId(String module){
		SysTable sysTable = SolrJUtil.getModel("id:"+module, core, solrReq, solrResp, SysTable.class);
		return sysTable == null ? -1L : sysTable.getGrowthId();
	}
	
	private static void addGrowthId(String module, long id){
		SysTable sysTable = new SysTable(module);
		sysTable.setGrowthId(id);
		sysTable.setUpdateTime(new Date());
		SolrJUtil.addModel(sysTable, core, solrReq, solrResp);
	}
	
	/**
	 * 获得 自增ID
	 * @param module
	 * @return
	 */
	public static long getIncrId(String module){
		long interval = 2000L;//增长区间
		//取 缓存
		Long id = getCacheIncrId(module); //
		if(id == null){
			id = getGrowthId(module);
			if(id == -1)
				id = 0L;
		}
		//更新 索引 磁盘
		if(id%interval == 0){
			addGrowthId(module, id+interval);
		}
		id++;
		
		//put 到缓存
		putCacheIncrId(module, id);
		return id;
	}
	
	public static String getIncrIdStr(String module){
		return getIncrId(module)+"";
	}
	
	private static Long getCacheIncrId(String moduleKey){
		return (Long)EhcacheUtil.getInstance().get(CacheConsts.CACHE_SYS, moduleKey);
	}
	
	private static void putCacheIncrId(String moduleKey, Long id){
		EhcacheUtil.getInstance().put(CacheConsts.CACHE_SYS, moduleKey, id);
	}
	
	
//	public static long getIncrId(String module){
//		long interval = 200L;
//		//取内存
//		Long id = getCacheIncrId(module, false);
//		if(id == null){
//			//取磁盘
//			id = getCacheIncrId(module, true);
//			if(id == null)
//				id = 0L;
//		}
//		//写入 磁盘
//		if(id%interval == 0){
//			putCacheIncrId(module, id+interval, true);
//		}
//		id++;
//		
//		//写入 内存
//		putCacheIncrId(module, id, false);
//		return id;
//	}
	
}
