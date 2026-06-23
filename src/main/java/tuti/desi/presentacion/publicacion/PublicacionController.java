package tuti.desi.presentacion.publicacion;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import tuti.desi.accesoDatos.IPropiedadRepo;
import tuti.desi.entidades.EstadoDisponibilidad;
import tuti.desi.entidades.EstadoPublicacion;
import tuti.desi.entidades.Publicacion;
import tuti.desi.servicios.IPublicacionService;

@Controller
@RequestMapping("/publicaciones")
public class PublicacionController {

    private final IPublicacionService publicacionService;
    private final IPropiedadRepo propiedadRepo;

    public PublicacionController(IPublicacionService publicacionService,
                                 IPropiedadRepo propiedadRepo) {
        this.publicacionService = publicacionService;
        this.propiedadRepo = propiedadRepo;
    }

    @GetMapping
    public String listar(
            @RequestParam(required = false) Long idPropiedad,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) EstadoPublicacion estado,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax,
            Model model) {

        List<Publicacion> publicaciones = publicacionService.listar();

        if (idPropiedad != null) {
            publicaciones = publicaciones.stream()
                    .filter(p -> p.getPropiedad() != null &&
                                 p.getPropiedad().getId().equals(idPropiedad))
                    .toList();
        }

        if (ciudad != null && !ciudad.isBlank()) {
            publicaciones = publicaciones.stream()
                    .filter(p -> p.getPropiedad() != null &&
                                 p.getPropiedad().getCiudad() != null &&
                                 p.getPropiedad().getCiudad().getNombre()
                                         .toLowerCase()
                                         .contains(ciudad.toLowerCase()))
                    .toList();
        }

        if (estado != null) {
            publicaciones = publicaciones.stream()
                    .filter(p -> p.getEstado() == estado)
                    .toList();
        }

        if (precioMin != null) {
            publicaciones = publicaciones.stream()
                    .filter(p -> p.getPrecioMensual() != null &&
                                 p.getPrecioMensual().compareTo(precioMin) >= 0)
                    .toList();
        }

        if (precioMax != null) {
            publicaciones = publicaciones.stream()
                    .filter(p -> p.getPrecioMensual() != null &&
                                 p.getPrecioMensual().compareTo(precioMax) <= 0)
                    .toList();
        }

        model.addAttribute("publicaciones", publicaciones);
        return "publicacion/listaPublicaciones";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("publicacionForm", new PublicacionForm());

        model.addAttribute("propiedades",
                propiedadRepo.findByEstadoAndEliminadaFalse(
                        EstadoDisponibilidad.DISPONIBLE));

        return "publicacion/crearPublicacion";
    }

    @PostMapping
    public String guardar(@Valid @ModelAttribute PublicacionForm form,
                          BindingResult result,
                          Model model,
                          RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {

            model.addAttribute("propiedades",
                    propiedadRepo.findByEstadoAndEliminadaFalse(
                            EstadoDisponibilidad.DISPONIBLE));

            return "publicacion/crearPublicacion";
        }

        try {

            if (form.getId() == null) {
                publicacionService.crear(form);
                redirectAttributes.addFlashAttribute("mensajeExito",
                        "Publicación creada correctamente.");
            } else {
                publicacionService.modificar(form);
                redirectAttributes.addFlashAttribute("mensajeExito",
                        "Publicación actualizada correctamente.");
            }

            return "redirect:/publicaciones";

        } catch (RuntimeException e) {

            result.reject("error", e.getMessage());

            model.addAttribute("propiedades",
                    propiedadRepo.findByEstadoAndEliminadaFalse(
                            EstadoDisponibilidad.DISPONIBLE));

            return "publicacion/crearPublicacion";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id,
                           RedirectAttributes redirectAttributes) {

        try {
            publicacionService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Publicación eliminada correctamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    e.getMessage());
        }

        return "redirect:/publicaciones";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {

        model.addAttribute("publicacionForm",
                publicacionService.buscarParaEditar(id));

        model.addAttribute("propiedades",
                propiedadRepo.findByEstadoAndEliminadaFalse(
                        EstadoDisponibilidad.DISPONIBLE));

        return "publicacion/crearPublicacion";
    }
}
