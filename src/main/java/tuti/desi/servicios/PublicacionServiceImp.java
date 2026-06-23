package tuti.desi.servicios;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuti.desi.accesoDatos.IHistorialEstadoPublicacionRepo;
import tuti.desi.accesoDatos.IPublicacionRepo;
import tuti.desi.accesoDatos.IPropiedadRepo;
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

    public PublicacionServiceImp(IPublicacionRepo repo,
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
                .orElseThrow(() -> new RuntimeException("Propiedad inexistente"));

        if (propiedad.getEstado() != EstadoDisponibilidad.DISPONIBLE) {
            throw new RuntimeException("Propiedad no disponible.");
        }

        if (repo.existsByPropiedadAndEstadoAndEliminadaFalse(
                propiedad, EstadoPublicacion.ACTIVA)) {
            throw new RuntimeException("Ya existe una publicación activa.");
        }

        Publicacion p = new Publicacion();
        p.setPropiedad(propiedad);
        p.setPrecioMensual(form.getPrecioMensual());
        p.setCondiciones(form.getCondiciones());
        p.setDescripcion(form.getDescripcion());
        p.setFechaPublicacion(form.getFechaPublicacion());
        p.setEstado(EstadoPublicacion.ACTIVA);
        p.setEliminada(false);

        repo.save(p);

        registrarHistorial(p, EstadoPublicacion.ACTIVA);
    }

    @Override
    public void modificar(PublicacionForm form) {

        Publicacion p = repo.findById(form.getId())
                .orElseThrow(() -> new RuntimeException("No encontrada"));

        p.setPrecioMensual(form.getPrecioMensual());
        p.setCondiciones(form.getCondiciones());
        p.setDescripcion(form.getDescripcion());
        p.setFechaPublicacion(form.getFechaPublicacion());

        if (form.getEstado() != null && form.getEstado() != p.getEstado()) {

            if (form.getEstado() == EstadoPublicacion.ACTIVA) {

                boolean existeActiva =
                        repo.existsByPropiedadAndEstadoAndEliminadaFalse(
                                p.getPropiedad(), EstadoPublicacion.ACTIVA);

                if (existeActiva && p.getEstado() != EstadoPublicacion.ACTIVA) {
                    throw new RuntimeException("Ya existe una publicación activa.");
                }

                if (p.getPropiedad().getEstado() != EstadoDisponibilidad.DISPONIBLE) {
                    throw new RuntimeException("Propiedad no disponible.");
                }
            }

            p.setEstado(form.getEstado());

            registrarHistorial(p, form.getEstado());
        }

        repo.save(p);
    }

    @Override
    public void eliminar(Long id) {

        Publicacion p = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("No encontrada"));

        p.setEliminada(true);
        repo.save(p);
    }

    @Override
    public PublicacionForm buscarParaEditar(Long id) {

        return new PublicacionForm(
                repo.findById(id)
                        .orElseThrow(() -> new RuntimeException("No encontrada"))
        );
    }

    private void registrarHistorial(Publicacion p, EstadoPublicacion estado) {

        HistorialEstadoPublicacion h = new HistorialEstadoPublicacion();
        h.setPublicacion(p);
        h.setEstado(estado);
        h.setFechaHora(LocalDateTime.now());

        historialRepo.save(h);
    }
}