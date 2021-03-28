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
import com.tfg.app.aplicacion.controladores.errores.PreexistingEntityException;
import com.tfg.app.aplicacion.modelos.entidades.Empresa;
import com.tfg.app.aplicacion.servicios.IEmpresaService;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/empresas")
@Api(tags = "EmpresaController", value = "EmpresaController", description = "Controlador para la gestión de empresas")
public class EmpresaController {

	@Autowired
	protected IEmpresaService empresa_servicio;

	// adm
	@Operation(summary = "Muestra todos los roles", responses = {
			@ApiResponse(responseCode = "200", description = "OK.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Empresa.class)) }) })
	@GetMapping("/")
	public List<Empresa> index() {
		return empresa_servicio.findAll();
	}

	// adm, soporte, usuario
	@Operation(summary = "Muestra los detalles de una empresa buscándolo por su id", responses = {
			@ApiResponse(responseCode = "200", description = "OK.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Empresa.class)) }),
			@ApiResponse(responseCode = "404", description = "Not Found.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object")) }) })
	@GetMapping("/{id}")
	public ResponseEntity<?> ver(@PathVariable Long id) {

		Optional<Empresa> oempresa = null;
		Empresa empresa = null;
		Map<String, Object> response = new HashMap<>();

		try {
			oempresa = empresa_servicio.findById(id);

			if (oempresa.isPresent()) {
				empresa = oempresa.get();
				return new ResponseEntity<Empresa>(empresa, HttpStatus.OK);
			} else {
				response.put("mensaje",
						"La empresa con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// soporte y adm
	@Operation(summary = "Crea o actualiza una nueva empresa", responses = {
			@ApiResponse(responseCode = "201", description = "Created.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Empresa.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object")) }) })
	@PostMapping("/")
	public ResponseEntity<?> guardar(@Valid @RequestBody Empresa empresa, BindingResult result) throws PreexistingEntityException {
		Empresa enuevo = null;
		Map<String, Object> response = new HashMap<>(), errores = new HashMap<>();;

		if (result.hasErrors()) {
			errores= ControlErrores.validarMap(result);
		}

		try {
			enuevo = empresa_servicio.save(empresa);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (PreexistingEntityException e) {
			errores.put("correo", e.getMessage());
			return ResponseEntity.badRequest().body(errores);
		}

		response.put("mensaje", "La empresa ha sido creado con éxito!");
		response.put("empresa", enuevo);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	// soporte y adm
	@Operation(summary = "Elimina una empresa", responses = {
			@ApiResponse(responseCode = "204", description = "No Content.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Empresa.class)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object")) }) })
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id) {
		empresa_servicio.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	@Operation(summary = "Muestra todos las empresas en el rango indicado", responses = {
			@ApiResponse(responseCode = "200", description = "OK.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Empresa.class)) }) })
	@GetMapping("/pagina")
	public ResponseEntity<?> index(Pageable pageable) {
		return ResponseEntity.ok().body(empresa_servicio.findAll(pageable));
	}

}