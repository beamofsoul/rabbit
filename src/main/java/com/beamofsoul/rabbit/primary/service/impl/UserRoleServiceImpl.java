package com.beamofsoul.rabbit.primary.service.impl;

import static com.beamofsoul.rabbit.management.util.BooleanExpressionUtils.addExpression;
import static com.beamofsoul.rabbit.management.util.BooleanExpressionUtils.like;
import static com.beamofsoul.rabbit.management.util.BooleanExpressionUtils.toLongValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.beamofsoul.rabbit.management.util.CacheUtils;
import com.beamofsoul.rabbit.management.util.CollectionUtils;
import com.beamofsoul.rabbit.management.util.PageUtils;
import com.beamofsoul.rabbit.primary.entity.Role;
import com.beamofsoul.rabbit.primary.entity.UserRole;
import com.beamofsoul.rabbit.primary.entity.UserRoleCombineRole;
import com.beamofsoul.rabbit.primary.entity.query.QRole;
import com.beamofsoul.rabbit.primary.entity.query.QUser;
import com.beamofsoul.rabbit.primary.entity.query.QUserRoleCombineRole;
import com.beamofsoul.rabbit.primary.repository.UserRoleCombineRoleRepository;
import com.beamofsoul.rabbit.primary.repository.UserRoleRepository;
import com.beamofsoul.rabbit.primary.service.RoleService;
import com.beamofsoul.rabbit.primary.service.UserRoleService;
import com.beamofsoul.rabbit.primary.service.UserService;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

