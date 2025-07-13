package me.model.exceptions;

public class UsuarioDuplicadoException extends RuntimeException {
    public UsuarioDuplicadoException(String msg) {
        super(msg);
    }
}
