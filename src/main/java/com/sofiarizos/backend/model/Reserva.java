package com.sofiarizos.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String email;
    private String telefono;
    private LocalDate fecha;
    private LocalTime hora;
    private String tipoCabello;
    private String textura;
    private String cueroCabelludo;
    private String objetivo;

    @Column(columnDefinition = "TEXT")
    private String rutina;

    @Column(columnDefinition = "TEXT")
    private String productos;

    @Column(columnDefinition = "TEXT")
    private String fotos;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    // ---------------- TRANSIENT ----------------
    @Transient
    public List<String> fotosLista = new ArrayList<>();

    @Transient
    public List<String> rutinaLista = new ArrayList<>();

    @Transient
    public List<String> productosLista = new ArrayList<>();

    // ---------------- GETTERS Y SETTERS ----------------
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

    // ---------------- MÉTODO PARA PARSEAR JSON ----------------
    public void actualizarListas() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (this.fotos != null && !this.fotos.isEmpty()) {
                this.fotosLista = mapper.readValue(this.fotos, new TypeReference<List<String>>() {});
            }
        } catch (Exception e) { this.fotosLista = new ArrayList<>(); }

        try {
            if (this.rutina != null && !this.rutina.isEmpty()) {
                this.rutinaLista = mapper.readValue(this.rutina, new TypeReference<List<String>>() {});
            }
        } catch(Exception e) { this.rutinaLista = new ArrayList<>(); }

        try {
            if (this.productos != null && !this.productos.isEmpty()) {
                this.productosLista = mapper.readValue(this.productos, new TypeReference<List<String>>() {});
            }
        } catch(Exception e) { this.productosLista = new ArrayList<>(); }
    }
}
