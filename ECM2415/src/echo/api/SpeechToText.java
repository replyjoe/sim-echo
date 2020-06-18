package echo.api;


/**
 * This class provides an interface that accepts
 * a byte array representing speech and converts
 * it into a text string
 * 
 * @author 650033777
 */
public interface SpeechToText {
    
    /**
     * convertSpeech method that when overridden by a class that implements this
     * interface will convert a byte array of speech to a string
     * 
     * @param speech      the speech audio to be converted in byte array format
     * @return            the string of the speech
     * 
     * @throws AnswerException if an error occurs in converting the speech 
     *                         in the API service
     */
    public String convertSpeech(byte[] speech) throws AnswerException;
}
