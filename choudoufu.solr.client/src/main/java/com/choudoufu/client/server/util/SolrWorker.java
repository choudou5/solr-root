package com.choudoufu.client.server.util;

import org.apache.solr.client.solrj.SolrServer;

/**
 * 描述：工作器
 * 构建组：solr-client
 * 作者：xuhaowen
 * 邮箱: xuhaowende@sina.cn
 * 日期:Mar 14, 2016-3:12:41 PM
 * 版权：徐浩文版权所有
 */
public class SolrWorker{
	
	private SolrServer solrServer;
    private long lastAccessTime;
    
    public SolrWorker(SolrServer solrServer){
    	this.solrServer = solrServer;
    	this.lastAccessTime = System.currentTimeMillis();
    }
    
    public void shutdown() {
        try {
            if(solrServer != null){
            	solrServer.shutdown();
            }
        }catch(Exception e){
        	System.out.println("solrServer shutdown error:"+e.getMessage());
        }finally {
        	solrServer = null;
        }
    }
    
    public void setAccessTime() {
		this.lastAccessTime = System.currentTimeMillis();
	}
    
	public SolrServer getSolrServer() {
		return solrServer;
	}
	public void setSolrServer(SolrServer solrServer) {
		this.solrServer = solrServer;
	}
	public long getLastAccessTime() {
		return lastAccessTime;
	}
	public void setLastAccessTime(long lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}
    
}
