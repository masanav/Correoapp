package com.tfg.app.aplicacion.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tfg.app.aplicacion.modelos.entidades.Proveedor;

public interface IProveedorService {
	List<Proveedor> findAll();
	Optional<Proveedor> findById(Long id);
	Proveedor save(Proveedor proveedor);
    void deleteById(Long id);
    Optional<Proveedor> findFirstByIdOrderByAsc();
    Page<Proveedor> findAll(Pageable pageable);
}
