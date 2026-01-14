package com.sofiarizos.backend.controller;

import com.sofiarizos.backend.model.Reserva;
import com.sofiarizos.backend.security.Sanitizer;
import com.sofiarizos.backend.service.EmailService;
import com.sofiarizos.backend.service.ReservaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

    private final ReservaService reservaService;
    private final EmailService emailService;
    private final Sanitizer sanitizer;

    private final Pattern phonePattern = Pattern.compile("^\\+?\\d{7,15}$");
    private final Pattern emailPattern =
            Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    public ReservaController(
            ReservaService reservaService,
            EmailService emailService,
            Sanitizer sanitizer
    ) {
        this.reservaService = reservaService;
        this.emailService = emailService;
        this.sanitizer = sanitizer;
    }

    // ================= CREAR RESERVA (JSON) =================
    @PostMapping
    public ResponseEntity<?> crearReserva(@RequestBody Reserva r) {
        try {
            // 🔐 Sanitizar
            r.setNombre(sanitizer.clean(r.getNombre()));
            r.setEmail(sanitizer.clean(r.getEmail()));
            r.setTelefono(sanitizer.clean(r.getTelefono()));

            // ✅ Validaciones
            if (!phonePattern.matcher(r.getTelefono()).matches()) {
                return ResponseEntity.badRequest().body("Teléfono inválido");
            }

            if (!emailPattern.matcher(r.getEmail()).matches()) {
                return ResponseEntity.badRequest().body("Email inválido");
            }

            r.setCreadoEn(LocalDateTime.now());

            Reserva guardada = reservaService.guardarReserva(r);

            // 📧 Email (NO rompe la reserva)
            try {
                emailService.notificarReserva(
                        guardada.getNombre(),
                        guardada.getFecha().toString(),
                        guardada.getHora().toString()
                );
            } catch (Exception e) {
                System.err.println("⚠️ Error enviando correo: " + e.getMessage());
            }

            return ResponseEntity.ok("Reserva creada correctamente");

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error en los datos enviados: " + e.getMessage());
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
            return ResponseEntity.ok(List.of());
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
