package tuti.desi.accesoDatos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tuti.desi.entidades.Persona;

public interface IPersonaRepo extends JpaRepository<Persona, Long> {

    List<Persona> findAllByOrderByApellidoAscNombreAsc();

}