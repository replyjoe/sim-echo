package echo.api;

/**
 * KeyException class provides a custom exception which is thrown when
 * a key is missing from the configuration files
 * 
 * @author 650033777
 */
public class KeyException extends Exception {

    /**
     * One argument answer exception
     * @param message   message to be displayed when the exception is caught
     */
    public KeyException(String message) {
        super(message);
    }

    
    /**
     * Two argument answer exception
     * @param message   message to be displayed when the exception is caught
     * @param throwable Throwable object to be thrown when the exception is
     *                  caught
     */
    public KeyException(String message, Throwable throwable) {
        super(message, throwable);
    }

}