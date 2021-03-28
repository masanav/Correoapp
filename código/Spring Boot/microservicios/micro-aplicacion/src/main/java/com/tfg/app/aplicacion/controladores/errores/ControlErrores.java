package com.tfg.app.aplicacion.controladores.errores;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public class ControlErrores {

	public static ResponseEntity<?> validarReturn(BindingResult result) {
		Map<String, Object> errores = new HashMap<>();
		errores=validarMap(result);
		return ResponseEntity.badRequest().body(errores);
	}
	
	public static Map<String, Object> validarMap(BindingResult result) {
		Map<String, Object> errores = new HashMap<>();
		result.getFieldErrors().forEach(err -> {
			errores.put(err.getField(), " El campo " + err.getField() + " " + err.getDefaultMessage());
		});
		return errores;
	}
}
