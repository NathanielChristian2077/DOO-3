package me.model.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import me.model.entidades.User;

public class DAOUser {
    public void cadastrarUser(User u) { // TODO: implementar cadastro
        String sql = "INSERT INTO usuario (id, email, username, password, tipo) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void editarUser(User u) { // TODO: implementar edição dos dados da própria conta

    }

    public void deletarConta(User u) { // TODO: implementar a deleção da própria conta e no caso de ADMs, contas de outros usuarios

    }

    // Verificações são feitas apenas nos gerenciadores
    public List<User> buscarPorUsername(String nome) { // TODO: apenas adms podem buscar ou listar usuarios
        return null;
    }

    public List<User> listarUsuarios() { // TODO: apenas adms podem buscar ou listar usuarios
        return null;
    }

    public User buscarPorId(String id) { // TODO: implementar, nesse caso, será usado apenas por adms e pelo próprio sistema, no caso de deleção e edição
        return null;
    }
}
