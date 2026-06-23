
package tuti.desi.presentacion.publicacion;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tuti.desi.entidades.EstadoPublicacion;
import tuti.desi.entidades.Publicacion;

public class PublicacionForm {

    private Long id;

    @NotNull(message = "El precio mensual es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio mensual debe ser un número positivo")
    private BigDecimal precioMensual;

    @NotBlank(message = "Las condiciones de alquiler son obligatorias")
    private String condiciones;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotNull(message = "La fecha de publicación es obligatoria")
    private LocalDate fechaPublicacion;

    @NotNull(message = "Debe seleccionar una propiedad")
    private Long idPropiedad;
    private EstadoPublicacion estado;

    public PublicacionForm() {
    }

    public PublicacionForm(Publicacion publicacion) {

        this.id = publicacion.getId();

        this.condiciones = publicacion.getCondiciones();
        this.descripcion = publicacion.getDescripcion();
        this.fechaPublicacion = publicacion.getFechaPublicacion();
        this.estado = publicacion.getEstado();
        this.precioMensual = publicacion.getPrecioMensual();
        this.idPropiedad = publicacion.getPropiedad() == null
                ? null
                : publicacion.getPropiedad().getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrecioMensual() {
        return precioMensual;
    }

    public void setPrecioMensual(BigDecimal precioMensual) {
        this.precioMensual = precioMensual;
    }

    public String getCondiciones() {
        return condiciones;
    }

    public void setCondiciones(String condiciones) {
        this.condiciones = condiciones;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public EstadoPublicacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoPublicacion estado) {
        this.estado = estado;
    }

    public Long getIdPropiedad() {
        return idPropiedad;
    }

    public void setIdPropiedad(Long idPropiedad) {
        this.idPropiedad = idPropiedad;
    }
}
