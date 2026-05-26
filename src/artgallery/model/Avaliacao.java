package artgallery.model;

public class Avaliacao {
    private String usuario;
    private int nota;
    private String comentario;

    public Avaliacao(String usuario, int nota, String comentario){
        this.usuario = usuario;
        this.nota = nota;
        this.comentario = comentario;
    }

    public String getUsuario(){
        return usuario;
    }
    public int getNota(){
        return nota;
    }
    public String getComentario(){
        return comentario;
    }
}
