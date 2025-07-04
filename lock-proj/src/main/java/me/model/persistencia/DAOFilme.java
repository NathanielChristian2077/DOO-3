package me.model.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import me.model.entidades.Filme;

public class DAOFilme {
    public void cadastrarFilme(Filme f) throws SQLException { // TODO: implementar cadastro (apenas ADMs)
        String sql = "INSERT INTO filme (id, nome, descricao, duracao, preco, qualidade, genero) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexaoBD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setObject(1, UUID.fromString(f.getId()));
            stmt.setString(2, f.getNome());
            stmt.setString(3, f.getDescricao());
            stmt.setInt(4, (int) f.getDuracao().toMinutes());
            stmt.setBigDecimal(5, f.getPreco());
            stmt.setString(6, f.getQualidade().name());
            stmt.setString(7, f.getGeneros().toString());
            stmt.executeUpdate();
        }
    }

    public void editarFilme(Filme f) { // TODO: implementar edição (apenas ADMs)

    }

    public void deletarFilme(Filme f) { // TODO: implementar deleção (apenas ADMs)

    }

    public void buscarPorNome(String nome) {

    }

    public List<Filme> listarFilmes() {
        return null;
    }
    
    public List<Filme> filtrarFilmes() {
        return null;
    }
}
