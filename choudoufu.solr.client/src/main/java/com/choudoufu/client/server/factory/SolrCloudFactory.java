package com.choudoufu.client.server.factory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.choudoufu.client.server.ISolrServerFactory;

/**
 * solr cloud模式工厂
 * @author xuhaowende
 * @date 2017年4月14日
 */
public class SolrCloudFactory implements ISolrServerFactory {

	private static final Logger logger = LoggerFactory.getLogger(SolrCloudFactory.class);

	private Map<String, CloudSolrServer> multiColl = new HashMap<String, CloudSolrServer>();

	@Override
	public void init(String zk, String collNames, int clientTimeOut,
			int connTimeOut) {
		logger.info("SolrCloudFactory 初始化开始");
		if (StringUtils.isEmpty(collNames)) {
			logger.error("collNames为空");
		} else {
			String[] collNamesArray = collNames.split(",");
			if (ArrayUtils.isNotEmpty(collNamesArray)) {
				for (String collName : collNamesArray) {
					if (StringUtils.isEmpty(collName)) {
						continue;
					}
					CloudSolrServer server = new CloudSolrServer(zk);
					server.setDefaultCollection(collName);
					server.setRequestWriter(new BinaryRequestWriter());
					server.setZkClientTimeout(clientTimeOut);
					server.setZkConnectTimeout(connTimeOut);
					server.setParser(new XMLResponseParser());
					
					server.connect();
					//zk正常连接
					if (server.getZkStateReader() != null && server.getZkStateReader().getClusterState() != null) {
						logger.info(server.getZkStateReader().getClusterState().toString());
						multiColl.put(collName, server);
					}
				}
				logger.info("SolrCloudFactory 初始化完成");
			}else{
				logger.error("SolrCloudFactory 初始化未完成, collNames为空！");
			}
		}
	}
	
	@Override
	public void init(String httpUrl, String collNames, int soTimeOut,
			int connTimeOut, int poolSize, int maxTotalConnSize,
			int maxRetries) {
		// TODO Auto-generated method stub
	}
	

	@Override
	public SolrServer getSolrServer(String collName) {
		if (multiColl != null && multiColl.size() > 0) {
			SolrServer coreServer = multiColl.get(collName);
			if (coreServer == null) {
				logger.error("collName:" + collName + "不存在");
			} else {
				return coreServer;
			}
		}
		logger.error("SolrCloudFactory solrServer不存在:" + collName);
		return null;
	}

	@Override
	public void destroy() {
		logger.info("SolrCloudFactory 关闭开始");
		if (MapUtils.isNotEmpty(multiColl)) {
			Iterator<CloudSolrServer> solrServerIte = multiColl.values().iterator();
			while (solrServerIte.hasNext()) {
				SolrServer solrServer = solrServerIte.next();
				if (solrServer != null) {
					solrServer.shutdown();
					solrServer = null;
				}
			}
			multiColl.clear();
		}
		logger.info("SolrCloudFactory 关闭完成");
	}

	@Override
	public int checkCrash(String collName, Exception e) {
		return -1;
	}

}
