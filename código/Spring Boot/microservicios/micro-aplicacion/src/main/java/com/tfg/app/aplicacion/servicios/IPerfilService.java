package com.tfg.app.aplicacion.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.tfg.app.aplicacion.controladores.errores.PreexistingEntityException;
import com.tfg.app.aplicacion.controladores.errores.PreservarAdminException;
import com.tfg.app.aplicacion.modelos.entidades.Perfil;

public interface IPerfilService {
	List<Perfil> findAll();
	Optional<Perfil> findById(Long id);
	Perfil save(Perfil perfil) throws PreexistingEntityException, PreservarAdminException;
	Perfil saveFoto(Perfil perfil, MultipartFile archivo)  throws PreexistingEntityException, PreservarAdminException;
    void deleteById(Long id);
    List<Long> findAllId();
    Page<Perfil> findAll(Pageable pageable);
    Integer countByRol(Long id);
    void deleteByIds(List<Long> ids);
}
