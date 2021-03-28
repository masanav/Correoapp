package com.tfg.app.aplicacion.servicios;

import com.tfg.app.aplicacion.modelos.entidades.Orden;

public interface IJavaMailSenderService {
    Orden send(Long idorden);
}
