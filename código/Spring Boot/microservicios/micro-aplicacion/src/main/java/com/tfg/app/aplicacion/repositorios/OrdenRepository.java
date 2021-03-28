package com.tfg.app.aplicacion.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.app.aplicacion.modelos.entidades.Contacto;
import com.tfg.app.aplicacion.modelos.entidades.Orden;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
	@ApiOperation(value = "Busca las ordenes que tiene una plantilla", response = Contacto.class, httpMethod = "GET")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Orden encontrada"),
			@ApiResponse(code = 404, message = "Orden no encontrada") })
	@ApiParam(value = "Recibe el identificador de la plantilla", required = true, allowMultiple = false)
	List<Orden> findByPlantillaId(Long id);
}
