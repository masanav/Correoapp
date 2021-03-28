package com.tfg.app.aplicacion.servicios.implementaciones;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tfg.app.aplicacion.modelos.entidades.Orden;
import com.tfg.app.aplicacion.modelos.entidades.Plantilla;
import com.tfg.app.aplicacion.repositorios.OrdenRepository;
import com.tfg.app.aplicacion.repositorios.PlantillaRepository;
import com.tfg.app.aplicacion.servicios.IPlantillaService;

@Service
public class PlantillaServiceImpl implements IPlantillaService {

	@Autowired
	private PlantillaRepository plantilla_repo;

	@Autowired
	private OrdenRepository orden_repo;
	
	@Override
	public List<Plantilla> findAll() {
		return (List<Plantilla>) plantilla_repo.findAll();
	}

	@Override
	public Optional<Plantilla> findById(Long id) {
		return plantilla_repo.findById(id);
	}

	@Override
	public Plantilla save(Plantilla plantilla) {
		return plantilla_repo.save(plantilla);
	}

	@Override
	public void deleteById(Long id) {
		Optional<Plantilla> o = plantilla_repo.findById(id);
		if (o.isPresent()) {
			Plantilla p = o.get();
			List<Orden> ordenes = orden_repo.findByPlantillaId(p.getId());
			orden_repo.deleteInBatch(ordenes);
		}
		plantilla_repo.deleteById(id);
	}

	@Override
	public List<Plantilla> findByEmpresaId(Long id) {
		return plantilla_repo.findByEmpresaId(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<Plantilla> findAll(Pageable pageable) {
		return plantilla_repo.findAll(pageable);
	}

}