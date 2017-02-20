package com.choudoufu.client.service.impl;

import java.net.ConnectException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.choudoufu.client.model.SolrBaseModel;
import com.choudoufu.client.model.DataImport.Command;
import com.choudoufu.client.page.Page;
import com.choudoufu.client.service.SolrService;
import com.choudoufu.client.util.HttpClientUtil;
import com.choudoufu.client.util.SolrUtil;
import com.choudoufu.client.util.SolrUtil.ServerType;

/**
 * 描述：Solr API 接口实现类
 * 构建组：solr-client
 * 作者：徐浩文
 * 邮箱: xuhaowende@sina.cn
 * 日期:Jun 18, 2015-1:50:01 PM
 * 版权：徐浩文版权所有
 * </pre>
 */
public class SolrServiceImpl implements SolrService {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(SolrServiceImpl.class);
	
	static ExecutorService EXECUTOR = Executors.newFixedThreadPool(100);
	
	private com.choudoufu.client.server.SolrServer solrServer;

	public SolrServiceImpl(com.choudoufu.client.server.SolrServer solrServer) {
		this.solrServer = solrServer;
	}
	
	/**
	 * 根据ID 单个查询
	 * @param moduleName solr模块请求地址
	 * @param id 主键ID
	 * @param objectClass solr模块对象
	 * @return T
	 * @exception 
	 * @since  1.0.0
	 */
	@Override
	public <T extends SolrBaseModel> T findById(String moduleName, String id,
			Class<T> objectClass) {
		SolrQuery query = new SolrQuery("id:"+id);
		List<T> list = query(moduleName, query, objectClass);
		if(list != null && list.size() > 0)
			return list.get(0);
		return null;
	}
	
	/**
	 * 根据ID 批量查询
	 * @param moduleName solr模块请求地址
	 * @param ids 主键ID集合
	 * @param objectClass solr模块对象
	 * @return List<T>
	 * @exception 
	 * @since  1.0.0
	 */
	@Override
	public <T extends SolrBaseModel> List<T> findByIds(String moduleName,
			List<String> ids, Class<T> objectClass) {
		StringBuilder str = new StringBuilder();
		for (String id : ids) {
			str.append("id:" + id + " OR ");
		}
		SolrQuery query = new SolrQuery(str.substring(0, str.length()-4));
		return query(moduleName, query, objectClass);
	}
	
