package com.beamofsoul.rabbit;

//import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableTransactionManagement
@ServletComponentScan
@SpringBootApplication
public class Application extends SpringBootServletInitializer {
	/**
	 * In order to package the project as .war file, so extends {@link SpringBootServletInitializer}
	 * and override its' configure(SpringApplicationBuilder application) method.
	 * @param SpringApplicationBuilder application
	 * @return SpringApplicationBuilder
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
//	@Bean
//	public CommandLineRunner run(ApplicationContext cxt) {
//		return args -> {
//			String[] beanDefinitionNames = cxt.getBeanDefinitionNames();
//			for (String beanDefinitionName : beanDefinitionNames) {
//				System.out.println(beanDefinitionName);
//			}
//		};
//	}
}
