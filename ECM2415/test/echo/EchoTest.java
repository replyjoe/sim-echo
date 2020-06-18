package echo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * EchoTest provides unit tests of the Echo class methods
 * 
 * @author 640034510
 */
public class EchoTest {
    
    Echo testEcho;
    
    public EchoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        testEcho = new Echo();
    }
    
    @After
    public void tearDown() {
        testEcho = null;
    }
    
    /**
     * Test no argument constructor for Echo. Redundant but necessary for code
     * coverage.
     * @author 640034510 
     */
    @Test
    public void testEcho(){
        Echo newEcho = new Echo();
        assertNotNull(newEcho);
    }   
    
    /**
     * Test of newEcho method, of class Echo.
     * Ensures a new echo is initialised correctly.
     * 
     * @author 640034510 
     */
    @Test
    public void testNewEcho() {
        System.out.println("newEcho");
        testEcho.newEcho();
        assertNotNull("frame is null",testEcho.frame);
        assertNotNull("button is null",testEcho.onOffBtn);
        assertNotNull("functions are null",testEcho.echoFun);
        assertTrue("Frame not created.", testEcho.frame.isVisible());
    }

    /**
     * Test of main method, of class Echo.
     * @author 640034510 
     */
    @Test
    public void testMain() {
        //Nothing to test.
    }
    
}
