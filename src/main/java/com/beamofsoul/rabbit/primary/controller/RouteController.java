package com.beamofsoul.rabbit.primary.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.beamofsoul.rabbit.management.util.HttpSessionUtils;

/**
 * @ClassName RouteController
 * @Description Return specific HTML5 pages required
 * @author MingshuJian
 * @Date 2017年8月25日 下午2:26:29
 * @version 1.0.0
 */
@Controller
public class RouteController {
	
	@GetMapping({"/" , "/index"})
	public String index() {
		return "index";
	}

	@GetMapping("/login")
	public String login(HttpSession session) {
		return HttpSessionUtils.isCurrentUserExist(session) ? "redirect:/index" : "login";  
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "login";
	}

	@GetMapping("/expired")
	public String expired(Map<String, Object> map) {
		map.put("expired", "The account has been re-logged elsewhere");
		return "login";
	}

	@GetMapping("/anonymous")
	public String anonymousLogin() {
		return "admin_index";
	}

	@GetMapping("/document")
    public String document() {
        return "redirect:swagger-ui.html";
    }
}
