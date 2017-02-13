package com.lianj.solrj.server.factory;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lianj.solrj.server.util.HttpProp;
import com.lianj.solrj.server.util.SolrConnectPool;

/**
 * <pre> 
 * 描述：Solr分布式服务
 * 构建组：lianj-solrj
 * 作者：徐浩文
 * 邮箱: xuhaowende@sina.cn
 * 日期:Jul 14, 2016-9:54:50 AM
 * 版权：联结 公司版权所有
 * </pre>
 */
public class CloudFactory {

	private final static Logger LOGGER = LoggerFactory.getLogger(CloudFactory.class);
	
	private int INIT_SIZE = 3;
	
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
    	if(StringUtils.isBlank(modules)){
    		LOGGER.error("initConnection params: modules is null");
    		return;
    	}
    	prop.getMaxPoolSize();
    	String[] moduleArray = modules.split(",");
    	for (String module : moduleArray) {
    		batchInit(module, prop);//批量初始化
		}
    }
    
    public void restartInit(HttpProp prop, String moduleName){
    	LOGGER.debug("init cloud restartInit："+moduleName);
    	SolrConnectPool pool = SolrConnectPool.getInstance();
    	pool.remove(moduleName);
    	batchInit(moduleName, prop);//批量初始化
    }
    
    private void batchInit(String collection, HttpProp prop){
    	for (int i = 0; i < INIT_SIZE; i++) {
			getSolrServer(collection, prop);
		}
    }
	
	public org.apache.solr.client.solrj.SolrServer getSolrServer(String collection, HttpProp prop) {
		SolrConnectPool pool = SolrConnectPool.getInstance();
		pool.setCloudModel(true);
		if(pool.isCreate(collection)){
			CloudSolrServer solrCloudServer = new CloudSolrServer(prop.getHost());
			try {
				solrCloudServer.setRequestWriter(new BinaryRequestWriter());
				solrCloudServer.setZkClientTimeout(prop.getSoTimeOut());
				solrCloudServer.setZkConnectTimeout(prop.getConnectionTimeOut());
				solrCloudServer.setParser(new XMLResponseParser());
				solrCloudServer.setDefaultCollection(collection);
				solrCloudServer.connect();
				 //添加 连接对象 到池子
	            pool.add(collection, solrCloudServer);
	            return solrCloudServer;
			} catch (Exception ex) {
				LOGGER.error("连接"+prop.getHost()+"失败", ex);
			}
		}
		return pool.getConnection(collection);
	}

}
