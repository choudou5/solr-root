package com.choudoufu.solr.cloud;

import java.io.File;
import java.io.IOException;

import org.apache.solr.cloud.ZkController;
import org.apache.solr.common.cloud.SolrZkClient;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ZkCustomController {
	
	private static Logger log = LoggerFactory.getLogger(ZkCustomController.class);
	

	public static final String CONFIGS_ZKNODE = "/configs";
	public final static String COLLECTION_PARAM_PREFIX = "collection.";
	public final static String CONFIGNAME_PROP = "configName";
	

	public static String getZkConfigsPath(String configName){
		configName = getCollName(configName);
		return ZkController.CONFIGS_ZKNODE + "/"+ configName;
	}

	/**
	 * 是否 存在 配置目录
	 * @param configName
	 * @return
	 */
	public static boolean existsConfigName(final SolrZkClient zkClient, String configName) throws KeeperException, InterruptedException {
		String zkPath = getZkConfigsPath(configName);
		return zkClient.exists(zkPath, false);
	}
	
	public static void uploadConfigFile(final SolrZkClient zkClient, File file, String configName) throws IOException, KeeperException, InterruptedException {
		String zkPath = getZkConfigsPath(configName);
		if (!file.getName().startsWith(".")) {
	        if (!file.isDirectory()) {
	          log.info("zkClient.makePath fileName:{}, zkPath:{};", file.getName(), zkPath);
	          zkClient.makePath(zkPath + "/" + file.getName(), file, false, true);
	        }
	    }
	}
	
	public static void uploadConfigDir(final SolrZkClient zkClient, File dir, String configName) throws IOException, KeeperException, InterruptedException {
		uploadToZK(zkClient, dir, getZkConfigsPath(configName));
	}
	
	public static void uploadToZK(SolrZkClient zkClient, File dir, String zkPath) throws IOException, KeeperException, InterruptedException {
		log.info("uploadToZK dir:{}, zkPath:{};", dir.getPath(), zkPath);
		File[] files = dir.listFiles();
	    if (files == null) {
	      throw new IllegalArgumentException("Illegal directory: " + dir);
	    }
	    for(File file : files) {
	      if (!file.getName().startsWith(".")) {
	        if (!file.isDirectory()) {
	          log.info("zkClient.makePath fileName:{}, zkPath:{};", file.getName(), zkPath);
	          zkClient.makePath(zkPath + "/" + file.getName(), file, false, true);
	        } else {
	          uploadToZK(zkClient, file, zkPath + "/" + file.getName());
	        }
	      }
	    }
	}
	
	/**
	 * 删除 zk文件
	 * @param fileName
	 * @param configName
	 */
	public static void deleteZkFile(final SolrZkClient zkClient, String fileName, String configName) {
		String zkPath = getZkConfigsPath(configName);
		log.info("delteZKFile fileName:{}, zkPath:{};", fileName, zkPath);
        try {
          zkClient.delete(zkPath+"/"+fileName, -1, false);
        } catch (Exception justLogIt) {
          log.warn("Failed to delete zkPath "+zkPath+" fileName "+fileName+" due to: "+justLogIt);
        }
	}
	
	public static String getCollName(String collName){
		if(collName.contains("_shard")){
			collName = collName.split("_shard")[0];
		}
		return collName;
	}
	
}