package tuti.desi.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;

import tuti.desi.entidades.HistorialEstadoPublicacion;

public interface IHistorialEstadoPublicacionRepo
        extends JpaRepository<HistorialEstadoPublicacion, Long> {

}
