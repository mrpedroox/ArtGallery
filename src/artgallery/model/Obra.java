package artgallery.model;

import java.util.Vector;

public abstract class Obra {
    private String titulo;
    private String autor;
    private boolean ativa;
    private Vector<Avaliacao> avaliacoes;

    public Obra(String titulo, String autor){
        this.titulo = titulo;
        this.autor = autor;
        ativa = true;
        avaliacoes = new Vector<Avaliacao>();
    }

    public String getTitulo(){
        return titulo;
    }
    public String getAutor(){
        return autor;
    }
    public boolean isAtiva(){
        return ativa;
    }
    public void setAtiva(boolean ativa){
        this.ativa = ativa;
    }

    public void adicionarAvaliacao(Avaliacao avaliacao){avaliacoes.add(avaliacao);}

    public double mediaAvaliacoes(){
    	if (avaliacoes.isEmpty()) return 0;
        double soma = 0;
        for (Avaliacao a : avaliacoes) soma += a.getNota();
        return soma / avaliacoes.size();
    }
    
    public Vector<Avaliacao> getAvaliacoes(){
    	return avaliacoes;
    }

    public abstract String exibirDetalhes();
}
