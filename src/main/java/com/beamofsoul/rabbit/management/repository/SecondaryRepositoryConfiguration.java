package com.beamofsoul.rabbit.management.repository;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
		basePackages={"com.beamofsoul.rabbit.secondary.repository"},
		transactionManagerRef="secondaryTransactionManager",
		entityManagerFactoryRef="secondaryEntityManagerFactory",
		repositoryFactoryBeanClass=RepositoryFactoryCreator.class)
@EnableSpringDataWebSupport
public class SecondaryRepositoryConfiguration extends BaseJpaRepositoryConfiguration {

	private static final String JPA_ENTITY_PACKAGE_PATH = "com.beamofsoul.rabbit.secondary.entity";
	
	@Bean("secondaryDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.secondary")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean("secondaryEntityManager")
	public EntityManager entityManager() {
		return getEntityManager(entityManagerFactory());
	}
	
	@Bean("secondaryEntityManagerFactory")
	@Override
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {		
		return getEntityManagerFactory(dataSource(), JPA_ENTITY_PACKAGE_PATH, "secondaryPersistenceUnit");
	}
	
	@Bean("secondaryTransactionManager")
	PlatformTransactionManager transactionManager() {
		return getTransactionManager();
	}
}
