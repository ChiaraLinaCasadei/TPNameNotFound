package tuti.desi.servicios;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuti.desi.presentacion.propiedad.PropiedadForm;
import tuti.desi.entidades.Ciudad;
import tuti.desi.entidades.EstadoDisponibilidad;
import tuti.desi.entidades.HistorialEstadoPropiedad;
import tuti.desi.entidades.Persona;
import tuti.desi.entidades.Propiedad;
import tuti.desi.accesoDatos.ICiudadRepo;
import tuti.desi.accesoDatos.IHistorialEstadoPropiedadRepo;
import tuti.desi.accesoDatos.IPersonaRepo;
import tuti.desi.accesoDatos.IPropiedadRepo;

@Service
@Transactional
public class PropiedadServiceImp implements IPropiedadService {

    private IPropiedadRepo propiedadRepository;
    private ICiudadRepo ciudadRepository;
    private IPersonaRepo personaRepository;
    private IHistorialEstadoPropiedadRepo historialRepository;

    public PropiedadServiceImp(
    		IPropiedadRepo propiedadRepository,
    		ICiudadRepo ciudadRepository,
    		IPersonaRepo personaRepository,
    		IHistorialEstadoPropiedadRepo historialRepository) {

        this.propiedadRepository = propiedadRepository;
        this.ciudadRepository = ciudadRepository;
        this.personaRepository = personaRepository;
        this.historialRepository = historialRepository;
    }

    @Override
    public void crear(PropiedadForm form) {

        Ciudad ciudad = ciudadRepository.findById(form.getIdCiudad())
                .orElseThrow(() -> new RuntimeException("Ciudad inexistente"));

        Persona propietario = personaRepository.findById(form.getIdPropietario())
                .orElseThrow(() -> new RuntimeException("Propietario inexistente"));

        boolean existe = propiedadRepository
                .existsByDireccionIgnoreCaseAndCiudadAndEliminadaFalse(
                        form.getDireccion(),
                        ciudad);

        if (existe) {
            throw new IllegalArgumentException(
                    "Ya existe una propiedad activa con esa dirección y ciudad.");
        }

        Propiedad propiedad = new Propiedad();

        propiedad.setDireccion(form.getDireccion());
        propiedad.setCiudad(ciudad);
        propiedad.setTipo(form.getTipo());
        propiedad.setCantidadAmbientes(form.getCantidadAmbientes());
        propiedad.setMetrosCuadrados(form.getMetrosCuadrados());
        propiedad.setDescripcion(form.getDescripcion());
        propiedad.setComodidades(form.getComodidades());
        propiedad.setPropietario(propietario);

        propiedad.setEliminada(false);

        EstadoDisponibilidad estado =
                form.getEstado() != null
                        ? form.getEstado()
                        : EstadoDisponibilidad.DISPONIBLE;

        propiedad.setEstado(estado);

        propiedadRepository.save(propiedad);

        HistorialEstadoPropiedad historial = new HistorialEstadoPropiedad();

        historial.setPropiedad(propiedad);
        historial.setEstado(estado);
        historial.setFechaHora(LocalDateTime.now());

        historialRepository.save(historial);
    }
}