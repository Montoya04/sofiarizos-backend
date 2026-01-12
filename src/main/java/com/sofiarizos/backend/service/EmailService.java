package com.sofiarizos.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${admin.email}")
    private String adminEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private void enviar(String asunto, String mensaje) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(adminEmail); // 📩 ADMIN
        email.setSubject(asunto);
        email.setText(mensaje);
        mailSender.send(email);
    }

    // 🔔 RESERVAS
    public void notificarReserva(String nombre, String fecha, String hora) {
        enviar(
            "📌 Nueva reserva registrada",
            "Se ha registrado una nueva reserva:\n\n" +
            "Nombre: " + nombre + "\n" +
            "Fecha: " + fecha + "\n" +
            "Hora: " + hora
        );
    }

    // 🔔 MASTERCLASS PERSONALIZADA
    public void notificarMasterclass(String nombre, String email, String telefono) {
        enviar(
            "🎓 Nueva inscripción – Masterclass personalizada",
            "Nueva inscripción:\n\n" +
            "Nombre: " + nombre + "\n" +
            "Email: " + email + "\n" +
            "Teléfono: " + telefono
        );
    }

    // 🔔 CURSOS
    public void notificarCurso(String curso, String alumno) {
        enviar(
            "📚 Nueva inscripción a curso",
            "Se ha registrado una nueva inscripción:\n\n" +
            "Curso: " + curso + "\n" +
            "Alumno: " + alumno
        );
    }
}

