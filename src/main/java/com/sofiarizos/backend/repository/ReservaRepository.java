package com.sofiarizos.backend.repository;

import com.sofiarizos.backend.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    boolean existsByFechaAndHora(LocalDate fecha, LocalTime hora);

    @Query("SELECT r.hora FROM Reserva r WHERE r.fecha = :fecha")
    List<LocalTime> findHorasByFecha(LocalDate fecha);
}
