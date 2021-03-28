package com.tfg.app.aplicacion.clientes;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.tfg.app.comusuarios.modelos.entidades.Rol;
import com.tfg.app.comusuarios.modelos.entidades.Usuario;

@FeignClient(name="micro-usuarios")
public interface UsuarioClienteFeign {

	@GetMapping("/usuarios/ver/{id}")
	public Usuario findByIdUsuario(@PathVariable Long id);
	
	@DeleteMapping("/usuarios/ver/{id}")
	public Usuario deleteByIdUsuario(@PathVariable Long id);
	
	@GetMapping("/usuarios/buscar")
	public Usuario findByUsername(@RequestParam String username);
	
	@DeleteMapping("/usuarios/borrar")
	public void deleteByIds(@RequestParam List<Long> ids);
	
	@PostMapping("/usuarios/guardar")
	public Usuario saveUsuario(@RequestBody Usuario usuario);
	
	@DeleteMapping("/usuarios/destruir")
	public void destruir();
	
	@GetMapping("/roles/ver")
	public List<Rol> findAllRol();
	
	@GetMapping("/roles/ver/{id}")
	public Rol findByIdRol(@PathVariable Long id);
}