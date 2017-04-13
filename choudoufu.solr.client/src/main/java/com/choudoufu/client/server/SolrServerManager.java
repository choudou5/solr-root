package com.choudoufu.client.server;

import com.choudoufu.client.server.factory.SolrCloudFactory;
import com.choudoufu.client.server.factory.SolrMasterSlaveFactory2;
import com.choudoufu.client.server.factory.SolrStandAloneFactory;

/**
 * solr服务管理器
 * @author xuhaowende
 * @date 2017年4月14日
 */
public class SolrServerManager {

	
	public static final int DEF_SO_TIME_OUT = 10000;
	public static final int DEF_CONN_TIME_OUT = 10000;
	public static final int DEF_POOL_SIZE = 2;
	public static final int DEF_MAX_TOTAL_CONN_SIZE = 1000;
	public static final int DEF_MAX_RETRIES = 1;
	
	private String solrModel;
	private String host;
	private String collNames;
	private int soTimeOut = DEF_SO_TIME_OUT;//读取超时
	private int connTimeOut = DEF_CONN_TIME_OUT;//连接超时
	private int poolSize = DEF_POOL_SIZE; //连接池个数（针对单个集合）
	private int maxTotalConnSize = DEF_MAX_TOTAL_CONN_SIZE; //最大总连接数（针对 httpClient）
	private int maxRetries = DEF_MAX_RETRIES;//最大重试次数
	
	private ISolrServerFactory solrFactory;
	
	public void init() {
	   //分布式
       if("cloud".equals(solrModel)){
    	   solrFactory = new SolrCloudFactory();
    	   solrFactory.init(host, collNames, soTimeOut, connTimeOut);
       }
       //单机
       else if("single".equals(solrModel)){
    	   solrFactory = new SolrStandAloneFactory();
    	   solrFactory.init(host, collNames, soTimeOut, connTimeOut, poolSize, maxTotalConnSize, maxRetries);
       }
       //主从 模式
       else if("master_slave".equals(solrModel)){
    	   solrFactory = new SolrMasterSlaveFactory2();
    	   solrFactory.init(host, collNames, soTimeOut, connTimeOut, poolSize, maxTotalConnSize, maxRetries);
       }
       
    }

	/**
	 * 获得 solr服务
	 * @param collName 集合名
	 * @return
	 */
    public org.apache.solr.client.solrj.SolrServer getSolrServer(String collName) {
        return solrFactory.getSolrServer(collName);
    }

    public void destroy() {
    	solrFactory.destroy();
    }

    
	public String getSolrModel() {
		return solrModel;
	}

	public void setSolrModel(String solrModel) {
		this.solrModel = solrModel;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setCollNames(String collNames) {
		this.collNames = collNames;
	}

	public void setSoTimeOut(int soTimeOut) {
		this.soTimeOut = soTimeOut;
	}

	public void setConnTimeOut(int connTimeOut) {
		this.connTimeOut = connTimeOut;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public void setMaxTotalConnSize(int maxTotalConnSize) {
		this.maxTotalConnSize = maxTotalConnSize;
	}

	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}

}
