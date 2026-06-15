package tuti.desi.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;

import tuti.desi.entidades.Ciudad;
import tuti.desi.entidades.Propiedad;

public interface IPropiedadRepo extends JpaRepository<Propiedad, Long> {

    boolean existsByDireccionIgnoreCaseAndCiudadAndEliminadaFalse(
            String direccion,
            Ciudad ciudad);

}