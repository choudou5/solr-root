package com.choudoufu.solr.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.core.SolrCore;

import com.choudoufu.solr.constants.SysConsts;
import com.choudoufu.solr.schema.entity.SysTable;
import com.choudoufu.solr.schema.entity.SysTableField;
import com.choudoufu.solr.util.SolrHelper;
import com.choudoufu.solr.util.SolrJUtil;
import com.choudoufu.solr.util.UserUtil;

public class SchemaService {

	/**
	 * 获得 表信息
	 * @param tableName
	 * @return
	 */
	public static SysTable getTableInfo(String tableName){
		SolrCore core = SolrHelper.getCore(SysConsts.MODULE_TABLE);
		return SolrJUtil.getModelData(SolrJUtil.getSolrQuery("id:"+tableName), core, SysTable.class);
	}
	
	/**
	 * 获得 表字段信息
	 * @param tableName
	 * @return
	 */
	public static List<SysTableField> getTableFields(String tableName){
		SolrCore core = SolrHelper.getCore(SysConsts.MODULE_TABLE_FIELD);
		return SolrJUtil.listModelData(SolrJUtil.getSolrQuery("table:"+tableName), core, SysTableField.class);
	}
	
	/**
	 * 保存
	 * @param req
	 * @param table
	 * @param fields
	 */
	public static void save(HttpServletRequest req, SysTable table, SysTableField[] fields){
		//保存 表信息
		saveTable(req, table);
		//保存 表字段
		saveTableFields(req, table.getId(), fields);
	}
	
	/**
	 * 保存 表信息
	 * @param req
	 * @param table
	 */
	public static void saveTable(HttpServletRequest req, SysTable table){
		SolrCore core = SolrHelper.getCore(SysConsts.MODULE_TABLE);
		SysTable oldTbl = SolrJUtil.getModelData(SolrJUtil.getSolrQuery("id:"+table.getId()), core, SysTable.class);
		if(oldTbl != null){//修改
			oldTbl.setTitle(table.getTitle());
			oldTbl.setExplain(table.getExplain());
			oldTbl.setIcon(table.getIcon());
			oldTbl.setUpdateBy(UserUtil.getSessionUser(req).getLoginName());
			oldTbl.setUpdateTime(new Date());
			SolrJUtil.addModelData(oldTbl, core);
		}else{//新增
			table.setCreateBy(UserUtil.getSessionUser(req).getLoginName());
			table.setUpdateTime(new Date());
			table.setGrowthId(1L);
			SolrJUtil.addModelData(table, core);
		}
	}
	
	
	/**
	 * 保存 表字段信息
	 * @param req
	 * @param tableName
	 */
	public static void saveTableFields(HttpServletRequest req, String tableName, SysTableField[] fields){
		SolrCore core = SolrHelper.getCore(SysConsts.MODULE_TABLE_FIELD);
		List<SysTableField> tblFields = getTableFields(tableName);
		if(CollectionUtils.isNotEmpty(tblFields)){
			//删除旧数据
			SolrJUtil.delModelData("table:"+tableName, core);
		}
		SolrJUtil.addModelDatas(fields, core);
	}
	
	
	/**
	 * 是否存在表
	 * @param tableName
	 * @return
	 */
	public static boolean existTable(String tableName){
		SolrCore core = SolrHelper.getCore(SysConsts.MODULE_TABLE);
		SysTable table = SolrJUtil.getModelData(SolrJUtil.getSolrQuery("id:"+tableName), core, SysTable.class);
		return table==null?false:true;
	}
	
}
