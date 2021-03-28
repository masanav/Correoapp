package com.tfg.app.zuul.swagger.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@EnableOpenApi
@Configuration
public class SwaggerDocumentationConfiguration {

	ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Microservicio de Zuul simétrico").description(
				"Este servicio es el enrutador bloqueante entre microservicios. \n"+
				"Incorpora un servidor de recursos con Oauth2 con clave privada. \n"+
				"Pivotal ha puesto en modo mantenimiento el servidor de autorización, \n"+
				"que afecta al servidor de recursos y, aunque estan desarrollando el suyo propio, \n"+
				"no hay una alternativa aún para el enrutador de Cloud Gateway, \n"+
				"el enrutador no bloqueante de Spring")
				// .termsOfServiceUrl("Libre de uso").version("1.0").license("Creative Commons")
				.version("0.0.1-SNAPSHOT")// .contact(new Contact("Miguel Angel", "pagina", "correo"))
				.build();
	}

	@Bean
	public Docket configureControllerPackageAndConvertors() {
		return new Docket(DocumentationType.OAS_30).select()
				// .paths(PathSelectors.any()).apis(RequestHandlerSelectors.any())
				.apis(RequestHandlerSelectors.none()).build()
				//.paths(PathSelectors.any())
				//.apis(RequestHandlerSelectors.any()).build()
				.useDefaultResponseMessages(false).apiInfo(apiInfo());
	}

}