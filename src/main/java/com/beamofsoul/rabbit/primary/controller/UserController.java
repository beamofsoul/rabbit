package com.beamofsoul.rabbit.primary.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.beamofsoul.rabbit.management.util.Constants;
import com.beamofsoul.rabbit.management.util.JSONUtils;
import com.beamofsoul.rabbit.management.util.PageUtils;
import com.beamofsoul.rabbit.primary.entity.User;
import com.beamofsoul.rabbit.primary.service.UserService;

import io.swagger.annotations.ApiOperation;

/**
 * @ClassName UserController
 * @Description Restful controller for user module and other APIs
 * @author MingshuJian
 * @Date 2017年8月29日 上午10:52:08
 * @version 1.0.0
 */
@RestController
@RequestMapping("/admin/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@ApiOperation(value = "创建用户", notes = "根据User对象创建用户")
	@PostMapping("/add")
	public User createUser(@RequestBody User user) {
		return userService.create(user);
	}

	@ApiOperation(value="获取所有用户详细信息", notes="获取用户信息列表")
	@GetMapping("/list")
	public List<User> getAllUsers() {
		return userService.get();
	}
	
	@ApiOperation(value="获取用户详细信息列表", notes="根据一组用户ID获取对应的用户信息列表")
	@GetMapping("/list/{ids}")
	public List<User> getUsers(@PathVariable Long... ids) {
		return userService.get(ids);
	}
	
	@ApiOperation(value = "更新用户", notes = "根据User对象更新用户")
	@PutMapping("/update")
	public User updateUser(@RequestBody User user) {
		return userService.update(user);
	}
	
	@ApiOperation(value="删除一个或多个用户", notes="根据一个或多个用户ID删除对应的用户信息")
	@PreAuthorize("authenticated and hasPermission('user','user:delete')")
	@DeleteMapping("/delete/{ids}")
	public long deleteUsers(@PathVariable Long... ids) {
		return userService.delete(ids);
	}
	
	/***************************************************************************/
	
	@ApiOperation(value="分页查询多个用户", notes="根据一个或多个查询条件查询对应的分页用户信息")
	@PreAuthorize("authenticated and hasPermission('user','user:list')")
	@GetMapping("/page")
	public Page<User> getPageableUsers(@RequestParam Map<String, Object> params) {
		JSONObject jsonParams = JSONObject.parseObject(JSON.toJSONString(params));
		Object conditions = jsonParams.get("conditions");
		return userService.get(PageUtils.parsePageable(params), 
				userService.search(JSONUtils.formatAndParseObject(conditions)));
	}
	
	@ApiOperation(value="获取某个用户详细信息", notes="根据一个用户ID获取对应的用户信息")
	@GetMapping("/single/{userId}")
	public User getUser(@PathVariable long userId) {
		return userService.get(userId);
	}
	
	@ApiOperation(value="判断某个用户昵称是否唯一", notes="根据一个用户名称判断是否该用户名称唯一(如果同时接收到一个用户ID, 则在判断用户名称唯一的过程中排除给定的用户ID所指向的用户名称)")
	@GetMapping("/isUsernameUnique")
	public boolean isUsernameUnique(@RequestParam Map<String, Object> params) {
		String username = params.get("uniqueData").toString();
		Long userId = params.containsKey(Constants.DEFAULT_ENTITY_PRIMARY_KEY) ? 
				Long.valueOf(params.get(Constants.DEFAULT_ENTITY_PRIMARY_KEY).toString()) : 
				null;
		return userService.isUsernameUnique(username, userId);
	}
	
	@ApiOperation(value="判断某个用户昵称是否唯一", notes="根据一个用户昵称判断是否该昵称唯一(如果同时接收到一个用户ID, 则在判断昵称唯一的过程中排除给定的用户ID所指向的用户昵称)")
	@GetMapping("/isNicknameUnique")
	public boolean isNicknameUnique(@RequestParam Map<String, Object> params) {
		String nickname = params.get("uniqueData").toString();
		Long userId = params.containsKey(Constants.DEFAULT_ENTITY_PRIMARY_KEY) ? 
				Long.valueOf(params.get(Constants.DEFAULT_ENTITY_PRIMARY_KEY).toString()) : 
				null;
		return userService.isNicknameUnique(nickname, userId);
	}
	
	
	/**
	@RequestMapping(value = "/isUsed", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject isUsed(@RequestBody String objectIds) {
		return newInstance("isUsed", userService.isUsed(objectIds));
	}
	
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject changePassword(@RequestBody Map<String, String> map, HttpSession session) {
		boolean changed = userService.changePassword(UserUtils.getLongUserId(session), map.get("currentPassword"), map.get("latestPassword"));
		if (changed) UserUtils.getTraditionalUser(session).setPassword(map.get("latestPassword"));
		return newInstance("changed", changed);
	}
	
	@RequestMapping(value = "/updatePhoto", method = RequestMethod.POST)
	public void updatePhoto(@RequestParam("file") MultipartFile[] files) {
		MultipartFile file = files[0];
		System.out.println(file.getOriginalFilename());
		System.out.println(file.getSize());
	}
	 */
}
