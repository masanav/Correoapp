package com.tfg.app.oauth.servicios;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tfg.app.comusuarios.modelos.entidades.Usuario;
import com.tfg.app.oauth.clientes.UsuarioClienteFeign;

import feign.FeignException;

/*
 * UserDetailsService es una interfaz de Spring Security hay que implementar el
 * método que recibe un username para devolver todos sus datos, si lo encuentra
 * en un usuario del tipo que maneja Spring Security
 */

@Service
public class UsuarioServicio implements IUsuarioServicio, UserDetailsService {
	
	private Logger log = LoggerFactory.getLogger(UsuarioServicio.class);

	@Autowired
	private UsuarioClienteFeign cliente;

	@Override
	@Transactional (readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			log.debug(username);
			Usuario usuario = cliente.findByUsername(username);
			
			System.out.println(usuario.getUsername());
			System.out.println(usuario.getRol().getNombre());

			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority(usuario.getRol().getNombre()));

			log.info("Usuario autenticado: " + username);

			return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true,
					authorities);
		} catch (FeignException e) {
			log.error("No existe el usuario: " + username);
			throw new UsernameNotFoundException("Error: no existe el usuario '" + username);
		}
	}

	@Override
	@Transactional (readOnly = true)
	public Usuario findByUsername(String username) {
		return cliente.findByUsername(username);
	}

	@Override
	public Usuario update(Usuario usuario) {
		return cliente.update(usuario);
	}
}
