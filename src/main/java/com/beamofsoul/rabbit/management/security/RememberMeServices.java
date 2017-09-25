package com.beamofsoul.rabbit.management.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

/**
 * @ClassName RememberMeServices
 * @Description {@link org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices} extension that customizes actions happen when onLoginSuccess and processAutoLoginCookie once logging user uses remember-me functionality.  
 * @author MingshuJian
 * @Date 2017年8月28日 下午4:53:26
 * @version 1.0.0
 */
public class RememberMeServices extends TokenBasedRememberMeServices {

	// @Autowired
	// private AuthenticationUserDetailsService
	// authenticationUserDetailsService;

	// @Autowired
	// private AuthenticationSuccessHandler authenticationSuccessHandler;

	public RememberMeServices(String key, UserDetailsService userDetailsService) {
		super(key, userDetailsService);
	}

	@Override
	public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication successfulAuthentication) {
		super.onLoginSuccess(request, response, successfulAuthentication);
		SecurityContextHolder.getContext().setAuthentication(successfulAuthentication);
		this.afterOnLoginSuccess(request, response, successfulAuthentication);
	}

	private void afterOnLoginSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication successfulAuthentication) {
		// User currentUser =
		// authenticationUserDetailsService.getUser0(successfulAuthentication.getName());
		// saveSessionProperties(request, convertToUserExtension(currentUser),
		// authenticationSuccessHandler.new
		// LoginRecordHandler().saveLoginRecord(currentUser, request,
		// response));
	}

	@Override
	protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request,
			HttpServletResponse response) {
		UserDetails userDetails = super.processAutoLoginCookie(cookieTokens, request, response);
		// this.afterProcessAutoLoginCookie(userDetails, request, response);
		return userDetails;
	}

	// private void afterProcessAutoLoginCookie(UserDetails userDetails,
	// HttpServletRequest request, HttpServletResponse response) {
	// User currentUser =
	// customUserDetailsService.getUser0(userDetails.getUsername());
	// saveSessionProperties(request, convertToUserExtension(currentUser),
	// customAuthenticationSuccessHandler.new
	// LoginRecordHandler().saveLoginRecord(currentUser, request, response));
	// }
	//
	// private void saveSessionProperties(HttpServletRequest request,
	// UserExtension userExtension, Login currentLogin) {
	// saveCurrentUser(request.getSession(), userExtension);
	// request.getSession().setAttribute("currentLogin", currentLogin);
	// }
}
