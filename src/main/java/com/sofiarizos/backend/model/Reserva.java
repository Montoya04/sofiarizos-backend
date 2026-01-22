package com.sofiarizos.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "reservas")
public class Reserva {

    // ================= ID =================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================= DATOS PERSONALES =================
    private String nombre;
    private String email;
    private String telefono;

    // ================= FECHA Y HORA =================
    private LocalDate fecha;
    private LocalTime hora;

    // ================= DETALLES =================
    private String tipoCabello;
    private String textura;
    private String cueroCabelludo;
    private String objetivo;

    // ================= CAMPOS JSON =================
    @Column(columnDefinition = "TEXT")
    private String rutina;

    @Column(columnDefinition = "TEXT")
    private String productos;

    @Column(columnDefinition = "TEXT")
    private String fotos;

    // ================= AUDITORÍA =================
    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    // ================= TRANSIENT (PARA EL FRONT) =================
    @Transient
    private List<String> rutinaLista = new ArrayList<>();

    @Transient
    private List<String> productosLista = new ArrayList<>();

    @Transient
    private List<String> fotosLista = new ArrayList<>();

    // ================= JPA CALLBACK =================
    @PostLoad
    public void onLoad() {
        actualizarListas();
    }

    // ================= GETTERS & SETTERS =================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }

    public String getTipoCabello() { return tipoCabello; }
    public void setTipoCabello(String tipoCabello) { this.tipoCabello = tipoCabello; }

    public String getTextura() { return textura; }
    public void setTextura(String textura) { this.textura = textura; }

    public String getCueroCabelludo() { return cueroCabelludo; }
    public void setCueroCabelludo(String cueroCabelludo) { this.cueroCabelludo = cueroCabelludo; }

    public String getObjetivo() { return objetivo; }
    public void setObjetivo(String objetivo) { this.objetivo = objetivo; }

    public String getRutina() { return rutina; }
    public void setRutina(String rutina) { this.rutina = rutina; }

    public String getProductos() { return productos; }
    public void setProductos(String productos) { this.productos = productos; }

    public String getFotos() { return fotos; }
    public void setFotos(String fotos) { this.fotos = fotos; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }

    public List<String> getRutinaLista() { return rutinaLista; }
    public List<String> getProductosLista() { return productosLista; }
    public List<String> getFotosLista() { return fotosLista; }

    // ================= PARSEO JSON =================
    private void actualizarListas() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            if (rutina != null && !rutina.isBlank()) {
                rutinaLista = mapper.readValue(rutina, new TypeReference<List<String>>() {});
            }
        } catch (Exception e) {
            rutinaLista = new ArrayList<>();
        }

        try {
            if (productos != null && !productos.isBlank()) {
                productosLista = mapper.readValue(productos, new TypeReference<List<String>>() {});
            }
        } catch (Exception e) {
            productosLista = new ArrayList<>();
        }

        try {
            if (fotos != null && !fotos.isBlank()) {
                fotosLista = mapper.readValue(fotos, new TypeReference<List<String>>() {});
            }
        } catch (Exception e) {
            fotosLista = new ArrayList<>();
        }
    }
}
