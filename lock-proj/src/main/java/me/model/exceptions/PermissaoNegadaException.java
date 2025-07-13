package me.model.exceptions;

public class PermissaoNegadaException extends SecurityException {
    public PermissaoNegadaException (String msg) {
        super(msg);
    }
}
