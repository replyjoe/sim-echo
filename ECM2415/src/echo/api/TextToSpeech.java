package echo.api;

/**
 * This class provides an interface to convert from text to
 * a byte array of the spoken text
 * 
 * @author 650033777
 */
 public interface TextToSpeech{
     /**
      * convertToAudioBytes method that when overridden in a class 
      * that implements this interface will convert a string of text 
      * to a byte array format of the audio of the spoken text
      * 
      * @param text  text to be spoken
      * @return      a byte array format of the audio of the spoken text
      */
    public byte[] convertToAudioBytes(String text);
 }
