package me.model.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.model.entidades.Filme;
import me.model.entidades.User;
import me.model.enums.Qualidade;
import me.model.exceptions.*;

public class DAOUser {
    public void cadastrarUser(User u) {
        try {
            String sql = "INSERT INTO usuario (id, email, username, password, tipo) VALUES (?, ?, ?, ?, ?)";
            try (Connection con = ConexaoBD.getConnection();
                 PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setObject(1, UUID.fromString(u.getId()));
                stmt.setString(2, u.getEmail());
                stmt.setString(3, u.getUsername());
                stmt.setString(4, u.getPassword());
                stmt.setString(5, u.getTipo().name());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new OperacaoBancoException("Erro ao cadastrar usuário.", e);
        }
    }

    public boolean editarUser(User u) {
        try {
            String sql = "UPDATE usuario SET email = ?, username = ?, password = ?, tipo = ? WHERE id = ?";
            try (Connection con = ConexaoBD.getConnection();
                 PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, u.getEmail());
                stmt.setString(2, u.getUsername());
                stmt.setString(3, u.getPassword());
                stmt.setObject(4, UUID.fromString(u.getId()));
                int linhas = stmt.executeUpdate();
                if (linhas == 0) throw new UsuarioNaoEncontradoException("Usuário não encontrado para edição.");
                return true;
            }
        } catch (SQLException e) {
            throw new OperacaoBancoException("Erro ao editar usuário.", e);
        }
    }

    public boolean deletarConta(User u) {
        try {
            String sql = "DELETE FROM usuario WHERE id = ?";
            try (Connection con = ConexaoBD.getConnection();
                 PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setObject(1, UUID.fromString(u.getId()));
                int linhas = stmt.executeUpdate();
                if (linhas == 0) throw new UsuarioNaoEncontradoException("Usuário não encontrado para deleção.");
                return true;
            }
        } catch (SQLException e) {
            throw new OperacaoBancoException("Erro ao deletar usuário.", e);
        }
    }

    public List<User> buscarPorUser(String text) {
        List<User> users = new ArrayList<>();
        try {
            String sql = """
                SELECT id, email, username FROM usuario
                WHERE email ILIKE ? OR username ILIKE ?
                LIMIT 20
            """;
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
            if (users.isEmpty()) throw new UsuarioNaoEncontradoException("Nenhum usuário encontrado.");
            return users;
        } catch (SQLException e) {
            throw new OperacaoBancoException("Erro ao buscar usuário.", e);
        }
    }

    public List<User> listarUsuarios() {
        List<User> users = new ArrayList<>();
        try {
            String sql = "SELECT id, email, username FROM usuario ORDER BY username";
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
        } catch (SQLException e) {
            throw new OperacaoBancoException("Erro ao listar usuários.", e);
        }
    }

    public User buscarPorId(String id) {
        try {
            String sql = "SELECT * FROM usuario WHERE id = ?";
            try (Connection con = ConexaoBD.getConnection();
                 PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setObject(1, UUID.fromString(id));
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new User(
                            rs.getString("id"),
                            rs.getString("email"),
                            rs.getString("username"));
                    }
                }
            }
            throw new UsuarioNaoEncontradoException("Usuário não encontrado.");
        } catch (SQLException e) {
            throw new OperacaoBancoException("Erro ao buscar usuário por ID.", e);
        }
    }

    public boolean emailExiste(String email) {
        try {
            String sql = "SELECT COUNT(*) FROM usuario WHERE email = ?";
            try (Connection con = ConexaoBD.getConnection();
                 PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, email);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            throw new OperacaoBancoException("Erro ao verificar email.", e);
        }
    }

    public boolean usernameExiste(String username) {
        try {
            String sql = "SELECT COUNT(*) FROM usuario WHERE username = ?";
            try (Connection con = ConexaoBD.getConnection();
                 PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            throw new OperacaoBancoException("Erro ao verificar username.", e);
        }
    }

    public boolean jaAlugouFilme(User user, Filme filme) {
        try {
            String sql = "SELECT COUNT(*) FROM usuario_filme WHERE usuario_id = ? AND filme_id = ?";
            try (Connection con = ConexaoBD.getConnection();
                 PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setObject(1, UUID.fromString(user.getId()));
                stmt.setObject(2, UUID.fromString(filme.getId()));
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            throw new OperacaoBancoException("Erro ao verificar aluguel de filme.", e);
        }
    }

    public boolean alugarFilme(User user, Filme filme) {
        try {
            if (jaAlugouFilme(user, filme)) {
                throw new FilmeJaAlugadoException("Filme já alugado por este usuário.");
            }
            String sql = "INSERT INTO usuario_filme (usuario_id, filme_id) VALUES (?, ?)";
            try (Connection con = ConexaoBD.getConnection();
                 PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setObject(1, UUID.fromString(user.getId()));
                stmt.setObject(2, UUID.fromString(filme.getId()));
                stmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            throw new OperacaoBancoException("Erro ao alugar filme.", e);
        }
    }

    public boolean desalugarFilme(User user, Filme filme) {
        try {
            if (!jaAlugouFilme(user, filme)) {
                throw new FilmeNaoAlugadoException("Filme não está alugado por este usuário.");
            }
            String sql = "DELETE FROM usuario_filme WHERE usuario_id = ? AND filme_id = ?";
            try (Connection con = ConexaoBD.getConnection();
                 PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setObject(1, UUID.fromString(user.getId()));
                stmt.setObject(2, UUID.fromString(filme.getId()));
                int linhas = stmt.executeUpdate();
                return linhas > 0;
            }
        } catch (SQLException e) {
            throw new OperacaoBancoException("Erro ao desalugar filme.", e);
        }
    }

    public List<Filme> listarFilmesAlugados(User user) {
        List<Filme> filmes = new ArrayList<>();
        try {
            String sql = "SELECT f.* FROM filme f JOIN usuario_filme uf ON f.id = uf.filme_id WHERE uf.usuario_id = ?";
            try (Connection con = ConexaoBD.getConnection();
                 PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setObject(1, UUID.fromString(user.getId()));
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Filme f = new Filme(
                            rs.getString("id"),
                            rs.getString("nome"),
                            rs.getString("descricao"),
                            java.time.Duration.ofMinutes(rs.getInt("duracao")),
                            rs.getBigDecimal("preco"),
                            Qualidade.valueOf(rs.getString("qualidade")),
                            new DAOFilme().stringToGeneros(rs.getString("genero"))
                        );
                        filmes.add(f);
                    }
                }
            }
            return filmes;
        } catch (SQLException e) {
            throw new OperacaoBancoException("Erro ao listar filmes alugados.", e);
        }
    }
}
