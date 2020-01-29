package com.ratnamit.monitoringwebservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * @author sumitmalpure1089
 *
 */
@Configuration
@EnableSwagger2
public class ApplicationConfig {
	 
	@Bean
	    public Docket api() {
	        return new Docket(DocumentationType.SWAGGER_2)
	                .apiInfo(getApiInfo())
	                .select()
	                .apis(RequestHandlerSelectors.basePackage("com.ratnamit.monitoringwebservice.controllers"))
	                .paths(PathSelectors.any())
	                .build();
	    }

	private ApiInfo getApiInfo() {
        Contact contact = new Contact("Sumit Malpure", "https://www.linkedin.com/in/sumit-malpure-6738a844/", "sumit_malpure@rediffmail.com");
        return new ApiInfoBuilder()
                .title("Monitoring Service API")
                .description("Monitoring Service to monitor microservices")
                .contact(contact)
                .build();
    }
	    
}
