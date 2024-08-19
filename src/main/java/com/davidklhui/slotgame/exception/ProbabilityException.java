package com.davidklhui.slotgame.exception;

public class ProbabilityException extends SlotException {
    public ProbabilityException() {
    }

    public ProbabilityException(String message) {
        super(message);
    }

    public ProbabilityException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProbabilityException(Throwable cause) {
        super(cause);
    }

    public ProbabilityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
