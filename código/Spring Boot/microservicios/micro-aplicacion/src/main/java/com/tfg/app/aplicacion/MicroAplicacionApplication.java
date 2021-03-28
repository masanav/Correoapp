package com.tfg.app.aplicacion;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.annotation.PreDestroy;

import org.apache.commons.lang.RandomStringUtils;
import org.loremipsum.LoremIpsum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.github.javafaker.Faker;
import com.tfg.app.aplicacion.clientes.UsuarioClienteFeign;
import com.tfg.app.aplicacion.controladores.errores.PreexistingEntityException;
import com.tfg.app.aplicacion.controladores.errores.PreservarAdminException;
import com.tfg.app.aplicacion.modelos.entidades.Contacto;
import com.tfg.app.aplicacion.modelos.entidades.Empresa;
import com.tfg.app.aplicacion.modelos.entidades.Orden;
import com.tfg.app.aplicacion.modelos.entidades.Perfil;
import com.tfg.app.aplicacion.modelos.entidades.Plantilla;
import com.tfg.app.aplicacion.modelos.entidades.Proveedor;
import com.tfg.app.aplicacion.servicios.IContactoService;
import com.tfg.app.aplicacion.servicios.IEmpresaService;
import com.tfg.app.aplicacion.servicios.IOrdenService;
import com.tfg.app.aplicacion.servicios.IPerfilService;
import com.tfg.app.aplicacion.servicios.IPlantillaService;
import com.tfg.app.aplicacion.servicios.IProveedorService;

@EnableFeignClients
@EnableJpaRepositories
@EnableEurekaClient
@SpringBootApplication
public class MicroAplicacionApplication implements CommandLineRunner {

	@Autowired
	private UsuarioClienteFeign cliente;

	@Autowired
	private IProveedorService proveedor_s;

	@Autowired
	private IContactoService contacto_s;

	@Autowired
	private IPlantillaService plantilla_s;

	@Autowired
	private IEmpresaService empresa_s;
	
	@Autowired
	private IOrdenService orden_s;

	@Autowired
	private IPerfilService perfil_s;

	private Integer t_perfiles=20, t_empresas=10, t_contactos=200, t_plantillas=3;
	private List<Empresa> lempresa = new ArrayList<Empresa>();
	private Proveedor p = new Proveedor();

	public static void main(String[] args) {
		SpringApplication.run(MicroAplicacionApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		precarga_proveedor();
		precarga_empresa();
		precarga_plantillas();
		precarga_contacto();
		precarga_perfiles();
		precarga_ordenes();
	}
	
	public void precarga_ordenes() {
		Orden o = new Orden();
		Plantilla p = new Plantilla ();
	
		p.setId(1L);
		o.setPlantilla(p);
		orden_s.save(o);
	}

	public void precarga_plantillas() {
		Plantilla p = null, pl=null;
		Empresa e = null;
		
		for (Integer i = 0; i < t_plantillas; i++) {
			p = new Plantilla();
			p.setAsunto(LoremIpsum.createSentence(5));
			p.setTexto("Estimado cliente:<br>" + LoremIpsum.createParagraph());
			for (Integer j = 0; j < t_empresas; j++) {
				pl= new Plantilla();
				pl.setAsunto(p.getAsunto());
				pl.setTexto(p.getTexto());
				e = new Empresa();
				e.setId(Long.valueOf(j + 1));
				pl.setEmpresa(e);
				plantilla_s.save(pl);
			}
		}
	}

	public void precarga_contacto() {
		Faker faker = new Faker(new Locale("es"));
		Contacto c = null;
		Empresa e = null;
		
		for (Integer i = 0; i < t_contactos; i++) {
			c = new Contacto();
			c.setNombre(faker.superhero().name()+" "+faker.color().name());
			c.setApellidos(faker.name().lastName() + " " + faker.name().lastName());
			c.setEnabled(true);
			c.setCorreo(faker.internet().emailAddress());
			e = new Empresa();
			e = lempresa.get(1 + (int) (new Random().nextFloat() * (t_empresas - 1)));
			c.setEmpresa(e);
			System.out.println(c);
			contacto_s.save(c);
		}
	}

	public void precarga_proveedor() {
		Faker faker = new Faker(new Locale("es"));
		p.setNombre(faker.company().name());
		p.setDireccion(
				faker.address().streetAddress() + ", " + faker.address().zipCode() + " " + faker.address().city());
		p.setCorreo(faker.internet().emailAddress());
		p.setHorario("L-V de 08:00 a 18:00 y S de 08:00 a 14:00 (solo mañanas)");
		p.setTelefono(faker.phoneNumber().cellPhone());
		p.setUrl_direccion(faker.internet().url());
		System.out.println(p);
		proveedor_s.save(p);
	}

	public void precarga_empresa() throws PreexistingEntityException {
		Faker faker = new Faker(new Locale("es"));

		Empresa e = new Empresa();

		// el proveedor tambien es empresa
		e.setCorreo(p.getCorreo());
		e.setNombre(p.getNombre());
		e.setDireccion(p.getDireccion());
		e.setFirmaCorreo(e.getNombre() + "<br>" + e.getDireccion() + "<br>Correo enviado mediante la plataforma de "
				+ e.getNombre() + " que puede visitar en " + p.getUrl_direccion());
		lempresa.add(e);
		empresa_s.save(e);

		for (Integer i = 0; i < t_empresas - 1; i++) {
			e = new Empresa();
			e.setNombre(faker.company().name());
			e.setDireccion(
					faker.address().streetAddress() + ", " + faker.address().zipCode() + " " + faker.address().city());
			e.setCorreo(faker.internet().emailAddress());
			e.setFirmaCorreo(
					e.getNombre() + "<br>" + e.getDireccion() + "<br>Correo enviado mediante la plataforma de "
							+ e.getNombre() + " que puede visitar en " + p.getUrl_direccion());
			lempresa.add(e);
			empresa_s.save(e);
		}

	}

	public void precarga_perfiles() throws PreexistingEntityException, PreservarAdminException {
		Faker faker = new Faker(new Locale("es"));

		Empresa e = new Empresa();
		
		e.setId(1L);
		Perfil p= new Perfil ("admin","admin",true,1L,"Administrador", "Principal", e);
		perfil_s.save(p);
		
		p= new Perfil ("soporte","soporte",true,2L,"Soporte", "Secundario", e);
		perfil_s.save(p);
		
		p= new Perfil ("usuario","usuario",true,3L,"Usuario", "Tercero", e);
		perfil_s.save(p);
	
		// creo los perfiles
		for (Integer i = 0; i < t_perfiles - 3; i++) {
			e = new Empresa();
			e = lempresa.get(1 + (int) (new Random().nextFloat() * (t_empresas - 1)));
			p = new Perfil();
			p = new Perfil ((faker.dragonBall().character() + faker.animal().name()).replace(" ", ""),RandomStringUtils.random(5, true, true),true,2L,faker.superhero().name(), faker.name().lastName() + " " + faker.name().lastName(), e);
			if (i+1>t_empresas) {
				p.setRol(3L);
			}

			perfil_s.save(p);
		}

	}

	@PreDestroy
	public void destruir() {
		cliente.destruir();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
