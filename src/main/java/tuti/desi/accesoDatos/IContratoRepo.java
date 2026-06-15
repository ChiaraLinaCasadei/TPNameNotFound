package tuti.desi.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;

import tuti.desi.entidades.Contrato;
import tuti.desi.entidades.EstadoContrato;
import tuti.desi.entidades.Propiedad;

public interface IContratoRepo extends JpaRepository<Contrato, Long> {

    boolean existsByPropiedadAndEstadoAndEliminadoFalse(
            Propiedad propiedad,
            EstadoContrato estado);

}