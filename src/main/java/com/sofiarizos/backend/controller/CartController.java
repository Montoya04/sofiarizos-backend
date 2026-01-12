package com.sofiarizos.backend.controller;

import com.sofiarizos.backend.model.Cart;
import com.sofiarizos.backend.model.CartItem;
import com.sofiarizos.backend.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*") // en prod restringir al dominio real
public class CartController {
    private final CartService service;
    public CartController(CartService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<?> createCart() {
        Cart c = service.createCart();
        return ResponseEntity.ok(c);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCart(@PathVariable String id) {
        return service.getCart(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<Cart> addItem(@PathVariable String id, @Valid @RequestBody CartItem item) {
        Cart updated = service.addItem(id, item);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<Cart> removeItem(@PathVariable String id, @PathVariable Long itemId) {
        Cart updated = service.removeItem(id, itemId);
        return ResponseEntity.ok(updated);
    }

    // DTO para confirmar con validaciones
    public static class ConfirmRequest {
        @NotBlank(message = "El nombre es obligatorio")
        public String nombre;

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Email inválido")
        public String email;

        @NotBlank(message = "El teléfono es obligatorio")
        @Pattern(regexp = "^[0-9+\\s\\-]{7,20}$", message = "Teléfono inválido")
        public String telefono;

        public String comentario;
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Cart> confirm(@PathVariable String id, @Valid @RequestBody ConfirmRequest req) {
        Cart confirmed = service.confirmCart(id, req.nombre, req.email, req.telefono, req.comentario);
        return ResponseEntity.ok(confirmed);
    }
}
