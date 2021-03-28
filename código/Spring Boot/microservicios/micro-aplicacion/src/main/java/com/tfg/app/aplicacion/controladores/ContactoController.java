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
import com.tfg.app.aplicacion.modelos.entidades.Contacto;
import com.tfg.app.aplicacion.servicios.IContactoService;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/contactos")
@Api(tags = "ContactoController", value = "ContactoController", description = "Controlador para la gestión de contactos")
public class ContactoController {

	@Autowired
	protected IContactoService contacto_servicio;

	// adm
	@Operation(summary = "Muestra todos los contactos", responses = {
			@ApiResponse(responseCode = "200", description = "OK.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Contacto.class)) }) })
	@GetMapping("/")
	public List<Contacto> index() {
		return contacto_servicio.findAll();
	}

	// adm, soporte, usuario
	@Operation(summary = "Muestra los detalles de un contacto buscándolo por su id", responses = {
			@ApiResponse(responseCode = "200", description = "OK.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Contacto.class)) }),
			@ApiResponse(responseCode = "404", description = "Not Found.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object")) }) })
	@GetMapping("/{id}")
	public ResponseEntity<?> ver(@PathVariable Long id) {

		Optional<Contacto> ocontacto = null;
		Contacto contacto = null;
		Map<String, Object> response = new HashMap<>();

		try {
			ocontacto = contacto_servicio.findById(id);

			if (ocontacto.isPresent()) {
				contacto = ocontacto.get();
				return new ResponseEntity<Contacto>(contacto, HttpStatus.OK);
			} else {
				response.put("mensaje",
						"El contacto con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// soporte y adm
	@Operation(summary = "Crea o actualiza un nuevo contacto", responses = {
			@ApiResponse(responseCode = "201", description = "Created.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Contacto.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object")) }) })
	@PostMapping("/")
	public ResponseEntity<?> guardar(@Valid @RequestBody Contacto contacto, BindingResult result) {
		Contacto cnuevo = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			return ControlErrores.validarReturn(result);
		}

		try {
			cnuevo = contacto_servicio.save(contacto);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El contacto ha sido creado con éxito!");
		response.put("contacto", cnuevo);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	// soporte y adm
	@Operation(summary = "Elimina un contacto", responses = {
			@ApiResponse(responseCode = "204", description = "No Content.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Contacto.class)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object")) }) })
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id) {
		contacto_servicio.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Busca un contacto buscándolo por nombre o apellidos", responses = {
			@ApiResponse(responseCode = "200", description = "OK.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Contacto.class)) }),
			@ApiResponse(responseCode = "404", description = "Not Found.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object")) }) })
	// todos
	@GetMapping("/buscar/{term}")
	public List<Contacto> busqueda(@PathVariable String term) {
		return contacto_servicio.findByNombreOrApellidos(term);
	}

	@Operation(summary = "Muestra los contactos de una empresa", responses = {
			@ApiResponse(responseCode = "200", description = "OK.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Contacto.class)) }) })
	@GetMapping("/empresa/{id}")
	public List<Contacto> verPorEmpresa(@PathVariable Long id) {
		return contacto_servicio.findByEmpresaId(id);
	}

	@Operation(summary = "Muestra todos los contactos en el rango indicado", responses = {
			@ApiResponse(responseCode = "200", description = "OK.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Contacto.class)) }) })
	@GetMapping("/pagina")
	public ResponseEntity<?> index(Pageable pageable) {
		return ResponseEntity.ok().body(contacto_servicio.findAll(pageable));
	}

	@Operation(summary = "Muestra todos los contactos coincidentes con el término de búsqueda, nombre o apellido", responses = {
			@ApiResponse(responseCode = "200", description = "OK.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Contacto.class)) }) })
	@GetMapping("/filtrar/{term}")
	public ResponseEntity<?> filtrar(@PathVariable String term){
		return ResponseEntity.ok(contacto_servicio.findByNombreOrApellido(term));
	}
	 
}