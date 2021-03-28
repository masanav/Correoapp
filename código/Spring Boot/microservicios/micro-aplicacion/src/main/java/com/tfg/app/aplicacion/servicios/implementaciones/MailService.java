package com.tfg.app.aplicacion.servicios.implementaciones;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.extern.java.Log;

@Log
@Service
public class MailService {
	
	@Autowired
	private JavaMailSender sender;
	
	@Value("${spring.mail.username}")
	private String remitente;

	public MailService(){}

	public static int noOfQuickServiceThreads = 20;
	
	/**
	 * this statement create a thread pool of twenty threads
	 * here we are assigning send mail task using ScheduledExecutorService.submit();
	 */
	private ScheduledExecutorService quickService = Executors.newScheduledThreadPool(noOfQuickServiceThreads); // Creates a thread pool that reuses fixed number of threads(as specified by noOfThreads in this case).
	
	public void sendASynchronousMail(String toEmail,String subject,String text) throws MailException,RuntimeException{
		log.log(Level.WARNING, "inside sendASynchronousMail method");
		SimpleMailMessage mail=new SimpleMailMessage();
		mail.setFrom(remitente);
		mail.setTo(toEmail);
		mail.setSubject(subject);
		mail.setText("This a ASynchronousMail : "+text);
		quickService.submit(new Runnable() {
			@Override
			public void run() {
				try{
				sender.send(mail);
				}catch(Exception e){
					log.severe("Exception occur while send a mail : "+e.getCause().toString());
				}
			}
		});
	}

}