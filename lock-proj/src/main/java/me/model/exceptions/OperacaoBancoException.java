package me.model.exceptions;

public class OperacaoBancoException extends RuntimeException {
    public OperacaoBancoException(String msg) {
        super(msg);
    }
    public OperacaoBancoException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
