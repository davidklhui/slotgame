package com.davidklhui.slotgame.exception;

public class SlotConfigException extends SlotException{
    public SlotConfigException() {
    }

    public SlotConfigException(String message) {
        super(message);
    }

    public SlotConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public SlotConfigException(Throwable cause) {
        super(cause);
    }

    public SlotConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
