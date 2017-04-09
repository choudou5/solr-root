package com.choudoufu.solr.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.core.SolrCore;

import com.choudoufu.solr.constants.SysConsts;
import com.choudoufu.solr.constants.UserTypeEnum;
import com.choudoufu.solr.schema.entity.Schema;
import com.choudoufu.solr.schema.entity.SolrConfig;
import com.choudoufu.solr.schema.entity.SolrField;
import com.choudoufu.solr.util.GenFileUtil;
import com.choudoufu.solr.util.IdGrowthUtil;
import com.choudoufu.solr.util.SolrHelper;
import com.choudoufu.solr.util.SolrJUtil;
import com.choudoufu.solr.util.UserUtil;
import com.choudoufu.sys.entity.User;

public class SchemaService {

	/**
	 * 获得 应用信息
	 * @param schemaName
	 * @return
	 */
	public static Schema getSchema(String schemaName){
		SolrCore core = SolrHelper.getCore(SysConsts.MODULE_SOLR_SCHEMA);
		return SolrJUtil.getModelData(SolrJUtil.getSolrQuery("name:"+schemaName), core, Schema.class);
	}
	
	/**
	 * 获得 应用字段信息
	 * @param schemaName
	 * @return
	 */
	public static List<SolrField> getSchemaFields(String schemaName){
		SolrCore core = SolrHelper.getCore(SysConsts.MODULE_SOLR_FIELD);
		return SolrJUtil.listModelData(SolrJUtil.getSolrQuery("schemaName:"+schemaName), core, SolrField.class);
	}
	
	/**
	 * 保存
	 * @param req
	 * @param schema
	 * @param fields
	 */
	public static void save(HttpServletRequest req, Schema schema, SolrConfig solrConfig){
		//保存 应用信息
		saveSchema(req, schema);
		//保存 应用字段
		saveSchemaFields(req, schema.getName(), schema.getFields());
		GenFileUtil.genSchema(SolrHelper.getSolrHome(), solrConfig, schema);
	}
	
	/**
	 * 删除 schema
	 * @param req
	 * @param schemaName
	 */
	public static void delete(HttpServletRequest req, String schemaName){
		//权限校验
		if(!isPermission(req, schemaName))
			throw new SolrException(ErrorCode.FORBIDDEN, "对不起，您无权操作!");
		
		SolrCore core = SolrHelper.getCore(SysConsts.MODULE_SOLR_SCHEMA);
		//删除 schema数据
		SolrJUtil.delModelData("name:"+schemaName, core);
		//删除 应用字段
		deleteSchemaFields(req, SolrHelper.getCore(SysConsts.MODULE_SOLR_FIELD), schemaName);
		//删除 配置文件
		GenFileUtil.deleteSchema(SolrHelper.getSolrHome(), schemaName);
	}
	
	/**
	 * 权限校验（校验是否为：管理员 或 创建人）
	 * @param request
	 * @return
	 */
	public static boolean isPermission(HttpServletRequest req, String schemaName){
		SolrCore core = SolrHelper.getCore(SysConsts.MODULE_SOLR_SCHEMA);
		Schema oldTbl = SolrJUtil.getModelData(SolrJUtil.getSolrQuery("name:"+schemaName), core, Schema.class);
		User user = UserUtil.getSessionUser(req);
		if(oldTbl == null || user == null)
			return false;
		if(user.getUserType() == UserTypeEnum.ADMIN.getValue() || oldTbl.getCreateBy().equals(user.getLoginName()))
			return true;
		return false;
	}
	
	/**
	 * 添加数据过滤
	 * @param req
	 * @param query
	 */
	public static void addDataFilter(HttpServletRequest req, SolrQuery query){
		User user = UserUtil.getSessionUser(req);
		if(user != null && user.getUserType() != UserTypeEnum.ADMIN.getValue()){
			query.addFilterQuery("createBy:"+user.getLoginName());
		}
	}
	
	/**
	 * 保存 应用信息
	 * @param req
	 * @param schema
	 */
	public static void saveSchema(HttpServletRequest req, Schema schema){
		SolrCore core = SolrHelper.getCore(SysConsts.MODULE_SOLR_SCHEMA);
		User user = UserUtil.getSessionUser(req);
		String schemaName = getSchemaName(schema.getName(), user);
		Schema oldTbl = SolrJUtil.getModelData(SolrJUtil.getSolrQuery("name:"+schemaName), core, Schema.class);
		if(oldTbl != null){//修改
			oldTbl.setTitle(schema.getTitle());
			oldTbl.setExplain(schema.getExplain());
			oldTbl.setUpdateBy(user.getLoginName());
			oldTbl.setUpdateTime(new Date());
			SolrJUtil.addModelData(oldTbl, core);
		}else{//新增
			schema.setName(schemaName);
			schema.setCreateBy(user.getLoginName());
			schema.setUpdateTime(new Date());
			schema.setGrowthId(1L);
			SolrJUtil.addModelData(schema, core);
		}
		
	}
	
	private static String getSchemaName(String schemaName,  User user){
		if(user != null && user.getUserType() == UserTypeEnum.TEMP.getValue()){
			schemaName = user.getLoginName()+SysConsts.CHAR_UNDERLINE+schemaName;
		}
		return schemaName;
	}
	
	/**
	 * 保存 应用字段信息
	 * @param req
	 * @param schemaName
	 * @param fields
	 */
	public static void saveSchemaFields(HttpServletRequest req, String schemaName, SolrField[] fields){
		SolrCore core = SolrHelper.getCore(SysConsts.MODULE_SOLR_FIELD);
		//删除旧数据
		deleteSchemaFields(req, core, schemaName);
		
		//设置数据
		int sortNo = 1;
		for (SolrField field : fields) {
			field.setId(IdGrowthUtil.getIncrIdStr(SysConsts.MODULE_SOLR_FIELD));
			field.setSchemaName(schemaName);
			field.setSortNo(sortNo);
			sortNo++;
		}
		SolrJUtil.addModelDatas(fields, core);
	}
	
	private static void deleteSchemaFields(HttpServletRequest req, SolrCore core, String schemaName){
		List<SolrField> tblFields = getSchemaFields(schemaName);
		if(CollectionUtils.isNotEmpty(tblFields)){
			//删除旧数据
			SolrJUtil.delModelData("schemaName:"+schemaName, core);
		}
	}
	
	
	
	/**
	 * 是否存在应用
	 * @param schemaName
	 * @return
	 */
	public static boolean existSchema(String schemaName, HttpServletRequest req){
		SolrCore core = SolrHelper.getCore(SysConsts.MODULE_SOLR_SCHEMA);
		User user = UserUtil.getSessionUser(req);
		Schema schema = SolrJUtil.getModelData(SolrJUtil.getSolrQuery("name:"+getSchemaName(schemaName, user)), core, Schema.class);
		return schema==null?false:true;
	}
	
}
