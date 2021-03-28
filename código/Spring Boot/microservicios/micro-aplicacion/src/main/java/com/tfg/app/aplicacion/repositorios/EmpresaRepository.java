package com.tfg.app.aplicacion.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.app.aplicacion.modelos.entidades.Empresa;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
	Optional<Empresa> findByCorreo(String correo);
}
