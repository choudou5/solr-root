package com.choudoufu.solr.handler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.request.CollectionAdminRequest.Create;
import org.apache.solr.client.solrj.response.CollectionAdminResponse;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.cloud.SolrZkClient;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrResourceLoader;
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wltea.analyzer.dic.Dictionary;

import com.choudoufu.solr.cloud.ZkCustomController;
import com.choudoufu.solr.common.params.CustomParams;
import com.choudoufu.solr.common.params.CustomParams.CustomAction;

/**
 * 自定义 管理器
 * 
 * @author xuhaowen
 * @serial 2017-1-7
 */
public class CustomHandler extends RequestHandlerBase {

	protected static Logger log = LoggerFactory.getLogger(CustomHandler.class);

	protected final CoreContainer coreContainer;
	
	public final static String MODEL_CLOUD = "cloud";
	
	protected static final String SOLR_HOME = SolrResourceLoader.locateSolrHome();
	    
	private final SolrZkClient zkClient;
	
	public CustomHandler() {
		super();
		this.coreContainer = null;
		zkClient = null;
	}

	public CustomHandler(final CoreContainer coreContainer) {
		this.coreContainer = coreContainer;
		if(coreContainer.isZooKeeperAware()){
			zkClient = coreContainer.getZkController().getZkClient();
		}else{
			zkClient = null;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	final public void init(NamedList args) {
		throw new SolrException(
				SolrException.ErrorCode.SERVER_ERROR,
				"CustomHandler should not be configured in solrconf.xml\n" + "it is a special Handler configured directly by the RequestDispatcher");
	}

	public CoreContainer getCoreContainer() {
		return this.coreContainer;
	}

	@Override
	public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp)
			throws Exception {
		// Make sure the cores is enabled
		CoreContainer cores = getCoreContainer();
		if (cores == null) {
			throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "Core container instance missing");
		}

		// 挑选 action
		SolrParams params = req.getParams();
		CustomAction action = null;
		String actionParam = params.get(CustomParams.ACTION);
		if (actionParam != null)
			action = CustomAction.get(actionParam);
		if (action == null){
			this.handleUnknownAction(req);
			return;
		}
		handleRequestInternal(req, rsp, action);
	}

	protected void handleRequestInternal(SolrQueryRequest req,
			SolrQueryResponse rsp, CustomAction action) throws Exception {
		if (action != null) {
			switch (action) {
				case UPLOAD_CONF: {
					this.handleUploadConfAction(req, rsp);
					break;
				}
				case DELETE_ZK_CONF_FILE: {
					this.handleDeleteZkConfFileAction(req, rsp);
					break;
				}
				case RELOAD_IK_DICT: {
					this.handleReloadIkDictAction(req, rsp);
					break;
				}
				default: {
					this.handleUnknownAction(req);
					break;
				}
			}
		}
		rsp.setHttpCaching(false);
	}

