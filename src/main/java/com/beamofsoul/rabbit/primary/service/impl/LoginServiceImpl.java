package com.beamofsoul.rabbit.primary.service.impl;

import static com.beamofsoul.rabbit.management.util.BooleanExpressionUtils.addExpression;
import static com.beamofsoul.rabbit.management.util.BooleanExpressionUtils.like;
import static com.beamofsoul.rabbit.management.util.BooleanExpressionUtils.toLongValue;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.beamofsoul.rabbit.primary.entity.Login;
import com.beamofsoul.rabbit.primary.entity.QLogin;
import com.beamofsoul.rabbit.primary.repository.LoginRepository;
import com.beamofsoul.rabbit.primary.service.LoginService;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

@Service("loginService")
public class LoginServiceImpl extends BaseAbstractService implements LoginService {

	@Autowired
	private LoginRepository loginRepository;

	@Override
	public Login create(Login instance) {
		return loginRepository.save(instance);
	}

	@Override
	public Login update(Login instance) {
		Login originalLogin = loginRepository.findOne(instance.getId());
		BeanUtils.copyProperties(instance, originalLogin);
		return loginRepository.save(originalLogin);
	}

	@Override
	@Transactional
	public long delete(Long... instanceIds) {
		return loginRepository.deleteByIds(instanceIds);
	}

	@Override
	public Login get(Long instanceId) {
		return loginRepository.findOne(instanceId);
	}
	

	@Override
	public List<Login> get(Long... instanceIds) {
		return loginRepository.findByIds(instanceIds);
	}

	@Override
	public List<Login> get() {
		return loginRepository.findAll();
	}

	@Override
	public Page<Login> get(Pageable pageable) {
		return loginRepository.findAll(pageable);
	}

	@Override
	public Page<Login> get(Pageable pageable, Predicate predicate) {
		return loginRepository.findAll(predicate, pageable);
	}
	
	@Override
	public BooleanExpression search(JSONObject conditions) {
		if (conditions == null) return null;
		
		QLogin login = QLogin.login;
		BooleanExpression exp = null;
		
		String id = conditions.getString(login.id.getMetadata().getName());
		exp = addExpression(id, exp, login.id.eq(toLongValue(id)));
		
		String nickname = conditions.getString(login.user.nickname.getMetadata().getName());
		exp = addExpression(nickname, exp, login.user.nickname.like(like(nickname)));
		
		String email = conditions.getString(login.user.email.getMetadata().getName());
		exp = addExpression(email, exp, login.user.email.like(like(email)));
		
		String operatingSystem = conditions.getString(login.operatingSystem.getMetadata().getName());
		exp = addExpression(operatingSystem, exp, login.operatingSystem.like(like(operatingSystem)));
		
		String browser = conditions.getString(login.browser.getMetadata().getName());
		exp = addExpression(browser, exp, login.browser.like(like(browser)));
		
		String ipAddress = conditions.getString(login.ipAddress.getMetadata().getName());
		exp = addExpression(ipAddress, exp, login.ipAddress.like(like(ipAddress)));
		
		String brand = conditions.getString(login.brand.getMetadata().getName());
		exp = addExpression(brand, exp, login.brand.like(like(brand)));
		
		String model = conditions.getString(login.model.getMetadata().getName());
		exp = addExpression(model, exp, login.model.like(like(model)));
		
		String screenSize = conditions.getString(login.screenSize.getMetadata().getName());
		exp = addExpression(screenSize, exp, login.screenSize.like(like(screenSize)));
		
		return exp;
	}
}
