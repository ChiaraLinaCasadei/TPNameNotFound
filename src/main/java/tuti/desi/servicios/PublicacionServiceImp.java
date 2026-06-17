package tuti.desi.servicios;

import java.util.List;

import org.springframework.stereotype.Service;

import tuti.desi.accesoDatos.IPublicacionRepo;
import tuti.desi.entidades.Publicacion;

@Service
public class PublicacionServiceImp implements IPublicacionService {

    private final IPublicacionRepo repo;

    public PublicacionServiceImp(IPublicacionRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<Publicacion> listar() {
        return repo.findByEliminadaFalse();
    }
}
