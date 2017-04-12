package com.choudoufu.client.server.factory;

import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.choudoufu.client.server.util.HttpProp;

/**
 * <pre> 
 * 描述：Solr分布式服务
 * 构建组：solr-client
 * 作者：徐浩文
 * 邮箱: xuhaowende@sina.cn
 * 日期:Jul 14, 2016-9:54:50 AM
 * 版权：徐浩文版权所有
 * </pre>
 */
public class CloudFactory {

	private final static Logger LOGGER = LoggerFactory.getLogger(CloudFactory.class);
	
	private CloudFactory() {}
	
	/**内部类，用于实现lzay机制 */
    private static class SingleCloudHolder{
        /** 单例变量  */
        private static CloudFactory instance = new CloudFactory();
    }
    
    /**获取单例对象实例 */
    public static CloudFactory getInstance() {
        return SingleCloudHolder.instance;
    }
    
    public void initConnection(HttpProp prop, String modules){
    	LOGGER.debug("init cloud initConnection："+modules);
    }
    
    public void restartInit(HttpProp prop, String moduleName){
    	LOGGER.debug("init cloud restartInit："+moduleName);
    }
    
	public org.apache.solr.client.solrj.SolrServer getSolrServer(String collection, HttpProp prop) {
		try {
			CloudSolrServer solrCloudServer = new CloudSolrServer(prop.getHost());
			solrCloudServer.setRequestWriter(new BinaryRequestWriter());
			solrCloudServer.setZkClientTimeout(prop.getSoTimeOut());
			solrCloudServer.setZkConnectTimeout(prop.getConnectionTimeOut());
			solrCloudServer.setParser(new XMLResponseParser());
			solrCloudServer.setDefaultCollection(collection);
			solrCloudServer.connect();
            return solrCloudServer;
		} catch (Exception ex) {
			LOGGER.error("连接"+prop.getHost()+"失败", ex);
		}
		return null;
	}

}
