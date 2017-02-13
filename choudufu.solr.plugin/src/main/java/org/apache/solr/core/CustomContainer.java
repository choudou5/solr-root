package org.apache.solr.core;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.solr.cloud.ZkController;
import org.apache.solr.cloud.ZkCustomController;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.cloud.ZooKeeperException;
import org.apache.solr.handler.admin.CustomHandler;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义 容器
 * @author xuhaowen
 * @serial 2017-1-7
 */
public class CustomContainer{

	protected static final Logger log = LoggerFactory.getLogger(CustomContainer.class);

	/** @deprecated will be remove in Solr 5.0 (SOLR-4622) */
	public static final String DEFAULT_HOST_CONTEXT = "solr";
	/** @deprecated will be remove in Solr 5.0 (SOLR-4622) */
	public static final String DEFAULT_HOST_PORT = "8983";
	
	protected ZkCustomController zkController = null;
	protected CustomHandler customHandler = null;

	protected final SolrResourceLoader loader;
	protected final String solrHome;
	protected final ConfigSolr cfg;
	

	public CustomContainer() {
		this(new SolrResourceLoader(SolrResourceLoader.locateSolrHome()));
	}

	public CustomContainer(SolrResourceLoader loader) {
		this(loader, ConfigSolr.fromSolrHome(loader, loader.getInstanceDir()));
	}

	public CustomContainer(SolrResourceLoader loader, ConfigSolr config) {
		this.loader = checkNotNull(loader);
		this.solrHome = loader.getInstanceDir();
		this.cfg = checkNotNull(config);
	}

	/**
	 * This method allows subclasses to construct a CustomContainer without any
	 * default init behavior.
	 * 
	 * @param testConstructor
	 *            pass (Object)null.
	 * @lucene.experimental
	 */
	protected CustomContainer(Object testConstructor) {
		loader = null;
		solrHome = null;
		cfg = null;
	}

