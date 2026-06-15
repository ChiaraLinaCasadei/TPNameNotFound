package tuti.desi.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;

import tuti.desi.entidades.HistorialEstadoPropiedad;

public interface IHistorialEstadoPropiedadRepo
        extends JpaRepository<HistorialEstadoPropiedad, Long> {

}
