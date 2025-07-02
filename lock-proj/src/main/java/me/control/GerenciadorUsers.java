package me.control;

import java.util.List;

import me.model.entidades.User;
import me.model.enums.TipoUser;
import me.model.persistencia.DAOUser;

public class GerenciadorUsers {
    private DAOUser daoUser = new DAOUser();

    public void cadastrarUserPublico(String email, String username, String senha) {
        User novo = new User(email, username, senha, TipoUser.COMUM);
        daoUser.cadastrarUser(novo);
    }

    public void editarUser(User solicitante) {
        daoUser.editarUser(buscarPorId(solicitante.getId()));
    }

    public void deletarConta(User solicitante) {
        daoUser.deletarConta(buscarPorId(solicitante.getId()));

    }

    public void cadastrarUserAsADM(User solicitante,String email, String username, String senha, TipoUser tipo) {
        if (!solicitante.isAdmin()) {
            throw new SecurityException("Usuários comuns não tem acesso a essa ação.");
        }
        User novo = new User(email, username, senha, TipoUser.COMUM);
        daoUser.cadastrarUser(novo);
    }

    public List<User> listarUsuarios(User solicitante) {
        if (!solicitante.isAdmin()) {
            throw new SecurityException("Usuários comuns não tem acesso a essa ação.");
        }
        return daoUser.listarUsuarios();
    }

    public List<User> buscarPorUsername(User solicitante, String nome) {
        if (!solicitante.isAdmin()) {
            throw new SecurityException("Usuários comuns não tem acesso a essa ação. Na realidade, você nem deveria estar aqui.");
        }
        return daoUser.buscarPorUsername(nome);
    }

    public User buscarPorId(String id) {
        return daoUser.buscarPorId(id);
    }    
}
