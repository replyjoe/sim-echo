package echo.sound;

import echo.api.AnswerException;
import org.junit.Test;

/**
 * RecordSoundTest provides unit tests for methods in the RecordSoundTest class. 
 * Currently this contains no testing as we are performing integration testing 
 * on this class, but we have * this template if we later decide to spoof a microphone.
 *
 * @author 620007467
 * @author 650033777
 */
public class RecordSoundTest {

    /**
     * Test of resetStream method, of class RecordSound.
     * @throws echo.AnswerException
     */
    @Test
    public void testResetStream() throws AnswerException {
        System.out.println("resetStream");
        
        /* fail("The test case is a prototype.");
        RecordSound instance = new RecordSound();
        instance.resetStream();
        */
    }

    /**
     * Test of calculateNoiseValue method, of class RecordSound.
     */
    @Test
    public void testCalculateNoiseValue() {
        System.out.println("calculateNoiseValue");
        
        /* fail("The test case is a prototype.");
        List<Float> sampleList = null;
        float expResult = 0.0F;
        float result = RecordSound.calculateNoiseValue(sampleList);
        assertEquals(expResult, result, 0.0);
        */
    }

    /**
     * Test of byteArrayToNoiseValue method, of class RecordSound.
     */
    @Test
    public void testByteArrayToNoiseValue() {
        System.out.println("byteArrayToNoiseValue");
        
        /* fail("The test case is a prototype.");
        byte[] byteArray = null;
        float expResult = 0.0F;
        float result = RecordSound.byteArrayToNoiseValue(byteArray);
        //assertEquals(expResult, result, 0.0);
        */
    }

    /**
     * Test of getSoundIn method, of class RecordSound.
     */
    @Test
    public void testGetSoundIn() {
        System.out.println("getSoundIn");
        
        /* fail("The test case is a prototype.");
        RecordSound instance = new RecordSound();
        byte[] expResult = null;
        byte[] result = instance.getSoundIn();
        assertArrayEquals(expResult, result);
        */
    }
    
}
