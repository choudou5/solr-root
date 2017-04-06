package com.choudoufu.solr.web.modules.collection;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.solr.core.SolrCore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.choudoufu.solr.constants.SysConsts;
import com.choudoufu.solr.schema.entity.Schema;
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
		List<Schema> schemas = SolrJUtil.listModelData(SolrJUtil.getSolrQuery(SolrJUtil.QUERY_ALL), core, Schema.class);
		model.addAttribute("schemas", schemas);
		return "/console/collection/list";
    }
	
}
