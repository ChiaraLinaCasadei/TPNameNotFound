package tuti.desi.excepciones;


public class NoSePuedeEliminarPropiedadException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
    public NoSePuedeEliminarPropiedadException(String mensaje) {
        super(mensaje);
    }

}
