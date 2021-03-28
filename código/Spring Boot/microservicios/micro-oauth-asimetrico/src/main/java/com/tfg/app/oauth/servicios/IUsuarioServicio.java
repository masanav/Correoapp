package com.tfg.app.oauth.servicios;

import com.tfg.app.comusuarios.modelos.entidades.Usuario;

public interface IUsuarioServicio {
	
	public Usuario findByUsername(String username);
	public Usuario update(Usuario usuario);
}
