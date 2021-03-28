package com.tfg.app.oauth.clientes;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.tfg.app.comusuarios.modelos.entidades.Usuario;

//@FeignClient(name="microservicio-usuarios", decode404 = true)
@FeignClient(name="micro-usuarios")
public interface UsuarioClienteFeign {

	//public Usuario findByUsername(@RequestParam(name = "username", required=true) String username);
	@GetMapping("/usuarios/buscar")
	public Usuario findByUsername(@RequestParam String username);
	
	@PostMapping("/usuarios/guardar")
	public Usuario update(@RequestBody Usuario usuario);
	
	/*
	@FeignClient(name="microservicio-usuarios")
	public interface UsuarioClienteFeign {

	//public Usuario findByUsername(@RequestParam(name = "username", required=true) String username);
	@GetMapping("/usuarios/buscar")
	public Usuario findByUsername(@RequestParam String username);
	
	@PutMapping("/usuarios/{id}")
	public Usuario update(@RequestBody Usuario usuario, @PathVariable Long id);
}
	 */
}