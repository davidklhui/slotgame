package com.davidklhui.slotgame.exception;

public class PayoutException extends SlotGameException{

    public PayoutException() {
    }

    public PayoutException(String message) {
        super(message);
    }

    public PayoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public PayoutException(Throwable cause) {
        super(cause);
    }

    public PayoutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
