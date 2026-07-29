package tuti.desi.servicios;

import java.util.List;

import tuti.desi.entidades.Propiedad;
import tuti.desi.presentacion.propiedad.PropiedadFiltroDTO;
import tuti.desi.presentacion.propiedad.PropiedadForm;

public interface IPropiedadService {

    void crear(PropiedadForm form);
    void eliminar(Long propiedadId);
    void actualizar(PropiedadForm form);
	List<Propiedad> buscar(PropiedadFiltroDTO filtro);

}