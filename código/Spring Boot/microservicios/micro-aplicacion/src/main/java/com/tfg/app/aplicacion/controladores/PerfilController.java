package com.tfg.app.aplicacion.controladores;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tfg.app.aplicacion.controladores.errores.ControlErrores;
import com.tfg.app.aplicacion.controladores.errores.PreexistingEntityException;
import com.tfg.app.aplicacion.controladores.errores.PreservarAdminException;
import com.tfg.app.aplicacion.modelos.entidades.Perfil;
import com.tfg.app.aplicacion.servicios.IPerfilService;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/perfiles")
@Api(tags = "PerfilController", value = "PerfilController", description = "Controlador para la gestión de perfiles")
public class PerfilController {

	@Value("${spring.servlet.multipart.location}")
	private String ruta_fotos_perfil;

	@Autowired
	protected IPerfilService perfil_servicio;

	@Operation(summary = "Muestra todos los perfiles", responses = {
			@ApiResponse(responseCode = "200", description = "OK.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Perfil.class)) }) })
	@GetMapping("/")
	public List<Perfil> index() {
		return perfil_servicio.findAll();
	}

	@Operation(summary = "Muestra los detalles de un perfil buscándolo por su id", responses = {
			@ApiResponse(responseCode = "200", description = "OK.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Perfil.class)) }),
			@ApiResponse(responseCode = "404", description = "Not Found.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object")) }) })
	@GetMapping("/{id}")
	public ResponseEntity<?> ver(@PathVariable Long id) {

		Optional<Perfil> operfil = null;
		Perfil perfil = null;
		Map<String, Object> response = new HashMap<>();

		try {
			operfil = perfil_servicio.findById(id);

			if (operfil.isPresent()) {
				perfil = operfil.get();
				return new ResponseEntity<Perfil>(perfil, HttpStatus.OK);
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

	@Operation(summary = "Crea o actualiza un nuevo perfil", responses = {
			@ApiResponse(responseCode = "201", description = "Created.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Perfil.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object")) }) })
	@PostMapping("/")
	public ResponseEntity<?> guardar(@Valid @RequestBody Perfil perfil, BindingResult result) throws PreservarAdminException {
		Perfil pnuevo = null;
		Map<String, Object> response = new HashMap<>(), errores = new HashMap<>();

		if (result.hasErrors()) {
			errores= ControlErrores.validarMap(result);
		}

		try {
			if (perfil.getEmpresa()==null || perfil.getEmpresa()!=null & perfil.getEmpresa().getId()==null) {
				errores.put("empresa", "Seleccione una empresa");
				return ResponseEntity.badRequest().body(errores);
			}
			pnuevo = perfil_servicio.save(perfil);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			log.error(e.getMostSpecificCause().getMessage());
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (PreexistingEntityException e) {
			errores.put("username", "El username introducido ya esta ocupado");
			return ResponseEntity.badRequest().body(errores);
		} catch (PreservarAdminException e) {
			errores.put("rol", "El administrador no abandona el barco");
			return ResponseEntity.badRequest().body(errores);
		}catch(feign.FeignException e) {
			return ResponseEntity.badRequest().body(errores);
		} catch (Exception e) {
			log.error(e);
		}

		response.put("mensaje", "El perfil ha sido creado con éxito!");
		response.put("perfil", pnuevo);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@Operation(summary = "Elimina un perfil", responses = {
			@ApiResponse(responseCode = "204", description = "No Content.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Perfil.class)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object")) }) })
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id) {
		perfil_servicio.deleteById(id);
		return ResponseEntity.noContent().build();
	}


	@Operation(summary = "Crea o actualiza un nuevo perfil", responses = {
			@ApiResponse(responseCode = "201", description = "Created.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Perfil.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object")) }) })
	@PostMapping("/foto")
	public ResponseEntity<?> guardarConFoto(@Valid Perfil perfil, BindingResult result,
			@RequestParam MultipartFile archivo) throws IOException, PreservarAdminException {
		Perfil pnuevo = null;
		Map<String, Object> response = new HashMap<>(), errores = new HashMap<>();

		if (result.hasErrors()) {
			errores= ControlErrores.validarMap(result);
		}

		try {
			pnuevo = perfil_servicio.saveFoto(perfil, archivo);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (PreexistingEntityException e) {
			errores.put("username", " El username introducido ya esta ocupado");
			return ResponseEntity.badRequest().body(errores);
		} catch (PreservarAdminException e) {
			errores.put("rol", "El administrador no abandona el barco");
			return ResponseEntity.badRequest().body(errores);
		}

		response.put("mensaje", "El perfil con foto ha sido creado con éxito!");
		response.put("perfil", pnuevo);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

	}

	@Operation(summary = "Devuelve el binario de una foto de un perfil", responses = {
			@ApiResponse(responseCode = "200", description = "OK.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Perfil.class)) }),
			@ApiResponse(responseCode = "404", description = "Not Found.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object")) }) })
	@GetMapping("/uploads/img/{id}")
	public ResponseEntity<?> verFoto(@PathVariable Long id) throws IOException {

		Optional<Perfil> o = perfil_servicio.findById(id);

		if (o.isEmpty() || o.get().getFotolob() == null) {
			return ResponseEntity.notFound().build();
		}

		Resource imagen = new ByteArrayResource(o.get().getFotolob());

		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imagen);
	}

	@Operation(summary = "Muestra todos los perfiles en el rango indicado", responses = {
			@ApiResponse(responseCode = "200", description = "OK.", content = {
					@Content(mediaType = "application/json", schema = @Schema(type = "object", implementation = Perfil.class)) }) })
	@GetMapping("/pagina")
	public ResponseEntity<?> index(Pageable pageable) {
		return ResponseEntity.ok().body(perfil_servicio.findAll(pageable));
	}
}