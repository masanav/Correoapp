package com.tfg.app.oauth.security.eventos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.tfg.app.comusuarios.modelos.entidades.Usuario;
import com.tfg.app.oauth.servicios.IUsuarioServicio;

import feign.FeignException;

@Component
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher {

	private Logger log = LoggerFactory.getLogger(AuthenticationSuccessErrorHandler.class);

	@Autowired
	private IUsuarioServicio usuarioServicio;

	@Autowired
	private Environment env;

	@Override
	public void publishAuthenticationSuccess(Authentication authentication) {

		if (authentication.getAuthorities().size() == 0) {
			return; // when authentication.getAuthorities().size() == 0 then user is client
					// application username
		}

		log.info(((UserDetails) authentication.getPrincipal()).getUsername());
		UserDetails user = (UserDetails) authentication.getPrincipal();
		String mensaje = "Usuario autenticado: " + user.getUsername();
		System.out.println(mensaje);

		Usuario usuario = usuarioServicio.findByUsername(authentication.getName());

		if (usuario.getIntentos() != null && usuario.getIntentos() > 0) {
			usuario.setIntentos(0);
			usuarioServicio.update(usuario);
		}

	}

	@Override
	public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
		String mensaje = "Error en el Login: " + exception.getMessage();
		log.error(mensaje);

		try {

			StringBuilder errors = new StringBuilder();
			errors.append(mensaje);

			Usuario usuario = usuarioServicio.findByUsername(authentication.getName());
			if (usuario.getIntentos() == null) {
				usuario.setIntentos(0);
			}

			log.info("Intentos actual es de: " + usuario.getIntentos());

			usuario.setIntentos(usuario.getIntentos() + 1);

			log.info("Intentos después es de: " + usuario.getIntentos());

			errors.append(" - Intentos del login: " + usuario.getIntentos());

			if (usuario.getIntentos() >= 3) {
				String errorMaxIntentos = String.format("El usuario %s des-habilitado por máximos intentos.",
						usuario.getUsername());
				log.error(errorMaxIntentos);
				errors.append(" - " + errorMaxIntentos);
				usuario.setEnabled(false);
			}

			usuarioServicio.update(usuario);

		} catch (FeignException e) {
			log.error(String.format("El usuario %s no existe en el sistema", authentication.getName()));
		}

	}

}
