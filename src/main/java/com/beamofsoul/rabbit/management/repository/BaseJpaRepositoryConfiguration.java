package com.beamofsoul.rabbit.management.repository;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseJpaRepositoryConfiguration {
	
	@Value("${spring.jpa.show-sql}")  
    String showSql;
    
    @Value("${spring.jpa.properties.hibernate.dialect}")  
    String dialect;
    
    @Value("${spring.jpa.hibernate.use-new-id-generator-mappings}")
    String useNewIdGeneratorMappings;
    
    @Value("${spring.jpa.hibernate.ddl-auto}")
    String ddlAuto;
    
    @Value("${spring.jpa.hibernate.naming.physical-strategy}")
    String physicalStrategy;
    
    @Value("${spring.jpa.hibernate.naming.implicit-strategy}")
    String implicitStrategy;
    
    BaseJpaRepositoryConfiguration() {
    	log.info(this.getClass().getSimpleName());
    }

	boolean getGeneratDdlStrategy() {
		return true; 
	}

	HibernateJpaVendorAdapter getHibernateJpaVendorAdapter() {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(getGeneratDdlStrategy());
		return vendorAdapter;
	}

	Properties getProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.show_sql", showSql);
		properties.setProperty("hibernate.dialect", dialect);
		properties.setProperty("hibernate.hbm2ddl.auto", ddlAuto);
		properties.setProperty("hibernate.id.new_generator_mappings", useNewIdGeneratorMappings);
		properties.setProperty("hibernate.implicit_naming_strategy", implicitStrategy);
		properties.setProperty("hibernate.physical_naming_strategy", physicalStrategy);
		return properties;
	}
	
	LocalContainerEntityManagerFactoryBean getEntityManagerFactory(final DataSource dataSource, final String entityPackagePath2Scan, final String persistenceUnitName) {
		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setPersistenceUnitName(persistenceUnitName);
		factoryBean.setJpaVendorAdapter(getHibernateJpaVendorAdapter());
		factoryBean.setPackagesToScan(entityPackagePath2Scan);
		factoryBean.setDataSource(dataSource);
		factoryBean.setJpaProperties(getProperties());
		factoryBean.afterPropertiesSet();
		return factoryBean;
	}
	
	EntityManager getEntityManager(@NonNull LocalContainerEntityManagerFactoryBean entityManagerFactory) {
		return entityManagerFactory.getObject().createEntityManager();
	}
	
	public abstract LocalContainerEntityManagerFactoryBean entityManagerFactory();
	
	PlatformTransactionManager getTransactionManager() {
		return new JpaTransactionManager(entityManagerFactory().getObject());
	}
}
