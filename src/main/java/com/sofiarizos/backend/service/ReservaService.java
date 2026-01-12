package com.sofiarizos.backend.service;

import com.sofiarizos.backend.model.Reserva;
import com.sofiarizos.backend.repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public Reserva guardarReserva(Reserva reserva) {

        if (reserva.getNombre() == null || reserva.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio");

        if (reserva.getEmail() == null || reserva.getEmail().isBlank())
            throw new IllegalArgumentException("El email es obligatorio");

        if (reserva.getTelefono() == null || reserva.getTelefono().isBlank())
            throw new IllegalArgumentException("El teléfono es obligatorio");

        if (reserva.getFecha().isBefore(LocalDate.now()))
            throw new IllegalArgumentException("La fecha no puede ser pasada");

        if (reserva.getHora().isBefore(LocalTime.of(7, 0)) ||
            reserva.getHora().isAfter(LocalTime.of(22, 0)))
            throw new IllegalArgumentException("Hora fuera del rango permitido");

        if (reservaRepository.existsByFechaAndHora(reserva.getFecha(), reserva.getHora()))
            throw new IllegalArgumentException("La hora seleccionada ya está ocupada");

        return reservaRepository.save(reserva);
    }

    public List<Reserva> obtenerTodas() {
    List<Reserva> reservas = reservaRepository.findAll();
    reservas.forEach(Reserva::actualizarListas);
    return reservas;
}

    public List<LocalTime> obtenerHorasOcupadas(LocalDate fecha) {
        return reservaRepository.findHorasByFecha(fecha);
    }

    public void eliminarPorId(Long id) {
        reservaRepository.deleteById(id);
    }
}
