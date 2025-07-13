package view;

import me.control.GerenciadorUsers;
import me.control.GerenciadorFilmes;
import me.model.entidades.User;
import me.model.entidades.Filme;
import me.model.enums.TipoUser;
import me.model.enums.Qualidade;
import me.model.enums.Genero;
import me.model.exceptions.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class App {
    public static void main(String[] args) {
        GerenciadorUsers gerUsers = new GerenciadorUsers();
        GerenciadorFilmes gerFilmes = new GerenciadorFilmes();

        try {
            // Teste: Cadastro de usuário comum
            try {
                gerUsers.cadastrarUserPublico("teste@email.com", "usuarioTeste", "senha123");
                System.out.println("Usuário comum cadastrado!");
            } catch (DadosInvalidosException | UsuarioDuplicadoException e) {
                System.out.println("Erro ao cadastrar usuário comum: " + e.getMessage());
            }

            // Teste: Cadastro de usuário duplicado
            try {
                gerUsers.cadastrarUserPublico("teste@email.com", "usuarioTeste", "senha123");
            } catch (UsuarioDuplicadoException e) {
                System.out.println("Duplicidade detectada: " + e.getMessage());
            }

            // Teste: Cadastro de usuário com dados inválidos
            try {
                gerUsers.cadastrarUserPublico("", "", "");
            } catch (DadosInvalidosException e) {
                System.out.println("Dados inválidos detectados: " + e.getMessage());
            }

            // Teste: Cadastro de admin
            User admin = new User("admin@email.com", "admin", "adminpass", TipoUser.ADMIN);
            try {
                gerUsers.cadastrarUserAsADM(admin, admin.getEmail(), admin.getUsername(), admin.getPassword(), admin.getTipo());
                System.out.println("Usuário admin cadastrado!");
            } catch (PermissaoNegadaException | UsuarioDuplicadoException e) {
                System.out.println("Erro ao cadastrar admin: " + e.getMessage());
            }

            // Teste: Cadastro de filme por admin
            Set<Genero> generos = new HashSet<>();
            generos.add(Genero.ACAO);
            generos.add(Genero.DRAMA);
            Filme filme = new Filme("Filme Teste", "Descrição do filme", Duration.ofMinutes(120), new BigDecimal("19.99"), Qualidade.HD, generos);
            try {
                gerFilmes.cadastrarFilme(admin, filme);
                System.out.println("Filme cadastrado!");
            } catch (PermissaoNegadaException | DadosInvalidosException | OperacaoBancoException e) {
                System.out.println("Erro ao cadastrar filme: " + e.getMessage());
            }

            // Teste: Cadastro de filme por usuário comum (sem permissão)
            User usuarioComum = gerUsers.buscarPorUser(admin, "usuarioTeste").get(0);
            try {
                gerFilmes.cadastrarFilme(usuarioComum, filme);
            } catch (PermissaoNegadaException e) {
                System.out.println("Permissão negada ao cadastrar filme: " + e.getMessage());
            }

            // Teste: Alugar filme
            try {
                boolean alugado = gerUsers.alugarFilme(usuarioComum, filme);
                System.out.println("Filme alugado? " + alugado);
            } catch (FilmeJaAlugadoException | DadosInvalidosException | OperacaoBancoException e) {
                System.out.println("Erro ao alugar filme: " + e.getMessage());
            }

            // Teste: Alugar filme duplicado
            try {
                gerUsers.alugarFilme(usuarioComum, filme);
            } catch (FilmeJaAlugadoException e) {
                System.out.println("Alugar duplicado detectado: " + e.getMessage());
            }

            // Teste: Listar filmes alugados
            try {
                List<Filme> filmesAlugados = gerUsers.listarFilmesAlugados(usuarioComum);
                System.out.println("Filmes alugados pelo usuário:");
                for (Filme f : filmesAlugados) {
                    System.out.println("- " + f.getNome());
                }
            } catch (OperacaoBancoException e) {
                System.out.println("Erro ao listar filmes alugados: " + e.getMessage());
            }

            // Teste: Desalugar filme
            try {
                boolean desalugado = gerUsers.desalugarFilme(usuarioComum, filme);
                System.out.println("Filme desalugado? " + desalugado);
            } catch (FilmeNaoAlugadoException | OperacaoBancoException e) {
                System.out.println("Erro ao desalugar filme: " + e.getMessage());
            }

            // Teste: Desalugar filme não alugado
            try {
                gerUsers.desalugarFilme(usuarioComum, filme);
            } catch (FilmeNaoAlugadoException e) {
                System.out.println("Desalugar não alugado detectado: " + e.getMessage());
            }

            // Teste: Listar filmes alugados após desalugar
            try {
                List<Filme> filmesAlugados = gerUsers.listarFilmesAlugados(usuarioComum);
                System.out.println("Filmes alugados após desalugar:");
                for (Filme f : filmesAlugados) {
                    System.out.println("- " + f.getNome());
                }
            } catch (OperacaoBancoException e) {
                System.out.println("Erro ao listar filmes alugados: " + e.getMessage());
            }

            // Teste: Buscar usuário inexistente
            try {
                gerUsers.buscarPorId("00000000-0000-0000-0000-000000000000");
            } catch (UsuarioNaoEncontradoException e) {
                System.out.println("Usuário não encontrado: " + e.getMessage());
            }

            // Teste: Buscar filme por nome inexistente
            try {
                List<Filme> filmes = gerFilmes.buscarPorNome("Filme Inexistente");
                if (filmes.isEmpty()) {
                    System.out.println("Nenhum filme encontrado com esse nome.");
                }
            } catch (OperacaoBancoException e) {
                System.out.println("Erro ao buscar filme: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
