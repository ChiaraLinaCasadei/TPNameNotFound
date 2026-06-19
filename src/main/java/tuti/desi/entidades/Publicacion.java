package tuti.desi.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private BigDecimal precioMensual;
    
    private String condiciones;
    
    private LocalDate fechaPublicacion;
    
    private boolean eliminada;

    private String descripcion;

    @Enumerated(EnumType.STRING)
    private EstadoPublicacion estado;

    @ManyToOne(optional = false)
    @JoinColumn(name = "propiedad_id")
    private Propiedad propiedad;

    @OneToMany(mappedBy = "publicacion")
    private List<Visita> visitas = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrecioMensual () {
    	return precioMensual;
    }

    public void setPrecioMensual (BigDecimal precioMensual)  {
        this.precioMensual = precioMensual;
    }

    public String getCondiciones() {
        return condiciones;
    }

    public void setCondiciones(String condiciones) {
        this.condiciones = condiciones;
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
    
    public boolean getEliminada() {
        return eliminada;
    }

    public void setEliminada(boolean eliminada) {
        this.eliminada = eliminada;
    }
    
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Propiedad getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(Propiedad propiedad) {
        this.propiedad = propiedad;
    }

    public List<Visita> getVisitas() {
        return visitas;
    }

    public void setVisitas(List<Visita> visitas) {
        this.visitas = visitas;
    }

}