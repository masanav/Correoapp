package com.tfg.app.aplicacion.servicios.implementaciones;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tfg.app.aplicacion.clientes.UsuarioClienteFeign;
import com.tfg.app.aplicacion.controladores.errores.PreexistingEntityException;
import com.tfg.app.aplicacion.controladores.errores.PreservarAdminException;
import com.tfg.app.aplicacion.modelos.entidades.Perfil;
import com.tfg.app.aplicacion.repositorios.PerfilRepository;
import com.tfg.app.aplicacion.servicios.IPerfilService;
import com.tfg.app.comusuarios.modelos.entidades.Rol;
import com.tfg.app.comusuarios.modelos.entidades.Usuario;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class PerfilServiceImpl implements IPerfilService {

	@Autowired
	private UsuarioClienteFeign cliente;

	@Autowired
	private PerfilRepository repo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Value("${spring.servlet.multipart.location}")
	private String ruta_fotos_perfil;
	
	private Usuario usuario_anterior;

	@Override
	public List<Perfil> findAll() {
		return (List<Perfil>) repo.findAll();
	}

	@Override
	public Optional<Perfil> findById(Long id) {
		return repo.findById(id);
	}

	@Transactional
	@Override
	public Perfil save(Perfil perfil) throws PreexistingEntityException, PreservarAdminException {
		String clave = passwordEncoder.encode(perfil.getPassword());
		Usuario u = new Usuario();
		Rol r = new Rol();
		Integer conta=0;
		u.setEnabled(perfil.getEnabled());
		u.setPassword(clave);
		u.setUsername(perfil.getUsername());
		r.setId(perfil.getRol());
		u.setRol(r);
		u.setIntentos(perfil.getIntentos());

		u = comprobarExistenciaUsuario(u, perfil);
		if (usuario_anterior!=null && usuario_anterior.getRol().getId()==1 && u.getRol().getId()!=1) {
			conta=repo.countByRol(1L);
			if (conta==1) {
				throw new PreservarAdminException("Número de administradores insuficiente");
			}
		}

		System.out.println(u);
		cliente.saveUsuario(u);
		System.out.println(perfil);
		perfil.setIntentos(null);
		perfil.setUsername("#####");
		perfil.setPassword("#####");
		return repo.save(perfil);
	}

	@Transactional
	@Override
	public void deleteById(Long id) {
		Optional<Perfil> o = repo.findById(id);
		Perfil perfil = null;

		if (o.isPresent()) {
			perfil = o.get();
		}

		cliente.deleteByIdUsuario(id);
		repo.deleteById(id);
	}

	@Override
	public List<Long> findAllId() {
		return repo.findAllId();
	}

	@Override
	public Page<Perfil> findAll(Pageable pageable) {
		return repo.findAll(pageable);
	}

	@Transactional
	@Override
	public Perfil saveFoto(Perfil perfil, MultipartFile archivo) throws PreservarAdminException, PreexistingEntityException {
		String clave = passwordEncoder.encode(perfil.getPassword());
		Usuario u = new Usuario(), usuario_remoto = null;
		Rol r = new Rol();
		Integer conta=0;
		u.setEnabled(perfil.getEnabled());
		u.setPassword(clave);
		u.setUsername(perfil.getUsername());
		r.setId(perfil.getRol());
		u.setRol(r);
		u.setIntentos(perfil.getIntentos());
		System.out.println(u);

		u = comprobarExistenciaUsuario(u, perfil);
		if (usuario_anterior!=null && usuario_anterior.getRol().getId()==1 && u.getRol().getId()!=1) {
			conta=repo.countByRol(1L);
			if (conta==1) {
				throw new PreservarAdminException("Número de administradores insuficiente");
			}
		}

		try {
			perfil.setFotolob(archivo.getBytes());
		} catch (IOException e) {
			log.info("Error al guardar la foto");
			e.printStackTrace();
		}

		cliente.saveUsuario(u);
		perfil.setIntentos(null);
		perfil.setPassword("######");
		perfil.setUsername("######");
		return repo.save(perfil);
	}

	private Usuario getUsuarioByUsername(Perfil perfil) {
		Usuario u = null;
		try {
			u = cliente.findByUsername(perfil.getUsername());
		} catch (feign.FeignException e) {
			log.error("El username del usuario no existe en el microservicio de usuarios");
			log.error(e.getLocalizedMessage());
		} catch (Exception e) {
			System.out.println("Otra causa");
			log.error(e);
		}

		return u;
	}

	private Usuario getUsuarioById(Perfil perfil) {
		Usuario u = null;
		try {
			u = cliente.findByIdUsuario(perfil.getId());
		} catch (feign.FeignException e) {
			log.error("El id del usuario no existe en el microservicio de usuarios");
			log.error(e.getLocalizedMessage());
		} catch (Exception e) {
			System.out.println("Otra causa");
			log.error(e);
		}

		return u;
	}
	
	//metodo para revisar las restricciones impuestas en las entidades de las bases de datos con respecto al username
	private Usuario comprobarExistenciaUsuario(Usuario u, Perfil perfil) throws PreexistingEntityException {
		Usuario usuario_remoto_id = null, usuario_remoto_username = null;
		
		if (perfil.getId() != null) { // es una actualizacion
			usuario_remoto_id = getUsuarioById(perfil);
			if (usuario_remoto_id != null) {
				if (usuario_remoto_id.getUsername().compareTo(perfil.getUsername()) != 0) {
					usuario_remoto_username = getUsuarioByUsername(perfil);
					if (usuario_remoto_username != null) {
						throw new PreexistingEntityException("El username escogido ya existe, escoja otro");
					}
					// el username cambiado no existe, puede guardar
				}
				// el username no ha variado, puede guardar
				usuario_anterior=u;
				u.setId(usuario_remoto_id.getId());
				u.setFechaRegistro(usuario_remoto_id.getFechaRegistro());
			}
		} else { // es una creacion
			usuario_remoto_username = getUsuarioByUsername(perfil);
			if (usuario_remoto_username != null) {
				if (usuario_remoto_username.getUsername().compareTo(perfil.getUsername()) == 0) {
					throw new PreexistingEntityException("El username escogido ya existe, escoja otro");
				}
				usuario_anterior=u;
				u.setId(usuario_remoto_username.getId());
				u.setFechaRegistro(usuario_remoto_username.getFechaRegistro());
			}
		}
		return u;
	}

	@Override
	public Integer countByRol(Long id) {
		return repo.countByRol(id);
	}

	@Override
	public void deleteByIds(List<Long> ids) {
		try {
			cliente.deleteByIds(ids);
			List<Perfil> perfiles = repo.findAllById(ids);
			repo.deleteInBatch(perfiles);
		}catch(feign.FeignException e) {
			log.error(e.getMessage());
		}catch (Exception e) {
			System.out.println("Otra causa");
			log.error(e);
		}
	}
}