	/**
    * Load the cores defined for this CoreContainer
    */
    public void load()  {
    	log.info("Loading cores into CoreContainer [instanceDir={}]", loader.getInstanceDir());
    	
    	initZooKeeper(solrHome, cfg);
    	
    	customHandler = createHandler("org.apache.solr.handler.admin.CustomHandler", CustomHandler.class);
    }
	
    
    public void initZooKeeper(String solrHome, ConfigSolr config) {
    	
	    if (config.getCoreLoadThreadCount() <= 1) {
	      throw new SolrException(SolrException.ErrorCode.SERVER_ERROR,
	          "SolrCloud requires a value of at least 2 in solr.xml for coreLoadThreads");
	    }
	
	    initZooKeeper(solrHome,
	        config.getZkHost(), config.getZkClientTimeout(), config.getZkHostPort(), config.getZkHostContext(),
	        config.getHost(), config.getLeaderVoteWait(), config.getLeaderConflictResolveWait(), config.getGenericCoreNodeNames());
	}
	public void initZooKeeper(String solrHome, String zkHost, int zkClientTimeout, String hostPort,
	        String hostContext, String host, int leaderVoteWait, int leaderConflictResolveWait, boolean genericCoreNodeNames) {
		
		ZkCustomController zkController = null;
	    
	    // if zkHost sys property is not set, we are not using ZooKeeper
	    String zookeeperHost;
	    if(zkHost == null) {
	      zookeeperHost = System.getProperty("zkHost");
	    } else {
	      zookeeperHost = zkHost;
	    }

	    String zkRun = System.getProperty("zkRun");
	    
	    if (zkRun == null && zookeeperHost == null)
	        return;  // not in zk mode


	    // BEGIN: SOLR-4622: deprecated hardcoded defaults for hostPort & hostContext
	    if (null == hostPort) {
	      // throw new ZooKeeperException(SolrException.ErrorCode.SERVER_ERROR,
	      //               "'hostPort' must be configured to run SolrCloud");
	      log.warn("Solr 'hostPort' has not be explicitly configured, using hardcoded default of " + DEFAULT_HOST_PORT + ".  This default has been deprecated and will be removed in future versions of Solr, please configure this value explicitly");
	      hostPort = DEFAULT_HOST_PORT;
	    }
	    if (null == hostContext) {
	      // throw new ZooKeeperException(SolrException.ErrorCode.SERVER_ERROR,
	      //               "'hostContext' must be configured to run SolrCloud");
	      log.warn("Solr 'hostContext' has not be explicitly configured, using hardcoded default of " + DEFAULT_HOST_CONTEXT + ".  This default has been deprecated and will be removed in future versions of Solr, please configure this value explicitly");
	      hostContext = DEFAULT_HOST_CONTEXT;
	    }
	    // END: SOLR-4622

	    // zookeeper in quorum mode currently causes a failure when trying to
	    // register log4j mbeans.  See SOLR-2369
	    // TODO: remove after updating to an slf4j based zookeeper
	    System.setProperty("zookeeper.jmx.log4j.disable", "true");

	    int zkClientConnectTimeout = 30000;
	   
	    if (zookeeperHost != null) {

	        // we are ZooKeeper enabled
	        try {
	           log.info("Zookeeper client=" + zookeeperHost);          
	          String confDir = System.getProperty("bootstrap_confdir");
	          boolean boostrapConf = Boolean.getBoolean("bootstrap_conf");  
	          
	          if(!ZkController.checkChrootPath(zookeeperHost, (confDir!=null) || boostrapConf)) {
	            throw new ZooKeeperException(SolrException.ErrorCode.SERVER_ERROR,
	                "A chroot was specified in ZkHost but the znode doesn't exist. ");
	          }
	          
	          zkController = new ZkCustomController(zookeeperHost, zkClientTimeout, zkClientConnectTimeout);

	          if(confDir != null) {
	            File dir = new File(confDir);
	            if(!dir.isDirectory()) {
	              throw new IllegalArgumentException("bootstrap_confdir must be a directory of configuration files");
	            }
	            String confName = System.getProperty(ZkController.COLLECTION_PARAM_PREFIX+ZkController.CONFIGNAME_PROP, "configuration1");
	            zkController.uploadConfigDir(dir, confName);
	          }
	          
	        } catch (InterruptedException e) {
	          // Restore the interrupted status
	          Thread.currentThread().interrupt();
	          log.error("", e);
	          throw new ZooKeeperException(SolrException.ErrorCode.SERVER_ERROR,
	              "", e);
	        } catch (TimeoutException e) {
	          log.error("Could not connect to ZooKeeper", e);
	          throw new ZooKeeperException(SolrException.ErrorCode.SERVER_ERROR,
	              "", e);
	        } catch (IOException e) {
	          log.error("", e);
	          throw new ZooKeeperException(SolrException.ErrorCode.SERVER_ERROR,
	              "", e);
	        } catch (KeeperException e) {
	          log.error("", e);
	          throw new ZooKeeperException(SolrException.ErrorCode.SERVER_ERROR,
	              "", e);
	        }
	        

	      }
	      this.zkController = zkController;
	}
	
	protected <T> T createHandler(String handlerClass, Class<T> clazz) {
		return loader.newInstance(handlerClass, clazz, null, new Class[] { CustomContainer.class }, new Object[] { this });
	}

	public CustomHandler getCustomHandler() {
		return customHandler;
	}
	
	public ZkCustomController getZkController() {
		return zkController;
	}

	private volatile boolean isShutDown = false;

	public boolean isShutDown() {
		return isShutDown;
	}

	/**
	 * Stops
	 */
	public void shutdown() {
		log.info("Shutting down CustomContainer instance="+ System.identityHashCode(this));
		isShutDown = true;
		org.apache.lucene.util.IOUtils.closeWhileHandlingException(loader); 
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			if (!isShutDown) {
				log.error("CustomContainer was not shutdown prior to finalize(), indicates a bug -- POSSIBLE RESOURCE LEAK!!!  instance="+ System.identityHashCode(this));
			}
		} finally {
			super.finalize();
		}
	}
}
