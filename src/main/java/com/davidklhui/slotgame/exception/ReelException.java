package com.davidklhui.slotgame.exception;

public class ReelException extends SlotGameException{
    public ReelException() {
    }

    public ReelException(String message) {
        super(message);
    }

    public ReelException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReelException(Throwable cause) {
        super(cause);
    }

    public ReelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
