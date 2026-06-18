package artgallery.service;

import artgallery.archive.PersistenciaArquivo;
import artgallery.exceptions.ObraJaCadastradaException;
import artgallery.exceptions.ObraNaoEncontradaException;
import artgallery.interfaces.IArtGallery;
import artgallery.interfaces.IRepositorioObra;
import artgallery.model.Avaliacao;
import artgallery.model.Exposicao;
import artgallery.model.Obra;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class ArtGallery implements IArtGallery {
    private IRepositorioObra repositorio;
    private Vector<Exposicao> exposicoes;
    public ArtGallery(IRepositorioObra repositorio){
        this.repositorio = repositorio;
        this.exposicoes = PersistenciaArquivo.carregarExposicoes(repositorio.listar());
    }

    @Override
    public void publicarObra(Obra obra){
        try{
            repositorio.cadastrar(obra);
        } catch (ObraJaCadastradaException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removerObra(String titulo){
        Obra obra = repositorio.buscar(titulo);
        if(obra == null) throw new RuntimeException("OBRA NÃO ENCONTRADA");
        if(!obra.isAtiva()) throw new RuntimeException("OBRA JA DESATIVADA");
        repositorio.remover(titulo);
    }

    @Override
    public void avaliarObra(String titulo, Avaliacao avaliacao){
        Obra obra = repositorio.buscar(titulo);
        if(obra == null) throw new RuntimeException("OBRA NÃO ENCONTRADA");
        if(!obra.isAtiva()) throw new RuntimeException("OBRA JA DESATIVADA");
        obra.adicionarAvaliacao(avaliacao);
        try{
            repositorio.atualizar(obra);
        } catch(ObraNaoEncontradaException e){
            throw new RuntimeException(e.getMessage());
        }
    }
    @Override
    public Vector<Obra> listarObras(){
        Vector<Obra> ativas = new Vector<>();
        for(Obra o: repositorio.listar()){
            if(o.isAtiva()) ativas.add(o);
        }
        return ativas;
    }

    @Override
    public Vector<Obra> buscarPorAutor(String autor){
        Vector<Obra> resultado = new Vector<>();
        for(Obra o : repositorio.listar()){
            if(o.getAutor().equalsIgnoreCase(autor)) resultado.add(o);
        }
        return resultado;
    }

    @Override
    public Vector<Obra> topObras(){
        Vector<Obra> todas = new Vector<>(listarObras());
        Collections.sort(todas, new Comparator<Obra>() {
            @Override
            public int compare(Obra o1, Obra o2) {
                return Double.compare(o1.mediaAvaliacoes(), o2.mediaAvaliacoes());
            }
        });
        return todas;
    }

    @Override
    public Vector<Obra> obrasExpostas(String nomeExposicao){
        for(Exposicao e : exposicoes){
            if(e.getNome().equalsIgnoreCase(nomeExposicao)) return e.listarObras();
        }
        return new Vector<>();
    }

    public void criarExposicao(String nome){
        for(Exposicao e : exposicoes){
            if(e.getNome().equalsIgnoreCase(nome)) throw new RuntimeException("EXPOSIÇÃO JA CADASTRADA");
        }
        exposicoes.add(new Exposicao(nome));
        PersistenciaArquivo.salvarExposicoes(exposicoes);
    }

    public void adicionarObraAExposicao(String nomeExposicao, String tituloObra){
        Exposicao exposicao = null;
        for(Exposicao e : exposicoes){
            if(e.getNome().equalsIgnoreCase(nomeExposicao)){
                exposicao = e;
                break;
            }
        }
        if(exposicao == null) throw new RuntimeException("EXPOSIÇÃO NÃO ENCONTRADA");
        Obra obra = repositorio.buscar(tituloObra);
        if(obra == null) throw new RuntimeException("OBRA NÃO ENCONTRADA");
        exposicao.adicionarObra(obra);
        PersistenciaArquivo.salvarExposicoes(exposicoes);
    }
    public Vector<Exposicao> listarExposicoes(){
        return exposicoes;
    }

    public void atualizarObra(Obra obra) {
        try {
            this.repositorio.atualizar(obra);
        } catch (ObraNaoEncontradaException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
