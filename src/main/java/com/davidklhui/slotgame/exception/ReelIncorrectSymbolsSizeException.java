package com.davidklhui.slotgame.exception;

public class ReelIncorrectSymbolsSizeException extends ReelException {
    public ReelIncorrectSymbolsSizeException() {
    }

    public ReelIncorrectSymbolsSizeException(String message) {
        super(message);
    }

    public ReelIncorrectSymbolsSizeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReelIncorrectSymbolsSizeException(Throwable cause) {
        super(cause);
    }

    public ReelIncorrectSymbolsSizeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
