package com.tfg.app.aplicacion.controladores;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.app.aplicacion.controladores.errores.ControlErrores;
import com.tfg.app.aplicacion.modelos.entidades.Plantilla;
import com.tfg.app.aplicacion.servicios.IPlantillaService;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/plantillas")
@Api(tags = "PlantillaController", value = "PlantillaController", description = "Controlador para la gestión de plantillas")
public class PlantillaController {

	@Autowired
	protected IPlantillaService plantilla_servicio;

	// adm
	@Operation(summary = "Muestra todas las plantillas", responses = {
			@ApiResponse(responseCode = "200", description = "OK.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Plantilla.class)) }) })
	@GetMapping("/")
	public List<Plantilla> index() {
		return plantilla_servicio.findAll();
	}

	// adm, soporte, usuario
	@Operation(summary = "Muestra los detalles de una plantilla buscándolo por su id", responses = {
			@ApiResponse(responseCode = "200", description = "OK.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Plantilla.class)) }),
			@ApiResponse(responseCode = "404", description = "Not Found.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object")) }) })
	@GetMapping("/{id}")
	public ResponseEntity<?> ver(@PathVariable Long id) {

		Optional<Plantilla> oplantilla = null;
		Plantilla plantilla = null;
		Map<String, Object> response = new HashMap<>();

		try {
			oplantilla = plantilla_servicio.findById(id);

			if (oplantilla.isPresent()) {
				plantilla = oplantilla.get();
				return new ResponseEntity<Plantilla>(plantilla, HttpStatus.OK);
			} else {
				response.put("mensaje",
						"La plantilla con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Operation(summary = "Crea o actualiza una nueva plantilla", responses = {
			@ApiResponse(responseCode = "201", description = "Created.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Plantilla.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object")) }) })
	// soporte y adm
	@PostMapping("/")
	public ResponseEntity<?> guardar(@Valid @RequestBody Plantilla plantilla, BindingResult result) {
		Plantilla pnuevo = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			return ControlErrores.validarReturn(result);
		}

		try {
			pnuevo = plantilla_servicio.save(plantilla);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "La plantilla ha sido creado con éxito!");
		response.put("plantilla", pnuevo);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	// soporte y adm
	@Operation(summary = "Elimina una plantilla", responses = {
			@ApiResponse(responseCode = "204", description = "No Content.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Plantilla.class)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object")) }) })
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id) {
		plantilla_servicio.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Muestra las plantillas de una empresa", responses = {
			@ApiResponse(responseCode = "200", description = "OK.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Plantilla.class)) }) })
	@GetMapping("/empresa/{id}")
	public List<Plantilla> verPorEmpresa(@PathVariable Long id) {
		return plantilla_servicio.findByEmpresaId(id);
	}
	
	@Operation(summary = "Muestra todos las plantillas en el rango indicado", responses = {
			@ApiResponse(responseCode = "200", description = "OK.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Plantilla.class)) }) })
	@GetMapping("/pagina")
	public ResponseEntity<?> index(Pageable pageable) {
		return ResponseEntity.ok().body(plantilla_servicio.findAll(pageable));
	}

}