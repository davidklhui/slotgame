package com.davidklhui.slotgame.exception;

public class SlotGameException extends RuntimeException{
    public SlotGameException() {
    }

    public SlotGameException(String message) {
        super(message);
    }

    public SlotGameException(String message, Throwable cause) {
        super(message, cause);
    }

    public SlotGameException(Throwable cause) {
        super(cause);
    }

    public SlotGameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
