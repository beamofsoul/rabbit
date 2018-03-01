package com.beamofsoul.rabbit.primary.service;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.alibaba.fastjson.JSONObject;
import com.beamofsoul.rabbit.primary.entity.UserRole;
import com.beamofsoul.rabbit.primary.entity.UserRoleCombineRole;

public interface UserRoleService {

	Page<UserRoleCombineRole> get(Pageable pageable);
	Page<UserRoleCombineRole> get(Pageable pageable, JSONObject conditions);
	UserRoleCombineRole get(Long userId);
	UserRoleCombineRole update(UserRoleCombineRole userRoleCombineRole);
	
	UserRole create(UserRole instance);
	Collection<UserRole> create(Collection<UserRole> userRoles);
	void delete(Long instanceId);
	Long delete(Long... instanceIds);
	Long delete(Long userId, Long[] roleIds);
}