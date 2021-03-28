package com.tfg.app.usuarios.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.app.comusuarios.modelos.entidades.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

}