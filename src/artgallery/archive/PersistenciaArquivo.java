package artgallery.archive;

import artgallery.model.ArteGenerativa;
import artgallery.model.Modelagem3D;
import artgallery.model.Obra;
import artgallery.model.PinturaDigital;
import artgallery.model.Avaliacao;
import artgallery.model.Exposicao;
import artgallery.exceptions.NotaInvalidaException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Vector;

public class PersistenciaArquivo {
    private static String DIR = "dados";
    private static String OBRAS = DIR + File.separator + "obras.txt";
    private static String AVALS = DIR + File.separator + "avaliacoes.txt";
    private static String EXPOS = DIR + File.separator + "exposicoes.txt";
    private static String SEP = "|";
    private static String SEP_RE = "\\|";

    public static void inicializar(){
        new File(DIR).mkdirs();
    }
    public static void salvarObras(Vector<Obra> obras){
        try{
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(OBRAS), StandardCharsets.UTF_8));
            for(Obra o : obras){
                String linha = "";

                if(o instanceof PinturaDigital){
                    PinturaDigital p = (PinturaDigital) o; //DOWNCASTING
                    linha = "PINTURA" + SEP + escapar(p.getTitulo()) + SEP + escapar(p.getAutor()) + SEP + p.isAtiva() + SEP + escapar(p.getResolucao()) + SEP + escapar(p.getSoftwareUtilizado());
                } else if(o instanceof Modelagem3D){
                    Modelagem3D m = (Modelagem3D) o;
                    linha = "MODELAGEM3D" + SEP + escapar(m.getTitulo()) + SEP + escapar(m.getAutor()) + SEP + m.isAtiva() + SEP + m.getNumeroPoligonos() + SEP + escapar(m.getEngine());
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

    public static Vector<Obra> carregarObras(){
        Vector<Obra> obras = new Vector<>();
        File arquivo = new File(OBRAS);
        if (!arquivo.exists()) return obras;

        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo), StandardCharsets.UTF_8));
            String linha;
            while((linha = br.readLine())!=null) {
            	linha = linha.trim();
            	if(linha.isEmpty()) continue;
            	
            	String[] p = linha.split(SEP_RE, -1);
            	if (p.length < 6) continue;
            	String tipo = p[0];
            	String titulo = desescapar(p[1]);
            	String autor = desescapar(p[2]);
            	boolean ativa = Boolean.parseBoolean(p[3]);
            	Obra obra = null;
            	try {
            		switch (tipo) {
            		case "PINTURA":
            			obra = new PinturaDigital(titulo, autor, desescapar(p[4]), desescapar(p[5]));
            			break;
            		case "MODELAGEM3D":
            			obra = new Modelagem3D(titulo, autor, Integer.parseInt(p[4]), desescapar(p[5]));
            			break;
            		case "ARTEGENERATIVA":
            			obra = new ArteGenerativa(titulo, autor, desescapar(p[4]), Long.parseLong(p[5]));
            			break;
            		}
            	} catch(NumberFormatException e) {
            		System.err.println("Linha invalida em obras.txt: " + linha);
            	}
            	
            	if(obra!=null) {
            		obra.setAtiva(ativa);
            		obras.add(obra);
            	}
            }
        }catch(IOException e){
        	System.err.println("Erro ao carregar obras: "+ e.getMessage());
        }
		return obras;
    }
    
    public static void salvarAvaliacoes(Vector<Obra> obras) {
    	try {
    		PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(AVALS), StandardCharsets.UTF_8));
    		for(Obra o : obras) {
    			for(Avaliacao a : o.getAvaliacoes()) {
    				pw.println(escapar(o.getTitulo())+ SEP + escapar(a.getUsuario()) + SEP + a.getNota() + SEP + escapar(a.getComentario()));
    			}
    		}
    	} catch (IOException e) {
    		System.err.println("Erro ao salvar avaliações: " + e.getMessage());
    	}
    }
    public static void carregarAvaliacoes(Vector<Obra> obras) {
    	File arquivo = new File(AVALS);
    	if(!arquivo.exists()) return;
    	
    	try {
    		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo), StandardCharsets.UTF_8));
    		String linha;
    		while((linha = br.readLine())!=null) {
    			linha = linha.trim();
    			if(linha.isEmpty()) continue;
    			String[] p = linha.split(SEP_RE, 4);
    			if(p.length<4) continue;
    			
    			String tituloObra = desescapar(p[0]);
    			String usuario = desescapar(p[1]);
    			int nota;
    			try {
    				nota = Integer.parseInt(p[2]);
    			} catch(NumberFormatException e) {
    				continue;
    			}
    			String comentario = desescapar(p[3]);
    			
    			for(Obra o : obras) {
    				if(o.getTitulo().equalsIgnoreCase(tituloObra)) {
    					try {
    						o.adicionarAvaliacao(new Avaliacao(usuario, nota, comentario));
    					} catch(NotaInvalidaException e) {
    						System.err.println("Avaliação inválida ignorada: " + linha);
    					}
    					break;
    				}
    			}
    		}
    	} catch(IOException e) {
    		System.err.println("Erro ao salvar exposições: " + e.getMessage());
    	}
    }
    public static void salvarExposicoes(Vector<Exposicao> exposicoes) {
    	try {
    		PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(EXPOS), StandardCharsets.UTF_8));
    		for(Exposicao e : exposicoes) {
    			pw.println("EXPOSICAO"+SEP+ escapar(e.getNome()));
    			for(Obra o : e.listarObras()) {
    				pw.println("OBRA"+SEP+escapar(o.getTitulo()));
    			}
    		}
    	} catch(IOException e) {
    		System.err.println("Ero ao salvar exposições: " + e.getMessage());
    	}
    }
    
    public static Vector<Exposicao> carregarExposicoes(Vector<Obra> obras){
    	Vector<Exposicao> exposicoes = new Vector<>();
    	File arquivo = new File(EXPOS);
    	if(!arquivo.exists()) return exposicoes;
    	
    	try {
    		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo), StandardCharsets.UTF_8));
    		Exposicao atual = null;
    		String linha;
    		while((linha = br.readLine())!=null) {
    			linha = linha.trim();
    			if(linha.isEmpty()) continue;
    			
    			String[] p = linha.split(SEP_RE, 2);
    			if(p.length < 2) continue;
    			if("EXPOSICAO".equals(p[0])) {
    				atual = new Exposicao(desescapar(p[1]));
    				exposicoes.add(atual);
    			} else if("OBRA".equals(p[0])&&atual!=null){
					String tituloObra = desescapar(p[1]);
					for(Obra o : obras){
						if(o.getTitulo().equalsIgnoreCase(tituloObra)){
							atual.adicionarObra(o);
							break;
						}
					}
				}
    		}
    	} catch (IOException e){
			System.err.println("Erro ao carregar exposições: " + e.getMessage());
		}
		return exposicoes;
    }
    private static String escapar(String valor){
		if(valor == null) return "";
		return valor.replace("\\", "\\\\").replace("|", "\\pipe");
	}
	private static String desescapar(String valor){
		if(valor == null) return "";
		return valor.replace("\\pipe", "|").replace("\\\\", "\\");
	}
}
