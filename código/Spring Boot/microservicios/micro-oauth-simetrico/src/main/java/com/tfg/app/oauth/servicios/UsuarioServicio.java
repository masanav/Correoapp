package com.tfg.app.oauth.servicios;

import java.util.ArrayList;
import java.util.List;

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
import lombok.extern.slf4j.Slf4j;



/*
 * UserDetailsService es una interfaz de Spring Security hay que implementar el
 * método que recibe un username para devolver todos sus datos, si lo encuentra
 * en un usuario del tipo que maneja Spring Security
 */
@Slf4j
@Service
public class UsuarioServicio implements IUsuarioServicio, UserDetailsService {

	@Autowired
	private UsuarioClienteFeign cliente;

	//La anotacion aqui lo que hace es que todo forme parte de una transacción en vez de crear una nueva
	@Override
	@Transactional (readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			Usuario usuario = findByUsername(username);

			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority(usuario.getRol().getNombre()));

			log.info("Usuario devuelto: " + username);

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
