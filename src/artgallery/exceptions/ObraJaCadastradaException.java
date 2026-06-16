package artgallery.exceptions;

public class ObraJaCadastradaException extends Exception{
    public ObraJaCadastradaException(String autor, String titulo){
        super("OBRA JÁ CADASTRADA: "+ titulo + " DE "+ autor );
    }
}
