package com.tfg.app.aplicacion.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tfg.app.aplicacion.modelos.entidades.Orden;

public interface IOrdenService {
	List<Orden> findAll();
	Optional<Orden> findById(Long id);
	Orden save(Orden orden);
    void deleteById(Long id);
    Page<Orden> findAll(Pageable pageable);
    List<Orden> findByPlantillaId(Long id);
}
