package com.lianj.solrj.api;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;

import com.lianj.solrj.entity.SolrBaseEntity;
import com.lianj.solrj.page.Page;

/**
 * 描述：Solr API接口
 * 构建组：lianj-solrj
 * 作者：徐浩文
 * 邮箱: xuhaowende@sina.cn
 * 日期:Jun 18, 2015-1:50:01 PM
 * 版权：联结 公司版权所有
 * </pre>
 */
public abstract interface SolrAPI {
	
	  /**
	   * 根据ID 单个查询
	   * @param moduleName solr模块名
	   * @param id 主键ID
	   * @param objectClass solr模块对象
	   * @return T
	   * @exception 
	   * @since  1.0.0
	   */
	  public <T extends SolrBaseEntity> T findById(String moduleName, String id, Class<T> objectClass);
	  
	  
	  /**
	   * 根据ID 批量查询
	   * @param moduleName solr模块名
	   * @param ids 主键ID集合
	   * @param objectClass solr模块对象
	   * @return List<T>
	   * @exception 
	   * @since  1.0.0
	   */
	  public <T extends SolrBaseEntity> List<T> findByIds(String moduleName, List<String> ids, Class<T> objectClass);
	  
	  
	 /**
	   * 获得 Query响应对象，
	   * @param moduleName solr模块名
	   * @param query (注意：带特殊符号的参数值 记得用ClientUtils.escapeQueryChars转义)
	   * @return QueryResponse {.getResults()=doc结果集，.getBeans(classType)=对象结果集，.getHighlighting()=高亮结果集, .getFacetFields()=分片结果集}
	   * @exception 
	   * @since  1.0.0
	   */
	  public QueryResponse query(String moduleName, SolrQuery query);
	  
	  /**
	   * 查询
	   * @param moduleName solr模块名
	   * @param keyword 关键字/ query语法
	   * @param objectClass solr模块对象
	   * @param fields （非必填） 返回的字段  , 默认全部
	   * @return List<T>
	   * @exception 
	   * @since  1.0.0
	   */
	  public <T extends SolrBaseEntity> List<T> query(String moduleName, String keyword, Class<T> objectClass, String... fields);
	  
	  /**
	   * 分页查询
	   * @param moduleName solr模块名
	   * @param keyword 关键字/ query语法
	   * @param objectClass solr模块对象
	   * @param pageNo 页码数
	   * @param pageSize  返回条数
	   * @param fields （非必填） 返回的字段 , 默认全部
	   * @return List<T>
	   * @exception 
	   * @since  1.0.0
	   */
	  public <T extends SolrBaseEntity> List<T> query(String moduleName, String keyword, Class<T> objectClass, Integer pageNo, Integer pageSize, LinkedHashMap<String, ORDER> sortMap, String... fields);

	  /**
	   * 自定义 query查询
	   * @param moduleName solr模块名
	   * @param query solr查询对象
	   * @param objectClass solr模块对象
	   * @return List<T>
	   * @exception 
	   * @since  1.0.0
	   */
	  public <T extends SolrBaseEntity> List<T> query(String moduleName, SolrQuery query, Class<T> objectClass);
	  
	  /**
	   * 分页 查询
	   * @param moduleName solr模块名
	   * @param keyword 关键字/ query语法
	   * @param objectClass solr模块对象
	   * @param fields （非必填） 返回的字段  , 默认全部
	   * @return Page<T> 分页对象
	   * @exception 
	   * @since  1.0.0
	   */
	  public <T extends SolrBaseEntity> Page<T> queryPage(String moduleName, String keyword, Class<T> objectClass, String... fields);
	  
	  /**
	   * 分页查询
	   * @param moduleName solr模块名
	   * @param keyword 关键字/ query语法
	   * @param objectClass solr模块对象
	   * @param pageNo 页码数
	   * @param pageSize  返回条数
	   * @param fields （非必填） 返回的字段  , 默认全部
	   * @return Page<T> 分页对象
	   * @exception 
	   * @since  1.0.0
	   */
	  public <T extends SolrBaseEntity> Page<T> queryPage(String moduleName, String keyword, Class<T> objectClass, Integer pageNo, Integer pageSize, LinkedHashMap<String, ORDER> sortMap, String... fields);

