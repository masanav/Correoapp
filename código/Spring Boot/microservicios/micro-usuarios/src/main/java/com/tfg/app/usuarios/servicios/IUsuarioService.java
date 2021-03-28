package com.tfg.app.usuarios.servicios;

import java.util.List;
import java.util.Optional;

import com.tfg.app.comusuarios.modelos.entidades.Usuario;

public interface IUsuarioService {
	List<Usuario> findAll();
	Optional<Usuario> findById(Long id);
	Optional<Usuario> findByUsername(String username);
	Usuario save(Usuario usuario);
	void deleteById(Long id);
	void deleteByIds(List<Long> ids);
    void truncar ();
}