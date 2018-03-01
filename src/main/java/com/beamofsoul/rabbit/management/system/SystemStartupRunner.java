package com.beamofsoul.rabbit.management.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.beamofsoul.rabbit.management.repository.BaseJpaRepositoryConfiguration;
import com.beamofsoul.rabbit.primary.service.RolePermissionService;

import lombok.extern.slf4j.Slf4j;

/**
 * 实现自定义CommandLineRunner
 * 自定义服务器启动时执行某些操作
 * SpringBoot在应用程序启动后,会遍历CommondLineRunner接口的实例并运行它们的run方法
 * @Order 该注解用于排序多个自定义实现的CommandLineRunner实例,其中数值越小,越先执行
 * @author MingshuJian
 */
@Component
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SystemStartupRunner implements CommandLineRunner {
	
	@Autowired
	private RolePermissionService rolePermissionService;

	@Override
	public void run(String... args) throws Exception {
		log.info("服务启动执行,执行加载数据等操作...");
		BaseJpaRepositoryConfiguration.removeGeneratedSharedEntityManagerCreatorBeanDefinitions();
		
//		SensitiveWordsMapping.fill(sensitiveWordService.findAll());
		
		DatabaseTableNameContainer.loadDatabaseTableNames();
		DatabaseTableEntityMapping.initTableEntityMap();
		AnnotationServiceNameMapping.loadServiceMap();
		AnnotationRepositoryNameMapping.loadRepositoryMap();
		
		RolePermissionsMapping.fill(rolePermissionService.findAllRolePermissionMapping());
	}
}
