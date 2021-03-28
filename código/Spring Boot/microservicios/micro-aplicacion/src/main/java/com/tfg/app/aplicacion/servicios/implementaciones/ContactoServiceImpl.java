package com.tfg.app.aplicacion.servicios.implementaciones;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tfg.app.aplicacion.modelos.entidades.Contacto;
import com.tfg.app.aplicacion.repositorios.ContactoRepository;
import com.tfg.app.aplicacion.servicios.IContactoService;

@Service
public class ContactoServiceImpl implements IContactoService {

	@Autowired
	private ContactoRepository repo;

	@Override
	public List<Contacto> findAll() {
		return (List<Contacto>) repo.findAll();
	}

	@Override
	public Optional<Contacto> findById(Long id) {
		return repo.findById(id);
	}

	@Override
	public Contacto save(Contacto contacto) {
		if (contacto.getId()!=null) {
			Optional<Contacto> o = findById(contacto.getId());
			if (o.isPresent()) {
				contacto.setFechaRegistro(o.get().getFechaRegistro());
			}
		}
		return repo.save(contacto);
	}

	@Override
	public void deleteById(Long id) {
		repo.deleteById(id);
	}

	@Override
	public List<Contacto> findByNombreOrApellidos(String term) {
		return repo.findByNombreOrApellidos(term);
	}

	@Override
	public Optional<Contacto> findByCorreo(String correo) {
		return repo.findByCorreo(correo);
	}

	@Override
	public List<Contacto> findByEmpresaId(Long id) {
		return repo.findByEmpresaId(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Contacto> findAll(Pageable pageable) {
		return repo.findAll(pageable);
	}

	@Override
	public List<Contacto> findByNombreOrApellido(String term) {
		return repo.findByNombreOrApellidos(term);
	}

}