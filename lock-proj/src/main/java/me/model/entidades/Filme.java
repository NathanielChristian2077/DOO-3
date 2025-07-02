package me.model.entidades;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Set;
import java.util.UUID;

import me.model.enums.Genero;
import me.model.enums.Qualidade;

public class Filme {                // No Banco de Dados:
    private String id;              // UUID
    private String nome;            // varchar(100)
    private String descricao;       // varchar(2000)
    private Duration duracao;       // INT (minutos), **Aqui esta em hh:mm:ss**
    private BigDecimal preco;       // DECIMAL(3,2)
    private Qualidade qualidade;    // Novo Type -> Qualidade
    private Set<Genero> generos;    // Novo Type -> Genero

    public Filme(String nome, String descricao, Duration duracao, BigDecimal preco, Qualidade qualidade, Set<Genero> generos) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.descricao = descricao;
        this.duracao = duracao;
        this.preco = preco;
        this.qualidade = qualidade;
        this.generos = generos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Duration getDuracao() {
        return duracao;
    }

    public void setDuracao(Duration duracao) {
        this.duracao = duracao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Qualidade getQualidade() {
        return qualidade;
    }

    public void setQualidade(Qualidade qualidade) {
        this.qualidade = qualidade;
    }

    public Set<Genero> getGeneros() {
        return generos;
    }

    public void setGeneros(Set<Genero> generos) {
        this.generos = generos;
    }
}
