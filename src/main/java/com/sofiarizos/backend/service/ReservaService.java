package com.sofiarizos.backend.service;

import com.sofiarizos.backend.model.Reserva;
import com.sofiarizos.backend.repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository repo;

    public ReservaService(ReservaRepository repo) {
        this.repo = repo;
    }

    public Reserva guardarReserva(Reserva r) {
        return repo.save(r);
    }

    public boolean existeReserva(LocalDate fecha, LocalTime hora) {
        return repo.existsByFechaAndHora(fecha, hora);
    }

    public List<LocalTime> obtenerHorasOcupadas(LocalDate fecha) {
        return repo.findHorasByFecha(fecha);
    }

    public List<Reserva> obtenerTodas() {
        return repo.findAll();
    }

    public void eliminarPorId(Long id) {
        repo.deleteById(id);
    }
}
