package easylog.core;

/**
 * Exception for errors concerning easylog.
 */
public class EasylogException extends RuntimeException {

    public EasylogException() {
    }

    public EasylogException(String message) {
        super(message);
    }

    public EasylogException(String message, Throwable cause) {
        super(message, cause);
    }

    public EasylogException(Throwable cause) {
        super(cause);
    }

    public EasylogException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
