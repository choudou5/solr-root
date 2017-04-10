package com.choudoufu.solr.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.solr.core.SolrCore;

import com.choudoufu.solr.schema.entity.MessageBoard;
import com.choudoufu.solr.schema.entity.Schema;
import com.choudoufu.solr.util.IdGrowthUtil;
import com.choudoufu.solr.util.IpUtil;
import com.choudoufu.solr.util.SolrHelper;
import com.choudoufu.solr.util.SolrJUtil;

public class MessageBoardService {

	public static final String SOLR_MODULE = "message_board";
	
	/**
	 * 获得 应用信息
	 * @param schemaName
	 * @return
	 */
	public static Schema getSchema(String schemaName){
		SolrCore core = SolrHelper.getCore(SOLR_MODULE);
		return SolrJUtil.getModelData(SolrJUtil.getSolrQuery("name:"+schemaName), core, Schema.class);
	}

	
	/**
	 * 新增
	 * @param req
	 * @param model
	 */
	public static void add(HttpServletRequest req, MessageBoard model){
		SolrCore core = SolrHelper.getCore(SOLR_MODULE);
		model.setId(IdGrowthUtil.getIncrIdStr(SOLR_MODULE));
		model.setIp(IpUtil.getIpAddr(req));
		model.setCreateTime(new Date());
		model.setRead(false);
		SolrJUtil.addModelData(model, core);
	}
	
	/**
	 * 已读消息
	 * @param req
	 * @param id
	 */
	public static void read(HttpServletRequest req, String id){
		SolrCore core = SolrHelper.getCore(SOLR_MODULE);
		MessageBoard model = SolrJUtil.getModelData(SolrJUtil.getSolrQuery("id:"+id), core, MessageBoard.class);
		if(model != null){
			model.setRead(true);
			SolrJUtil.addModelData(model, core);
		}
	}
}
