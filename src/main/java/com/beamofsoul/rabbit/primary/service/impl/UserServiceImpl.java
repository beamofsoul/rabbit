package com.beamofsoul.rabbit.primary.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.beamofsoul.rabbit.primary.entity.User;
import com.beamofsoul.rabbit.primary.repository.UserRepository;
import com.beamofsoul.rabbit.primary.service.UserService;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

@Service("userService")
public class UserServiceImpl extends BaseAbstractService implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public User create(User instance) {
		return userRepository.save(instance);
	}

	@Override
	public User update(User instance) {
		User originalInstance = get(instance.getId());
		BeanUtils.copyProperties(instance, originalInstance);
		return userRepository.save(originalInstance);
	}

	@Override
	public long delete(Long... instanceIds) {
		return userRepository.deleteByIds(instanceIds);
	}

	@Override
	public User get(Long instanceId) {
		return userRepository.getOne(instanceId);
	}

	@Override
	public List<User> get(Long... instanceIds) {
		return userRepository.findByIds(instanceIds);
	}

	@Override
	public Page<User> get(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	@Override
	public Page<User> get(Pageable pageable, Predicate predicate) {
		return userRepository.findAll(predicate, pageable);
	}

	@Override
	public List<User> get() {
		return userRepository.findAll();
	}

	@Override
	public BooleanExpression search(JSONObject condition) {
		return null;
	}
}
