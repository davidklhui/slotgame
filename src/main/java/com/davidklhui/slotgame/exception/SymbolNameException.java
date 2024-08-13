package com.davidklhui.slotgame.exception;

public class SymbolNameException extends SymbolException{

    public SymbolNameException() {
    }

    public SymbolNameException(String message) {
        super(message);
    }

    public SymbolNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public SymbolNameException(Throwable cause) {
        super(cause);
    }

    public SymbolNameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
