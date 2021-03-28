package com.tfg.app.usuarios.controladores;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.app.comusuarios.modelos.entidades.Usuario;
import com.tfg.app.usuarios.servicios.IUsuarioService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/usuarios")
@Api(tags = "UsuarioController", value = "UsuarioController", description = "Controlador para la gestión de usuarios")
public class UsuarioController {
	public static final String USER_HEADER = "Username";

	@Autowired
	protected IUsuarioService usuario_servicio;

	@Operation(summary = "Endpoint de prueba para recibir parámetros de los headers")
	@GetMapping("/prueba/*")
	public String detalle(@RequestHeader("Username") String username) {

		log.info(username);
		return "nada";
	}

	@Operation(summary = "Muestra todos los usuarios", responses = {
			@ApiResponse(responseCode = "200", description = "OK.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Usuario.class)) }) })
	@GetMapping("/ver")
	public List<Usuario> index() {
		return usuario_servicio.findAll();
	}

	@Operation(summary = "Muestra los detalles de un usuario buscándolo por su id", responses = {
			@ApiResponse(responseCode = "200", description = "OK.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Usuario.class)) }),
			@ApiResponse(responseCode = "404", description = "Not Found.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object")) }) })
	@GetMapping("/ver/{id}")
	public ResponseEntity<?> ver(@PathVariable Long id) {
		log.info("viendo");

		Optional<Usuario> ousuario = null;
		Usuario usuario = null;
		Map<String, Object> response = new HashMap<>();

		try {
			ousuario = usuario_servicio.findById(id);

			if (ousuario.isPresent()) {
				usuario = ousuario.get();
				return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
			} else {
				response.put("mensaje",
						"El usuario ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Operation(summary = "Muestra los detalles de un usuario buscándolo por su username", responses = {
			@ApiResponse(responseCode = "200", description = "OK.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Usuario.class)) }),
			@ApiResponse(responseCode = "404", description = "Not Found.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object")) }) })
	@GetMapping("/buscar")
	public ResponseEntity<?> buscar(@RequestParam String username) {
		log.info("buscando");
		log.info(username);

		Optional<Usuario> ousuario = null;
		Usuario usuario = null;
		Map<String, Object> response = new HashMap<>();
		try {
			ousuario = usuario_servicio.findByUsername(username);
			if (ousuario.isPresent()) {
				usuario = ousuario.get();
				log.info("usuario encontrado");
				System.out.println(usuario);
				log.info(usuario.getUsername() + " " + usuario.getRol().getNombre());
				return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
			} else {
				response.put("mensaje", "El usuario : ".concat(username.concat(" no existe en la base de datos!")));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Operation(summary = "Crea o actualiza un nuevo usuario", responses = {
			@ApiResponse(responseCode = "201", description = "Created.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Usuario.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object")) })
	})
	@PostMapping("/guardar")
	public ResponseEntity<?> guardar(@Valid @RequestBody Usuario usuario, BindingResult result) {
		Usuario unuevo = null;
		log.info("guardando");
		System.out.println(usuario);
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			log.error("si hay errores");
			System.out.println(result);
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					// .map(err -> {return "El campo '" + err.getField() +"' "+
					// err.getDefaultMessage();})
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		log.error("no hay errores");

		try {
			unuevo = usuario_servicio.save(usuario);
		} catch (DataAccessException e) {
			log.error("error en la data exception");
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El perfil ha sido creado con éxito!");
		response.put("usuario", unuevo);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@Operation(summary = "Borra un usuario", responses = {
			@ApiResponse(responseCode = "204", description = "No Content.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Usuario.class)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object")) }) })
	@DeleteMapping("/ver/{id}")
	public ResponseEntity<?> eliminarId(@PathVariable Long id) {
		usuario_servicio.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	@Operation(summary = "Borra un usuario", responses = {
			@ApiResponse(responseCode = "204", description = "No Content.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Usuario.class)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object")) }) })
	@DeleteMapping("/borrar")
	public ResponseEntity<?> eliminarIds(@RequestParam List<Long> ids) {
		log.error("solicita eliminacion");
		log.error(ids);
		ids.forEach(p->System.out.println(p));
		usuario_servicio.deleteByIds(ids);
		return ResponseEntity.noContent().build();
	}

	@ApiOperation(value = "Endpoint para facilitar el desarrollo del microservicio de aplicación")
	@DeleteMapping("/destruir")
	public void destruir() {
		usuario_servicio.truncar();
	}
}