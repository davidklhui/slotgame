package com.davidklhui.slotgame.exception;

public class SymbolException extends SlotGameException{

    public SymbolException() {
    }

    public SymbolException(String message) {
        super(message);
    }

    public SymbolException(String message, Throwable cause) {
        super(message, cause);
    }

    public SymbolException(Throwable cause) {
        super(cause);
    }

    public SymbolException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
