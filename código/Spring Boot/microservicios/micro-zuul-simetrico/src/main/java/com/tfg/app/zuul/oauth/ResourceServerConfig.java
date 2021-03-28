package com.tfg.app.zuul.oauth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@RefreshScope
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{

	@Value("${spring.security.oauth2.jwt.clave}")
	private String jwtKey;
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		//para acceder: http://localhost:8090/swagger-ui/index.html
		//para conseguir token: http://localhost:8090/api/security/oauth/token con grant_type, username y password, basic authentication
		http.authorizeRequests()
		.antMatchers(HttpMethod.GET, "/api/documentacion/**", "**/v3/api-docs").anonymous()
		.antMatchers("/api/usuarios/usuarios/**").permitAll()
		.antMatchers("/api/security/oauth/**").permitAll()
		.antMatchers(HttpMethod.GET,"/api/aplicacion/perfiles/{id}").authenticated()
		.antMatchers(HttpMethod.GET,"/api/aplicacion/perfiles/uploads/img/**").permitAll()
		.antMatchers("/api/aplicacion/perfiles/","/api/aplicacion/perfiles/**","/api/aplicacion/perfiles/uploads/img/**").hasAnyRole("ADMINISTRADOR","SOPORTE")
		.antMatchers(HttpMethod.GET,"/api/aplicacion/empresas/{id}**").authenticated()
		.antMatchers(HttpMethod.DELETE,"/api/aplicacion/empresas/","/api/aplicacion/empresas/{id}**").hasRole("ADMINISTRADOR")
		.antMatchers(HttpMethod.OPTIONS,"/api/aplicacion/empresas/","/api/aplicacion/empresas/{id}**").hasRole("ADMINISTRADOR")
		.antMatchers(HttpMethod.POST,"/api/aplicacion/empresas/","/api/aplicacion/empresas/{id}**").hasRole("ADMINISTRADOR")
		.antMatchers(HttpMethod.PUT,"/api/aplicacion/empresas/","/api/aplicacion/empresas/{id}**").hasRole("ADMINISTRADOR")
		.antMatchers("/api/aplicacion/plantillas/","/api/aplicacion/plantillas/**").authenticated()
		.antMatchers(HttpMethod.GET,"/api/aplicacion/proveedores/primero").permitAll()
		.antMatchers(HttpMethod.POST,"/api/aplicacion/proveedores/").hasRole("ADMINISTRADOR")
		.antMatchers(HttpMethod.GET,"/api/aplicacion/ordenes/","/api/aplicacion/ordenes/**").authenticated()
		.antMatchers(HttpMethod.POST,"/api/aplicacion/ordenes/","/api/aplicacion/ordenes/**").authenticated()
		.antMatchers(HttpMethod.PUT,"/api/aplicacion/ordenes/","/api/aplicacion/ordenes/**").authenticated()
		.antMatchers(HttpMethod.DELETE,"/api/aplicacion/ordenes/","/api/aplicacion/ordenes/**").denyAll()
		.antMatchers("/api/aplicacion/contactos/","/api/aplicacion/contactos/**").authenticated()
		.and().cors().configurationSource(corsConfigurationSource());
	}
	
	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		tokenConverter.setSigningKey(jwtKey);
		return tokenConverter;
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource(){
	    CorsConfiguration corsConfig = new CorsConfiguration();
	    corsConfig.addAllowedOrigin("http://localhost:4200");
	    corsConfig.setAllowedMethods(Arrays.asList("POST","GET","DELETE","OPTIONS"));

	    corsConfig.setAllowCredentials(true);
	    corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    //se aplica a todas las rutas la configuración establecida
	    source.registerCorsConfiguration("/**", corsConfig);

	    return source;
	}

	//para que quede registrado a nivel global este filtro. Si no se anota con bean, solo se queda a nivel de security
	@Bean
    public FilterRegistrationBean corsFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(corsConfigurationSource()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}