package com.tfg.app.zuul.oauth.filtros;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class addUsername extends ZuulFilter {

	private static Logger log = LoggerFactory.getLogger(addUsername.class);
	public static final String USER_HEADER = "Username";

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		log.debug(ctx.getRequest().getRequestURI());

		if (ctx.getRequest().getRequestURI() != null && ctx.getRequest().getRequestURI().startsWith("/api/security/oauth/token") || ctx.getRequest().getRequestURI().contains("/actuator")) {
			return false;
		}
		return true;
	}

	@Override
	public Object run() throws ZuulException {

		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();

		log.debug("Filtro ejecutado");
		if (request.getUserPrincipal()!=null) {
			log.debug(request.getUserPrincipal().getName());
		}
		
		//ctx.addZuulRequestHeader(USER_HEADER, request.getUserPrincipal().getName());
		
		return null;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 1;
	}

}