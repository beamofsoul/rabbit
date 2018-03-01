package com.beamofsoul.rabbit.management.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private SecurityConfigurationProperties props;
	
	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;
	
	@Autowired
	private AuthenticationUserDetailsService authenticationUserDetailsService;
	
	@Autowired
	private SecurityPermissionEvaluator securityPermissionEvaluator;
	
	@Autowired
	private SecretKeyAuthenticationProvider secretKeyAuthenticationProvider;
	
	@Autowired
	private AccessDeniedHandler defaultAccessDeniedHandler;
	
	@Autowired
//	private org.thymeleaf.templateresolver.TemplateResolver tmeplateResolver;
	private ITemplateResolver templateResolver;
	
	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
		templateEngine.addDialect(new SpringSecurityDialect());
		return templateEngine;
	}
	
	@Bean
	public TokenBasedRememberMeServices rememberMeServices() {
		return new RememberMeServices(props.getRememberMeCookieName(), authenticationUserDetailsService);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		// Just response as 403 when access denied by Ajax
		http.exceptionHandling().accessDeniedHandler(defaultAccessDeniedHandler);
		
		http.csrf().disable().authorizeRequests()
		.antMatchers(props.getAdminRoleMatchers()).hasAnyRole(props.getAdminRoles())
		.antMatchers(props.getNonAuthenticatedMatchers()).permitAll()
		.and().formLogin()
			.loginPage(props.getLoginPage()).permitAll().defaultSuccessUrl(props.getDefaultLoginSuccessUrl(), props.isAlwaysUseDefaultSuccessUrl()).successHandler(authenticationSuccessHandler)
			.and().logout().logoutUrl(props.getLogoutUrl()).logoutSuccessUrl(props.getDefaultLogoutSuccessUrl())
			.and().sessionManagement().maximumSessions(props.getMaximumSessions())
			.maxSessionsPreventsLogin(props.isMaxSessionsPreventsLogin()).expiredUrl(props.getExpiredUrl())
			.and().and()
			.rememberMe()
				.tokenValiditySeconds(props.getTokenValiditySeconds())
				.rememberMeParameter(props.getRememberMeParameter())
				.rememberMeServices(rememberMeServices());
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(props.getIgnoringMatchers());
		/**
		 * 指定WebSecurity中使用自定义PermissionEvaluator
		 * 否则，网页模板中<sec:authorize access="hasPermission('...','...')">将会失效
		 */
		DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
		handler.setPermissionEvaluator(securityPermissionEvaluator);
		web.expressionHandler(handler);
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(secretKeyAuthenticationProvider);
	}
}
