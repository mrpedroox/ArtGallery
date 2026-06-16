package artgallery.exceptions;

public class ObraNaoEncontradaException extends Exception{
    public ObraNaoEncontradaException(String titulo, String autor){
        super("OBRA NÃO ENCONTRADA: " + titulo + " DE " + autor);
    }
}
