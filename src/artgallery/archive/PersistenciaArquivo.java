package artgallery.archive;

import artgallery.model.ArteGenerativa;
import artgallery.model.Modelagem3D;
import artgallery.model.Obra;
import artgallery.model.PinturaDigital;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Vector;

public class PersistenciaArquivo {
    private String DIR = "dados";
    private String OBRAS = DIR + File.separator + "obras.txt";
    private String AVALS = DIR + File.separator + "avaliacoes.txt";
    private String EXPOS = DIR + File.separator + "exposicoes.txt";
    private String SEP = "|";
    private String SEP_RE = "\\|";

    public void inicializar(){
        new File(DIR).mkdirs();
    }
    public void salvarObras(Vector<Obra> obras){
        try{
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(OBRAS), StandardCharsets.UTF_8));
            for(Obra o : obras){
                String linha = "";

                if(o instanceof PinturaDigital){
                    PinturaDigital p = (PinturaDigital) o; //DOWNCASTING
                    linha = "PINTURA" + SEP + escapar(p.getTitulo()) + SEP + escapar(p.getAutor()) + SEP + p.isAtiva() + SEP + escapar(p.getResolucao()) + SEP + escapar(p.getSoftwareUtilizado());
                } else if(o instanceof Modelagem3D){
                    Modelagem3D m = (Modelagem3D) o;
                    linha = "MODELAGEM3D" + SEP + escapar(m.getTitulo()) + SEP + escapar(m.getAutor()) + SEP + m.isAtiva() + SEP + escapar(m.getNumeroPoligonos()) + SEP + escapar(m.getEngine());
                } else if(o instanceof ArteGenerativa){
                    ArteGenerativa a = (ArteGenerativa) o;
                    linha = "ARTEGENERATIVA" + SEP + escapar(a.getTitulo()) + SEP + escapar(a.getAutor()) + SEP + a.isAtiva() + SEP + escapar(a.getAlgoritmo()) + SEP + a.getSeed();
                }

                pw.println(linha);
            }
        }catch (IOException e){
            System.err.println("Erro ao salvar obras: " + e.getMessage());
        }
    }

    public Vector<Obra> carregarObras(){
        Vector<Obra> obras = new Vector<>();
        File arquivo = new File(OBRAS);
        if (!arquivo.exists()) return obras;

        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo), StandardCharsets.UTF_8));
            
        }catch(IOException e){

        }
    }
}
