package me.control;

import java.sql.SQLException;
import java.util.List;

import me.model.entidades.User;
import me.model.enums.TipoUser;
import me.model.persistencia.DAOUser;

public class GerenciadorUsers {
    private DAOUser daoUser = new DAOUser();

    public void cadastrarUserPublico(String email, String username, String senha) throws SQLException {
        User novo = new User(email, username, senha, TipoUser.COMUM);
        daoUser.cadastrarUser(novo);
    }

    public void editarUser(User solicitante) throws SQLException {
        daoUser.editarUser(buscarPorId(solicitante.getId()));
    }

    public void deletarConta(User solicitante) throws SQLException {
        daoUser.deletarConta(buscarPorId(solicitante.getId()));

    }

    public void cadastrarUserAsADM(User solicitante,String email, String username, String senha, TipoUser tipo) throws SQLException {
        if (!solicitante.isAdmin()) {
            throw new SecurityException("Usuários comuns não tem acesso a essa ação.");
        }
        User novo = new User(email, username, senha, TipoUser.COMUM);
        daoUser.cadastrarUser(novo);
    }

    public List<User> listarUsuarios(User solicitante) throws SQLException {
        if (!solicitante.isAdmin()) {
            throw new SecurityException("Usuários comuns não tem acesso a essa ação.");
        }
        return daoUser.listarUsuarios();
    }

    public List<User> buscarPorUser(User solicitante, String text) throws SQLException {
        if (!solicitante.isAdmin()) {
            throw new SecurityException("Usuários comuns não tem acesso a essa ação. Na realidade, você nem deveria estar aqui.");
        }
        return daoUser.buscarPorUser(text);
    }

    public User buscarPorId(String id) throws SQLException {
        return daoUser.buscarPorId(id);
    }    
}
