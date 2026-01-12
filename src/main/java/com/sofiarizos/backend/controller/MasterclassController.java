package com.sofiarizos.backend.controller;

import com.sofiarizos.backend.model.Masterclass;
import com.sofiarizos.backend.service.EmailService;
import com.sofiarizos.backend.service.MasterclassService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/masterclass")
@CrossOrigin(origins = "*")
public class MasterclassController {

    private final MasterclassService masterclassService;
    private final EmailService emailService;

    public MasterclassController(
            MasterclassService masterclassService,
            EmailService emailService
    ) {
        this.masterclassService = masterclassService;
        this.emailService = emailService;
    }

    // ✅ INSCRIPCIÓN MASTERCLASS
    @PostMapping("/masterclass")
    public ResponseEntity<?> inscribirMasterclass(
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String telefono
    ) {
    // Guardar inscripción (BD o lógica)
    
        emailService.notificarMasterclass(nombre, email, telefono);

        return ResponseEntity.ok("Inscripción enviada correctamente");
    }
}
