package com.tfg.app.aplicacion.servicios.implementaciones;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.tfg.app.aplicacion.modelos.entidades.Contacto;
import com.tfg.app.aplicacion.modelos.entidades.Empresa;
import com.tfg.app.aplicacion.modelos.entidades.Orden;
import com.tfg.app.aplicacion.modelos.entidades.Plantilla;
import com.tfg.app.aplicacion.servicios.IJavaMailSenderService;
import com.tfg.app.aplicacion.servicios.IOrdenService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Primary
public class JavaMailSenderServiceAsyncImpl implements IJavaMailSenderService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private IOrdenService orden_s;

	@Value("${spring.mail.username}")
	private String remitente;

	public static int n_hilos = 20;
	private ScheduledExecutorService quickService = Executors.newScheduledThreadPool(n_hilos);

	public Orden send(Long idorden) throws MailException, RuntimeException {
		MimeMessage mensaje = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mensaje);

		Orden orden = null;
		Optional<Orden> oo = orden_s.findById(idorden);

		if (oo.isPresent()) {
			orden = oo.get();
			
			Plantilla plantilla = orden.getPlantilla();
			Empresa empresa = plantilla.getEmpresa();
			List<Contacto> contactos = empresa.getContactos();

			//final Orden forden = orden;
			quickService.submit(new Runnable() {

				@Override
				public void run() {

					for (Contacto destinatario : contactos) {
						if (destinatario.getEnabled()) {
							try {
								helper.setText(plantilla.getTexto() + "<br>--<br>"
										+ empresa.getFirmaCorreo(), true);
								helper.setSubject(plantilla.getAsunto());
								helper.setTo(destinatario.getCorreo());
								helper.setFrom(remitente);

							} catch (SendFailedException e) {
								log.error("Hubo un error al enviar el mail: {} a {}", e, destinatario);
							} catch (MessagingException e) {
								log.error("Hubo un error al procesar el mail: {} a {}", e, destinatario);
							} catch (Exception e) {
								log.error("ERROR GENERAL");
								System.out.println(e.getMessage());
							}
							javaMailSender.send(mensaje);
							log.info("Correo enviado!");
						} else {
							log.warn(destinatario + " inactivo");
						}
					}
					log.info("Envío terminado");
				}
			});
			if (orden.getPlantilla().getEmpresa().getContactos().size() > 0) {
				orden.setEstado(true);
			}
			
		}
		orden_s.save(orden);
		return orden;
	}
}
