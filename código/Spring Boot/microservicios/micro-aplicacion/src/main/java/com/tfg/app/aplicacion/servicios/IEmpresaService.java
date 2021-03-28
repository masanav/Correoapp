package com.tfg.app.aplicacion.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tfg.app.aplicacion.controladores.errores.PreexistingEntityException;
import com.tfg.app.aplicacion.modelos.entidades.Empresa;

public interface IEmpresaService {
	List<Empresa> findAll();
	Optional<Empresa> findById(Long id);
	Empresa save(Empresa empresa) throws PreexistingEntityException;
	void deleteById(Long id);
	Page<Empresa> findAll(Pageable pageable);
	Optional<Empresa> findByCorreo(String correo);
}
