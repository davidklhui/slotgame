package com.davidklhui.slotgame.exception;

public class SymbolProbabilityException extends SymbolException {

    public SymbolProbabilityException() {
    }

    public SymbolProbabilityException(String message) {
        super(message);
    }

    public SymbolProbabilityException(String message, Throwable cause) {
        super(message, cause);
    }

    public SymbolProbabilityException(Throwable cause) {
        super(cause);
    }

    public SymbolProbabilityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
