package com.sofiarizos.backend.controller;

import com.sofiarizos.backend.model.Reserva;
import com.sofiarizos.backend.security.Sanitizer;
import com.sofiarizos.backend.service.ReservaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import java.util.Map;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final Sanitizer sanitizer;

    public ReservaController(
            ReservaService reservaService,
            Sanitizer sanitizer
    ) {
        this.reservaService = reservaService;
        this.sanitizer = sanitizer;
    }

    // ================= CREAR RESERVA =================
    @PostMapping(consumes = "multipart/form-data")
public ResponseEntity<?> crearReserva(
        @RequestParam String nombre,
        @RequestParam String email,
        @RequestParam String telefono,
        @RequestParam String fecha,
        @RequestParam String hora,
        @RequestParam(required = false) String tipoCabello,
        @RequestParam(required = false) String textura,
        @RequestParam(required = false) String cueroCabelludo,
        @RequestParam(required = false) String objetivo,
        @RequestParam(required = false) String rutina,
        @RequestParam(required = false) String productos,
        @RequestParam(required = false) List<MultipartFile> fotos
) {
    try {
        Reserva r = new Reserva();
        r.setNombre(nombre);
        r.setEmail(email);
        r.setTelefono(telefono);
        r.setFecha(LocalDate.parse(fecha));
        r.setHora(LocalTime.parse(hora));
        r.setTipoCabello(tipoCabello);
        r.setTextura(textura);
        r.setCueroCabelludo(cueroCabelludo);
        r.setObjetivo(objetivo);
        r.setRutina(rutina);
        r.setProductos(productos);
        r.setCreadoEn(LocalDateTime.now());

        reservaService.guardarReserva(r);

        // ✅ AQUÍ VA
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("ok", true));

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.internalServerError()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("ok", false));
    }
}


    // ================= HORAS OCUPADAS =================
    @GetMapping("/horas-ocupadas")
    public ResponseEntity<List<String>> obtenerHorasOcupadas(
            @RequestParam String fecha
    ) {
        try {
            LocalDate date = LocalDate.parse(fecha);

            List<String> horas = reservaService.obtenerHorasOcupadas(date)
                    .stream()
                    .map(h -> h.toString().substring(0, 5))
                    .toList();

            return ResponseEntity.ok(horas);

        } catch (Exception e) {
            e.printStackTrace();
            // 🔴 IMPORTANTE: NO devolver 200 en error
            return ResponseEntity.internalServerError().body(List.of());
        }
    }

    // ================= LISTAR =================
    @GetMapping
    public ResponseEntity<List<Reserva>> obtenerReservas() {
        return ResponseEntity.ok(reservaService.obtenerTodas());
    }

    // ================= ELIMINAR =================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarReserva(@PathVariable Long id) {
        reservaService.eliminarPorId(id);
        return ResponseEntity.ok("Reserva eliminada");
    }
}
