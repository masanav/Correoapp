package com.tfg.app.aplicacion.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tfg.app.aplicacion.modelos.entidades.Contacto;

public interface IContactoService {
	List<Contacto> findAll();
	Optional<Contacto> findById(Long id);
	Contacto save(Contacto contacto);
    void deleteById(Long id);
    List<Contacto> findByNombreOrApellidos(String term);
    Optional<Contacto> findByCorreo(String correo);
    List<Contacto> findByEmpresaId(Long id);
	Page<Contacto> findAll(Pageable pageable);
	List<Contacto> findByNombreOrApellido(String term);
}
