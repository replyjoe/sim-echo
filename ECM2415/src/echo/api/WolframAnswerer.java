package echo.api;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * WolframAnswerer class provides an implementation of Answerer interface
 * using the Wolfram-Alpha API
 * 
 * @author 650033777
 * @author 620007467
 */
public class WolframAnswerer implements Answerer { 
    final String WOLFRAMKEY;
  
    /**
     * No argument constructor for the WolframAnswerer Class
     * This method fetches the key from the properties file to use with the API
     * 
     * @throws KeyException  if the key is missing from the configuration file
     *                       or the configuration file cannot be found
     * @author 650033777
     */
    public WolframAnswerer() throws KeyException {
        Properties properties = new Properties();
        
        try {
            properties.load(new FileInputStream("./res/config.properties"));
        } catch (IOException e) {
            throw new KeyException("Properties file couldn't be opened");
        }
        
        WOLFRAMKEY = properties.getProperty("WOLFRAMKEY", null);
        if(WOLFRAMKEY == null) {
            throw new KeyException("Properties file does not contain key: WOLFRAMKEY");
        }
    }
    
    
    /**
     * ask method asks a question input to the Wolfram Alpha API to retrieve an 
     * answer to the question
     * 
     * @param question the string text of the question to be asked
     * 
     * @return         string of the JSON response from Wolfram Alpha API
     * 
     * @throws AnswerException if an error occurs in contacting the API or
     *                         the response is of unexpected format/contents
     * 
     * @author 650033777
     */
    @Override
    public String ask(String question) throws AnswerException {
        question = urlEncode( question );
        if(question == null) {
            throw new AnswerException("Question couldn't be encoded");
        }
        
        final String method = "GET";
        final String url    
          = ( "http://api.wolframalpha.com/v1/spoken"
            + "?" + "appid"  + "=" + WOLFRAMKEY
            + "&" + "i"  + "=" + urlEncode( question )
            );
        
        final String[][] headers = new String[0][0];

        final byte[] body = new byte[0];
        byte[] response = HttpConnect.httpConnect(method, url, headers, body);
        
        if(response == null) {
            throw new AnswerException("Response is empty");
        }
        
        return new String(response);
    }
    
    
    /**
     * urlEncode method encodes a string to be in URL format by removing all non
     * alphanumeric and space characters
     * 
     * @param s     the string to be encoded in URL format
     * @return      the formatted URL format of the string
     * @author 650033777
     */
    protected static String urlEncode( String s ) {
        return s.replace(" ","+").replaceAll("[^A-Za-z0-9+]", "");
    }
    
}
