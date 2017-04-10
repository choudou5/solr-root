package com.choudoufu.solr.web.modules.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.choudoufu.solr.schema.entity.MessageBoard;
import com.choudoufu.solr.service.MessageBoardService;
import com.choudoufu.solr.web.modules.BaseController;

/**
 * 公共 Controller
 * @author xuhaowende
 * @date 2017年4月11日
 */
@Controller  
@Scope("prototype")   
@RequestMapping("/console/common")  
public class CommonController extends BaseController{

	/**
	 * 新增 留言
	 * @param message
	 * @param req
	 * @param attributes
	 * @return
	 */
	@RequestMapping(value="/guestbook",method=RequestMethod.POST)
	@ResponseBody
    public String read(MessageBoard message, HttpServletRequest req, RedirectAttributes attributes){
		//数据 验证
		if (!beanValidator(attributes, message)){
			return writeJson(STATUS_UNVALID_PARAM, attributes.asMap().get("message"));
		}
		try {
			MessageBoardService.add(req, message);
			return writeJson(STATUS_OK, "新增留言成功！");
		} catch (Exception e) {
			return writeJson("新增留言失败", e);
		}
    }
	
}
