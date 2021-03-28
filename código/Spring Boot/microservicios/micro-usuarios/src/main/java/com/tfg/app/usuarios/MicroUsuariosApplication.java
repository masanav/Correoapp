package com.tfg.app.usuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.tfg.app.comusuarios.modelos.entidades.Rol;
import com.tfg.app.usuarios.servicios.IRolService;

@EntityScan({"com.tfg.app.comusuarios.modelos.entidades"})
@EnableEurekaClient
@SpringBootApplication
public class MicroUsuariosApplication implements CommandLineRunner {
	
	@Autowired
	private IRolService rol_s;

	public static void main(String[] args) {
		SpringApplication.run(MicroUsuariosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Rol r1 = new Rol();
		Rol r2 = new Rol();
		Rol r3 = new Rol();
		r1.setNombre("ROLE_ADMINISTRADOR");
		r2.setNombre("ROLE_SOPORTE");
		r3.setNombre("ROLE_USUARIO");
		
		rol_s.save(r1);
		rol_s.save(r2);
		rol_s.save(r3);
	}

}
