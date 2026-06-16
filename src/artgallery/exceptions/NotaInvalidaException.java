package artgallery.exceptions;

public class NotaInvalidaException extends Exception{
    public NotaInvalidaException(int nota){
        super("NOTA INVÁLIDA: "+ nota + ". PRECISA SER ENTRE 0 E 10");
    }
}