   /**
    * 获得 Query响应对象，
    * @param moduleName solr模块请求地址
    * @param query
    * @return QueryResponse {.getResults()=doc结果集，.getBeans(classType)=对象结果集，.getHighlighting()=高亮结果集, .getFacetFields()=分片结果集}
    * @exception 
    * @since  1.0.0
    */
	@Override
	public QueryResponse query(String moduleName, SolrQuery query) {
		SolrServer server = solrServer.getSolrServer(moduleName);
		try {
			return server.query(query);
		} catch (SolrServerException e) {
			LOGGER.error("get QueryResponse fail;", e);
			
			//重试机制
			boolean retry = crashRecord(moduleName,e);
			if(retry){
				LOGGER.debug("solr server is crash, on retry.");
				server = solrServer.getSolrServer(moduleName);
				try {
					return server.query(query);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		return null;
	}
	
	 /**
	   * 查询
	   * @param moduleName solr模块请求地址
	   * @param keyword 关键字/ query语法
	   * @param objectClass solr模块对象
	   * @param fields （非必填） 返回的字段  , 默认全部
	   * @return List<T>
	   * @exception 
	   * @since  1.0.0
	   */
	  @Override
	  public <T extends SolrBaseModel> List<T> query(String moduleName, String keyword, Class<T> objectClass, String... fields){
		  return query(moduleName, keyword, objectClass, null, null, null, fields);
	  }
	  
	  /**
	   * 分页查询
	   * @param moduleName solr模块请求地址
	   * @param keyword 关键字/ query语法
	   * @param objectClass solr模块对象
	   * @param pageNo 页码数
	   * @param pageSize  返回条数
	   * @param fields （非必填） 返回的字段  , 默认全部
	   * @return List<T>
	   * @exception 
	   * @since  1.0.0
	   */
	  @Override
	  public <T extends SolrBaseModel> List<T> query(String moduleName, String keyword, Class<T> objectClass, Integer pageNo, Integer pageSize, LinkedHashMap<String, ORDER> sortMap, String... fields){
		  if(StringUtils.isBlank(keyword))
			  return null;
		  SolrQuery query = SolrUtil.parseQueryStr(keyword, pageNo, pageSize, sortMap, fields);
		  return getListResult(moduleName, query, objectClass);
	  }

	  /**
	   * 自定义 query查询
	   * @param moduleName solr模块请求地址
	   * @param query solr查询对象
	   * @param objectClass solr模块对象
	   * @return List<T>
	   * @exception 
	   * @since  1.0.0
	   */
	  @Override
	  public <T extends SolrBaseModel> List<T> query(String moduleName, SolrQuery query, Class<T> objectClass){
		  return getListResult(moduleName, query, objectClass);
	  }

      private <T extends SolrBaseModel> List<T> getListResult(String moduleName, SolrQuery query, Class<T> objectClass){
    	  SolrServer server = solrServer.getSolrServer(moduleName);
		try {
			if(server == null)
				  return null;
			
			QueryResponse response = server.query(query);
			SolrDocumentList docList = response.getResults();
			//是否 高亮
			isHighLighting(query, response, docList);
			DocumentObjectBinder binder = new DocumentObjectBinder();
			return binder.getBeans(objectClass, docList);
		} catch (SolrServerException e) {
			LOGGER.error("solr query fail, queryStr:{};", new Object[]{query.toString(), e});
			
			//重试机制
			boolean retry = crashRecord(moduleName,e);
			if(retry){
				LOGGER.debug("solr server is crash, on retry.");
				server = solrServer.getSolrServer(moduleName);
				try {
					QueryResponse response = server.query(query);
					SolrDocumentList docList = response.getResults();
					//是否 高亮
					isHighLighting(query, response, docList);
					DocumentObjectBinder binder = new DocumentObjectBinder();
					return binder.getBeans(objectClass, docList);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			return null;
		}
	  }
	  
      /**
       * 包装 高亮数据
       * @param docList
       * @param pkId 主键ID
       * @param hlField 高亮字段
       * @param hlValues  高亮后的值
       * @return SolrDocumentList
       * @exception 
       * @since  1.0.0
       */
      @SuppressWarnings("unchecked")
	  private void warpHighLighting(SolrDocumentList docList, String pkId, String hlField, List<String> hlValues, String hl_simple_pre, String hl_simple_post){
		  for (SolrDocument doc : docList) {
			  String id  = (String)doc.get("id");
			  if(pkId.equals(id)){
				  //拿到 高亮字段 原值
				  Object hlFieldOriginalVal = doc.get(hlField);
				  
				  //原值为集合时
				  if(hlFieldOriginalVal instanceof Collection) {
					  List<Object> array = (List<Object>)hlFieldOriginalVal;
					  for (String child : hlValues) {
						 //替换 高亮值的 前后描述
						 String hlVal = child.replace(hl_simple_pre, "").replace(hl_simple_post, "");
						 int dex = array.indexOf(hlVal);
						 if(dex != -1){
							 array.set(dex, child);
						 }
					  }
		    	  }else{
		    		  doc.setField(hlField, hlValues.get(0));
		    	  }
			  }
		  }
       }
      
      /**
       * 是否 高亮
       * @param query
       * @param response
       * @param docList 
       * @exception 
       * @since  1.0.0
       */
      private void isHighLighting(SolrQuery query, QueryResponse response, SolrDocumentList docList){
    	  String hl = query.get("hl");//是否开启高亮
    	  String hlFieldStr = query.get("hl.fl");//高亮字段
    	  String hl_simple_pre = query.get("hl.simple.pre");//高亮 前缀
    	  String hl_simple_post = query.get("hl.simple.post");//高亮 后缀
			if("true".equals(hl) && StringUtils.isNotBlank(hlFieldStr) 
					&& docList != null && docList.size() > 0){
				String[] hlFields = hlFieldStr.split(",");
				//获得 高亮数据
				Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
				if(highlighting != null && highlighting.size() > 0){
					//遍历高亮数据
					for (String id : highlighting.keySet()) {
						//ID对应的 高亮记录
						Map<String, List<String>> hlResult = highlighting.get(id);
						if(hlResult != null && hlResult.size() > 0){
							//为设置高亮的字段 重新设值
							for (String hlField : hlFields) {
								List<String> hlValues = hlResult.get(hlField);//高亮字段 值（为list 是因为 有可能该字段存放的是 list）
								if(hlValues != null){
									//包装 高亮数据
									warpHighLighting(docList, id, hlField, hlValues, hl_simple_pre, hl_simple_post);
								}
							}
						}
					}
				}
			}
      }
      
      
	  private <T extends SolrBaseModel> Page<T> getPageResult(String moduleName, SolrQuery query, Class<T> objectClass, Integer pageNo, Integer pageSize){
		  Page<T> page = new Page<T>(pageSize, pageNo);
		  SolrServer server = solrServer.getSolrServer(moduleName);
		  try {
			   if(server == null)
				  return null;
			  
				QueryResponse response = server.query(query);
				SolrDocumentList docList = response.getResults();
				//是否 高亮
				isHighLighting(query, response, docList);
				DocumentObjectBinder binder = new DocumentObjectBinder();
				page.setResult(binder.getBeans(objectClass, docList));
				page.setFacets(response.getFacetFields());
				page.setTotalCount(docList.getNumFound());
			} catch (SolrServerException e) {
				LOGGER.error("solr query fail, queryStr:{};", new Object[]{query.toString(), e});
				
				boolean retry = crashRecord(moduleName,e);
				if(retry){//重试机制
					LOGGER.debug("solr server is crash, on retry.");
					server = solrServer.getSolrServer(moduleName);
					try {
						QueryResponse response = server.query(query);
						SolrDocumentList docList = response.getResults();
						//是否 高亮
						isHighLighting(query, response, docList);
						DocumentObjectBinder binder = new DocumentObjectBinder();
						page.setResult(binder.getBeans(objectClass, docList));
						page.setFacets(response.getFacetFields());
						page.setTotalCount(docList.getNumFound());
						page.setqTime(response.getQTime());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		  	return page;
	  }
	  
	  /**
	   * 分页 查询
	   * @param moduleName solr模块请求地址
	   * @param keyword 关键字/ query语法
	   * @param objectClass solr模块对象
	   * @param fields （非必填） 返回的字段  , 默认全部
	   * @return Page<T> 分页对象
	   * @exception 
	   * @since  1.0.0
	   */
	  @Override
	  public <T extends SolrBaseModel> Page<T> queryPage(String moduleName, String keyword, Class<T> objectClass, String... fields){
		  return queryPage(moduleName, keyword, objectClass, null, null, null, fields);
	  }
	  
	  /**
	   * 分页查询
	   * @param moduleName solr模块请求地址
	   * @param keyword 关键字/ query语法
	   * @param objectClass solr模块对象
	   * @param pageNo 页码数
	   * @param pageSize  返回条数
	   * @param fields （非必填） 返回的字段  , 默认全部
	   * @return Page<T> 分页对象
	   * @exception 
	   * @since  1.0.0
	   */
	  @Override
	  public <T extends SolrBaseModel> Page<T> queryPage(String moduleName, String keyword, Class<T> objectClass, Integer pageNo, Integer pageSize, LinkedHashMap<String, ORDER> sortMap, String... fields){
		  if(StringUtils.isBlank(keyword))
			  return null;
		  SolrQuery query = SolrUtil.parseQueryStr(keyword, pageNo, pageSize, sortMap, fields);
		  return getPageResult(moduleName, query, objectClass, pageNo, pageSize);
	  }

	  /**
	   * 自定义 query查询
	   * @param moduleName solr模块请求地址
	   * @param query solr查询对象
	   * @param objectClass solr模块对象
	   * @return Page<T> 分页对象
	   * @exception 
	   * @since  1.0.0
	   */
	  @Override
	  public <T extends SolrBaseModel> Page<T> queryPage(String moduleName, SolrQuery query, Class<T> objectClass){
		  int pageNo = (query.getStart()/(query.getRows()<1?10:query.getRows()))+1;
		  return getPageResult(moduleName, query, objectClass, pageNo, query.getRows());
	  }
	  
	  /**
	   * 添加索引数据
	   * @param moduleName solr模块请求地址
	   * @param obj solr模块对象
	   * @exception 
	   * @since  1.0.0
	   */
	  @Override
	  public <T extends SolrBaseModel> boolean addObject(String moduleName, T obj){
		  SolrServer server = solrServer.getSolrServer(moduleName);
		  if(server == null)
			  return false;
		  try {
				server.addBean(obj);
				server.commit(false, false);
			} catch (Exception e) {
				//回滚
				try {server.rollback();} catch (Exception e1) {}
				LOGGER.error("solr addObject fail, obj:{};", new Object[]{obj, e});
				
				//重试机制
				boolean retry = crashRecord(moduleName,e);
				if(retry){
					LOGGER.debug("solr server is crash, on retry.");
					server = solrServer.getSolrServer(moduleName);
					try {
						server.addBean(obj);
						server.commit(false, false);
						return true;
					} catch (Exception e1) {
						try {server.rollback();} catch (Exception e2) {}
						e1.printStackTrace();
					}
				}
				return false;
			}
		  return true;
	  }
	  
	  /**
	   * [异步] 添加索引数据
	   * @param moduleName solr模块请求地址
	   * @param obj solr模块对象
	   * @exception 
	   * @since  1.0.0
	   */
	  @Override
	  public <T extends SolrBaseModel> void addObjectByAsync(final String moduleName,
		  final T obj) {
		  EXECUTOR.execute(new Runnable() {
			@Override
			public void run() {
				addObject(moduleName, obj);
			}
		  });
	  }

	  /**
	   * 批量 添加索引数据
	   * @param moduleName solr模块请求地址
	   * @param list solr模块对象 集合 
	   * @exception 
	   * @since  1.0.0
	   */
	  @Override
	  public <T extends SolrBaseModel> boolean addList(String moduleName, List<T> list){
		  SolrServer server = solrServer.getSolrServer(moduleName);
		  if(server == null)
			  return false;
		  try {
				server.addBeans(list);
				server.commit(false, false);
		  } catch (Exception e) {
				//回滚
				try {server.rollback();} catch (Exception e1) {}
				LOGGER.error("solr addList fail;", e);
				
				//重试机制
				boolean retry = crashRecord(moduleName,e);
				if(retry){
					LOGGER.debug("solr server is crash, on retry.");
					server = solrServer.getSolrServer(moduleName);
					try {
						server.addBeans(list);
						server.commit(false, false);
						return true;
					} catch (Exception e1) {
						try {server.rollback();} catch (Exception e2) {}
						e1.printStackTrace();
					}
				}
				return false;
		  }
		  return true;
	  }

	  @Override
	  public <T extends SolrBaseModel> void addListByAsync(final String moduleName,
			final List<T> list) {
		  EXECUTOR.execute(new Runnable() {
			  @Override
			  public void run() {
				  addList(moduleName, list);
			  }
		  });
	  }
	  
	  /**
	   * 根据 语句删除 索引记录
	   * @param moduleName solr模块请求地址
	   * @param queryStr 例如（状态为禁用的索引记录）：status:disable
	   * @exception 
	   * @since  1.0.0
	   */
	  @Override
	  public boolean deleteByQuery(String moduleName, String queryStr){
		  SolrServer server = solrServer.getSolrServer(moduleName);
		  if(server == null)
			  return false;
		  try {
				server.deleteByQuery(queryStr);
				server.commit(false, false);
			} catch (Exception e) {
				//回滚
				try {server.rollback();} catch (Exception e1) {}
				LOGGER.error("solr deleteByQuery fail, queryStr:{};", new Object[]{queryStr, e});
				
				//重试机制
				boolean retry = crashRecord(moduleName,e);
				if(retry){
					LOGGER.debug("solr server is crash, on retry.");
					server = solrServer.getSolrServer(moduleName);
					try {
						server.deleteByQuery(queryStr);
						server.commit(false, false);
						return true;
					} catch (Exception e1) {
						try {server.rollback();} catch (Exception e2) {}
						e1.printStackTrace();
					}
				}
				return false;
			}
		  return true;
	  }

	  /**
	   * 根据 id 删除 索引记录
	   * @param moduleName solr模块请求地址
	   * @param id 
	   * @exception 
	   * @since  1.0.0
	   */
	  @Override
	  public boolean deleteById(String moduleName, String id){
		  SolrServer server = solrServer.getSolrServer(moduleName);
		  if(server == null)
			  return false;
		  try {
				server.deleteById(id);
				server.commit(false, false);
			} catch (Exception e) {
				//回滚
				try {server.rollback();} catch (Exception e1) {}
				LOGGER.error("solr deleteById fail, id:{};", new Object[]{id, e});
				
				//重试机制
				boolean retry = crashRecord(moduleName,e);
				if(retry){
					LOGGER.debug("solr server is crash, on retry.");
					server = solrServer.getSolrServer(moduleName);
					try {
						server.deleteById(id);
						server.commit(false, false);
						return true;
					} catch (Exception e1) {
						try {server.rollback();} catch (Exception e2) {}
						e1.printStackTrace();
					}
				}
				return false;
			}
		  return true;
	  }

	  /**
	   * 批量删除索引记录
	   * @param moduleName solr模块请求地址
	   * @param ids 索引主键值list
	   * @exception 
	   * @since  1.0.0
	   */
	  @Override
	  public boolean deleteByIds(String moduleName, List<String> ids){
		  SolrServer server = solrServer.getSolrServer(moduleName);
		  if(server == null)
			  return false;
		  try {
				server.deleteById(ids);
				server.commit(false, false);
			} catch (Exception e) {
				//回滚
				try {server.rollback();} catch (Exception e1) {}
				LOGGER.error("solr deleteByIds fail;", e);
				
				//重试机制
				boolean retry = crashRecord(moduleName,e);
				if(retry){
					LOGGER.debug("solr server is crash, on retry.");
					server = solrServer.getSolrServer(moduleName);
					try {
						server.deleteById(ids);
						server.commit(false, false);
						return true;
					} catch (Exception e1) {
						try {server.rollback();} catch (Exception e2) {}
						e1.printStackTrace();
					}
				}
				return false;
			}
		  return true;
	  }
	  /**
	   * 删除 指定solr模块 所有索引数据
	   * @param moduleName solr模块请求地址
	   * @exception 
	   * @since  1.0.0
	   */
	  @Override
	  public boolean deleteAll(String moduleName){
		  return deleteByQuery(moduleName, "*:*");
	  }
	  
	  /**
	   * ping服务器是否连接成功
	   * @param moduleName solr模块请求地址
	   * @return String  响应信息
	   * @exception 
	   * @since  1.0.0
	   */
	  @Override
	  public String ping(String moduleName){
		  SolrServer server = solrServer.getSolrServer(moduleName);
		  try {
			    SolrPingResponse response = server.ping();
			    return response.getResponse().toString();
			} catch (Exception e) {
				LOGGER.error("solr ping fail;", e);
				
				//重试机制
				boolean retry = crashRecord(moduleName, e);
				if(retry){
					LOGGER.debug("solr server is crash, on retry.");
					server = solrServer.getSolrServer(moduleName);
					try {
						SolrPingResponse response = server.ping();
						return response.getResponse().toString();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				return null;
			}
	  }
	  
	  /**
	   * 宕机记录
	   */
	  private boolean crashRecord(String moduleName, Exception e){
		  boolean retry = false;
		  if(ServerType.MASTER_SLAVE.name().toLowerCase().equals(solrServer.getSolrModel())){
			  //连接超时
			  if(e.getCause() instanceof ConnectException){
				  solrServer.changeMasterHost(moduleName);
				  retry = true;
			  }
		  }else if(ServerType.CLOUD.name().toLowerCase().equals(solrServer.getSolrModel())){
			  
		  }
		  return retry;
	  }

	  /**
	   * 优化(碎片整理，优化索引结构,合并索引文件) 提高性能，但需要一定的时间
	   * <br/> 注意： 建议定时调用
	   * @param moduleName  solr模块请求地址
	   * @exception 
	   * @since  1.0.0
	   */
	  @Override
	  public void optimize(String moduleName){
		  SolrServer server = solrServer.getSolrServer(moduleName);
		  try {
			    LOGGER.debug("正在优化 {} 索引库...", moduleName);
				long start = System.currentTimeMillis();
				server.optimize(false, false);
				long end = System.currentTimeMillis();
				LOGGER.debug("优化 {} 索引库完毕，花费时间:{} 秒;", new Object[]{moduleName, Long.valueOf((end - start) / 1000L)});
			} catch (Exception ex) {
				LOGGER.error("solr optimize fail, model:{};", new Object[]{moduleName, ex});
				
				//重试机制
				boolean retry = crashRecord(moduleName,ex);
				if(retry){
					LOGGER.debug("solr server is crash, on retry.");
					server = solrServer.getSolrServer(moduleName);
					try {
						LOGGER.debug("正在优化 {} 索引库...", moduleName);
						long start = System.currentTimeMillis();
						server.optimize(false, false);
						long end = System.currentTimeMillis();
						LOGGER.debug("优化 {} 索引库完毕，花费时间:{} 秒;", new Object[]{moduleName, Long.valueOf((end - start) / 1000L)});
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
	  }
	  
	  /**
	   * 过滤 分片数据（可以统计关键字及出现的次数、或是做自动补全提示）
	   * @param facets  分片数据
	   * @param wordMinLen  分片词语 最小长度
	   * @return Map<String,Long> {key=分片值，value=值出现次数}
	   * @exception 
	   * @since  1.0.0
	   */
	  @Override
	  public Map<String, Long> filterFacetData(List<FacetField> facets, int wordMinLen){
		  if(facets == null)
			  return new HashMap<String, Long>();
		  Map<String,Long> map = new LinkedHashMap<String,Long>();
		  for (FacetField facet : facets) {
				List<Count> facetCounts = facet.getValues();
				 for (FacetField.Count count : facetCounts) {
					 if(count.getName().length() >= wordMinLen);
					 map.put(count.getName(), count.getCount());
				 }
		  }
		  return map;
	  }
	  
	  private boolean canSendHttp(){
		  if(ServerType.CLOUD.name().toLowerCase().equals(solrServer.getSolrModel()))
			  return false;
		  return true;
	  }
	  

	@Override
	public void dataImport(String moduleName, String entity, boolean fullImport, Boolean clean, Boolean optimize) {
		 if(!canSendHttp()){
			 LOGGER.error("cloud model not can send http.");
			 return;
		 }
		 Map<String, String> params = new HashMap<String, String>();
		 String url = solrServer.getHttpHost()+moduleName+"/dataimport";
		 try {
			  	LOGGER.debug("数据导入 {} ", moduleName);
				long start = System.currentTimeMillis();
				params.put("command", fullImport?Command.IMPORT_FULL:Command.IMPORT_DELTA);
				if(StringUtils.isNotBlank(entity)){
					params.put("entity", entity);
				}
				params.put("commit", "true");
				params.put("clean", clean.toString());
				params.put("optimize", optimize==null?"false":optimize.toString());
				params.put("indent", "true");
				params.put("wt", "json");
				params.put("verbose", "false");
				params.put("debug", "false");
				String msg = HttpClientUtil.sendPostRequest(url, params);
				long end = System.currentTimeMillis();
				LOGGER.debug("数据导入 {} 完毕，msg:{}, 花费时间:{} 秒;", new Object[]{moduleName, msg, Long.valueOf((end - start) / 1000L)});
			} catch (Exception ex) {
				LOGGER.error("solr dataImport fail, model:{};", new Object[]{moduleName, ex});
				
				//重试机制
				boolean retry = crashRecord(moduleName,ex);
				if(retry){
					LOGGER.debug("solr server is crash, on retry.");
					try {
						long start = System.currentTimeMillis();
						String msg = HttpClientUtil.sendPostRequest(url, params);
						long end = System.currentTimeMillis();
						LOGGER.debug("数据导入 {} 完毕，msg:{}, 花费时间:{} 秒;", new Object[]{moduleName, msg, Long.valueOf((end - start) / 1000L)});
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
	}

	@Override
	public String dataImportStatus(String moduleName) {
		 if(!canSendHttp()){
			 LOGGER.error("cloud model not can send http.");
			 return null;
		 }
		 String result = null;
		  try {
			  	LOGGER.debug("数据导入 状态信息 {} ", moduleName);
				long start = System.currentTimeMillis();
				Map<String, String> params = new HashMap<String, String>();
				params.put("command", Command.STATUS);
				params.put("indent", "true");
				params.put("wt", "json");
				String url = solrServer.getHttpHost()+moduleName+"/dataimport";
				result = HttpClientUtil.sendGetRequest(url, params);
				long end = System.currentTimeMillis();
				LOGGER.debug("数据导入 状态信息 {}，result:{}, 花费时间:{} 秒;", new Object[]{moduleName, result, Long.valueOf((end - start) / 1000L)});
			} catch (Exception ex) {
				LOGGER.error("solr dataImportStatus fail, model:{};", new Object[]{moduleName, ex});
			}
		  return result;
	}

	@Override
	public boolean updateExtDictHandle(String moduleName) {
		 if(!canSendHttp()){
			 LOGGER.error("cloud model not can send http.");
			 return false;
		 }
		boolean flag = false;
		try {
		  	LOGGER.debug("更新 扩展词库处理 {};", moduleName);
		  	String url = solrServer.getHttpHost()+moduleName+"/analysis/updateExtDic";
			String result = HttpClientUtil.sendGetRequest(url, null);
			if(StringUtils.isNotBlank(result)){
				String tag = "reload_ext_dict_is:";
				int index = result.indexOf(tag);
				if(index != -1){
					String status = result.substring(index+tag.length(), result.length());
					if(status.startsWith("true")){
						flag = true;
					}
				}
			}
			LOGGER.debug("更新 扩展词库处理 {}，flag:{}, result:{};", new Object[]{moduleName, flag, result});
		} catch (Exception ex) {
			LOGGER.error("solr updateExtDictHandle fail, model:{};", new Object[]{moduleName, ex});
		}
		return flag;
	}

}