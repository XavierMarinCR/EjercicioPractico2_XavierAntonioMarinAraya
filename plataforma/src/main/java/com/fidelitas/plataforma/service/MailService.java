package com.fidelitas.plataforma.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarCorreoBienvenida(String destino, String nombre) {

        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destino);
        mensaje.setSubject("Bienvenido a la Plataforma de Eventos");

        String contenido = "Hola " + nombre + ",\n\n"
                + "Su cuenta ha sido creada exitosamente en la plataforma de eventos.\n"
                + "Ya puede iniciar sesión con su correo y contraseña.\n\n"
                + "Saludos,\nEquipo de Plataforma";

        mensaje.setText(contenido);

        mailSender.send(mensaje);
    }
}