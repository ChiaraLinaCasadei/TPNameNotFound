package tuti.desi.presentacion.publicacion;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import tuti.desi.entidades.Publicacion;
import tuti.desi.servicios.IPublicacionService;

@Controller
@RequestMapping("/publicaciones")
public class PublicacionController {

    private final IPublicacionService publicacionService;

    public PublicacionController(IPublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

    @GetMapping
    public String listar(Model model) {

        List<Publicacion> publicaciones = publicacionService.listar();

        model.addAttribute("publicaciones", publicaciones);

        System.out.println("Entrando al listado de publicaciones");

        return "publicacion/listado";
    }
}
