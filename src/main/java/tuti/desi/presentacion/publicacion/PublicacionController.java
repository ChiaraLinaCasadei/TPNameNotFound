package tuti.desi.presentacion.publicacion;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import tuti.desi.accesoDatos.IPropiedadRepo;
import tuti.desi.entidades.EstadoDisponibilidad;
import tuti.desi.servicios.IPublicacionService;
import tuti.desi.presentacion.publicacion.PublicacionForm;

@Controller

@RequestMapping("/publicaciones")
public class PublicacionController {

    private final IPublicacionService publicacionService;
    private final IPropiedadRepo propiedadRepo;

    public PublicacionController(
            IPublicacionService publicacionService,
            IPropiedadRepo propiedadRepo) {

        this.publicacionService = publicacionService;
        this.propiedadRepo = propiedadRepo;
    }

    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "publicaciones",
                publicacionService.listar());

        return "publicacion/listaPublicaciones";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {

        model.addAttribute("publicacionForm", new PublicacionForm());

        model.addAttribute(
                "propiedades",
                propiedadRepo.findByEstadoAndEliminadaFalse(
                        EstadoDisponibilidad.DISPONIBLE));

        return "publicacion/crearPublicacion";
    }

    @PostMapping
    public String guardar(
            @Valid @ModelAttribute PublicacionForm form,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {

            model.addAttribute(
                    "propiedades",
                    propiedadRepo.findByEstadoAndEliminadaFalse(
                            EstadoDisponibilidad.DISPONIBLE));

            return "publicacion/crearPublicacion";
        }

        try {

            publicacionService.crear(form);
            return "redirect:/publicaciones";

        } catch (RuntimeException e) {

            result.reject("error", e.getMessage());

            model.addAttribute(
                    "propiedades",
                    propiedadRepo.findByEstadoAndEliminadaFalse(
                            EstadoDisponibilidad.DISPONIBLE));

            return "publicacion/crearPublicacion";
        }
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {

        publicacionService.eliminar(id);

        return "redirect:/publicaciones";
    }
}
