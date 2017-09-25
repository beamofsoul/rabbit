package com.beamofsoul.rabbit.primary.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beamofsoul.rabbit.primary.entity.User;
import com.beamofsoul.rabbit.primary.repository.UserRepository;

import io.swagger.annotations.ApiOperation;

/**
 * @ClassName UserController
 * @Description Return specific restful data required for user module
 * @author MingshuJian
 * @Date 2017年8月29日 上午10:52:08
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@ApiOperation(value = "创建用户", notes = "根据User对象创建用户")
	@PostMapping("/add")
	public User createUser(@RequestBody User user) {
		return userRepository.save(user);
	}

	@ApiOperation(value="获取所有用户详细信息", notes="获取用户信息列表")
	@GetMapping("/list")
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	@ApiOperation(value="获取用户详细信息列表", notes="根据一组用户ID获取对应的用户信息列表")
	@GetMapping("/{ids}")
	public List<User> getUsers(@PathVariable Long... ids) {
		return userRepository.findAllById(Arrays.asList(ids));
	}
	
	@ApiOperation(value = "更新用户", notes = "根据User对象更新用户")
	@PutMapping("/update")
	public User updateUser(@RequestBody User user) {
		return userRepository.saveAndFlush(user);
	}
	
	@ApiOperation(value="删除用户", notes="根据用户ID删除对应的用户信息")
	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable Long id) {
		userRepository.deleteById(id);
	}
}
