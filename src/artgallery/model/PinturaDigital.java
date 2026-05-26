package artgallery.model;

public class PinturaDigital extends Obra{
    private String resolucao;
    private String softwareUtilizado;

    public PinturaDigital(String titulo, String autor, String resolucao, String softwareUtilizado){
        super(titulo, autor);
        this.resolucao = resolucao;
        this.softwareUtilizado = softwareUtilizado;
    }

    public String getResolucao() {
        return resolucao;
    }
    public String getSoftwareUtilizado(){
        return softwareUtilizado;
    }

    @Override
    public String exibirDetalhes(){
        return "Titulo: " + getTitulo() + "\nAutor: " + getAutor() + "\nTitulo: Pintura Digital\nResolução: " + getResolucao() + "\nSoftware: " + getSoftwareUtilizado();
    }

}
