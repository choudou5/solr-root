package com.choudoufu.client.server.factory;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.choudoufu.client.server.ISolrServerFactory;
import com.choudoufu.client.server.SolrServerManager;

/**
 * 主从 模式 工厂
 * @author xuhaowende
 * @date 2017年4月14日
 */
public class SolrMasterSlaveFactory2 implements ISolrServerFactory{
	
	private static final Logger logger = LoggerFactory.getLogger(SolrMasterSlaveFactory2.class);

	private Map<String, List<HttpSolrServer>> poolColl = new HashMap<String, List<HttpSolrServer>>(10);
	
	
	private List<String> okUrls = new ArrayList<String>(3);//正常机器
	private List<String> failUrls = new ArrayList<String>(3);//异常机器
	private int useUrlIndex;//使用中的下标
	
	@Override
	public void init(String zk, String collNames, int clientTimeOut,
			int connTimeOut) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void init(String httpUrls, String collNames, int soTimeOut,
			int connTimeOut, int poolSize, int maxTotalConnSize, int maxRetries) {
		logger.info("SolrMasterSlaveFactory 初始化开始");
		if (StringUtils.isEmpty(collNames)) {
			logger.error("collNames为空");
		} else {
			
			String[] httpUrlArray = httpUrls.split(",");//首个为主机，其后为从机
			if(ArrayUtils.isNotEmpty(httpUrlArray)){
				int tempMainHttpUrlIndex = -1;
				int httpUrlIndex = 0;
				for (String httpUrl : httpUrlArray) {
					String[] collNamesArray = collNames.split(",");
					if (ArrayUtils.isNotEmpty(collNamesArray)) {
						for (String collName : collNamesArray) {
							if (StringUtils.isEmpty(collName)) {
								continue;
							}
							
							//集合池
							List<HttpSolrServer> collPoll = new ArrayList<HttpSolrServer>(poolSize);
							for (int i = 0; i < poolSize; i++) {
								HttpSolrServer server = buildHttpSolrServer(httpUrl, collName, soTimeOut, connTimeOut, maxTotalConnSize, maxRetries);
								if(server != null && (httpUrlIndex==0 || collPoll.size() != poolSize)){
									collPoll.add(server);
									if(!okUrls.contains(httpUrl)){
										okUrls.add(httpUrl);
									}
									if(tempMainHttpUrlIndex == -1){
										tempMainHttpUrlIndex = httpUrlIndex;
										useUrlIndex = httpUrlIndex;
									}
								}
								server = null;
							}
							poolColl.put(collName, collPoll);
							collPoll = null;
						}
						logger.info("SolrMasterSlaveFactory 初始化完成");
					}else{
						logger.error("SolrMasterSlaveFactory 初始化未完成, collNames为空！");
					}
					httpUrlIndex++;
				}
				
				//未有可用机器
				if(tempMainHttpUrlIndex == -1){
					useUrlIndex = -1;
				}
			}else{
				logger.error("SolrMasterSlaveFactory 初始化未完成, httpUrls为空！");
			}
		}
	}
	
	//构建 solr连接
	private HttpSolrServer buildHttpSolrServer(String httpUrl, String collName, int soTimeOut,
			int connTimeOut, int maxTotalConnSize, int maxRetries){
		HttpSolrServer server = new HttpSolrServer(httpUrl+collName);
		server.setRequestWriter(new BinaryRequestWriter());
		server.setSoTimeout(soTimeOut);
		server.setConnectionTimeout(connTimeOut);
		server.setDefaultMaxConnectionsPerHost(1000);
		server.setMaxTotalConnections(maxTotalConnSize);
		server.setFollowRedirects(false);
		server.setAllowCompression(true);
		server.setMaxRetries(maxRetries);
		server.setParser(new XMLResponseParser());
		try {
			SolrPingResponse ping = server.ping();
			if (ping.getStatus() == 200) {
				logger.info("ping {} status:{}, qTime:{};", collName, ping.getStatus(), ping.getQTime());
				return server;
			}else{
				logger.info("ping {} fail status:{}, qTime:{};", collName, ping.getStatus(), ping.getQTime());
			}
		} catch (Exception e) {
			logger.error("SolrMasterSlaveFactory ping {} fail.", collName, e);
		}
		return null;
	}

	@Override
	public SolrServer getSolrServer(String collName) {
		if (MapUtils.isNotEmpty(poolColl)) {
			List<HttpSolrServer> collPoll = poolColl.get(collName);
			if (collPoll == null) {
				logger.error("collName:" + collName + " 无可用 连接.");
			} else {
				//负载均衡
				long time = System.currentTimeMillis();
				int index = (int) (time%collPoll.size());
				return collPoll.get(index);
			}
		}
		logger.error("SolrMasterSlaveFactory solrServer不存在:" + collName);
		return null;
	}

	@Override
	public void destroy() {
		logger.info("SolrStandAloneFactory 关闭开始");
		if (MapUtils.isNotEmpty(poolColl)) {
			Iterator<List<HttpSolrServer>> solrServerIte = poolColl.values().iterator();
			while (solrServerIte.hasNext()) {
				List<HttpSolrServer> collPool = solrServerIte.next();
				if(CollectionUtils.isNotEmpty(collPool)){
					for (HttpSolrServer solrServer : collPool) {
						if (solrServer != null) {
							solrServer.shutdown();
							solrServer = null;
						}
					}
				}
				collPool = null;
			}
			poolColl.clear();
		}
		logger.info("SolrMasterSlaveFactory 关闭完成");
	}

	@Override
    public int checkCrash(String collName, Exception e){
    	//宕机切换
		if(e.getCause() instanceof ConnectException){
			logger.warn("连接 "+collName+" 出现宕机, 立即切换！");
			failUrls.add(okUrls.get(useUrlIndex));
			okUrls.remove(useUrlIndex);
			//切换新可用机器
			if(okUrls.size() > 0){
				if(useUrlIndex == okUrls.size()){
					useUrlIndex = 0;
				}else{
					useUrlIndex++;
				}
			}else{
				//无可用机器，尝试重新连接 失败机器
				logger.info("连接 "+collName+" 宕机， 暂无可用机器， 尝试重新连接 异常机器.");
				if(CollectionUtils.isNotEmpty(failUrls)){
					init(StringUtils.join(failUrls, ","), collName, SolrServerManager.DEF_SO_TIME_OUT, SolrServerManager.DEF_CONN_TIME_OUT, SolrServerManager.DEF_POOL_SIZE, SolrServerManager.DEF_MAX_TOTAL_CONN_SIZE, SolrServerManager.DEF_MAX_RETRIES);
				}
			}
			return useUrlIndex;
		}
		return -1;
    }
	
	/**
	 * 负载均衡测试
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		int poolSize = 3;
		List<String> collPoll = genList(poolSize);
		for (int i = 1; i < poolSize*1000; i++) {
			if (collPoll == null) {
				logger.error("collPoll 不存在");
			} else {
				//负载均衡
				long time = System.currentTimeMillis();
				int index = (int) (time%collPoll.size());
				System.out.println(collPoll.get(index)+"----index:"+index);
			}
			int sleep = (int)Math.abs(Math.random()*100);
			if(i%10 == 0){
				System.out.println("sleep:"+sleep);
				Thread.sleep(sleep);
			}
		}
	}
	
	private static List<String> genList(int loop){
		List<String> collPoll = new ArrayList<String>(11);
		for (int i = 0; i < loop; i++) {
			collPoll.add("模块"+i);
		}
		return collPoll;
	}

}