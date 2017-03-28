package com.choudoufu.solr.web.modules.collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.common.SolrException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.choudoufu.solr.schema.entity.Schema;
import com.choudoufu.solr.schema.entity.SolrConfig;
import com.choudoufu.solr.service.SchemaService;
import com.choudoufu.solr.web.modules.BaseController;

/**
 * 数据模型 Controller
 * @author xuhaowen
 * @date 2017年3月5日
 */
@Controller  
@Scope("prototype")   
@RequestMapping("/console/collection/schema")  
public class SchemaController extends BaseController{

	private final String PAGE_ADD = "/console/page?path=collection/schema/add"; 
	
	/**
	 * 编辑
	 * @param schemaName
	 * @param req
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.GET)
    public String edit(String schemaName, HttpServletRequest req, Model model){
		model.addAttribute("table", SchemaService.getSchema(schemaName));
		model.addAttribute("tableFields", SchemaService.getSchemaFields(schemaName));
		return "/console/collection/schema/edit";
    }
	
	/**
	 * 保存
	 * @param schema
	 * @param req
	 * @param resp
	 * @param attributes
	 * @return
	 */
	@RequestMapping(value={"/save"} ,method=RequestMethod.POST)  
    public String save(Schema schema, SolrConfig solrConfig, 
    		HttpServletRequest req, HttpServletResponse resp, RedirectAttributes attributes){  
		//数据 验证
		if (!beanValidator(attributes, schema)){
			return redirect(PAGE_ADD, attributes);
		}
		
		try {
			SchemaService.save(req, schema, solrConfig);
		} catch (SolrException e) {
			return redirect(PAGE_ADD, attributes, "保存失败："+e.getMessage());
		}
		return redirect(PAGE_ADD, attributes, "保存成功!");
    }
	
	/**
	 * 检查 schemaName是否存在
	 * @param name
	 * @param req
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/checkSchemaName",method=RequestMethod.GET)
	@ResponseBody
    public String checkSchemaName(String name, HttpServletRequest req, Model model){
		 boolean exists = SchemaService.existSchema(name, req);
		 return exists?"false":"true";
    }
	
}
