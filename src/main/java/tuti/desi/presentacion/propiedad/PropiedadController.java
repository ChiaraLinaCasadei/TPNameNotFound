package tuti.desi.presentacion.propiedad;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import tuti.desi.entidades.EstadoDisponibilidad;
import tuti.desi.entidades.Propiedad;
import tuti.desi.entidades.TipoPropiedad;
import tuti.desi.excepciones.NoSePuedeEliminarPropiedadException;
import tuti.desi.accesoDatos.ICiudadRepo;
import tuti.desi.accesoDatos.IPersonaRepo;
import tuti.desi.accesoDatos.IPropiedadRepo;
import tuti.desi.servicios.IPropiedadService;

@Controller
@RequestMapping("/propiedades")
public class PropiedadController {

    private  IPropiedadService propiedadService;
    private  ICiudadRepo ciudadRepository;
    private  IPersonaRepo personaRepository;
    private IPropiedadRepo propiedadRepository;

    public PropiedadController(
    		IPropiedadService propiedadService,
    		ICiudadRepo ciudadRepository,
    		IPersonaRepo personaRepository,
    		IPropiedadRepo propiedadRepository) {

        this.propiedadService = propiedadService;
        this.ciudadRepository = ciudadRepository;
        this.personaRepository = personaRepository;
        this.propiedadRepository = propiedadRepository;
    }

    @GetMapping("/nueva")
    public String mostrarFormulario(Model model, boolean altaExitosa) {

        model.addAttribute("propiedadForm", new PropiedadForm());

        model.addAttribute("ciudades",
                ciudadRepository.findAll());

        model.addAttribute("propietarios",
                personaRepository.findAllByOrderByApellidoAscNombreAsc());

        model.addAttribute("tipos",
                TipoPropiedad.values());

        model.addAttribute("estados",
                EstadoDisponibilidad.values());
        
        if (altaExitosa) {
            model.addAttribute("mensajeExito",
                    "La propiedad fue registrada correctamente.");
        }

        return "propiedad/crearPropiedad";
    }

    @PostMapping
    public String guardar(
            @Valid @ModelAttribute PropiedadForm propiedadForm,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {

            cargarCombos(model);

            return "propiedad/crearPropiedad";
        }

        try {

            propiedadService.crear(propiedadForm);

            return "redirect:/propiedades/nueva?altaExitosa=true";

        } catch (IllegalArgumentException e) {

            bindingResult.reject("duplicada", e.getMessage());

            cargarCombos(model);

            return "propiedad/crearPropiedad";
        }
    }
    
    @GetMapping
    public String listar(Model model) {

        model.addAttribute("propiedades",
                propiedadRepository.findByEliminadaFalse());

        return "propiedad/listado";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id,
                           RedirectAttributes redirectAttributes) {

        try {

            propiedadService.eliminar(id);

            redirectAttributes.addFlashAttribute(
                    "mensajeExito",
                    "Propiedad eliminada correctamente.");

        } catch (NoSePuedeEliminarPropiedadException e) {

            redirectAttributes.addFlashAttribute(
                    "mensajeError",
                    e.getMessage());
        }

        return "redirect:/propiedades";
    }
    
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {

        Propiedad propiedad = propiedadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Propiedad inexistente"));

        PropiedadForm form = new PropiedadForm();

        form.setId(propiedad.getId());
        form.setDireccion(propiedad.getDireccion());
        form.setTipo(propiedad.getTipo());
        form.setCantidadAmbientes(propiedad.getCantidadAmbientes());
        form.setMetrosCuadrados(propiedad.getMetrosCuadrados());
        form.setDescripcion(propiedad.getDescripcion());
        form.setComodidades(propiedad.getComodidades());
        form.setEstado(propiedad.getEstado());

        form.setIdCiudad(propiedad.getCiudad().getId());
        form.setIdPropietario(propiedad.getPropietario().getId());

        model.addAttribute("propiedadForm", form);

        model.addAttribute("ciudades",
                ciudadRepository.findAll());

        model.addAttribute("propietarios",
                personaRepository.findAllByOrderByApellidoAscNombreAsc());

        model.addAttribute("tipos",
                TipoPropiedad.values());

        model.addAttribute("estados",
                EstadoDisponibilidad.values());

        cargarCombos(model);

        return "propiedad/crearPropiedad";
    }
    
    @PostMapping("/{id}")
    public String actualizar(@ModelAttribute PropiedadForm propiedad,
                             RedirectAttributes redirectAttributes) {

        try {

            propiedadService.actualizar(propiedad);

            redirectAttributes.addFlashAttribute(
                    "mensajeExito",
                    "Propiedad actualizada correctamente.");

        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute(
                    "mensajeError",
                    e.getMessage());
        }

        return "redirect:/propiedades";
    }
    

    private void cargarCombos(Model model) {

        model.addAttribute("ciudades", ciudadRepository.findAll());

        model.addAttribute("propietarios",
                personaRepository.findAllByOrderByApellidoAscNombreAsc());

        model.addAttribute("tipos", TipoPropiedad.values());

        model.addAttribute("estados", EstadoDisponibilidad.values());
    }
}