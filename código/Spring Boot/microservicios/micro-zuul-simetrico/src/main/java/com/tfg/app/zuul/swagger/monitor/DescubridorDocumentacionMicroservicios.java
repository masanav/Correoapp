package com.tfg.app.zuul.swagger.monitor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

@Log4j2
@Component
@Primary
public class DescubridorDocumentacionMicroservicios implements SwaggerResourcesProvider {

	private final String VERSION_DOCS = "3.0";
	private final String URL_DOCS = "v3/api-docs/";
	private final RouteLocator routeLocator;

	public DescubridorDocumentacionMicroservicios(RouteLocator routeLocator) {
		this.routeLocator = routeLocator;
	}

	private SwaggerResource swaggerResource(String name, String location, String version) {
		SwaggerResource swaggerResource = new SwaggerResource();
		swaggerResource.setName(name);
		swaggerResource.setLocation(location);
		swaggerResource.setSwaggerVersion(version);
		return swaggerResource;
	}

	@Autowired
	private DiscoveryClient discoveryClient;

	// Descubridor automático basado en servicios levantados. Mira los servicios
	// levantados y construyo manualmente los parametros parámetros que necesita
	// swaggerResource para añadir los microservicios a la lista de los disponibles
	// para ver la documentación

	/*
	 * @Override public List<SwaggerResource> get() { List<SwaggerResource>
	 * resources = new ArrayList<>();
	 * discoveryClient.getServices().stream().forEach(serviceId -> {
	 * List<ServiceInstance> serviceInstances =
	 * discoveryClient.getInstances(serviceId); if (serviceInstances == null ||
	 * serviceInstances.isEmpty()) {
	 * System.out.println("No instances available for service : {} " + serviceId); }
	 * else { ServiceInstance instance = serviceInstances.get(0);
	 * 
	 * String nombre_microservicio = null; String ruta_microservicio = null;
	 * log.warn(instance.getInstanceId()); if
	 * (instance.getInstanceId().indexOf("localhost") != 0) { nombre_microservicio =
	 * instance.getInstanceId().split(":")[0]; ruta_microservicio = "/" +
	 * nombre_microservicio + "/" + URL_DOCS; log.error(ruta_microservicio); } else
	 * { nombre_microservicio = instance.getInstanceId().split(":")[1];
	 * ruta_microservicio = "/" + URL_DOCS; }
	 * 
	 * 
	 * //debido a que spring eliminó Oauth de la versión 2.4 de
	 * spring-cloud-starter-oauth2, la documentación de Oauth no esta disponible y
	 * es necesario omitirlo para que no aparezca el error de que no encuentra las
	 * api-docs
	 * 
	 * System.out.println(nombre_microservicio);
	 * System.out.println(nombre_microservicio.compareTo("micro-oauth"));
	 * System.out.println(ruta_microservicio); if
	 * (nombre_microservicio.compareTo("micro-oauth")!=0)
	 * resources.add(swaggerResource(nombre_microservicio, ruta_microservicio,
	 * VERSION_DOCS)); } });
	 * 
	 * return resources; }
	 */
	/**
	 * Descubridor automático basado en rutas. Mira en las rutas de Zuul para añadir
	 * los microservicios a la lista de los disponibles para ver la documentación
	 */

	@Override
	public List<SwaggerResource> get() {
		List<SwaggerResource> resources = new ArrayList<>();
		List<Route> routes = routeLocator.getRoutes();
		routes.forEach(route -> {

			/*
			 * control para que no duplique las apis disponibles, porque mete tanto las
			 * rutas como los servicios levantados, dando lugar a duplicidades la ruta de
			 * Zuul
			 */
			if (route.getId().indexOf("micro-") == 0) {
				log.error(route);
				if (route.getFullPath().compareTo("/micro-oauth/**") == 0) {
					//esta ruta es mismo servicio, por lo que hay que quitar el nombre del servicio de la ruta para conseguir el json de swagger
					resources.add(
							swaggerResource(route.getId(), route.getFullPath().replace("/micro-oauth/**", "/"+URL_DOCS), VERSION_DOCS));
				} else {
					resources.add(
							swaggerResource(route.getId(), route.getFullPath().replace("**", URL_DOCS), VERSION_DOCS));
				}
			}
		});
		return resources;
	}

	/*
	 * /** Descubridor estático basado en rutas. Necesita que se añadan a la lista
	 * cada uno de los microservicios que se quiere tener disponibles
	 */
	/*
	 * @Override public List<SwaggerResource> get() { List resources = new
	 * ArrayList<>(); resources.add(swaggerResource("microservicio-usuarios",
	 * "/api/usuarios/v3/api-docs", "3.0")); return resources; }
	 */

}
