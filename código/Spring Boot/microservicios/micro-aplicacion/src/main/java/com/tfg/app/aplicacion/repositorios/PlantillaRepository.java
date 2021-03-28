package com.tfg.app.aplicacion.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.app.aplicacion.modelos.entidades.Plantilla;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Repository
public interface PlantillaRepository extends JpaRepository<Plantilla, Long> {
	
	@ApiOperation(value = "Buscar las plantillas que tiene una empresa", response = Plantilla.class, httpMethod = "GET")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Plantillas encontradas"),
			@ApiResponse(code = 404, message = "La empresa no tiene plantillas") })
	@ApiParam(value = "Recibe el identificador de la empresa", required = true, allowMultiple = false)
	List<Plantilla> findByEmpresaId(Long id);
}
