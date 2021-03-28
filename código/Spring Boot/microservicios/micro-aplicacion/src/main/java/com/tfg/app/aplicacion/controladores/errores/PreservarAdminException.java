package com.tfg.app.aplicacion.controladores.errores;

public class PreservarAdminException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public PreservarAdminException(String message, Throwable cause) {
        super(message, cause);
    }
    public PreservarAdminException(String message) {
    	super(message);
    }
}
