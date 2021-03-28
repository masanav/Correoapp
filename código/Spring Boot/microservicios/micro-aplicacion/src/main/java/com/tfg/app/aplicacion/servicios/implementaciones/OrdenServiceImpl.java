package com.tfg.app.aplicacion.servicios.implementaciones;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tfg.app.aplicacion.modelos.entidades.Orden;
import com.tfg.app.aplicacion.repositorios.OrdenRepository;
import com.tfg.app.aplicacion.servicios.IOrdenService;

@Service
public class OrdenServiceImpl implements IOrdenService {

	@Autowired
	private OrdenRepository repo;

	@Override
	public List<Orden> findAll() {
		return (List<Orden>) repo.findAll();
	}

	@Override
	public Optional<Orden> findById(Long id) {
		return repo.findById(id);
	}

	@Override
	public Orden save(Orden orden) {
		return repo.save(orden);
	}

	@Override
	public void deleteById(Long id) {
		repo.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<Orden> findAll(Pageable pageable) {
		return repo.findAll(pageable);
	}

	@Override
	public List<Orden> findByPlantillaId(Long id) {
		return repo.findByPlantillaId(id);
	}

}