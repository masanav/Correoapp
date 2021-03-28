package com.tfg.app.aplicacion.repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tfg.app.aplicacion.modelos.entidades.Contacto;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Long> {

	@ApiOperation(value = "Buscar los contactos dado un término", response = Contacto.class, httpMethod = "GET")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Contacto encontrado"),
			@ApiResponse(code = 404, message = "Contacto no encontrado") })
	@ApiParam(value = "Recibe un término de búsqueda", required = true, allowMultiple = false)
	//@Query(value = "SELECT * FROM contactos CONCAT_WS(' ', nombre, apellidos) LIKE '%term%'", nativeQuery = true)
	@Query("select c from Contacto c where upper(c.nombre) like upper(concat('%', ?1, '%')) or upper(c.apellidos) like upper(concat('%', ?1, '%'))")
	List<Contacto> findByNombreOrApellidos(String term);

	@ApiOperation(value = "Buscar por correo", response = Contacto.class, httpMethod = "GET")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Contacto encontrado"),
			@ApiResponse(code = 404, message = "Contacto no encontrado") })
	@ApiParam(value = "Recibe el correo", required = true, allowMultiple = false)
	Optional<Contacto> findByCorreo(String correo);
	
	
	@ApiOperation(value = "Buscar los contactos que tiene una empresa", response = Contacto.class, httpMethod = "GET")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Contacto encontrado"),
			@ApiResponse(code = 404, message = "Contacto no encontrado") })
	@ApiParam(value = "Recibe el identificador de la empresa", required = true, allowMultiple = false)
	List<Contacto> findByEmpresaId(Long id);
}
