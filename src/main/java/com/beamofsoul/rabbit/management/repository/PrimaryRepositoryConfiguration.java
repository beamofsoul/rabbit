package com.beamofsoul.rabbit.management.repository;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
		basePackages={"com.beamofsoul.rabbit.primary.repository"},
		transactionManagerRef="primaryTransactionManager",
		entityManagerFactoryRef="primaryEntityManagerFactory",
		repositoryFactoryBeanClass=RepositoryFactoryCreator.class)
@EnableSpringDataWebSupport
public class PrimaryRepositoryConfiguration extends BaseJpaRepositoryConfiguration {

	private static final String JPA_ENTITY_PACKAGE_PATH = "com.beamofsoul.rabbit.primary.entity";
	
	@Bean("primaryDataSource")
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource.primary")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean("primaryEntityManager")
	@Primary
	public EntityManager entityManager() {
		return getEntityManager(entityManagerFactory());
	}
	
	@Bean("primaryEntityManagerFactory")
	@Primary
	@Override
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {		
		return getEntityManagerFactory(dataSource(), JPA_ENTITY_PACKAGE_PATH, "primaryPersistenceUnit");
	}
	
	@Bean("primaryTransactionManager")
	@Primary
	PlatformTransactionManager transactionManager() {
		return getTransactionManager();
	}
}
