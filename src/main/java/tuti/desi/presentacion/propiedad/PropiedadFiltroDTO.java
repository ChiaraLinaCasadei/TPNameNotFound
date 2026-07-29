package tuti.desi.presentacion.propiedad;

import tuti.desi.entidades.EstadoDisponibilidad;
import tuti.desi.entidades.TipoPropiedad;

public class PropiedadFiltroDTO {

    private String direccion;
    private Long idCiudad;
    private TipoPropiedad tipo;
    private EstadoDisponibilidad estado;
    
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Long getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(Long idCiudad) {
        this.idCiudad = idCiudad;
    }

    public TipoPropiedad getTipo() {
        return tipo;
    }

    public void setTipo(TipoPropiedad tipo) {
        this.tipo = tipo;
    }

    public EstadoDisponibilidad getEstado() {
        return estado;
    }

    public void setEstado(EstadoDisponibilidad estado) {
        this.estado = estado;
    }
}
