package com.davidklhui.slotgame.exception;

public class SlotException extends SlotGameException{
    public SlotException() {
    }

    public SlotException(String message) {
        super(message);
    }

    public SlotException(String message, Throwable cause) {
        super(message, cause);
    }

    public SlotException(Throwable cause) {
        super(cause);
    }

    public SlotException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
