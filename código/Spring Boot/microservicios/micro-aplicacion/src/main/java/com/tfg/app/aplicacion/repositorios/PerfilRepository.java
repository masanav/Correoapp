package com.tfg.app.aplicacion.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tfg.app.aplicacion.modelos.entidades.Perfil;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {

	@Query(value = "SELECT id FROM perfiles ORDER BY id ASC", nativeQuery = true)
	List<Long> findAllId();	
	
	Integer countByRol(Long id);
}
