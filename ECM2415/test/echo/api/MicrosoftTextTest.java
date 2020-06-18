package echo.api;

import echo.api.TextToSpeech;
import echo.api.MicrosoftText;
import echo.api.KeyException;
import echo.api.AnswerException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * MicrosoftTextTest provides unit tests of methods in TextToSpeech class.
 * @author 620007467
 */
public class MicrosoftTextTest {
    String testAccessToken;
    String bingSpeechKey;
    MicrosoftText testTextToSpeechObject;
    
    public MicrosoftTextTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws AnswerException {
        try {
            testTextToSpeechObject = new MicrosoftText();
        } catch (KeyException ex) {
            fail("Unable to load keys"); return;
        }
        
        bingSpeechKey = "c3e961dde49f4a7db78fb34207db37f8"; 
        testAccessToken = testTextToSpeechObject.getAccessKey(bingSpeechKey);
    }
    
    @After
    public void tearDown() {
        bingSpeechKey = null;
        testAccessToken = null;
        testTextToSpeechObject = null;
    }
    
    /**
     * Test of TextToSpeech constructor, of class TextToSpeech.
     */
    public void testTextToSpeech(){
        try{
            TextToSpeech testObj = new MicrosoftText();
            assertNotNull("Initialised TextToSpeech object was null in testTextToSpeech",testObj);
        } catch(KeyException e){
            System.out.println("Failed to initialise in constructor test.");
        }
    }

    /**
     * Test of getAccessToken method, of class TextToSpeech.
     * Tests that the response is of correct length, since this method is non
     * deterministic, cannot directly test the response contents.
     */
    @Test
    public void testGetAccessToken(){
        assertEquals("Retrieved access token was of the wrong length and therefore wrong format in testGetAccessToken.", testAccessToken.length(), 495); //Access token should be 495 in length
    }
    

    /**
     * Test of convertToAudioBytes method, of class TextToSpeech.
     */
    @Test
    public void testConvertToAudioBytes() {
        byte[] testByteArray;
        String testString = "Hello";
        testByteArray = testTextToSpeechObject.convertToAudioBytes(testString);
        assertNotNull("Assigned byte array was null in testConvertToAudioBytes.",testByteArray);
    }
}
