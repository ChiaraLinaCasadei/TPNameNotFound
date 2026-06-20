package tuti.desi.servicios;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuti.desi.accesoDatos.IHistorialEstadoPublicacionRepo;
import tuti.desi.accesoDatos.IPropiedadRepo;
import tuti.desi.accesoDatos.IPublicacionRepo;
import tuti.desi.entidades.EstadoDisponibilidad;
import tuti.desi.entidades.EstadoPublicacion;
import tuti.desi.entidades.HistorialEstadoPublicacion;
import tuti.desi.entidades.Propiedad;
import tuti.desi.entidades.Publicacion;
import tuti.desi.presentacion.publicacion.PublicacionForm;

@Service
@Transactional
public class PublicacionServiceImp implements IPublicacionService {

    private final IPublicacionRepo repo;
    private final IPropiedadRepo propiedadRepo;
    private final IHistorialEstadoPublicacionRepo historialRepo;

    public PublicacionServiceImp(
            IPublicacionRepo repo,
            IPropiedadRepo propiedadRepo,
            IHistorialEstadoPublicacionRepo historialRepo) {

        this.repo = repo;
        this.propiedadRepo = propiedadRepo;
        this.historialRepo = historialRepo;
    }

    @Override
    public List<Publicacion> listar() {
        return repo.findByEliminadaFalse();
    }

    @Override
    public void crear(PublicacionForm form) {

        Propiedad propiedad = propiedadRepo.findById(form.getIdPropiedad())
                .orElseThrow(() ->
                        new RuntimeException("Propiedad inexistente"));

        if (propiedad.getEstado() != EstadoDisponibilidad.DISPONIBLE) {
            throw new RuntimeException(
                    "Solo pueden publicarse propiedades disponibles.");
        }

        boolean existeActiva =
                repo.existsByPropiedadAndEstadoAndEliminadaFalse(
                        propiedad,
                        EstadoPublicacion.ACTIVA);

        if (existeActiva) {
            throw new RuntimeException(
                    "Ya existe una publicación activa para esta propiedad.");
        }

        Publicacion publicacion = new Publicacion();

        publicacion.setPropiedad(propiedad);
        publicacion.setPrecioMensual(form.getPrecioMensual());
        publicacion.setCondiciones(form.getCondiciones());
        publicacion.setDescripcion(form.getDescripcion());
        publicacion.setFechaPublicacion(form.getFechaPublicacion());

        publicacion.setEstado(EstadoPublicacion.ACTIVA);
        publicacion.setEliminada(false);

        repo.save(publicacion);

        HistorialEstadoPublicacion historial =
                new HistorialEstadoPublicacion();

        historial.setPublicacion(publicacion);
        historial.setEstado(EstadoPublicacion.ACTIVA);
        historial.setFechaHora(LocalDateTime.now());

        historialRepo.save(historial);
    }
    
    @Override
    public void eliminar(Long id) {

        Publicacion pub = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

        pub.setEliminada(true);

        repo.save(pub);
    }
}
