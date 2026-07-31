package tuti.desi.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tuti.desi.entidades.Ciudad;
import tuti.desi.entidades.Propiedad;
import tuti.desi.entidades.TipoPropiedad;
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

    @Query("""
            SELECT p
            FROM Propiedad p
            WHERE p.eliminada = false
              AND (
                    :direccion IS NULL
                    OR :direccion = ''
                    OR LOWER(p.direccion) LIKE LOWER(CONCAT('%', :direccion, '%'))
              )
              AND (
                    :idCiudad IS NULL
                    OR p.ciudad.id = :idCiudad
              )
              AND (
                    :tipo IS NULL
                    OR p.tipo = :tipo
              )
              AND (
                    :estado IS NULL
                    OR p.estado = :estado
              )
            ORDER BY p.id ASC
            """)
        List<Propiedad> buscar(
                @Param("direccion") String direccion,
                @Param("idCiudad") Long idCiudad,
                @Param("tipo") TipoPropiedad tipo,
                @Param("estado") EstadoDisponibilidad estado);
}