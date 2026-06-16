package artgallery.model;

import java.util.Vector;

public class Exposicao {
    private String nome;
    private Vector<Obra> obras;

    public Exposicao(String nome){
        this.nome = nome;
        obras = new Vector<Obra>();
    }
    public void adicionarObra(Obra obra){
        obras.add(obra);
    }
    public Vector<Obra> listarObras(){
        return obras;
    }
    public String getNome(){
        return nome;
    }
}
