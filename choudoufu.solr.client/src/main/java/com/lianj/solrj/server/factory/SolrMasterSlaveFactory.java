package com.lianj.solrj.server.factory;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lianj.solrj.server.util.HttpProp;
import com.lianj.solrj.server.util.SolrConnectPool;

/**
 * <pre>
 * 描述：主从 服务
 * 构建组：lianj-solrj
 * 作者：xuhaowen
 * 邮箱: xuhaowende@sina.cn
 * 日期:Jul 14, 2016-9:54:50 AM
 * 版权：联结 公司版权所有
 * </pre>
 */
public class SolrMasterSlaveFactory {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(SolrMasterSlaveFactory.class);
	
	private String host;
	private String useHost;//使用中的host
	private static int useMacIndex = 0;
	private static List<String> okHosts = new ArrayList<String>();
    
	private SolrMasterSlaveFactory() {}
	
	/**内部类，用于实现lzay机制 */
    private static class SingleMasterSlaveHolder{
        /** 单例变量  */
        private static SolrMasterSlaveFactory instance = new SolrMasterSlaveFactory();
    }
    
    /**获取单例对象实例 */
    public static SolrMasterSlaveFactory getInstance() {
        return SingleMasterSlaveHolder.instance;
    }
    
    public void init(String host){
    	this.host = host;
    }
    
	public SolrServer getSolrServer(String moduleName, HttpProp prop) {
		host = prop.getHost();
		SolrConnectPool pool = SolrConnectPool.getInstance();
		if(pool.isCreate(moduleName)){
			try {
				HttpSolrServer solrServer = getServer(moduleName, prop);
				//检查是否 宕机
				boolean flag = checkCrash(moduleName, solrServer);
				if(flag){
					solrServer = getServer(moduleName, prop);
				}
	            //添加 连接对象 到池子
	            pool.add(moduleName, solrServer);
				return solrServer;
			} catch (Exception ex) {
				LOGGER.error("连接"+moduleName+"失败", ex);
			}
		}
		return pool.getConnection(moduleName);
	}
    
    private HttpSolrServer getServer(String moduleName, HttpProp prop){
    	String url = "http://"+getUseHost()+"/"+moduleName;
    	HttpSolrServer solrServer = new HttpSolrServer(url);
	    solrServer.setRequestWriter(new BinaryRequestWriter());
        solrServer.setSoTimeout(prop.getSoTimeOut());
        solrServer.setConnectionTimeout(prop.getConnectionTimeOut());
        solrServer.setDefaultMaxConnectionsPerHost(prop.getMaxConnectionsPerHost());
        solrServer.setMaxTotalConnections(prop.getMaxTotalConnections());
        solrServer.setFollowRedirects(false);
        solrServer.setAllowCompression(true);
        solrServer.setMaxRetries(prop.getMaxRetries());
        solrServer.setParser(new XMLResponseParser());
        return solrServer;
    }
    
    /**
     * 检查 是否宕机
     */
    private boolean checkCrash(String moduleName, HttpSolrServer solrServer){
    	boolean flag = false;
		if(!okHosts.contains(useHost)){
			try {
				solrServer.ping();
				okHosts.add(useHost);
			} catch (Exception e) {
				//宕机切换
				if(e.getCause() instanceof ConnectException){
					LOGGER.warn(useHost+"宕机, 立即切换.");
					okHosts.remove(useHost);
					changeMasterHost();
					flag = true;
				}
			}
		}
		return flag;
    }
    
	public void initUseHost() {
		if(useHost == null && StringUtils.isNotBlank(host)){
			useHost = host.split(",")[0];
		}
	}
	
	public String getUseHost() {
		if (useHost == null) {
			initUseHost();
		}
		return useHost;
	}

	public void setUseHost(String useHost) {
		this.useHost = useHost;
	}
	
    private String[] getHosts(){
    	if(StringUtils.isNotBlank(host)){
    		String[] hostArray =  host.split(",");
    		String[] savleHosts = new String[hostArray.length-1];
    		int i = 0;
    		for (String str : hostArray) {
				if(str.equals(useHost)){
					continue;
				}
				savleHosts[i] = str;
				i++;
			}
    		return savleHosts;
    	}
    	return null;
    }

	public void changeMasterHost() {
		useMacIndex++;
		//设置 切换连接机器
		String[] hostArray = getHosts();
		if(ArrayUtils.isNotEmpty(hostArray)){
			String host = null;
			try {
				host = hostArray[useMacIndex];
			} catch (Exception e) {
				host = hostArray[0];//机器遍历完  使用默认主机
				useMacIndex = 0;
			}
			setUseHost(host);
		}
	}

	public String getHttpHost() {
		return "http://"+useHost+"/";
	}

}