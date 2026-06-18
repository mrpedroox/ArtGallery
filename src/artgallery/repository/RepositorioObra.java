package artgallery.repository;

import artgallery.archive.PersistenciaArquivo;
import artgallery.exceptions.ObraJaCadastradaException;
import artgallery.exceptions.ObraNaoEncontradaException;
import artgallery.interfaces.IRepositorioObra;
import artgallery.model.Obra;

import java.util.Vector;

public class RepositorioObra implements IRepositorioObra {
    private Vector<Obra> obras;

    public RepositorioObra(){
        PersistenciaArquivo.inicializar();
        obras = PersistenciaArquivo.carregarObras();
        PersistenciaArquivo.carregarAvaliacoes(obras);
    }
    @Override
    public void cadastrar(Obra obra) throws ObraJaCadastradaException {
        for(Obra o : obras){
            if(o.getTitulo().equalsIgnoreCase(obra.getTitulo()) && o.getAutor().equalsIgnoreCase(obra.getAutor())) throw new ObraJaCadastradaException(obra.getAutor(), obra.getTitulo());
        }
        obras.add(obra);
        persistir();
    }

    @Override
    public Obra buscar(String titulo){
        for(Obra o : obras){
            if(o.getTitulo().equalsIgnoreCase(titulo)) return o;
        }
        return null;
    }

    @Override
    public void atualizar(Obra obra) throws ObraNaoEncontradaException {
        for(int i = 0; i < obras.size(); i++){
            if(obras.get(i).getTitulo().equalsIgnoreCase(obra.getTitulo())){
                obras.set(i, obra);
                persistir();
                return;
            }
        }
        throw new ObraNaoEncontradaException(obra.getTitulo(), obra.getAutor());
    }

    @Override
    public void remover(String titulo) {
        Obra obra = buscar(titulo);
        if(obra!=null){
            obra.setAtiva(false);
            persistir();
        }
    }

    @Override
    public Vector<Obra> listar() {
        return obras;
    }

    private void persistir() {
        PersistenciaArquivo.salvarObras(obras);
        PersistenciaArquivo.salvarAvaliacoes(obras);
    }
}
