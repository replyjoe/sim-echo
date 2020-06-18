package echo.gui;

import echo.api.AnswerException;
import echo.gui.EchoFunction;
import echo.gui.EchoGUI;
import echo.gui.OnOffButton;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * EchoFunctionTest provides unit tests for the class 
 * EchoFunction to ensure that the class sets up and displays 
 * the echo program correctly.
 * 
 * @author 640034510
 */
public class EchoFunctionTest {
    
    
    OnOffButton button;
    AtomicInteger functionProgress;
    AtomicBoolean functionEnd;
    EchoFunction testEchoFun;
    EchoGUI testGUI;
    
    public EchoFunctionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {

    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        functionProgress = new AtomicInteger();
        functionEnd = new AtomicBoolean();
        Icon ONOFFIcon = new ImageIcon("./res/onoff.png");
        button  = new OnOffButton(ONOFFIcon,functionProgress,functionEnd);
        testEchoFun = new EchoFunction(button,functionProgress,functionEnd);
        
        button.echoFun = testEchoFun;
        button.gui = new EchoGUI();
        button.gui.addBackground(new JLabel());
        button.gui.addButton(button);
        
        functionEnd.set(false);
        functionProgress.set(0);
    }
    
    @After
    public void tearDown() {
        if(testEchoFun != null && testEchoFun.recorder != null){
            testEchoFun.recorder.closeStream();
        }
        button  = null;
        testEchoFun = null;
        functionProgress = null;
        functionEnd = null;
    }
 
    /**
     * Test doInBackground, of class EchoFunction, when the end flag has 
        already been set to true.
     * @author 640034510
    */
    @Test
    public void testdoFunctionsEndImmidiately(){
        functionEnd.set(true);
        functionProgress.set(10);
        try {
            testEchoFun.doFunctions();
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        assertEquals(functionProgress.get(),10);
    }  
    
    /**
     * Test of doInBackground to ensure that there is no undefined behavior 
     * occurring if a the end flag is set to null.
     * @throws Exception exception to test
     * @author 640034510
     */    
    @Test(expected = NullPointerException.class)
    public void testDoFunctionsNullEndFlag() throws Exception{
        EchoFunction testEchoFun2 
                = new EchoFunction(button,functionProgress, null);
        functionProgress.set(10);
        testEchoFun2.doFunctions();
    }  
    
    /**
     * Test of doInBackground to ensure that there is no undefined behavior 
     * occurring if a the progress flag is set to null.
     * @throws Exception exception to test
     * @author 640034510
     */
    /*
    @Test(expected = NullPointerException.class)
    public void testDoInBackgroundNullProgressFlag() throws Exception{
        functionEnd.set(false);
        functionProgress = null;
        testEchoFun.doFunctions();
    }  */ 

    /**
     * Test of doFunctions method, of class EchoFunction.
     * @author 640034510
     */
    /*
    @Test
    public void testDoFunctions() {
        System.out.println("doFunctions");
        Thread testThread = new Thread(){
            @Override
            public void run(){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    System.err.println(ex.getMessage());
                }finally{
                    functionEnd.set(true);
                }
            }
        };
        //testEchoFun.doFunctions();
        testThread.start();
        assertEquals(functionProgress.get(), 0);
    }*/

    
    /**
     * Test of webKnox method, of class EchoFunction
     * Ensures answer is returned from WebKnox API as expected.
     * Test may be commented out due to limited number of available questions
     * left under WebKnox API key.
     * @author 620007467
     */
    /*
    @Test
    public void testwebKnox(){
        String question = "What colour is the sky?";
        String expResult = "Sky's color is blue.";
        
        String testResult;
        
        testResult = testEchoFun.webKnox(question);
        assertEquals("answer does not match expected result",
                expResult,
                testResult);
    }
    */
    
    /**
     * Test of playError method, of class EchoFunction
     * Ensures enters speaking mode before playing error message.
     * 
     * @author 620007467
     */
    @Test
    public void testPlayError(){
        testEchoFun.playError();
        Icon BLUE = new ImageIcon("./res/echoblue.png");
        assertEquals(BLUE.getIconHeight(), 
                testGUI.background.getIcon().getIconHeight()
        );
    }
}
