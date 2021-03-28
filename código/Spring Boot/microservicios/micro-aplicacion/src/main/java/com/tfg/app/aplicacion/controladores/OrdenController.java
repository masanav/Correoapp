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
import com.tfg.app.aplicacion.modelos.entidades.Orden;
import com.tfg.app.aplicacion.servicios.IJavaMailSenderService;
import com.tfg.app.aplicacion.servicios.IOrdenService;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/ordenes")
@Api(tags = "OrdenController", value = "OrdenController", description = "Controlador para la gestión de órdenes")
public class OrdenController {

	@Autowired
	protected IOrdenService orden_servicio;

	@Autowired
	private IJavaMailSenderService javaMailSenderServiceImpl;

	// adm
	@Operation(summary = "Muestra todas las órdenes", responses = {
			@ApiResponse(responseCode = "200", description = "OK.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Orden.class)) }) })
	@GetMapping("/")
	public List<Orden> index() {
		return orden_servicio.findAll();
	}

	// adm, soporte
	@Operation(summary = "Muestra los detalles de una orden buscándolo por su id", responses = {
			@ApiResponse(responseCode = "200", description = "OK.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Orden.class)) }),
			@ApiResponse(responseCode = "404", description = "Not Found.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object")) }) })
	@GetMapping("/{id}")
	public ResponseEntity<?> ver(@PathVariable Long id) {

		Optional<Orden> oorden = null;
		Orden orden = null;
		Map<String, Object> response = new HashMap<>();

		try {
			oorden = orden_servicio.findById(id);

			if (oorden.isPresent()) {
				orden = oorden.get();
				return new ResponseEntity<Orden>(orden, HttpStatus.OK);
			} else {
				response.put("mensaje",
						"La orden con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// soporte y adm y user
	@Operation(summary = "Crea o actualiza una nueva orden", responses = {
			@ApiResponse(responseCode = "201", description = "Created.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Orden.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object")) }) })
	@PostMapping("/")
	public ResponseEntity<?> guardar(@Valid @RequestBody Orden orden, BindingResult result) {
		Orden onuevo = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			ControlErrores.validarReturn(result);
		}

		try {
			onuevo = orden_servicio.save(orden);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert de la orden en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "La orden ha sido creada con éxito!");
		response.put("orden", onuevo);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	// soporte y adm
	@Operation(summary = "Elimina una orden", responses = {
			@ApiResponse(responseCode = "204", description = "No Content.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Orden.class)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object")) }) })
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id) {
		orden_servicio.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Procesa la orden", responses = {
			@ApiResponse(responseCode = "201", description = "Created.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Orden.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object")) }) })
	@PostMapping("/enviar/{idorden}")
	public ResponseEntity<?> enviar(@PathVariable Long idorden) {
		Orden orden = null;
		Map<String, Object> response = new HashMap<>();

		try {
			orden=javaMailSenderServiceImpl.send(idorden);
		} catch (Exception e) {
			response.put("mensaje", "Error al procesar el envío");
			response.put("error", e.getMessage().concat(": ").concat(e.getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "La orden ha sido procesada con éxito!");
		response.put("orden", orden);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@Operation(summary = "Muestra todos las ordenes en el rango indicado", responses = {
			@ApiResponse(responseCode = "200", description = "OK.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Orden.class)) }) })
	@GetMapping("/pagina")
	public ResponseEntity<?> index(Pageable pageable) {
		return ResponseEntity.ok().body(orden_servicio.findAll(pageable));
	}

}