package me.control;

import me.model.entidades.Filme;
import me.model.entidades.User;
import me.model.exceptions.*;
import me.model.persistencia.DAOFilme;

import java.util.List;

public class GerenciadorFilmes {
    private final DAOFilme daoFilme;

    public GerenciadorFilmes() {
        this.daoFilme = new DAOFilme();
    }

    public void cadastrarFilme(User solicitante, Filme filme) {
        if (solicitante == null || !solicitante.isAdmin()) {
            throw new PermissaoNegadaException("Apenas administradores podem cadastrar filmes.");
        }
        if (filme == null) throw new DadosInvalidosException("Filme inválido.");
        try {
            daoFilme.cadastrarFilme(filme);
        } catch (Exception e) {
            throw new OperacaoBancoException("Erro ao cadastrar filme.", e);
        }
    }

    public boolean editarFilme(User solicitante, Filme filme) {
        if (solicitante == null || !solicitante.isAdmin()) {
            throw new PermissaoNegadaException("Apenas administradores podem editar filmes.");
        }
        if (filme == null) throw new DadosInvalidosException("Filme inválido.");
        try {
            return daoFilme.editarFilme(filme);
        } catch (Exception e) {
            throw new OperacaoBancoException("Erro ao editar filme.", e);
        }
    }

    public boolean deletarFilme(User solicitante, Filme filme) {
        if (solicitante == null || !solicitante.isAdmin()) {
            throw new PermissaoNegadaException("Apenas administradores podem deletar filmes.");
        }
        if (filme == null) throw new DadosInvalidosException("Filme inválido.");
        try {
            return daoFilme.deletarFilme(filme);
        } catch (Exception e) {
            throw new OperacaoBancoException("Erro ao deletar filme.", e);
        }
    }

    public List<Filme> buscarPorNome(String nome) {
        if (nome == null || nome.isBlank()) throw new DadosInvalidosException("Nome inválido.");
        try {
            return daoFilme.buscarPorNome(nome);
        } catch (Exception e) {
            throw new OperacaoBancoException("Erro ao buscar filme por nome.", e);
        }
    }

    public List<Filme> listarFilmes() {
        try {
            return daoFilme.listarFilmes();
        } catch (Exception e) {
            throw new OperacaoBancoException("Erro ao listar filmes.", e);
        }
    }

    public List<Filme> filtrarFilmes(String filtro, String valor) {
        if (filtro == null || filtro.isBlank() || valor == null) throw new DadosInvalidosException("Filtro/valor inválido.");
        try {
            return daoFilme.filtrarFilmes(filtro, valor);
        } catch (Exception e) {
            throw new OperacaoBancoException("Erro ao filtrar filmes.", e);
        }
    }
}
