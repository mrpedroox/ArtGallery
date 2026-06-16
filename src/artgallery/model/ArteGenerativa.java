package artgallery.model;

public class ArteGenerativa extends Obra{
    private String algoritmo;
    private long seed;

    public ArteGenerativa(String autor, String titulo, String algoritmo, long seed){
        super(titulo, autor);
        this.algoritmo = algoritmo;
        this.seed = seed;
    }
    public String getAlgoritmo(){
        return algoritmo;
    }

    public long getSeed(){
        return seed;
    }

    @Override
    public String exibirDetalhes(){
        return "Titulo: " + getTitulo() + "\nAutor: " + getAutor() + "\nTipo: Arte Generativa\nAlgoritmo: " + getAlgoritmo() + "\nSeed: " + getSeed();
    }
}
