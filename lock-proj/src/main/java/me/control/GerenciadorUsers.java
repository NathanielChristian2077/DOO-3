package me.control;

import java.util.List;

import me.model.entidades.Filme;
import me.model.entidades.User;
import me.model.enums.TipoUser;
import me.model.exceptions.*;
import me.model.persistencia.DAOUser;

public class GerenciadorUsers {

    public GerenciadorUsers() {
    }
    private DAOUser daoUser = new DAOUser();

    public void cadastrarUserPublico(String email, String username, String senha) {
        if (email == null || username == null || senha == null || email.isBlank() || username.isBlank() || senha.isBlank()) {
            throw new DadosInvalidosException("Todos os campos são obrigatórios.");
        }
        if (daoUser.emailExiste(email)) {
            throw new UsuarioDuplicadoException("Email já cadastrado.");
        }
        if (daoUser.usernameExiste(username)) {
            throw new UsuarioDuplicadoException("Username já cadastrado.");
        }
        User novo = new User(email, username, senha, TipoUser.COMUM);
        daoUser.cadastrarUser(novo);
    }

    public void editarUser(User solicitante) {
        if (solicitante == null) {
            throw new DadosInvalidosException("Usuário inválido.");
        }
        daoUser.editarUser(buscarPorId(solicitante.getId()));
    }

    public void deletarConta(User solicitante) {
        if (solicitante == null) {
            throw new DadosInvalidosException("Usuário inválido.");
        }
        daoUser.deletarConta(buscarPorId(solicitante.getId()));
    }

    public void cadastrarUserAsADM(User solicitante, String email, String username, String senha, TipoUser tipo) {
        if (solicitante == null || !solicitante.isAdmin()) {
            throw new PermissaoNegadaException("Usuários comuns não tem acesso a essa ação.");
        }
        if (daoUser.emailExiste(email)) {
            throw new UsuarioDuplicadoException("Email já cadastrado.");
        }
        if (daoUser.usernameExiste(username)) {
            throw new UsuarioDuplicadoException("Username já cadastrado.");
        }
        User novo = new User(email, username, senha, tipo);
        daoUser.cadastrarUser(novo);
    }

    public List<User> listarUsuarios(User solicitante) {
        if (solicitante == null || !solicitante.isAdmin()) {
            throw new PermissaoNegadaException("Usuários comuns não tem acesso a essa ação.");
        }
        return daoUser.listarUsuarios();
    }

    public List<User> buscarPorUser(User solicitante, String text) {
        if (solicitante == null || !solicitante.isAdmin()) {
            throw new PermissaoNegadaException("Usuários comuns não tem acesso a essa ação.");
        }
        return daoUser.buscarPorUser(text);
    }

    public User buscarPorId(String id) {
        if (id == null || id.isBlank()) {
            throw new DadosInvalidosException("ID inválido.");
        }
        return daoUser.buscarPorId(id);
    }

    public boolean alugarFilme(User solicitante, Filme filme) {
        if (solicitante == null) {
            throw new DadosInvalidosException("Usuário inválido.");
        }
        if (filme == null) {
            throw new DadosInvalidosException("Filme inválido.");
        }
        return daoUser.alugarFilme(solicitante, filme);
    }

    public boolean desalugarFilme(User solicitante, Filme filme) {
        if (solicitante == null) {
            throw new DadosInvalidosException("Usuário inválido.");
        }
        if (filme == null) {
            throw new DadosInvalidosException("Filme inválido.");
        }
        return daoUser.desalugarFilme(solicitante, filme);
    }

    public List<Filme> listarFilmesAlugados(User solicitante) {
        if (solicitante == null) {
            throw new DadosInvalidosException("Usuário inválido.");
        }
        return daoUser.listarFilmesAlugados(solicitante);
    }

    public User autenticar(String login, String senha) {
        if (login == null || senha == null || login.isBlank() || senha.isBlank()) {
            throw new DadosInvalidosException("Login e senha são obrigatórios.");
        }

        List<User> encontrados = daoUser.buscarPorUser(login);
        if (encontrados.isEmpty()) {
            throw new UsuarioNaoEncontradoException("Usuário não encontrado.");
        }
        for (User u : encontrados) {
            if (u.getUsername().equalsIgnoreCase(login) || u.getEmail().equalsIgnoreCase(login)) {
                // Aqui você deve buscar o usuário completo (com senha) pelo id
                User completo = daoUser.buscarPorId(u.getId());
                if (completo.getPassword().equals(senha)) {
                    return completo;
                } else {
                    throw new DadosInvalidosException("Senha incorreta.");
                }
            }
        }
        throw new UsuarioNaoEncontradoException("Usuário não encontrado.");
    }
}
