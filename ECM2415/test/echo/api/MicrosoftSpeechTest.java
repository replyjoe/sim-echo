package echo.api;

import echo.sound.PlaySound;
import echo.api.MicrosoftSpeech;
import echo.api.KeyException;
import echo.api.AnswerException;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * MicrosoftSpeechTest provides unit tests of methods in MicrosoftSpeech class.
 * 
 * @author 620007467
 * @author 650033777
 */
public class MicrosoftSpeechTest {
    MicrosoftSpeech instance;
    
    @Before
    public void setUp() {
        try {
            instance = new MicrosoftSpeech();
        } catch (KeyException ex) {
            fail("Could not construct class");
        }
    }
    
    @After
    public void tearDown() {
        instance = null;
    }
    
    /**
     * Introduces the suite of tests, and creates an instance of the class
     * 
     * @author 650033777
     */
    @BeforeClass
    static public void setUpClass() {
        System.out.println("MicrosoftSpeech");
    }
    
    /**
     * Test of convertSpeech method, of class MicrosoftSpeech.
     * Tests that the response is of correct length. We are unable to test
     * the actual contents of the string, as the key will change over time.
     * 
     * @author 650033777
     */
    @Test
    public void testRenewAccessTokenSuccess() {
        System.out.println("renewAccessToken - success");
        
        String key = instance.BINGSPEECHKEY1;
        
        try {
            String newKey = MicrosoftSpeech.renewAccessToken(key);
            
            assertEquals("key is not the correct length", 
                         newKey.length(), 
                         495);
            
        } catch (AnswerException ex) {
            fail("Could not renew key");
        }
    }
    
    /**
     * Test of convertSpeech method, of class SpeechToText.
     * Tests that when the method throws an exception successfully when response
     * is null.
     * 
     * @throws AnswerException
     * @author 650033777
     */
    @Test
    public void testRenewAccessTokenFailure() throws AnswerException {
        System.out.println("renewAccessToken - failure");
        
        String key = "fake";
        
        try {
            String newKey = MicrosoftSpeech.renewAccessToken(key);
            
            assertFalse("AnswerException was not thrown correctly",
                        newKey.length() != 496);
            
        } catch (AnswerException ex) {
            
        }
    }
    
    /**
     * Test of getText method, of class SpeechToText.
     * Uses a hard coded String of a known JSON response, and asserts that it 
     * extracts the relevant text as expected.
     * 
     * @author 650033777
     */
    @Test
    public void testGetText(){
        System.out.println("getText");
        
        String JSONExample = "{\"lexical\":\"test\"}";
        
        String expResult = "test";
        String result = MicrosoftSpeech.getText(JSONExample);
        
        assertEquals("getText did not parse json",expResult, result);
    }
    
    /**
     * Test of convertSpeech method of class SpeechToText.
     * Combines the previously tested methods to check that the methods work
     * together as expected.
     * 
     * @author 650033777
     */
    @Test
    public void testConvertSpeech() {
        System.out.println("convertSpeech");
        
        byte[] soundData = PlaySound.readByteArray("gable.wav");
        
        String expResult = "frankly my dear i don't give a damn";
        try {
            String result = instance.convertSpeech(soundData);
            
            assertEquals("Sound was not correctly understood",
                         expResult,
                         result);
            
        } catch (AnswerException ex) {
            fail("API call failed");
        }
    }
    
}
