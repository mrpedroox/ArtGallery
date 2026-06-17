package artgallery.repository;

import artgallery.exceptions.ObraJaCadastradaException;
import artgallery.exceptions.ObraNaoEncontradaException;
import artgallery.interfaces.IRepositorioObra;
import artgallery.model.Obra;

import java.util.Vector;

public class RepositorioObra implements IRepositorioObra {
    private Vector<Obra> obras;
    @Override
    public void cadastrar(Obra obra) throws ObraJaCadastradaException {
        try{

        }
    }

    @Override
    public Obra buscar(String titulo) throws ObraNaoEncontradaException {
        return null;
    }

    @Override
    public void atualizar(Obra obra) throws ObraNaoEncontradaException {

    }

    @Override
    public void remover(String titulo) {

    }

    @Override
    public Vector<Obra> listar() {
        return null;
    }
}
