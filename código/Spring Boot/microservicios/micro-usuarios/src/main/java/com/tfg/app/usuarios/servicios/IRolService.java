package com.tfg.app.usuarios.servicios;

import java.util.List;
import java.util.Optional;

import com.tfg.app.comusuarios.modelos.entidades.Rol;
import com.tfg.app.comusuarios.modelos.entidades.Usuario;

public interface IRolService {
	List<Rol> findAll();
	Optional<Rol> findById(Long id);
	Rol save(Rol rol);
    void deleteById(Long id);
}