package com.tfg.app.aplicacion.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tfg.app.aplicacion.modelos.entidades.Proveedor;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
	@Query(value="SELECT * FROM proveedores ORDER BY id ASC LIMIT 1", nativeQuery = true)
	Optional<Proveedor> findFirstByIdOrderByAsc();
}
