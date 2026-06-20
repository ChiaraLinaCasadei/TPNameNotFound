package tuti.desi.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;

import tuti.desi.entidades.Ciudad;
import tuti.desi.entidades.Propiedad;
import tuti.desi.entidades.EstadoDisponibilidad;

import java.util.List;

public interface IPropiedadRepo extends JpaRepository<Propiedad, Long> {

    boolean existsByDireccionIgnoreCaseAndCiudadAndEliminadaFalse(
            String direccion,
            Ciudad ciudad);
    
    List<Propiedad> findByEliminadaFalse();
    
    boolean existsByDireccionIgnoreCaseAndCiudadAndEliminadaFalseAndIdNot(
            String direccion,
            Ciudad ciudad,
            Long id);
    
    List<Propiedad> findByEstadoAndEliminadaFalse(
            EstadoDisponibilidad estado);

}