	  /**
	   * 自定义 query查询
	   * @param moduleName solr模块名
	   * @param query solr查询对象 (注意：带特殊符号的参数值 记得用ClientUtils.escapeQueryChars转义)
	   * @param objectClass solr模块对象 
	   * @return Page<T> 分页对象
	   * @exception 
	   * @since  1.0.0
	   */
	  public <T extends SolrBaseEntity> Page<T> queryPage(String moduleName, SolrQuery query, Class<T> objectClass);
	  
	  /**
	   * 添加索引数据
	   * @param moduleName solr模块名
	   * @param obj solr模块对象
	   * @exception 
	   * @since  1.0.0
	   */
	  public <T extends SolrBaseEntity> boolean addObject(String moduleName, T obj);
	  
	  /**
	   * [异步] 添加索引数据
	   * @param moduleName solr模块名
	   * @param obj solr模块对象
	   * @exception 
	   * @since  1.0.0
	   */
	  public <T extends SolrBaseEntity> void addObjectByAsync(String moduleName, T obj);

	  /**
	   * 批量 添加索引数据
	   * @param moduleName solr模块名
	   * @param list solr模块对象 集合 
	   * @exception 
	   * @since  1.0.0
	   */
	  public <T extends SolrBaseEntity> boolean addList(String moduleName, List<T> list);
	  
	  /**
	   * [异步] 批量 添加索引数据
	   * @param moduleName solr模块名
	   * @param list solr模块对象 集合 
	   * @exception 
	   * @since  1.0.0
	   */
	  public <T extends SolrBaseEntity> void addListByAsync(String moduleName, List<T> list);

	  /**
	   * 根据 语句删除 索引记录
	   * @param moduleName solr模块名
	   * @param queryStr 例如（状态为禁用的索引记录）：status:disable
	   * @exception 
	   * @since  1.0.0
	   */
	  public boolean deleteByQuery(String moduleName, String queryStr);

	  /**
	   * 根据 id 删除 索引记录
	   * @param moduleName solr模块名
	   * @param id 
	   * @exception 
	   * @since  1.0.0
	   */
	  public boolean deleteById(String moduleName, String id);

	  /**
	   * 批量删除索引记录
	   * @param moduleName solr模块名
	   * @param ids 索引主键值list
	   * @exception 
	   * @since  1.0.0
	   */
	  public boolean deleteByIds(String moduleName, List<String> ids);
	  
	  /**
	   * 删除 指定solr模块 所有索引数据
	   * @param moduleName solr模块名
	   * @exception 
	   * @since  1.0.0
	   */
	  public boolean deleteAll(String moduleName);
	  
	  /**
	   * ping服务器是否连接成功
	   * @param moduleName solr模块名
	   * @return String  响应信息
	   * @exception 
	   * @since  1.0.0
	   */
	  public String ping(String moduleName);

	  /**
	   * 优化(碎片整理，优化索引结构,合并索引文件) 提高性能，但需要一定的时间
	   * <br/> 注意： 建议定时调用
	   * @param moduleName  solr模块名
	   * @exception 
	   * @since  1.0.0
	   */
	  public void optimize(String moduleName);
	  
	  /**
	   * 数据导入
	   * @param moduleName  solr模块名
	   * @param entity dataImport实体 (允许为空，前提 当前模块只有一个 entity)
	   * @param fullImport true=全量导入，false=增量导入
	   * @param clean 是否要在索引开始构建之前删除之前的索引
	   * @param optimize 是否优化所有 （默认false）
	   */
	  public void dataImport(String moduleName, String entity, boolean fullImport, Boolean clean, Boolean optimize);
	  
	  /**
	   * 数据导入 状态查看
	   * @param moduleName  solr模块名
	   */
	  public String dataImportStatus(String moduleName);
	  
	  /**
	   * 过滤 分片数据（可以统计关键字及出现的次数、或是做自动补全提示）
	   * @param facets  分片数据
	   * @param wordMinLen  分片词语 最小长度
	   * @return Map<String,Long> {key=分片值，value=值出现次数}
	   * @exception 
	   * @since  1.0.0
	   */
	  public Map<String, Long> filterFacetData(List<FacetField> facets, int wordMinLen);
	  
	  /**
	   * 更新 扩展词库 处理(含停词)
	   * @param moduleName  solr模块名
	   */
	  public boolean updateExtDictHandle(String moduleName);
}