package com.choudoufu.solr.web.modules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.choudoufu.solr.common.beanvalidator.BeanValidators;
import com.choudoufu.solr.constants.SysConsts.DataStatusEnum;
import com.choudoufu.solr.util.JsonUtil;

public abstract class BaseController {
	
	protected Logger log = LoggerFactory.getLogger(getClass());

	protected int STATUS_OK = 200;
	
	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;

	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 message 中
	 */
	protected boolean beanValidator(Model model, Object object, Class<?>... groups) {
		try{
			BeanValidators.validateWithException(validator, object, groups);
		}catch(ConstraintViolationException ex){
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			list.add(0, "数据验证失败：");
			addMessage(model, list.toArray(new String[]{}));
			return false;
		}
		return true;
	}
	
	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 flash message 中
	 */
	protected boolean beanValidator(RedirectAttributes redirectAttributes, Object object, Class<?>... groups) {
		try{
			BeanValidators.validateWithException(validator, object, groups);
		}catch(ConstraintViolationException ex){
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			list.add(0, "数据验证失败：");
			addMessage(redirectAttributes, list.toArray(new String[]{}));
			return false;
		}
		return true;
	}
	
	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组，不传入此参数时，同@Valid注解验证
	 * @return 验证成功：继续执行；验证失败：抛出异常跳转400页面。
	 */
	protected void beanValidator(Object object, Class<?>... groups) {
		BeanValidators.validateWithException(validator, object, groups);
	}
	
	/**
	 * 添加Model消息
	 * @param message
	 */
	protected void addMessage(Model model, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages){
			sb.append(message).append(messages.length>1?"<br/>":"");
		}
		model.addAttribute("message", sb.toString());
	}
	
	
	protected ModelAndView toView(String viewName, String message) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName(viewName);
		mv.addObject("message", message);
		return mv;
	}
	
	protected String redirect(String path) {
		return "redirect:"+path;
	}
	
	protected String redirect(String path, RedirectAttributes attributes) {
		return "redirect:"+path;
	}
	
	protected String redirect(String path, RedirectAttributes attributes, String message) {
		attributes.addFlashAttribute("message", message);
		return "redirect:"+path;
	}
	
	/**
	 * 输出状态
	 * @param status
	 * @return Y/N
	 */
	protected String writeStatus(boolean status) {
		return status?DataStatusEnum.YES.getValue():DataStatusEnum.NO.getValue();
	}
	
	protected String writeJson(Object obj) {
		return JsonUtil.toString(obj);
	}
	
	protected String writeJson(int status, Object content){
		return writeJson(status, content, null, null);
	}
	
	protected String writeJson(int status, Object content, Map<String, Object> extParam){
		return writeJson(status, content, extParam, null);
	}
	
	protected String writeJson(int status, Object content, Exception e){
		return writeJson(status, content, null, e);
	}
	
	protected String writeJson(String defMsg, Exception e){
		log.error(defMsg, e);
		if(e instanceof SolrException){
			SolrException se = (SolrException)e;
			return writeJson(se.code(), se.getMessage(), null, e);
		}
		return writeJson(ErrorCode.BAD_REQUEST.code, defMsg, null, e);
	}
	
	protected String writeJson(int status, Object content, Map<String, Object> extParam, Exception e){
		Map<String, Object> result = new HashMap<String, Object>(4);
		result.put("status", status);
		result.put("content", content);
		if(extParam != null) result.put("extParam", extParam);
		if(e != null) result.put("error", e.getMessage());
		return JsonUtil.toString(result);
	}
	
}
