package com.choudoufu.solr.web.modules.report;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.core.SolrCore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.choudoufu.solr.constants.SysConsts;
import com.choudoufu.solr.schema.entity.Schema;
import com.choudoufu.solr.service.SchemaService;
import com.choudoufu.solr.util.SolrHelper;
import com.choudoufu.solr.util.SolrJUtil;
import com.choudoufu.solr.web.modules.BaseController;

/**
 * 首页统计
 * @author xuhaowen
 * @date 2017年3月1日
 */
@Controller  
@Scope("prototype")   
@RequestMapping("/console/index")  
public class IndexStatisticalController extends BaseController{

	
	/**
	 * app列表
	 * @param req
	 * @param resp
	 * @param attributes
	 * @return
	 */
	@RequestMapping(value={"/appList"} ,method=RequestMethod.GET)  
	@ResponseBody
    public String appList(HttpServletRequest req, HttpServletResponse resp, RedirectAttributes attributes){  
		SolrCore core = SolrHelper.getCore(SysConsts.MODULE_SOLR_SCHEMA);
		
		SolrQuery query = SolrJUtil.getSolrQuery(SolrJUtil.QUERY_ALL);
		//添加数据过滤
		SchemaService.addDataFilter(req, query);
		List<Schema> schemas = SolrJUtil.listModelData(query, core, Schema.class);
		return writeJson(schemas);
    }
	
}
