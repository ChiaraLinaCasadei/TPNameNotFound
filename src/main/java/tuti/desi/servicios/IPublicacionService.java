package tuti.desi.servicios;

import java.util.List;

import tuti.desi.entidades.Publicacion;
import tuti.desi.presentacion.publicacion.PublicacionForm;

public interface IPublicacionService {

    List<Publicacion> listar();

    void crear(PublicacionForm form);
    
    void eliminar(Long id);
    
    PublicacionForm buscarParaEditar(Long id);

    void modificar(PublicacionForm form);
}

