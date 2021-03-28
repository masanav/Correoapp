package com.tfg.app.usuarios.servicios.implementacion;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.app.comusuarios.modelos.entidades.Rol;
import com.tfg.app.usuarios.repositorios.RolRepository;
import com.tfg.app.usuarios.servicios.IRolService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class RolServiceImpl implements IRolService {

	@Autowired
	private RolRepository repo;

	@Override
	public List<Rol> findAll() {
		return (List<Rol>) repo.findAll();
	}

	@Override
	public Optional<Rol> findById(Long id) {
		return repo.findById(id);
	}

	@Override
	public Rol save(Rol rol) {
		log.info("ROL RECIBIDO");
		System.out.println(rol);
		return repo.save(rol);
	}

	@Override
	public void deleteById(Long id) {
		repo.deleteById(id);

	}

}