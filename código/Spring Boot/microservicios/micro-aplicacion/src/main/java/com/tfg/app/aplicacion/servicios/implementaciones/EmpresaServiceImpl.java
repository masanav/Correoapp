package com.tfg.app.aplicacion.servicios.implementaciones;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tfg.app.aplicacion.controladores.errores.PreexistingEntityException;
import com.tfg.app.aplicacion.modelos.entidades.Contacto;
import com.tfg.app.aplicacion.modelos.entidades.Empresa;
import com.tfg.app.aplicacion.modelos.entidades.Orden;
import com.tfg.app.aplicacion.modelos.entidades.Perfil;
import com.tfg.app.aplicacion.modelos.entidades.Plantilla;
import com.tfg.app.aplicacion.repositorios.ContactoRepository;
import com.tfg.app.aplicacion.repositorios.EmpresaRepository;
import com.tfg.app.aplicacion.repositorios.OrdenRepository;
import com.tfg.app.aplicacion.repositorios.PerfilRepository;
import com.tfg.app.aplicacion.repositorios.PlantillaRepository;
import com.tfg.app.aplicacion.servicios.IEmpresaService;
import com.tfg.app.aplicacion.servicios.IPerfilService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class EmpresaServiceImpl implements IEmpresaService {
	
	@Autowired
	private EmpresaRepository empresa_repo;
	
	@Autowired
	private PlantillaRepository plantilla_repo;
	
	@Autowired
	private OrdenRepository orden_repo;
	
	@Autowired
	private ContactoRepository contacto_repo;
	
	@Autowired
	private IPerfilService perfil_servicio;

	@Override
	public List<Empresa> findAll() {
		return (List<Empresa>) empresa_repo.findAll();
	}

	@Override
	public Optional<Empresa> findById(Long id) {
		return empresa_repo.findById(id);
	}

	@Override
	public Empresa save(Empresa empresa) throws PreexistingEntityException {
		if (empresa.getId()!=null) {
			Optional<Empresa> o = findById(empresa.getId());
			if (o.isPresent()) {
				empresa.setFechaRegistro(o.get().getFechaRegistro());
			}
		}else {
			Optional<Empresa> o = empresa_repo.findByCorreo(empresa.getCorreo());
			if (o.isPresent()) {
				throw new PreexistingEntityException("El correo indicado ya esta registrado. Escoja otro");
			}
		}
		return empresa_repo.save(empresa);
	}

	@Override
	public void deleteById(Long id) {
		Empresa em = new Empresa();
		Optional<Empresa> oe = null;
		List<Plantilla> plantillas = new ArrayList<Plantilla>();
		List<Long> idsPlantillas = new ArrayList<Long>();
		List<Orden> ordenes = new ArrayList<Orden>();
		
		List<Contacto> contactos = new ArrayList<Contacto>();
		List<Perfil> perfiles = new ArrayList<Perfil>();
		
		oe=empresa_repo.findById(id);
		if (oe.isPresent()) {
			em=oe.get();
			plantillas=em.getPlantillas();
			try {
				//busco las plantillas y sus id
				if (plantillas.size()>0) {
					plantillas.stream().forEach(p->idsPlantillas.add(p.getId()));
				}
				
				//busco las ordenes que usan las plantillas de la empresa
				for(Long i : idsPlantillas) {
					ordenes.addAll(orden_repo.findByPlantillaId(i));
				}
				
				//borro las ordenes y las plantillas
				orden_repo.deleteInBatch(ordenes);
				plantilla_repo.deleteInBatch(plantillas);
			}catch(Exception e) {
				System.out.println(e.getStackTrace());
			}
			
			//borro contactos
			contactos=em.getContactos();
			contacto_repo.deleteInBatch(contactos);
			
			//borro perfiles y usuarios remotos
			List<Long> idsperfiles = new ArrayList();
			perfiles=em.getPerfiles();
			idsperfiles=perfiles.stream().map(p->p.getId()).collect(Collectors.toList());
			perfil_servicio.deleteByIds(idsperfiles);
			
			//borro empresa
			empresa_repo.deleteById(id);
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<Empresa> findAll(Pageable pageable) {
		return empresa_repo.findAll(pageable);
	}

	@Override
	public Optional<Empresa> findByCorreo(String correo) {
		return empresa_repo.findByCorreo(correo);
	}

}