@Service("userRoleService")
public class UserRoleServiceImpl extends BaseAbstractService implements UserRoleService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserRoleRepository userRoleRepository;
	
	@Autowired
	private UserRoleCombineRoleRepository userRoleCombineRoleRepository;
	
	@Override
	public Page<UserRoleCombineRole> get(Pageable pageable) {
		pageable = PageUtils.setPageableSort(pageable, new Sort(Direction.ASC, QUserRoleCombineRole.userRoleCombineRole.userId.getMetadata().getName()));
		return userRoleCombineRoleRepository.findAll(pageable);
	}
	
	@Override
	public Page<UserRoleCombineRole> get(Pageable pageable, JSONObject conditions) {
		if (conditions != null) {
			QUserRoleCombineRole $ = QUserRoleCombineRole.userRoleCombineRole;
			BooleanExpression exp = null;
			
			String userId = conditions.getString($.userId.getMetadata().getName());
			exp = addExpression(userId, exp, $.userId.eq(toLongValue(userId)));
			
			String nickname = conditions.getString($.nickname.getMetadata().getName());
			exp = addExpression(nickname, exp, $.nickname.like(like(nickname)));
			
			String username = conditions.getString($.username.getMetadata().getName());
			exp = addExpression(username, exp, $.username.like(like(username)));
			
			String roleName = conditions.getString($.roleName.getMetadata().getName());
			if (StringUtils.isNotBlank(roleName))
				exp = addExpression(roleName, exp, $.roleName.stringValue().contains((roleName)));
			
			String roleId = conditions.getString($.roleId.getMetadata().getName());
			if (StringUtils.isNotBlank(roleId))
				exp = addExpression(roleId, exp, $.roleId.stringValue().contains(roleId));
			
			return userRoleCombineRoleRepository.findAll(exp, pageable);
		} else {
			return userRoleCombineRoleRepository.findAll(pageable);
		}
	}

	@Override
	public UserRoleCombineRole get(Long userId) {
		Predicate predicate = QUserRoleCombineRole.userRoleCombineRole.userId.eq(userId);
		return userRoleCombineRoleRepository.findOne(predicate).get();
	}

	@Transactional
	@Override
	public UserRoleCombineRole update(UserRoleCombineRole userRoleCombineRole) {
		// 1. 获取未修改的原始数据
		UserRoleCombineRole originalObject = get(userRoleCombineRole.getUserId());
		// 2. 解析原始数据与方法输入的当前修改数据
		final String delimiter = ",";
		List<String> originalRoleIdsList = Arrays.asList(originalObject.getRoleId().split(delimiter));
		List<String> currentRoleIdsList = Arrays.asList(userRoleCombineRole.getRoleId().split(delimiter));
		// 3. 定义需要增加的对象集合和需要删除的对象主键Id集合
		List<UserRole> neededObjList = new ArrayList<>(currentRoleIdsList.size());
		List<Long> abandonedList = new ArrayList<>(originalRoleIdsList.size());
		Long userId = userRoleCombineRole.getUserId();
		// 4. 定义需要增加的对象的角色Id集合和角色名称集合，在后续步骤中用于直接修改刚刚查询到的视图原始数据，从而节省一次检索数据的操作
		List<String> neededRoleIdList = new ArrayList<>(Arrays.asList(originalObject.getRoleId().split(delimiter)));
		List<String> neededRoleNameList = new ArrayList<>(Arrays.asList(originalObject.getRoleName().split(delimiter)));
		// 5. 初始化两个集合中的数据，将当前有而原始数据中没有的角色增加到需要增加的对象集合，将当前没有而原始数据中存在的角色增加到需要删除的对象主键Id集合中
		if(CollectionUtils.isNotBlank(currentRoleIdsList)) currentRoleIdsList.stream().filter(e->!originalRoleIdsList.contains(e)).forEach(e-> {
			Role role = roleService.get(Long.valueOf(e));
			neededRoleIdList.add(role.getId().toString());
			neededRoleNameList.add(role.getName());
			neededObjList.add(new UserRole(userService.get(userId),role));
		});
		if(CollectionUtils.isNotBlank(originalRoleIdsList)) originalRoleIdsList.stream().filter(e->!currentRoleIdsList.contains(e)).forEach(e-> {
			neededRoleNameList.remove(neededRoleIdList.indexOf(e));
			neededRoleIdList.remove(e);
			abandonedList.add(Long.valueOf(e));
		});
		// 6. 执行批量增加和删除
		if (neededObjList.size() > 0) create(neededObjList);
		if (abandonedList.size() > 0) delete(userId, abandonedList.toArray(new Long[abandonedList.size()]));
		// 7. 清除缓存中符合增加或删除条件的记录
		CacheUtils.remove("userCache", userId);
		// 8. 根据编辑好的角色Id和角色名称将原始数据修改，并返回用于显示，此方法省去一次重新检索数据的操作并能够解决增加或删除记录后不能在当前事务中获取最新数据的问题
//		return findUserRoleMappingByUserId(userId);
		originalObject.setRoleId(String.join(delimiter, neededRoleIdList));
		originalObject.setRoleName(String.join(delimiter, neededRoleNameList));
		return originalObject;
	}

	@Override
	public UserRole create(UserRole userRole) {
		return userRoleRepository.save(userRole);
	}
	
	@Override
	public Collection<UserRole> create(Collection<UserRole> userRoles) {
		return userRoleRepository.saveAll(userRoles);
	}

	@Override
	public void delete(Long id) {
		userRoleRepository.deleteById(id);
	}

	@Override
	public Long delete(Long... ids) {
		return userRoleRepository.deleteByIds(ids);
	}

	@Override
	public Long delete(Long userId, Long[] roleIds) {
		//此处如果使用QUserRole.user.id和QUserRole.role.id会出现异常
		//org.hibernate.hql.internal.ast.InvalidPathException: Invalid path: 'userRole.role.id'
		//跟踪进hibernate源代码后发现getImports映射不到实体类，所以在HQL转SQL时不能找到正确的对象路径
		//猜测为QueryDSL与Hibernate兼容问题或QueryDSL查询文件生成的问题
		//已经将问题在GitHub上进行了反应, 该问题的URL: https://github.com/querydsl/querydsl/issues/2102
		return userRoleRepository.deleteByPredicate(QRole.role.id.in(roleIds).and(QUser.user.id.eq(userId)));
	}
}
