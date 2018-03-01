package com.beamofsoul.rabbit.primary.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
	
	@GetMapping("/admin/adminIndex")
	public ModelAndView adminIndex(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		return new ModelAndView("admin_index");
	}
	
	@GetMapping("/admin/adminIndexContent")
	public ModelAndView adminIndexContent() {
		return new ModelAndView("/fragment/admin_content");
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
	
	@PreAuthorize("authenticated and hasPermission('user','user:list')")
	@GetMapping("/admin/user/adminList")
	public String adminList4UserModule() {
		return "/user/admin_user_list";
	}
	
	@PreAuthorize("authenticated and hasPermission('role','role:list')")
	@GetMapping("/admin/role/adminList")
    public String adminList4RoleModule() {
        return "/role/admin_role_list";
    }
	
	@PreAuthorize("authenticated and hasPermission('role','role:roleuser')")
	@RequestMapping(value = "admin/role/adminListWithUsers")
	public String adminList4RoleModuleWithUsers() {
		return "/role_user/admin_role_user_list";
	}
	
	@PreAuthorize("authenticated and hasPermission('role','permission:list')")
	@GetMapping("/admin/permission/adminList")
	public String adminList4PermissionModule() {
		return "/permission/admin_permission_list";
	}
	
	@PreAuthorize("authenticated and hasPermission('login','login:list')")
	@RequestMapping(value = "/admin/login/adminList")
	public String adminList4LoginModule() {
		return "/login/admin_login_list";
	}
}
