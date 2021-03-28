package com.tfg.app.usuarios.repositorios;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tfg.app.comusuarios.modelos.entidades.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findByUsername(String username);
	
	@Modifying
	@Transactional
	@Query(value = "TRUNCATE TABLE usuarios", nativeQuery = true)
	public void truncar();

}