package com.beamofsoul.rabbit.management.security;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.beamofsoul.rabbit.primary.entity.User;
import com.beamofsoul.rabbit.primary.entity.dto.UserExtension;

//import com.beamofsoul.bip.entity.Role;
//import com.beamofsoul.bip.entity.User;
//import com.beamofsoul.bip.service.UserService;

/**
 * @ClassName AuthenticationUserDetailsService
 * @Description {@link org.springframework.security.core.userdetails.UserDetailsService} implementation that customizes the way of loading user by username inputed.
 * @author MingshuJian
 * @Date 2017年1月19日 下午4:28:32
 * @version 1.0.0
 */
@Component
public class AuthenticationUserDetailsService implements UserDetailsService {

//	@Autowired
//	private UserService userService;
	
	/*
	 * (非 Javadoc)  
	 * <p>Title: loadUserByUsername</p>  
	 * <p>Description: load user by username from DB, then package it to an {@link com.beamofsoul.rabbit.primary.entity.dto.UserExtension} object instance, return it finally.</p>
	 * @param username
	 * @return UserExtension
	 * @throws UsernameNotFoundException  
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		User user = getUser(username);
		return convertToUserExtension(user);
	}

	protected static UserExtension convertToUserExtension(User user) {
		UserExtension userExtension = new UserExtension(
                user.getId(), user.getUsername(), user.getPassword(), user.getNickname(),
                true,//是否可用
                true,//是否过期
                true,//证书不过期为true
                true,//账户未锁定为true
                getAuthorities(user));
		return userExtension;
	}

	private User getUser(String username) {
		if (StringUtils.isBlank(username))
            throw new UsernameNotFoundException("用户名为空");
        User user = getUser0(username);
        if (user == null)
			throw new UsernameNotFoundException("用户不存在");
        if (user.getStatus().equals(User.Status.LOCKED))
        	throw new UsernameNotFoundException("用户已被锁定");
//        if (user.getRoles() == null || user.getRoles().size() == 0)
//        	throw new UsernameNotFoundException("用户暂未被分配角色");
		return user;
	}
	
	protected User getUser0(String username) {
		return new User(1L, "beamofsoul", "Justin1987.", "Justin", "beamofsoul@sina.com", 1);
//		return userService.findByUsername(username);
	}

	protected static Set<GrantedAuthority> getAuthorities(User user) {
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
//        for (Role role : user.getRoles())
//			authorities.add(new SimpleGrantedAuthority(
//					"ROLE_" + role.getName().toUpperCase()));
		return authorities;
	}

}
