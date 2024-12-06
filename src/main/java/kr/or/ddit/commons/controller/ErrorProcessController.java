package kr.or.ddit.commons.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/errors")
public class ErrorProcessController {
	@RequestMapping("{errorPage}")
	public String errors(@PathVariable String errorPage) {
		return "/errors/{errorPage}";
	}
}
