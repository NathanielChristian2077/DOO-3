package me.model.exceptions;

public class FilmeNaoEncontradoException extends RuntimeException {
    public FilmeNaoEncontradoException(String msg) {
        super(msg);
    }
}
