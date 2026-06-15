package tuti.desi.servicios;

import tuti.desi.presentacion.propiedad.PropiedadForm;

public interface IPropiedadService {

    void crear(PropiedadForm form);
    void eliminar(Long propiedadId);
    void actualizar(PropiedadForm form);

}