	/**
	 * Handle Unknown Action.
	 */
	protected void handleUnknownAction(SolrQueryRequest req) {
		throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "Unsupported operation: "+ req.getParams().get(CustomParams.ACTION));
	}
	
	/**
	 * 删除 zk配置文件
	 * @param req
	 */
	protected void handleDeleteZkConfFileAction(SolrQueryRequest req, SolrQueryResponse rsp) {
		SolrParams params = req.getParams();
		String coreName = params.get("core");
		String fileName = params.get("fileName");
		if(StringUtils.isBlank(coreName))
			throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "core参数 不能为空！");
		if(StringUtils.isBlank(fileName))
			throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "fileName参数 不能为空！");
		
		try {
			ZkCustomController.deleteZkFile(zkClient, fileName, coreName);
		} catch (Exception e) {
			log.warn("Fail deleteZkFile coreName "+coreName+" fileName"+fileName, e);
			throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "删除zk文件失败！");
		}
	}
	
	
	protected void handleUploadConfAction(SolrQueryRequest req, SolrQueryResponse rsp) throws SolrException {
		Iterable<ContentStream> streams = req.getContentStreams();
		if(!streams.iterator().hasNext()){
			throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "上传文件 不能为空！");
		}
		ContentStream stream = streams.iterator().next();
		if(StringUtils.isBlank(stream.getName())){
			throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "上传文件 不能为空！");
		}
		if(isContainChinese(stream.getName())){
			throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "不能上传 中文文件！");
		}
		
		String confType = checkConfType(stream.getName());
		switch (confType) {
			case CustomParams.CONF_TYPE_FILE:
				handleUpLoadConfFileAction(req, rsp, stream);
				break;
			case CustomParams.CONF_TYPE_ZIP:
				handleUpLoadCoreZipAction(req, rsp, stream);
				break;
			default:
				break;
		}
	}
	
	/**
	 * 重新 加载 IK词库
	 * @param req
	 * @param rsp
	 */
	protected void handleReloadIkDictAction(SolrQueryRequest req, SolrQueryResponse rsp) {
	    try
	    {
	      Dictionary.getSingleton().reLoadExtDict();
	    } catch (Exception e){
	    	log.warn("Fail IK ReLoadExtDic", e);
			throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "重新加载IK扩展词典失败！");
	    }
	}
	
	private String checkConfType(String fileName){
		if(fileName.toLowerCase().endsWith(".zip"))
			return CustomParams.CONF_TYPE_ZIP;
		if(fileName.toLowerCase().endsWith(".txt") || fileName.toLowerCase().endsWith(".xml"))
			return CustomParams.CONF_TYPE_FILE;
		throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "不支持上传 该文件类型！");
	}
	
	
	/**
	 * 上传 zip配置 action
	 */
	private void handleUpLoadCoreZipAction(SolrQueryRequest req, SolrQueryResponse rsp, ContentStream stream) throws SolrException {
		SolrParams params = req.getParams();
		String model = params.get("model");
		String deploy = params.get("deploy");
		log.info("core create command {}", params);
		try {
			String collName = uploadZip(stream, model);
			
			//自动部署
			if("yes".equals(deploy)){
				createCollection(collName, params);
			}
		} catch (Exception e) {
			throw new SolrException(ErrorCode.SERVER_ERROR, e);
		}
	}
	
	//创建 集合
	private void createCollection(String collName, SolrParams params){
		int shards = 2;
		String shardNum = params.get("shards");
		if(StringUtils.isNotBlank(shardNum)){
			try {
				shards = Integer.parseInt(shardNum);
			} catch (NumberFormatException e) {
				log.warn("Fail parseInt shards:"+shards, e);
			}
		}
		
		try {
			String zkHost = coreContainer.getZkController().getZkServerAddress();
			CloudSolrServer server = new CloudSolrServer(zkHost);
			CollectionAdminResponse response = createCollection(collName, shards, collName, server);
			log.info("response createCollection:"+response);
			if(!response.isSuccess()){
				throw new SolrException(ErrorCode.SERVER_ERROR, "部署"+collName+"集合失败！, response:"+response);
			}
		} catch (Exception e) {
			log.warn("Fail createCollection, collName:"+collName+", shards:"+shards, e);
			throw new SolrException(ErrorCode.SERVER_ERROR, "部署"+collName+"集合失败！"+e.getMessage());
		}
	}
	
	public static CollectionAdminResponse createCollection(String name, Integer shards, String conf, SolrServer server) throws SolrServerException, IOException{
		Create req = new Create();
		req.setCollectionName(name);
		req.setRouterName("compositeId");
		req.setNumShards(shards);
		req.setConfigName(conf);
		req.setReplicationFactor(shards);
	    req.setMaxShardsPerNode(shards);
		req.setAsyncId(null);
		return req.process(server);
	}
	
	/**
	 * 上传 conf file 配置
	 */
	private void handleUpLoadConfFileAction(SolrQueryRequest req, SolrQueryResponse rsp, ContentStream stream) throws SolrException {
		SolrParams params = req.getParams();
		String coreName = params.get("core");
		String confName = params.get("confName");
		String model = params.get("model");
		log.info("core {} upload conf command {}", coreName, params);
		if(StringUtils.isBlank(coreName) && StringUtils.isBlank(confName)){
			throw new SolrException(ErrorCode.BAD_REQUEST, "请选择  core 或 填写 configName");
		}
		
		try {
			uploadConfFile(coreName, confName, stream, model);
		} catch (Exception e) {
			throw new SolrException(ErrorCode.BAD_REQUEST, e);
		}
	}

	//上传 文件
	private void uploadConfFile(String collName, String confName, ContentStream stream, String model) throws Exception {
		if(MODEL_CLOUD.equals(model)){
			if(StringUtils.isBlank(confName))
				confName = collName;
			
			log.info("uploadConfFile cloud, confName:"+confName);
			//cloud上传 到临时目录
			File file = uploadCloudTempDir(stream);
			
			
//			try {
//				ZkCustomController.deleteZkFile(zkContr.getZkClient(), fileName, coreName);
				
//			ZkCustomController zkCustom = customContainer.getZkController();
			if(!ZkCustomController.existsConfigName(zkClient, confName)){
				throw new SolrException(ErrorCode.SERVER_ERROR, "ZK configs目录下 不存在 "+confName+" 目录");
			}
			ZkCustomController.uploadConfigFile(zkClient, file, confName);
			file.delete();
			return;
		}
		
		String filePath = SOLR_HOME +"/"+collName+ "/conf/"+stream.getName();
		log.info("uploadConfFile single, filePath:"+filePath);
		File file = saveFile(filePath, stream.getStream());
		file.delete();
	}
	
	/**
	 * 上传 到临时目录
	 * @param stream
	 * @return file
	 * @throws Exception
	 */
	private File uploadCloudTempDir(ContentStream stream) throws Exception {
		String solrHome = normalizePath(SOLR_HOME);
		String solrUploadTempDir = solrHome.substring(0, solrHome.lastIndexOf("/"))+"/solrUploadDir";
		File uploadFileDir = new File(solrUploadTempDir);
		if(!uploadFileDir.exists()){
			uploadFileDir.mkdirs();
		}
		
		String filePath = solrUploadTempDir+"/"+stream.getName();
		return saveFile(filePath, stream.getStream());
	}
	
	private File saveFile(String filePath, InputStream is) throws IOException{
		File file = new File(filePath);
		FileUtils.copyInputStreamToFile(is, file);
		return file;
	}
	
	private static String normalizePath(String path) {
	    if (path == null)
	      return null;
	    path = path.replace('/', File.separatorChar);
	    path = path.replace('\\', '/');
	    return path;
	}
	
	/**
	 * 上传 zip
	 * @param stream 内容流
	 * @param model 模式：single / cloud
	 * @return collName
	 * @throws Exception
	 */
	private String uploadZip(ContentStream stream, String model) throws Exception {
		String filePath = SOLR_HOME + stream.getName();
		String collName = stream.getName().replace(".zip", "");
		File zipFile = null;
		if(MODEL_CLOUD.equals(model)){
			log.info("uploadZip cloud, collName:"+collName);
			//cloud上传 到临时目录
			zipFile = uploadCloudTempDir(stream);
			filePath = zipFile.getPath();
		}else{
			zipFile = saveFile(filePath, stream.getStream());
		}
		
		InputStream is = null;
		ZipInputStream zis = null;
		BufferedInputStream bis = null;
		try {
			is = new FileInputStream(filePath);
			zis = new ZipInputStream(is);
			bis = new BufferedInputStream(zis);
			ZipEntry entry = null;
			File file = null;
			String pPath = zipFile.getParent();
			while ((entry = zis.getNextEntry()) != null) {
				String fName = entry.getName();
				if (fName.contains("/data/") || fName.equals("core.properties") || fName.equals("README.txt")) // 排除
					continue;
				file = new File(pPath, fName);
				if (entry.isDirectory()) {
					file.mkdirs();
					continue;
				}
				writeFile(bis, file);
			}
			
		} catch (Exception e) {
			log.error("upload file fail,", e);
			throw new SolrException(ErrorCode.BAD_REQUEST, "解析 zip文件 失败！");
		}finally{
			if(bis != null) bis.close();
			if(zis != null) zis.close();
			if(is != null) is.close();
			zipFile.delete();
		}
		
		if(MODEL_CLOUD.equals(model)){
			log.info("uploadZip cloud, upload to zookeeper collName:"+collName);
			String confDir = zipFile.getParent()+"/"+collName+"/conf";
			ZkCustomController.uploadConfigDir(zkClient, new File(confDir), collName);
			FileUtils.deleteDirectory(zipFile.getParentFile());//删除文件
		}
		return collName;
	}

	private void writeFile(BufferedInputStream bis, File file){
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			int b = -1;
			while ((b = bis.read()) != -1) {
				bos.write(b);
			}
			log.info("writeFile success:"+ file.getPath());
		} catch (Exception e) {
			log.error("writeFile", e);
		}finally{
			try {
				if(bos != null) bos.close();
				if(fos != null) fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 是否 存在中文
	 * @param str
	 * @return
	 */
    public static boolean isContainChinese(String str) {
    	if(StringUtils.isBlank(str))
    		return false;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
	
	// ////////////////////// SolrInfoMBeans methods //////////////////////

	@Override
	public String getDescription() {
		return "Manage Multiple Solr Cores";
	}

	@Override
	public String getSource() {
		return null;
	}

}