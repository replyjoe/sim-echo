package echo.api;

import echo.Echo;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * WebknoxAnswerer class provides an implementation of Answerer
 * using the Web Knox API
 * 
 * @author 650033777
 */
public class WebknoxAnswerer implements Answerer { 
  final String WEBKNOXKEY;
    
    /**
     * No argument constructor for the WebknoxAnswerer Class
     * <p>
     * This method fetches the key from the properties file to use with the API
     * 
     * @throws KeyException
     * @author 650033777
     */
    public WebknoxAnswerer() throws KeyException {
        Properties properties = new Properties();
        
        try {
            properties.load(new FileInputStream("./res/config.properties"));
        } catch (IOException e) {
            throw new KeyException("Properties file couldn't be opened");
        }
        
        WEBKNOXKEY = properties.getProperty("WEBKNOXKEY", null);
        if(WEBKNOXKEY == null) {
            throw new KeyException("Properties file does not contain key: WEBKNOXKEY");
        }
    }
    
    /**
     * ask method asks a question input to the Web Knox API to retrieve an 
     * answer to the question
     * 
     * @param question the string text of the question to be asked
     * @return string of the JSON response from Web Knox
     * @throws AnswerException 
     * @author 650033777
     */
    @Override
    public String ask(String question) throws AnswerException {
        question = urlEncode( question );
        if(question == null) {
            throw new AnswerException("Question couldn't be encoded");
        }
        
        final String method = "GET";
        final String url    = "https://webknox-question-answering.p.mashape.com"
                            + "/questions/answers"
                            + "?answerLookup=false"
                            + "&answerSearch=false"
                            + "&question="+question;
        
        final String[][] headers 
        = { { "X-Mashape-Key", WEBKNOXKEY         }
          , { "Accept"       , "application/json" }
          };

        byte[] response = HttpConnect.httpConnect(method, url, headers, null);
        
        if(response == null) {
            throw new AnswerException("Response is empty");
        }
        String answer =  getText(new String(response));
        
        if(answer == null) {
            throw new AnswerException("Response is invalid");
        }
        
        return answer;
    }
    
    
    /**
     * urlEncode method encodes a string to be in URL format by removing all non
     * alphanumeric and space characters
     * 
     * @param s
     * @return 
     * @author 650033777
     */
    protected static String urlEncode( String s ) {
        return s.replace(" ","+").replaceAll("[^A-Za-z0-9+]", "");
    }
    
    /**
     * Takes a JSON response and returns the raw text reply
     * 
     * @param response json response from the server
     * @return reply 
     * @author 650033777
     */
    static String getText(String response) {
        String pattern = "\"answer\":\"(.*?)\"";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(response);
        
        if(m.find()) {
           return m.group(1);
        }
        
        return null;
    }
    
}


