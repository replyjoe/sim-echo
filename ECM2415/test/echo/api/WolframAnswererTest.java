package echo.api;

import echo.api.WolframAnswerer;
import echo.api.KeyException;
import echo.api.AnswerException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 * WolframAnswererTest provides unit tests for methods in the WolframAnswerer class.
 * 
 * @author 620007467
 * @author 650033777
 */
public class WolframAnswererTest {

    
    /**
     * Introduces the suite of tests, and creates an instance of the class
     * 
     * @author 650033777
     */
    @BeforeClass
    static public void setUpClass() {
        System.out.println("WolframAnswerer");
    }
    
    /**
     * Test of WolframAnswerer constructor.
     * The constructor of WolframAnswerer loads a key from
     * a properties file, this test makes sure the key is in that file.
     * 
     * @author 650033777
     */
    public void testWolframAnswerer(){
        System.out.println("WolframAnswerer");
        
        try {
            WolframAnswerer instance = new WolframAnswerer();
        } catch (KeyException ex) {
            fail("Unable to load keys");
        }
    }
    
    /**
     * Test of ask method, of class WolframAnswerer
     * This class provides a simple way to ask a question in the form of
     * a string and get a string response back. This test checks that a response
     * is received, and matches what is to be expected.
     * 
     * @author 650033777
     */
    @Test
    public void testAsk() {
        System.out.println("ask");
        
        String question = "What colour is the sky?";
        String expResult = "The answer is sky blue";
        
        WolframAnswerer instance;
        try {
            instance = new WolframAnswerer();
        } catch (KeyException ex) {
            fail("Unable to load keys"); return;
        }
        
        String result;
        
        try {
            result = instance.ask(question);
            assertEquals("answer does not match expected result", 
                         expResult, 
                         result);
            
        } catch (AnswerException ex) {
            fail("wolfram did not respond as expected");
        }
    }
    
    /**
     * Test of urlEncode, of class WolframAnswerer
     * URLs cannot contain spaces, so we convert them to plus symbols. 
     * This test ensures that it can detect and replace those characters.
     * 
     * @author 650033777
     */
    public void testUrlEncode() {
        System.out.println("urlEncode");
        
        String result = WolframAnswerer.urlEncode("a b_c!");
        String expResult = "a+bc";
        
        assertEquals("encoding does not match expected result", 
                     expResult, 
                     result);
    }
    
    /**
     * Test of AnswerException, of class WolframAnswerer
     * Creates a scenario that should force an AnswerException to be thrown.
     * 
     * @throws AnswerException 
     * @author 620007467
     */
    @Test(expected = AnswerException.class)
    public void testAnswerException() throws AnswerException {
        System.out.println("AnswerException");
        
        WolframAnswerer instance;
        try {
            instance = new WolframAnswerer();
        } catch (KeyException ex) {
            fail("Unable to load keys"); return;
        }
                
        instance.ask("");
        
        fail("No exception was raised.");
    }
    
}
