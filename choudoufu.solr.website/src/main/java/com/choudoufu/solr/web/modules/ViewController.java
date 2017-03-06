package com.choudoufu.solr.web.modules;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Scope("prototype")
@RequestMapping("/console/page")
public class ViewController {

	@RequestMapping(value={"", "/"}, method = RequestMethod.GET)
	public String page(String path, RedirectAttributes attributes, Model model) {
		return "/console/" + path;
	}
}
