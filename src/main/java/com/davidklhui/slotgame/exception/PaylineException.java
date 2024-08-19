package com.davidklhui.slotgame.exception;

public class PaylineException extends SlotGameException {

    public PaylineException() {
    }

    public PaylineException(String message) {
        super(message);
    }

    public PaylineException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaylineException(Throwable cause) {
        super(cause);
    }

    public PaylineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
