package com.beamofsoul.rabbit.primary.service.impl;

import static com.beamofsoul.rabbit.management.util.BooleanExpressionUtils.addExpression;
import static com.beamofsoul.rabbit.management.util.BooleanExpressionUtils.like;
import static com.beamofsoul.rabbit.management.util.BooleanExpressionUtils.toIntegerValue;
import static com.beamofsoul.rabbit.management.util.BooleanExpressionUtils.toLocalDateTime;
import static com.beamofsoul.rabbit.management.util.BooleanExpressionUtils.toLongValue;
import static com.beamofsoul.rabbit.management.util.ImageUtils.deletePhotoFile;
import static com.beamofsoul.rabbit.management.util.ImageUtils.generateImageFilePath;
import static com.beamofsoul.rabbit.management.util.ImageUtils.getClearPhotoString;
import static com.beamofsoul.rabbit.management.util.ImageUtils.getPhotoType;
import static com.beamofsoul.rabbit.management.util.ImageUtils.imageToBase64;
import static com.beamofsoul.rabbit.management.util.ImageUtils.makePathEndWithDoubleSalsh;
import static com.beamofsoul.rabbit.management.util.ImageUtils.serializeImageFromBase64;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.beamofsoul.rabbit.management.annotation.Property;
import com.beamofsoul.rabbit.primary.entity.User;
import com.beamofsoul.rabbit.primary.entity.query.QUser;
import com.beamofsoul.rabbit.primary.repository.UserRepository;
import com.beamofsoul.rabbit.primary.service.UserService;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

@Service("userService")
public class UserServiceImpl extends BaseAbstractService implements UserService {
	
	@Property("project.user.photo-path")
	private static String USER_PHOTO_PATH;

	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	@Override
	public User create(User instance) {
		final String base64Photo = instance.getPhoto();
		if (StringUtils.isNotBlank(base64Photo)) {
			serializeUserPhoto(instance, base64Photo);
		}
		return userRepository.save(instance);
	}

	@Override
	public User update(User instance) {
		User originalInstance = get(instance.getId());
		
		// if the updated user still has a photo
		final String base64Photo = instance.getPhotoString();
		if (StringUtils.isNotBlank(base64Photo)) {
			serializeUserPhoto(instance, base64Photo);
		} else {
			// delete photo image file once the updated user's photo has been removed by updating
			String originalInstancePhoto = originalInstance.getPhoto();
			if (StringUtils.isNotBlank(originalInstancePhoto)) {
				deletePhotoFile(generateImageFilePath(USER_PHOTO_PATH, originalInstancePhoto));
			}
		}
		
		instance.setRoles(originalInstance.getRoles());
		BeanUtils.copyProperties(instance, originalInstance);
		return userRepository.save(originalInstance);
	}

	private void serializeUserPhoto(User instance, final String base64Photo) {
		String photoContent = getClearPhotoString(base64Photo);
		String photoType = getPhotoType(base64Photo);
		String photoPath = generateImageFilePath(USER_PHOTO_PATH, instance.getUsername(), photoType);
		if (serializeImageFromBase64(photoContent, photoPath)) {
			instance.setPhoto(photoPath.replace(makePathEndWithDoubleSalsh(USER_PHOTO_PATH), ""));
			instance.setPhotoString(base64Photo);
		} else {
			throw new RuntimeException("failed to generage user's photo when trying to convert base64 code to image");
		}
	}

	@Transactional
	@Override
	public long delete(Long... instanceIds) {
		List<User> users = this.get(instanceIds);
		users.stream().filter(e -> StringUtils.isNotBlank(e.getPhoto())).forEach(e -> deletePhotoFile(generateImageFilePath(USER_PHOTO_PATH, e.getPhoto())));
        return userRepository.deleteByIds(instanceIds);
	}

	@Override
	public User get(Long instanceId) {
		User one = userRepository.findOne(instanceId);
		loadPhotoString(one);
		return one;
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
	public BooleanExpression search(JSONObject conditions) {
		if (conditions == null) return null;

		QUser user = QUser.user;
		BooleanExpression exp = null;
		
		String id = conditions.getString(user.id.getMetadata().getName());
		exp = addExpression(id, exp, user.id.eq(toLongValue(id)));
		
		String nickname = conditions.getString(user.nickname.getMetadata().getName());
		exp = addExpression(nickname, exp, user.nickname.like(like(nickname)));
		
		String username = conditions.getString(user.username.getMetadata().getName());
		exp = addExpression(username, exp, user.username.like(like(username)));
		
		String password = conditions.getString(user.password.getMetadata().getName());
		exp = addExpression(password, exp, user.password.like(like(password)));
		
		String email = conditions.getString(user.email.getMetadata().getName());
		exp = addExpression(email, exp, user.email.like(like(email)));
		
		String phone = conditions.getString(user.phone.getMetadata().getName());
		exp = addExpression(phone, exp, user.phone.like(like(phone)));
		
		String status = conditions.getString(user.status.getMetadata().getName());
		exp = addExpression(status, exp, user.status.eq(toIntegerValue(status)));
		
		String createdDate = conditions.getString(user.createdDate.getMetadata().getName());
		exp = addExpression(createdDate, exp, user.createdDate.before(toLocalDateTime(createdDate)));
		
		return exp;
	}

	/**
	 * Get unique user instance by user-name, otherwise return a null value.
	 * The method is used when user login. 
	 * @see AuthenticationUserDetailsService#getUser0
	 * @param username - user-name used to get unique user instance.
	 * @return an instance of user have gotten or a null value. 
	 */
	@Transactional
	@Override
	public User get(String username) {
		User currentUser = userRepository.findByUsername(username);
		if (currentUser != null) {
			Hibernate.initialize(currentUser.getRoles());
			loadPhotoString(currentUser);
		}
		return currentUser;
	}

	/**
	 * Determine whether the given user-name is unique in current database table.
	 * @param username - user-name used to check unique user-name.
	 * @param userId - check unique user-name excluded the user instance of the given userId.
	 * @return if the user-name is unique (excluded the user instance of the given userId) return true, otherwise return false.
	 */
	@Override
	public boolean isUsernameUnique(String username, Long userId) {
		BooleanExpression predicate = QUser.user.username.eq(username);
		if (userId != null) {
			predicate = predicate.and(QUser.user.id.ne(userId));
		}
		return userRepository.count(predicate) == 0;
	}
	
	/**
	 * Determine whether the given nickname is unique in current database table.
	 * @param nickname - nickname used to check unique nickname.
	 * @param userId - check unique nickname excluded the user instance of the given userId.
	 * @return if the nickname is unique (excluded the user instance of the given userId) return true, otherwise return false.
	 */
	@Override
	public boolean isNicknameUnique(String nickname, Long userId) {
		BooleanExpression predicate = QUser.user.nickname.eq(nickname);
		if (userId != null) {
			predicate = predicate.and(QUser.user.id.ne(userId));
		}
		return userRepository.count(predicate) == 0;
	}
	
	private void loadPhotoString(User instance) {
		String photoString = null;
		if (StringUtils.isNotBlank(instance.getPhoto())) {
			photoString = imageToBase64(generateImageFilePath(USER_PHOTO_PATH, instance.getPhoto()));
			instance.setPhotoString(photoString);
		}
	}
}
