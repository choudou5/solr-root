package com.choudoufu.solr.web.modules.collection;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.core.SolrCore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.choudoufu.solr.common.vo.Select2Vo;
import com.choudoufu.solr.schema.entity.Schema;
import com.choudoufu.solr.schema.entity.SolrField;
import com.choudoufu.solr.service.SchemaService;
import com.choudoufu.solr.util.SchemaUtil;
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
		model.addAttribute("schemas", SchemaService.listSchemas(req));
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
	
	private final String DATA_PAGE_SEARCH = "/console/collection/data/search";
	private final String DATA_PAGE_LIST = "/console/collection/data/list";
	
	
	/**
	 * 数据搜索
	 * @param keyword
	 * @param schemaName
	 * @param req
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"/data/search"})
    public String dataSearch(String keyword, String schemaName, HttpServletRequest req, Model model){
		List<Select2Vo> schemaSelect2Vos = SchemaUtil.getSchemaSelect2Vos(req);
		if(StringUtils.isNotBlank(schemaName)){
//			boolean isPermission = SchemaService.isPermission(req, schemaName);
//			if(!isPermission)
//				return toView(DATA_PAGE_LIST, NO_PERMISSION, model);
			Schema schema = SchemaService.getCacheSchema(schemaName);
			List<SolrField> fields = SchemaService.getFields(schemaName, req);
			model.addAttribute("schema", schema);
			model.addAttribute("fields", fields);
		}
		model.addAttribute("schemaSelect2Vos", schemaSelect2Vos);
		return DATA_PAGE_LIST;
    }
	
	
	@RequestMapping(value={"/data/form"},method=RequestMethod.GET)
    public String dataForm(String schemaName, Map<String, String> inputMap, HttpServletRequest req, Model model){
		Schema schema = SchemaService.getCacheSchema(schemaName);
		List<SolrField> fields = SchemaService.getFields(schemaName, req);
		model.addAttribute("schema", schema);
		model.addAttribute("fields", fields);
		model.addAttribute("inputMap", inputMap);
		return "/console/collection/list";
    }
	
	@RequestMapping(value={"/data/save"},method=RequestMethod.POST)
    public String dataSave(String schemaName, Map<String, String> inputMap, HttpServletRequest req, RedirectAttributes attributes, Model model){
		try {
			boolean isPermission = SchemaService.isPermission(req, schemaName);
			if(!isPermission)
				return redirect(DATA_PAGE_SEARCH, attributes, NO_PERMISSION);
			
			SolrCore core = SolrHelper.getCore(schemaName);
			SolrJUtil.addModelData(inputMap, core);
			return redirect(DATA_PAGE_SEARCH, attributes);
		} catch (Exception e) {
			addMessage(model, "保存数据失败！", e);
			return dataForm(schemaName, inputMap, req, model);
		}
    }
	
	
	@RequestMapping(value={"/data/delete"},method=RequestMethod.POST)
	@ResponseBody
    public String delete(String schemaName, String id, HttpServletRequest req){
		try {
			boolean isPermission = SchemaService.isPermission(req, schemaName);
			if(!isPermission)
				return writeJson(STATUS_UNVALID_PARAM, "对不起，您无权操作！");
			
			SolrCore core = SolrHelper.getCore(schemaName);
			SolrJUtil.delModelData("id:"+id, core);
			return writeJson(STATUS_OK, "记录删除成功!");
		} catch (Exception e) {
			return writeJson("记录删除失败!", e);
		}
    }
	
	
	@RequestMapping(value={"/data/deleteAll"},method=RequestMethod.POST)
	@ResponseBody
    public String dataDeleteAll(String schemaName, HttpServletRequest req, Model model){
		try {
			boolean isPermission = SchemaService.isPermission(req, schemaName);
			if(!isPermission)
				return writeJson(STATUS_UNVALID_PARAM, "对不起，您无权操作！");
			
			SolrCore core = SolrHelper.getCore(schemaName);
			SolrJUtil.delModelData(SolrJUtil.QUERY_ALL, core);
			return writeJson(STATUS_OK, "记录删除成功!");
		} catch (Exception e) {
			return writeJson("记录删除失败!", e);
		}
    }
	
}
