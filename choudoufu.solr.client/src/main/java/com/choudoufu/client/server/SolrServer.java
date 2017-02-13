package com.choudoufu.client.server;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.choudoufu.client.server.factory.CloudFactory;
import com.choudoufu.client.server.factory.SingleFactory;
import com.choudoufu.client.server.factory.SolrMasterSlaveFactory;
import com.choudoufu.client.server.util.HttpProp;
import com.choudoufu.client.server.util.SolrConnectPool;
import com.choudoufu.client.util.SolrUtil.ServerType;

/**
 * <pre> 
 * 描述：Solr服务连接类
 * 构建组：solr-client
 * 作者：徐浩文
 * 邮箱: xuhaowende@sina.cn
 * 日期:Jun 19, 2015-4:44:43 PM
 * 版权：徐浩文版权所有
 * </pre>
 */
public class SolrServer {
	
	protected final static Logger LOGGER = LoggerFactory.getLogger(SolrServer.class);
	
	private String solrModel;//模式 single=单机,cloud=分布式,master_slave=主从
	private String host;
	private String initConnectionModules; //初始连接 模块
	private int maxPoolSize;
	private long checkExpirePeriod = 60000;//检查过期周期
	private Integer soTimeOut = 10000;
    private Integer connectionTimeOut = 10000;
    private Integer maxConnectionsPerHost = 1000;
    private Integer maxTotalConnections = 1000;
    private Integer maxRetries = 1;
    
    public SolrServer(){}
    
    /**
     * @param host
     * @param solrModel single=单机,cloud=分布式,master_slave=主从
     */
    public SolrServer(String host, ServerType solrModel){
    	this.host = host;
    	this.solrModel = solrModel.name().toLowerCase();
    }
    
    //Bean在实例化的过程中：Constructor > @PostConstruct >InitializingBean > init-method
    //Bean在销毁的过程中：@PreDestroy > DisposableBean > destroy-method
    @PostConstruct
    public void init(){
    	LOGGER.debug("init solrServer...");
    	//初始化 连接池信息
    	SolrConnectPool.initialized(maxPoolSize, checkExpirePeriod);
    	
    	if("cloud".equals(solrModel)){
    		HttpProp prop = new HttpProp(host, maxPoolSize, checkExpirePeriod, soTimeOut, connectionTimeOut, maxConnectionsPerHost, maxTotalConnections, maxRetries);
			CloudFactory.getInstance().initConnection(prop, initConnectionModules);
		}else if("master_slave".equals(solrModel)){
    		SolrMasterSlaveFactory.getInstance().init(host);
		}
    }
    
    /**
     * 获得 solr服务
     * @param moduleName 模块名
     * @return
     */
	public org.apache.solr.client.solrj.SolrServer getSolrServer(String moduleName) {
		HttpProp prop = new HttpProp(host, maxPoolSize, checkExpirePeriod, soTimeOut, connectionTimeOut, maxConnectionsPerHost, maxTotalConnections, maxRetries);
		if("cloud".equals(solrModel)){
			return CloudFactory.getInstance().getSolrServer(moduleName, prop);
		}else if("master_slave".equals(solrModel)){
			return SolrMasterSlaveFactory.getInstance().getSolrServer(moduleName, prop);
		}else{
			return SingleFactory.getInstance().getSolrServer(moduleName, prop);
		}
	}

	public String getSolrModel() {
		return solrModel;
	}

	public void setSolrModel(String solrModel) {
		this.solrModel = solrModel;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
	
	public String getInitConnectionModules() {
		return initConnectionModules;
	}

	public void setInitConnectionModules(String initConnectionModules) {
		this.initConnectionModules = initConnectionModules;
	}

	public Integer getSoTimeOut() {
		return soTimeOut;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public long getCheckExpirePeriod() {
		return checkExpirePeriod;
	}

	public void setCheckExpirePeriod(long checkExpirePeriod) {
		this.checkExpirePeriod = checkExpirePeriod;
	}

	public void setSoTimeOut(Integer soTimeOut) {
		this.soTimeOut = soTimeOut;
	}

	public Integer getConnectionTimeOut() {
		return connectionTimeOut;
	}

	public void setConnectionTimeOut(Integer connectionTimeOut) {
		this.connectionTimeOut = connectionTimeOut;
	}

	public Integer getMaxConnectionsPerHost() {
		return maxConnectionsPerHost;
	}

	public void setMaxConnectionsPerHost(Integer maxConnectionsPerHost) {
		this.maxConnectionsPerHost = maxConnectionsPerHost;
	}

	public Integer getMaxTotalConnections() {
		return maxTotalConnections;
	}

	public void setMaxTotalConnections(Integer maxTotalConnections) {
		this.maxTotalConnections = maxTotalConnections;
	}

	public Integer getMaxRetries() {
		return maxRetries;
	}

	public void setMaxRetries(Integer maxRetries) {
		this.maxRetries = maxRetries;
	}

	public void changeMasterHost(String moduleName) {
		if("cloud".equals(solrModel)){
			HttpProp prop = new HttpProp(host, maxPoolSize, checkExpirePeriod, soTimeOut, connectionTimeOut, maxConnectionsPerHost, maxTotalConnections, maxRetries);
			CloudFactory.getInstance().restartInit(prop, moduleName);
		}else if("master_slave".equals(solrModel)){
			SolrMasterSlaveFactory.getInstance().changeMasterHost();
		}
	}
	
	public String getHttpHost() {
		if("cloud".equals(solrModel)){
			return null;
		}else if("master_slave".equals(solrModel)){
			return SolrMasterSlaveFactory.getInstance().getHttpHost();
		}else{
			return host;
		}
	}
	
}
