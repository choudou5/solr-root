package com.choudoufu.solr.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.core.SolrCore;

import com.choudoufu.solr.constants.SysConsts;
import com.choudoufu.solr.constants.UserTypeEnum;
import com.choudoufu.solr.schema.entity.Schema;
import com.choudoufu.solr.schema.entity.SolrField;
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
	public static void save(HttpServletRequest req, Schema schema){
		//保存 应用信息
		saveSchema(req, schema);
		//保存 应用字段
		saveSchemaFields(req, schema.getName(), schema.getFields());
	}
	
	/**
	 * 保存 应用信息
	 * @param req
	 * @param schema
	 */
	public static void saveSchema(HttpServletRequest req, Schema schema){
		SolrCore core = SolrHelper.getCore(SysConsts.MODULE_SOLR_SCHEMA);
		String schemaName = schema.getName();
		User user = UserUtil.getSessionUser(req);
		if(user.getUserType() == UserTypeEnum.TEMP.getValue()){
			schemaName = user.getLoginName()+schema.getName();
		}
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
	
	
	/**
	 * 保存 应用字段信息
	 * @param req
	 * @param schemaName
	 * @param fields
	 */
	public static void saveSchemaFields(HttpServletRequest req, String schemaName, SolrField[] fields){
		SolrCore core = SolrHelper.getCore(SysConsts.MODULE_SOLR_FIELD);
		List<SolrField> tblFields = getSchemaFields(schemaName);
		if(CollectionUtils.isNotEmpty(tblFields)){
			//删除旧数据
			SolrJUtil.delModelData("schemaName:"+schemaName, core);
		}
		
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
	
	
	/**
	 * 是否存在应用
	 * @param schemaName
	 * @return
	 */
	public static boolean existSchema(String schemaName){
		SolrCore core = SolrHelper.getCore(SysConsts.MODULE_SOLR_SCHEMA);
		Schema schema = SolrJUtil.getModelData(SolrJUtil.getSolrQuery("name:"+schemaName), core, Schema.class);
		return schema==null?false:true;
	}
	
}
