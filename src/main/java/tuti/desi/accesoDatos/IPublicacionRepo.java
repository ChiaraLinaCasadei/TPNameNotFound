package tuti.desi.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import tuti.desi.entidades.Publicacion;
import java.util.List;

public interface IPublicacionRepo extends JpaRepository<Publicacion, Long> {

    List<Publicacion> findByEliminadaFalse();
}