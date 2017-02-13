package com.choudoufu.client.server.factory;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.choudoufu.client.server.util.HttpProp;
import com.choudoufu.client.server.util.SolrConnectPool;

/**
 * <pre> 
 * 描述：Solr 单机服务
 * 构建组：solr-client
 * 作者：徐浩文
 * 邮箱: xuhaowende@sina.cn
 * 日期:Jun 19, 2015-4:44:43 PM
 * 版权：徐浩文版权所有
 * </pre>
 */
public class SingleFactory {
   
	private final static Logger LOGGER = LoggerFactory.getLogger(SingleFactory.class);
	
	private SingleFactory() {}
	
	/**内部类，用于实现lzay机制 */
    private static class SingleHolder{
        /** 单例变量  */
        private static SingleFactory instance = new SingleFactory();
    }
    
    /**获取单例对象实例 */
    public static SingleFactory getInstance() {
        return SingleHolder.instance;
    }
	
	public SolrServer getSolrServer(String moduleName, HttpProp prop) {
		SolrConnectPool pool = SolrConnectPool.getInstance();
		if(pool.isCreate(moduleName)){
			try {
				HttpSolrServer solrServer = new HttpSolrServer(prop.getHost()+moduleName);
	    	    solrServer.setRequestWriter(new BinaryRequestWriter());
	            solrServer.setSoTimeout(prop.getSoTimeOut());
	            solrServer.setConnectionTimeout(prop.getConnectionTimeOut());
	            solrServer.setDefaultMaxConnectionsPerHost(prop.getMaxConnectionsPerHost());
	            solrServer.setMaxTotalConnections(prop.getMaxTotalConnections());
	            solrServer.setFollowRedirects(false);
	            solrServer.setAllowCompression(true);
	            solrServer.setMaxRetries(prop.getMaxRetries());
	            solrServer.setParser(new XMLResponseParser());
	            //添加 连接对象 到池子
	            pool.add(moduleName, solrServer);
				return solrServer;
			} catch (Exception ex) {
				LOGGER.error("连接"+moduleName+"失败", ex);
			}
		}
		return pool.getConnection(moduleName);
	}

}
