package com.davidklhui.slotgame.exception;

public class ReelIncorrectProbabilityException extends ReelException{
    public ReelIncorrectProbabilityException() {
    }

    public ReelIncorrectProbabilityException(String message) {
        super(message);
    }

    public ReelIncorrectProbabilityException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReelIncorrectProbabilityException(Throwable cause) {
        super(cause);
    }

    public ReelIncorrectProbabilityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
