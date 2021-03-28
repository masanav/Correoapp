package com.tfg.app.aplicacion.controladores.errores;

public class PreexistingEntityException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public PreexistingEntityException(String message, Throwable cause) {
        super(message, cause);
    }
    public PreexistingEntityException(String message) {
    	super(message);
    }
}
