package com.tfg.app.aplicacion.controladores;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
import com.tfg.app.aplicacion.modelos.entidades.Proveedor;
import com.tfg.app.aplicacion.servicios.IProveedorService;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/proveedores")
@Api(tags = "ProveedorController", value = "ProveedorController", description = "Controlador para la gestión de proveedores")
public class ProveedorController {

	@Autowired
	protected IProveedorService proveedor_servicio;

	@Operation(summary = "Muestra todos los proveedores", responses = {
			@ApiResponse(responseCode = "200", description = "OK.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Proveedor.class)) }) })
	@GetMapping("/")
	public List<Proveedor> index() {
		return proveedor_servicio.findAll();
	}

	@Operation(summary = "Muestra los detalles de un proveedor buscándolo por su id", responses = {
			@ApiResponse(responseCode = "200", description = "OK.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Proveedor.class)) }),
			@ApiResponse(responseCode = "404", description = "Not Found.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object")) }) })
	@GetMapping("/{id}")
	public ResponseEntity<?> ver(@PathVariable Long id) {

		Optional<Proveedor> oproveedor = null;
		Proveedor proveedor = null;
		Map<String, Object> response = new HashMap<>();

		try {
			oproveedor = proveedor_servicio.findById(id);

			if (oproveedor.isPresent()) {
				proveedor = oproveedor.get();
				return new ResponseEntity<Proveedor>(proveedor, HttpStatus.OK);
			} else {
				response.put("mensaje",
						"El proveedor ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Operation(summary = "Muestra los detalles de un proveedor buscándolo por su id", responses = {
			@ApiResponse(responseCode = "200", description = "OK.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Proveedor.class)) }),
			@ApiResponse(responseCode = "404", description = "Not Found.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object")) }) })
	@GetMapping("/primero")
	public ResponseEntity<?> verPrimero() {

		Optional<Proveedor> oproveedor = null;
		Proveedor proveedor = null;
		Map<String, Object> response = new HashMap<>();

		try {
			oproveedor = proveedor_servicio.findFirstByIdOrderByAsc();

			if (oproveedor.isPresent()) {
				proveedor = oproveedor.get();
				return new ResponseEntity<Proveedor>(proveedor, HttpStatus.OK);
			} else {
				response.put("mensaje", "No hay ni un solo proveedor en la base de datos!");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Operation(summary = "Crea o actualiza un nuevo proveedor", responses = {
			@ApiResponse(responseCode = "201", description = "Created.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Proveedor.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object")) }) })
	@PostMapping("/")
	public ResponseEntity<?> guardar(@Valid @RequestBody Proveedor proveedor, BindingResult result) {
		Proveedor pnuevo = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			ControlErrores.validarReturn(result);
		}

		try {
			pnuevo = proveedor_servicio.save(proveedor);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El proveedor ha sido creado con éxito!");
		response.put("proveedor", pnuevo);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@Operation(summary = "Elimina un proveedor", responses = {
			@ApiResponse(responseCode = "204", description = "No Content.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Proveedor.class)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object")) }) })
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id) {
		proveedor_servicio.deleteById(id);
		return ResponseEntity.noContent().build();
	}

}