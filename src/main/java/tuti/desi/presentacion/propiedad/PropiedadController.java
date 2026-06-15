package tuti.desi.presentacion.propiedad;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import tuti.desi.entidades.EstadoDisponibilidad;
import tuti.desi.entidades.TipoPropiedad;
import tuti.desi.accesoDatos.ICiudadRepo;
import tuti.desi.accesoDatos.IPersonaRepo;
import tuti.desi.servicios.IPropiedadService;

@Controller
@RequestMapping("/propiedades")
public class PropiedadController {

    private  IPropiedadService propiedadService;
    private  ICiudadRepo ciudadRepository;
    private  IPersonaRepo personaRepository;

    public PropiedadController(
    		IPropiedadService propiedadService,
    		ICiudadRepo ciudadRepository,
    		IPersonaRepo personaRepository) {

        this.propiedadService = propiedadService;
        this.ciudadRepository = ciudadRepository;
        this.personaRepository = personaRepository;
    }

    @GetMapping("/nueva")
    public String mostrarFormulario(Model model, String altaExitosa) {

        model.addAttribute("propiedadForm", new PropiedadForm());

        model.addAttribute("ciudades",
                ciudadRepository.findAll());

        model.addAttribute("propietarios",
                personaRepository.findAllByOrderByApellidoAscNombreAsc());

        model.addAttribute("tipos",
                TipoPropiedad.values());

        model.addAttribute("estados",
                EstadoDisponibilidad.values());
        
        if (altaExitosa != null) {
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

            return "redirect:/propiedades/nueva?altaExitosa";

        } catch (IllegalArgumentException e) {

            bindingResult.reject("duplicada", e.getMessage());

            cargarCombos(model);

            return "propiedad/crearPropiedad";
        }
    }

    private void cargarCombos(Model model) {

        model.addAttribute("ciudades", ciudadRepository.findAll());

        model.addAttribute("propietarios",
                personaRepository.findAllByOrderByApellidoAscNombreAsc());

        model.addAttribute("tipos", TipoPropiedad.values());

        model.addAttribute("estados", EstadoDisponibilidad.values());
    }
}