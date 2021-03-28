package com.tfg.app.aplicacion.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tfg.app.aplicacion.modelos.entidades.Plantilla;

public interface IPlantillaService {
	List<Plantilla> findAll();
	Optional<Plantilla> findById(Long id);
	Plantilla save(Plantilla plantilla);
    void deleteById(Long id);
    List<Plantilla> findByEmpresaId(Long id);
    Page<Plantilla> findAll(Pageable pageable);
}
