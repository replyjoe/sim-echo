package echo.api;

/**
 * AnswerException class provides a custom exception which is thrown during 
 * the answering process; this enables the ability to generically catch any
 * problems without the application crashing.
 * 
 * @author 650033777
 * @author 620007467
 */
public class AnswerException extends Exception {

    /**
     * One argument answer exception
     * @param message the message to be displayed when the exception is caught
     */
    public AnswerException(String message) {
        super(message);
    }

    
    /**
     * Two argument answer exception
     * @param message   the message to be displayed when the exception is caught
     * @param throwable Throwable object to throw when exception is caught
     */
    public AnswerException(String message, Throwable throwable) {
        super(message, throwable);
    }

}