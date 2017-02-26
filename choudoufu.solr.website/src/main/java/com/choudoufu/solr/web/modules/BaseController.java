package com.choudoufu.solr.web.modules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public abstract class BaseController {
	
	protected Logger log = LoggerFactory.getLogger(getClass());

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
	
}
