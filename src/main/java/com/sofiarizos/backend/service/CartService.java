package com.sofiarizos.backend.service;

import com.sofiarizos.backend.model.Cart;
import com.sofiarizos.backend.model.CartItem;
import com.sofiarizos.backend.model.Inscripcion;
import com.sofiarizos.backend.repository.CartItemRepository;
import com.sofiarizos.backend.repository.CartRepository;
import com.sofiarizos.backend.repository.InscripcionRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepo;
    private final CartItemRepository itemRepo;
    private final InscripcionRepository insRepo;

    public CartService(CartRepository cartRepo, CartItemRepository itemRepo, InscripcionRepository insRepo) {
        this.cartRepo = cartRepo;
        this.itemRepo = itemRepo;
        this.insRepo = insRepo;
    }

    public Cart createCart() {
        Cart c = new Cart();
        return cartRepo.save(c);
    }

    public Optional<Cart> getCart(String id) { return cartRepo.findById(id); }

    private String clean(String input) {
        if (input == null) return null;
        String s = input.replaceAll("<[^>]*>", "");
        s = s.replaceAll("[\\r\\n]+", " ").trim();
        return StringUtils.trimWhitespace(s);
    }

    public Cart addItem(String cartId, CartItem item) {
        Cart cart = cartRepo.findById(cartId).orElseGet(this::createCart);
        item.setCourseName(clean(item.getCourseName()));
        item.setType(clean(item.getType()));
        item.setCart(cart);
        itemRepo.save(item);
        cart.getItems().add(item);
        return cartRepo.save(cart);
    }

    public Cart removeItem(String cartId, Long itemId) {
        Cart cart = cartRepo.findById(cartId).orElseThrow();
        cart.getItems().removeIf(i -> i.getId().equals(itemId));
        itemRepo.deleteById(itemId);
        return cartRepo.save(cart);
    }

    // Confirmar carrito: llenar datos y guardar inscripciones
    public Cart confirmCart(String cartId, String nombre, String email, String telefono, String comentario) {
        Cart cart = cartRepo.findById(cartId).orElseThrow();
        cart.setNombre(clean(nombre));
        cart.setEmail(clean(email));
        cart.setTelefono(clean(telefono));
        cart.setComentario(clean(comentario));
        cart.setConfirmed(true);

        // Guardar inscripciones individuales (opcional)
        for (CartItem it : cart.getItems()) {
            Inscripcion ins = new Inscripcion();
            ins.setNombre(cart.getNombre());
            ins.setEmail(cart.getEmail());
            ins.setTelefono(cart.getTelefono());
            ins.setComentario(cart.getComentario());
            ins.setCurso(it.getCourseName());
            insRepo.save(ins);
        }

        return cartRepo.save(cart);
    }
}
