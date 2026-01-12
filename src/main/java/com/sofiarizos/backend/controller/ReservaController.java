package com.sofiarizos.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofiarizos.backend.model.Reserva;
import com.sofiarizos.backend.security.FileValidator;
import com.sofiarizos.backend.security.Sanitizer;
import com.sofiarizos.backend.service.CloudinaryService;
import com.sofiarizos.backend.service.EmailService;
import com.sofiarizos.backend.service.ReservaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

    private final ReservaService reservaService;
    private final EmailService emailService;
    private final Sanitizer sanitizer;
    private final FileValidator fileValidator;
    private final CloudinaryService cloudinaryService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Pattern phonePattern = Pattern.compile("^\\+?\\d{7,15}$");
    private final Pattern emailPattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    public ReservaController(
            ReservaService reservaService,
            EmailService emailService,
            Sanitizer sanitizer,
            FileValidator fileValidator,
            CloudinaryService cloudinaryService
    ) {
        this.reservaService = reservaService;
        this.emailService = emailService;
        this.sanitizer = sanitizer;
        this.fileValidator = fileValidator;
        this.cloudinaryService = cloudinaryService;
    }

    // ================= CREAR RESERVA =================
    @PostMapping
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
            nombre = sanitizer.clean(nombre);
            email = sanitizer.clean(email);
            telefono = sanitizer.clean(telefono);

            if (!phonePattern.matcher(telefono).matches())
                return ResponseEntity.badRequest().body("Teléfono inválido");

            if (!emailPattern.matcher(email).matches())
                return ResponseEntity.badRequest().body("Email inválido");

            Reserva r = new Reserva();
            r.setNombre(nombre);
            r.setEmail(email);
            r.setTelefono(telefono);
            r.setFecha(LocalDate.parse(fecha));
            r.setHora(LocalTime.parse(hora, DateTimeFormatter.ofPattern("HH:mm")));
            r.setTipoCabello(sanitizer.clean(tipoCabello));
            r.setTextura(sanitizer.clean(textura));
            r.setCueroCabelludo(sanitizer.clean(cueroCabelludo));
            r.setObjetivo(sanitizer.clean(objetivo));
            r.setCreadoEn(LocalDateTime.now());

            if (rutina != null)
                r.setRutina(rutina);

            if (productos != null)
                r.setProductos(productos);

            if (fotos != null && !fotos.isEmpty()) {
                List<String> urls = new ArrayList<>();
                for (MultipartFile f : fotos) {
                    fileValidator.validate(f);
                    urls.add(cloudinaryService.uploadImage(f));
                }
                r.setFotos(objectMapper.writeValueAsString(urls));
            }

            Reserva guardada = reservaService.guardarReserva(r);

            // 📧 EMAIL (NO rompe la reserva)
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
    public ResponseEntity<List<String>> obtenerHorasOcupadas(@RequestParam String fecha) {
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
