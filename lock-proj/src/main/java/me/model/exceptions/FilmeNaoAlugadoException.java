package me.model.exceptions;

public class FilmeNaoAlugadoException extends RuntimeException {
    public FilmeNaoAlugadoException(String msg) {
        super(msg);
    }
}
