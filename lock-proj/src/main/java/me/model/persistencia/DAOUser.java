package me.model.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.model.entidades.User;

public class DAOUser {
    public void cadastrarUser(User u) throws SQLException {
        String sql = "INSERT INTO usuario (id, email, username, password, tipo) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexaoBD.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setObject(1, UUID.fromString(u.getId()));
            stmt.setString(2, u.getEmail());
            stmt.setString(3, u.getUsername());
            stmt.setString(4, u.getPassword());
            stmt.setString(5, u.getTipo().name());
            stmt.executeUpdate();
        }
    }

    public boolean editarUser(User u) throws SQLException {
        String sql = "UPDATE usuario SET email = ?, username = ?, password = ?, tipo = ? WHERE id = ?";
        try (Connection con = ConexaoBD.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, u.getEmail());
            stmt.setString(2, u.getUsername());
            stmt.setString(3, u.getPassword());
            stmt.setObject(4, UUID.fromString(u.getId()));
            int linhas = stmt.executeUpdate();
            return linhas > 0;
        }
    }

    public boolean deletarConta(User u) throws SQLException { // TODO: implementar a deleção da própria conta e no caso
                                                              // de ADMs, contas de outros usuarios
        String sql = "DELETE FROM usuario WHERE id = ?";
        try (Connection con = ConexaoBD.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setObject(1, UUID.fromString(u.getId()));
            int linhas = stmt.executeUpdate();
            return linhas > 0;
        }
    }

    // Verificações são feitas apenas nos gerenciadores
    public List<User> buscarPorUser(String text) throws SQLException { // TODO: apenas adms podem buscar ou listar
                                                                       // usuarios
        String sql = """
                    SELECT id, email, username
                    WHERE email ILIKE ? OR username ILIKE ?
                    LIMIT 20
                """;

        List<User> users = new ArrayList<>();
        try (Connection con = ConexaoBD.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            String parametro = "%" + text + "%";
            stmt.setString(1, parametro);
            stmt.setString(2, parametro);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User u = new User(
                        rs.getString("id"),
                        rs.getString("email"),
                        rs.getString("username"));
                users.add(u);
            }
        }
        return users;
    }

    public List<User> listarUsuarios() throws SQLException { // TODO: apenas adms podem buscar ou listar usuarios
        String sql = "SELECT id, email, username FROM usuario ORDER BY username";
        List<User> users = new ArrayList<>();
        try (Connection con = ConexaoBD.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                User u = new User(
                        rs.getString("id"),
                        rs.getString("email"),
                        rs.getString("username"));
                users.add(u);
            }
        }
        return users;
    }

    public User buscarPorId(String id) throws SQLException { // TODO: implementar, nesse caso, será usado apenas por
                                                             // adms e pelo próprio sistema, no caso de deleção e edição
        String sql = "SELECT * FROM usuario WHERE id = ?";
        try (Connection con = ConexaoBD.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setObject(1, UUID.fromString(id));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User u = new User(
                            rs.getString("id"),
                            rs.getString("email"),
                            rs.getString("username"));
                    return u;
                }
                return null;
            }
        }
    }
}
