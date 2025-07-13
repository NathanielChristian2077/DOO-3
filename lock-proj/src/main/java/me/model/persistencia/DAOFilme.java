package me.model.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import me.model.entidades.Filme;

public class DAOFilme {
    private String generosToString(Set<me.model.enums.Genero> generos) {
        return String.join(",", generos.stream().map(Enum::name).toList());
    }

    private Set<me.model.enums.Genero> stringToGeneros(String str) {
        Set<me.model.enums.Genero> generos = new java.util.HashSet<>();
        if (str != null && !str.isEmpty()) {
            for (String s : str.split(",")) {
                generos.add(me.model.enums.Genero.valueOf(s.trim()));
            }
        }
        return generos;
    }

    public void cadastrarFilme(Filme f) throws SQLException { // apenas ADMs
        String sql = "INSERT INTO filme (id, nome, descricao, duracao, preco, qualidade, genero) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setObject(1, UUID.fromString(f.getId()));
            stmt.setString(2, f.getNome());
            stmt.setString(3, f.getDescricao());
            stmt.setInt(4, (int) f.getDuracao().toMinutes());
            stmt.setBigDecimal(5, f.getPreco());
            stmt.setString(6, f.getQualidade().name());
            stmt.setString(7, generosToString(f.getGeneros()));
            stmt.executeUpdate();
        }
    }

    public boolean editarFilme(Filme f) throws SQLException { // apenas ADMs
        String sql = "UPDATE filme SET nome = ?, descricao = ?, duracao = ?, preco = ?, qualidade = ?, genero = ? WHERE id = ?";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, f.getNome());
            stmt.setString(2, f.getDescricao());
            stmt.setInt(3, (int) f.getDuracao().toMinutes());
            stmt.setBigDecimal(4, f.getPreco());
            stmt.setString(5, f.getQualidade().name());
            stmt.setString(6, generosToString(f.getGeneros()));
            stmt.setObject(7, UUID.fromString(f.getId()));
            int linhas = stmt.executeUpdate();
            return linhas > 0;
        }
    }

    public boolean deletarFilme(Filme f) throws SQLException { // apenas ADMs
        String sql = "DELETE FROM filme WHERE id = ?";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setObject(1, UUID.fromString(f.getId()));
            int linhas = stmt.executeUpdate();
            return linhas > 0;
        }
    }

    public List<Filme> buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT * FROM filme WHERE nome ILIKE ?";
        List<Filme> filmes = new java.util.ArrayList<>();
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Filme f = new Filme(
                        rs.getString("id"),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        java.time.Duration.ofMinutes(rs.getInt("duracao")),
                        rs.getBigDecimal("preco"),
                        me.model.enums.Qualidade.valueOf(rs.getString("qualidade")),
                        stringToGeneros(rs.getString("genero"))
                    );
                    filmes.add(f);
                }
            }
        }
        return filmes;
    }

    public List<Filme> listarFilmes() throws SQLException {
        String sql = "SELECT * FROM filme ORDER BY nome";
        List<Filme> filmes = new java.util.ArrayList<>();
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             java.sql.ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Filme f = new Filme(
                    rs.getString("id"),
                    rs.getString("nome"),
                    rs.getString("descricao"),
                    java.time.Duration.ofMinutes(rs.getInt("duracao")),
                    rs.getBigDecimal("preco"),
                    me.model.enums.Qualidade.valueOf(rs.getString("qualidade")),
                    stringToGeneros(rs.getString("genero"))
                );
                filmes.add(f);
            }
        }
        return filmes;
    }
    
    public List<Filme> filtrarFilmes(String filtro, String valor) throws SQLException {
        String sql = "SELECT * FROM filme WHERE " + filtro + " ILIKE ?";
        List<Filme> filmes = new java.util.ArrayList<>();
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, "%" + valor + "%");
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Filme f = new Filme(
                        rs.getString("id"),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        java.time.Duration.ofMinutes(rs.getInt("duracao")),
                        rs.getBigDecimal("preco"),
                        me.model.enums.Qualidade.valueOf(rs.getString("qualidade")),
                        stringToGeneros(rs.getString("genero"))
                    );
                    filmes.add(f);
                }
            }
        }
        return filmes;
    }
}
