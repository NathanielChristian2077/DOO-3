package me.model.entidades;

import java.util.LinkedList;
import java.util.UUID;

import me.model.enums.TipoUser;

public class User {                     // No Banco de Dados:
    private String id;                  // UUID
    private String email;               // varchar(100)
    private String username;            // varchar(50)
    private String password;            // varchar(512)
    private TipoUser tipo;              // Novo Typre -> tipo
    private LinkedList<Filme> locados;  // id (de cada filme)
    
    public User(String email, String username, String password, TipoUser tipo) {
        this.id = UUID.randomUUID().toString();
        this.email = email;
        this.username = username;
        this.password = password;
        this.tipo = tipo;
        this.locados = new LinkedList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TipoUser getTipo() {
        return tipo;
    }

    public void setTipo(TipoUser tipo) {
        this.tipo = tipo;
    }

    public LinkedList<Filme> getLocados() {
        return locados;
    }

    public void setLocados(LinkedList<Filme> locados) {
        this.locados = locados;
    }

    public boolean isAdmin() {
        return tipo == TipoUser.ADMIN;
    }    
}
