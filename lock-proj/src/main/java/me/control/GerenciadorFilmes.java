package me.control;

import me.model.entidades.Filme;
import me.model.entidades.User;
import me.model.exceptions.PermissaoNegadaException;
import me.model.persistencia.DAOFilme;

import java.sql.SQLException;
import java.util.List;

public class GerenciadorFilmes {
    private final DAOFilme daoFilme;

    public GerenciadorFilmes() {
        this.daoFilme = new DAOFilme();
    }

    public void cadastrarFilme(User solicitante, Filme filme) throws SQLException {
        if (!solicitante.isAdmin()) {
            throw new PermissaoNegadaException("Apenas administradores podem cadastrar filmes.");
        }
        daoFilme.cadastrarFilme(filme);
    }

    public boolean editarFilme(User solicitante, Filme filme) throws SQLException {
        if (!solicitante.isAdmin()) {
            throw new PermissaoNegadaException("Apenas administradores podem editar filmes.");
        }
        return daoFilme.editarFilme(filme);
    }

    public boolean deletarFilme(User solicitante, Filme filme) throws SQLException {
        if (!solicitante.isAdmin()) {
            throw new PermissaoNegadaException("Apenas administradores podem deletar filmes.");
        }
        return daoFilme.deletarFilme(filme);
    }

    public List<Filme> buscarPorNome(String nome) throws SQLException {
        return daoFilme.buscarPorNome(nome);
    }

    public List<Filme> listarFilmes() throws SQLException {
        return daoFilme.listarFilmes();
    }

    public List<Filme> filtrarFilmes(String filtro, String valor) throws SQLException {
        return daoFilme.filtrarFilmes(filtro, valor);
    }
}
