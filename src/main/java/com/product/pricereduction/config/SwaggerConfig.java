package com.product.pricereduction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

	private static final String SWAGGER_API_VERSION = "1.0";
	private static final String LICENSE_TEXT = "License";
	private static final String TITLE = "API DOCUMENTATION";
	private static final String DESCRIPTION = "Product API Documentation";
	private static final String BASE_PACKAGE = "com.product.pricereduction.controller";


	private ApiInfo apiInfo() {		
		return new ApiInfoBuilder()
				.title(TITLE)
				.description(DESCRIPTION)
				.license(LICENSE_TEXT)
				.version(SWAGGER_API_VERSION)
				.build();
	}

	@Bean
	public Docket api() {                
		return new Docket(DocumentationType.SWAGGER_2)          
				.select()
				.apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo());
	}
}
