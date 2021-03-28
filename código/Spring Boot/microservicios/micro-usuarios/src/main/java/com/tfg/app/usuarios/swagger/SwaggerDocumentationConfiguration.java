package com.tfg.app.usuarios.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableOpenApi
public class SwaggerDocumentationConfiguration {

	ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Microservicio de usuarios y roles")
				.description("Este servicio contiene los usuarios y roles que son usados por el resto de servicios. Este enfoque permite aislar los datos \n"
						+ "de los usuarios de sus credenciales de acceso para aumentar la seguridad en caso de una instrusión no autorizada.")
				.termsOfServiceUrl("localhost:8090").version("1.0").license("Creative Commons BY-NC-SA 4.0")
				.licenseUrl("https://creativecommons.org/licenses/by-nc-sa/4.0/deed.es_ES").version("0.0.1-SNAPSHOT")
				.build();
	}

	@Bean
	public Docket configureControllerPackageAndConvertors() {
		return new Docket(DocumentationType.OAS_30).apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.tfg.app.usuarios.controladores"))
				//.paths(PathSelectors.ant("/usuarios"))
				//.paths(PathSelectors.ant("/roles/**"))
				.apis(RequestHandlerSelectors.any())
				.build().pathMapping("/api/usuarios").useDefaultResponseMessages(false).apiInfo(apiInfo());
	}

}