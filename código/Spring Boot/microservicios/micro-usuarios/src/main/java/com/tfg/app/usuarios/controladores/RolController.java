package com.tfg.app.usuarios.controladores;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.app.comusuarios.modelos.entidades.Rol;
import com.tfg.app.usuarios.repositorios.RolRepository;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/roles")
@Api(tags = "RolController", value = "RolController", description = "Controlador para la gestión de roles")
public class RolController {

	@Autowired
	private RolRepository rolrepo;

	@Operation(summary = "Muestra todos los roles", responses = {
			@ApiResponse(responseCode = "200", description = "OK.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Rol.class)) })
	})
	@GetMapping("/ver")
	public List<Rol> index() {

		return rolrepo.findAll();
	}

	@Operation(summary = "Muestra los detalles de un rol buscándolo por su id", responses = {
			@ApiResponse(responseCode = "200", description = "OK.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Rol.class)) }),
			@ApiResponse(responseCode = "404", description = "Not Found.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object")) })
	})
	@GetMapping("/ver/{id}")
	public ResponseEntity<?> ver(@PathVariable Long id) {

		Optional<Rol> orol = null;
		Rol rol = null;
		Map<String, Object> response = new HashMap<>();

		try {
			orol = rolrepo.findById(id);

			if (orol.isPresent()) {
				rol = orol.get();
				return new ResponseEntity<Rol>(rol, HttpStatus.OK);
			} else {
				response.put("mensaje",
						"El rol con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}