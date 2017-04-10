package com.choudoufu.solr.web.modules.sys;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.core.SolrCore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.choudoufu.solr.schema.entity.MessageBoard;
import com.choudoufu.solr.service.MessageBoardService;
import com.choudoufu.solr.service.SchemaService;
import com.choudoufu.solr.util.SolrHelper;
import com.choudoufu.solr.util.SolrJUtil;
import com.choudoufu.solr.web.modules.BaseController;

/**
 * 留言板 Controller
 * @author xuhaowende
 * @date 2017年4月10日
 */
@Controller  
@Scope("prototype")   
@RequestMapping("/console/messageBoard")  
public class MessageBoardController extends BaseController{

	
	/**
	 * 列表
	 * @param req
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"/list"},method=RequestMethod.GET)
    public String list(HttpServletRequest req, Model model){
		SolrCore core = SolrHelper.getCore(MessageBoardService.SOLR_MODULE);
		SolrQuery query = SolrJUtil.getSolrQuery(SolrJUtil.QUERY_ALL);
		//添加数据过滤
		SchemaService.addDataFilter(req, query);
		query.addSort("createTime", ORDER.desc);
		List<MessageBoard> messageList = SolrJUtil.listModelData(query, core, MessageBoard.class);
		model.addAttribute("messageList", messageList);
		return "/console/messageBoard/list";
    }
	
	/**
	 * 消息读取
	 * @param id
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/read",method=RequestMethod.GET)
	@ResponseBody
    public String read(String id, HttpServletRequest req){
		try {
			MessageBoardService.read(req, id);
			return writeJson(STATUS_OK, "读取消息:"+id);
		} catch (Exception e) {
			return writeJson("读取消息:"+id+" 失败", e);
		}
    }
}
