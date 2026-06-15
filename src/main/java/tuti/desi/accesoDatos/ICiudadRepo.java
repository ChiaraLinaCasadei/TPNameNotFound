package tuti.desi.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;

import tuti.desi.entidades.Ciudad;

public interface ICiudadRepo extends JpaRepository<Ciudad, Long> {

}