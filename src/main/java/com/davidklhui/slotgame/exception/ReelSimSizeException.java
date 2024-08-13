package com.davidklhui.slotgame.exception;

public class ReelSimSizeException extends ReelException {
    public ReelSimSizeException() {
    }

    public ReelSimSizeException(String message) {
        super(message);
    }

    public ReelSimSizeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReelSimSizeException(Throwable cause) {
        super(cause);
    }

    public ReelSimSizeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
