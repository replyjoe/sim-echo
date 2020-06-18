package echo.api;

/**
 * Answerer interface provides an interface that accepts
 * questions and provide answerers
 * 
 * @author 650033777
 * @author 620007467
 */
public interface Answerer {
    /**
     * Undefined ask method to be defined in a class that implements the
     * Answerer interface.
     * 
     * @param question          The question to be sent to the Wolfram Alpha API
     * @return                  When defined in implementing class, the return
     *                          of this method will be a string answer in JSON
     *                          format
     * @throws AnswerException  If an error occurs in the process of contacting
     *                          the API to ask a question
     */
    public String ask(String question) throws AnswerException; 
}
