package echo.api;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * MicrosoftText class converts text in String format to a byte array
 * Current default API in use for this functionality is Microsoft Cognitive
 * Services. 
 * 
 * @author 620007467
 * @author 650033777
 * @author David Wakeling
 */
 public class MicrosoftText implements TextToSpeech{
    final String MICROCOGKEY;
    final static String FORMAT = "riff-16khz-16bit-mono-pcm";
    final static String GENDER = "Female";
    final static String LANG   = "en-US";
    final static String METHOD = "POST";
   
    /**
     * No argument constructor for the TextToSpeech Class
     * This method fetches the key from the properties file to use with the API
     * 
     * @throws KeyException if the key is missing from the configuration file
     *                      or the properties file is missing
     * @author 650033777
     */
    public MicrosoftText() throws KeyException {
        Properties properties = new Properties();
        
        try {
            properties.load(new FileInputStream("./res/config.properties"));
        } catch (IOException e) {
            throw new KeyException("Properties file couldn't be opened");
        }
        
        MICROCOGKEY = properties.getProperty("MICROCOGKEY", null);
        if(MICROCOGKEY == null) {
            throw new KeyException(
                        "Properties file does not contain key: MICROCOGKEY");
        }
        
    }
    
    /**
     * getAccessKey method gets a refreshed access token for the Microsoft
     * Cognitive Services API, which lasts for 10 minutes.
     * 
     * @param key   Access key used to generate an access token.
     * @return      the renewed access token
     */
    public String getAccessKey( String key ){
      final String url =
        "https://api.cognitive.microsoft.com/sts/v1.0/issueToken";
      final byte[] body = {};
      final String[][] headers
        = { { "Ocp-Apim-Subscription-Key", MICROCOGKEY                   }
          , { "Content-Length"           , String.valueOf( body.length ) }
          };
      byte[] response = HttpConnect.httpConnect( METHOD, url, headers, body );
      return new String( response );
    }
    
    /**
     * convertToAudioBytes method converts text to an array of 
     * bytes corresponding to the audio of the spoken text Uses Microsoft 
     * Cognitive Services API for the conversion of text to sound bytes.
     * 
     * @param text  the text to be converted
     * @return      the corresponding sound bytes of the spoken text
     */
    @Override
    public byte[] convertToAudioBytes(String text){
      String token = getAccessKey(MICROCOGKEY);
      String lang = LANG;
      String gender = GENDER;
      String format = FORMAT;
      
      final String method = "POST";
      final String url = "https://speech.platform.bing.com/synthesize";
      final byte[] body
        = ( "<speak version='1.0' xml:lang='en-us'>"
          + "<voice xml:lang='" + lang   + "' "
          + "xml:gender='"      + gender + "' "
          + "name='Microsoft Server Speech Text to Speech Voice"
          + " (en-US, ZiraRUS)'>"
          + text
          + "</voice></speak>" ).getBytes();
      final String[][] headers
        = { { "Content-Type"             , "application/ssml+xml"        }
          , { "Content-Length"           , String.valueOf( body.length ) }
          , { "Authorization"            , "Bearer " + token             }
          , { "X-Microsoft-OutputFormat" , format                        }
          };
      byte[] response = HttpConnect.httpConnect( method, url, headers, body );
      return response;
    }
 }