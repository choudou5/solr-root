package com.choudoufu.solr.web.modules.collection;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.core.SolrCore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.choudoufu.solr.constants.SysConsts;
import com.choudoufu.solr.schema.entity.Schema;
import com.choudoufu.solr.service.SchemaService;
import com.choudoufu.solr.util.SolrHelper;
import com.choudoufu.solr.util.SolrJUtil;
import com.choudoufu.solr.web.modules.BaseController;

/**
 * 集合 Controller
 * @author xuhaowende
 * @date 2017年4月4日
 */
@Controller  
@Scope("prototype")   
@RequestMapping("/console/collection")  
public class CollectionController extends BaseController{

	
	/**
	 * 列表
	 * @param req
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"/list"},method=RequestMethod.GET)
    public String list(HttpServletRequest req, Model model){
		SolrCore core = SolrHelper.getCore(SysConsts.MODULE_SOLR_SCHEMA);
		SolrQuery query = SolrJUtil.getSolrQuery(SolrJUtil.QUERY_ALL);
		//添加数据过滤
		SchemaService.addDataFilter(req, query);
		List<Schema> schemas = SolrJUtil.listModelData(query, core, Schema.class);
		model.addAttribute("schemas", schemas);
		return "/console/collection/list";
    }
	
	@RequestMapping(value="/delete",method=RequestMethod.GET)
	@ResponseBody
    public String delete(String schemaName, HttpServletRequest req){
		try {
			SchemaService.delete(req, schemaName);
			return writeJson(STATUS_OK, "删除成功");
		} catch (Exception e) {
			return writeJson("删除集合失败", e);
		}
    }
	
}
