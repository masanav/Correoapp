package com.tfg.app.aplicacion.servicios.implementaciones;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tfg.app.aplicacion.modelos.entidades.Proveedor;
import com.tfg.app.aplicacion.repositorios.ProveedorRepository;
import com.tfg.app.aplicacion.servicios.IProveedorService;

@Service
public class ProveedorServiceImpl implements IProveedorService {

	@Autowired
	private ProveedorRepository repo;

	@Override
	public List<Proveedor> findAll() {
		return (List<Proveedor>) repo.findAll();
	}

	@Override
	public Optional<Proveedor> findById(Long id) {
		return repo.findById(id);
	}

	@Override
	public Proveedor save(Proveedor proveedor) {
		return repo.save(proveedor);
	}

	@Override
	public void deleteById(Long id) {
		repo.deleteById(id);
		
	}

	@Override
	public Optional<Proveedor> findFirstByIdOrderByAsc() {
		return repo.findFirstByIdOrderByAsc();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<Proveedor> findAll(Pageable pageable) {
		return repo.findAll(pageable);
	}
}