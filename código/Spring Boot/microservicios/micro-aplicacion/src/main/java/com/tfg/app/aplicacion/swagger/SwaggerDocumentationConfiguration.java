package com.tfg.app.aplicacion.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@EnableOpenApi
@Configuration
public class SwaggerDocumentationConfiguration {

	ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Microservicio de Aplicación").description(
				"Este servicio es la aplicación en si misma. \n"+
				"Permite enviar correos html a los contactos que la empresa tenga. \n"+
				"Dicha empresa puede modificar su información y gestionar sus usuarios. \n"+
				"El administrador puede hacer todas las operaciones y es el encargado de dar acceso a las empresas que lo soliciten. \n"+
				"Es una plataforma pensada para que un proveedor pueda ofrecer el servicio a otras empresas, \n"+
				"aunque puede ser usada por grandes corporaciones y sus departamentos o franquiciados")
				.termsOfServiceUrl("localhost:8090").version("1.0").license("Creative Commons BY-NC-SA 4.0")
				.licenseUrl("https://creativecommons.org/licenses/by-nc-sa/4.0/deed.es_ES").version("0.0.1-SNAPSHOT")
				.version("0.0.1-SNAPSHOT")
				.build();
	}

	@Bean
	public Docket configureControllerPackageAndConvertors() {
		return new Docket(DocumentationType.OAS_30).select()
				// .paths(PathSelectors.any()).apis(RequestHandlerSelectors.any())
				.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
				.paths(PathSelectors.any()) 
				.build()
				.pathMapping("/api/aplicacion")
				.useDefaultResponseMessages(false).apiInfo(apiInfo());
	}

}