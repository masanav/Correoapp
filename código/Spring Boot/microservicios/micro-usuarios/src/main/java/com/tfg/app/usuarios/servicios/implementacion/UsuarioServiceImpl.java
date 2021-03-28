package com.tfg.app.usuarios.servicios.implementacion;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.app.comusuarios.modelos.entidades.Usuario;
import com.tfg.app.usuarios.repositorios.UsuarioRepository;
import com.tfg.app.usuarios.servicios.IUsuarioService;

@Service
public class UsuarioServiceImpl implements IUsuarioService {
	private static Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);

	@Autowired
	private UsuarioRepository repo;

	@Override
	public List<Usuario> findAll() {
		return (List<Usuario>) repo.findAll();
	}

	@Override
	public Optional<Usuario> findById(Long id) {
		return repo.findById(id);
	}

	@Override
	public Usuario save(Usuario usuario) {
		log.info ("USUARIO RECIBIDO");
		System.out.println(usuario);
		return repo.save(usuario);
	}

	@Override
	public void deleteById(Long id) {
		repo.deleteById(id);
	}

	@Override
	public Optional<Usuario> findByUsername(String username) {
		return repo.findByUsername(username);
	}

	@Override
	public void truncar() {
		repo.truncar();
	}

	@Override
	public void deleteByIds(List<Long> ids) {
		List<Usuario> usuarios = repo.findAllById(ids);
		repo.deleteInBatch(usuarios);
	}

}