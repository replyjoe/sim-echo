package echo.gui;

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
 * OnOffButtonTest provides unit tests for the methods in OnOfButton class
 * @author 640033101
 * @author 640034510
 */
public class OnOffButtonTest {
    
    OnOffButton testButton;
    AtomicInteger functionProgress;
    AtomicBoolean functionEnd;
    EchoGUI testGUI;
    EchoFunction echoFun;
    public OnOffButtonTest() {
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
        testButton  = new OnOffButton(ONOFFIcon,functionProgress,functionEnd);
        
        JLabel background = new JLabel();
        testGUI = new EchoGUI();
        testGUI.background = background;
        
        
        echoFun = new EchoFunction(testButton,functionProgress,functionEnd);
        
        functionProgress.set(0);
        functionEnd.set(false);
    }
    
    @After
    public void tearDown() {
        if(echoFun != null && echoFun.recorder != null){
            echoFun.recorder.closeStream();
        }
        functionProgress = null;
        functionEnd = null;
        testButton = null;
        testGUI = null;
        echoFun = null;
    }


    
    /**
     * Test of addGui method, of class OnOffButton.
     */
    @Test
    public void testAddGui() {
        System.out.println("addGui");
        testButton.addGui(testGUI);
        assertTrue(testGUI.equals(testButton.gui));
    }
    
    /**
     * Test of addEchoFunctions method, of class OnOffButton.
     * @author 640034510
     */
    @Test
    public void testAddEchoFunctions() {
        System.out.println("addEchoFunctions");
        testButton.addEchoFunctions(echoFun);
        assertTrue(echoFun.equals(testButton.echoFun));
    }

    /**
     * Test of enterListeningMode method, of class OnOffButton.
     */
    @Test
    public void testEnterListeningMode() {
        System.out.println("enterListeningMode");
        testButton.addGui(testGUI);
        testGUI.addButton(testButton);
        testButton.enterListeningMode();
        Icon CYAN = new ImageIcon("./res/echocyan.png");
        assertEquals(CYAN.getIconHeight(), 
                testGUI.background.getIcon().getIconHeight()
        );  
    }
    
    /**
     * Test of enterSpeakingMode method, of class OnOffButton.
     * @author 640034510
     */
    @Test
    public void testEnterSpeakingMode() {
        System.out.println("enterSpeakingMode");
        testButton.addGui(testGUI);
        testGUI.addButton(testButton);
        testButton.enterSpeakingMode();
        Icon BLUE = new ImageIcon("./res/echoblue.png");
        assertEquals(BLUE.getIconHeight(), 
                testGUI.background.getIcon().getIconHeight()
        );
    }

    /**
     * Test of enterOffMode method, of class OnOffButton.
     * @author 640034510
     */
    @Test
    public void testEnterOffMode() {
        System.out.println("enterOffMode");
        testButton.addGui(testGUI);
        testGUI.addButton(testButton);
        testButton.enterOffMode();
        Icon BLACK = new ImageIcon("./res/echooff.png");
        assertEquals(BLACK.getIconHeight(), 
                testGUI.background.getIcon().getIconHeight()
        );
    } 
    
    
    /**
     * Test of activateOnOffButton method, of class OnOffButton. This is to 
     * ensure that no erratic behaviour occurs when functions have not been
     * passed.
     * @author 640034510
     */
    @Test
    public void testActivateOnOffButtonNullFunctions() {
        System.out.println("activateOnOffButton - Null Functions");
        testButton.gui = testGUI;
        testButton.echoFun = null;
        testButton.activateOnOffButton();
        assertFalse(testButton.onOff);
    }
    
    /**
     * Test of activateOnOffButton method, of class OnOffButton. This is to 
     * ensure that no erratic behaviour occurs when the end flag has not been
     * initialised.
     * @author 640034510
    */
    @Test
    public void testActivateOnOffButtonNullEndFlag() {
        System.out.println("activateOnOffButton - Null End Flag");
        testButton.gui = null;
        testButton.echoFun = echoFun;
        testButton.activateOnOffButton();
        assertFalse(testButton.onOff);
    }
    
     /**
     * Test of turnBtnLightsOn method, of class EchoGUI. Ensure that button 
     * image is changed
     * @author 640034510
     */
    @Test
    public void testTurnBtnLightsOn() {
        System.out.println("buttonLightsOn");
        testButton.gui = testGUI;
        testGUI.addButton(testButton);
        testButton.turnBtnLightsOn();        
        Icon on = new ImageIcon("./res/onoffLIT.png");
        assertEquals(on.getIconHeight(), 
                testGUI.onOffBtn.getIcon().getIconHeight()
        );
    }

    /**
     * Test of buttonLightsOff method, of class EchoGUI. Ensure that button 
     * image is changed
     * @author 640034510
     */
    @Test
    public void testTurnBtnLightsOff() {
        System.out.println("buttonLightsOn");
        testButton.gui = testGUI;
        testGUI.addButton(testButton);
        testButton.turnBtnLightsOff();
        Icon on = new ImageIcon("./res/onoffLIT.png");
        assertEquals(on.getIconHeight(), 
                testGUI.onOffBtn.getIcon().getIconHeight()
        );
    }

    /**
     * Test of activateOnOffButton method, of class OnOffButton.
     */
    @Test
    public void testActivateOnOffButton() {
        System.out.println("activateOnOffButton");
        testButton.activateOnOffButton();
        assertTrue(testButton.isEnabled());
    }

    /**
     * Test of onButtonDelay method, of class OnOffButton.
     */
    @Test
    public void testOnButtonDelay() {
        System.out.println("onButtonDelay");
        int ms = 1;
        try {
            testButton.enableButton.set(false);
            testButton.onButtonDelay(ms);
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            System.err.println(ex.getMessage());
        }
        assertTrue(testButton.enableButton.get());
    }

    /**
     * Test of offButtonDelay method, of class OnOffButton.
     */
    @Test
    public void testOffButtonDelay() {
        System.out.println("offButtonDelay");
        int ms = 1;
        Thread thread = new Thread(){
                @Override 
                public void run(){
                    for (int i = 0; i < 1000;i++){
                        //Do nothing
                    }
                }
        };
        try {
            testButton.enableButton.set(false);
            testButton.offButtonDelay(ms, thread);
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            System.err.println(ex.getMessage());
        }
        assertTrue(testButton.enableButton.get());
    }


    /**
     * Test of remoteForceButtonOff method, of class OnOffButton.
     */
    @Test
    public void testRemoteForceButtonOff() {
        System.out.println("remoteForceButtonOff");
        testButton.remoteForceButtonOff();
        
        assertEquals(true,testButton.functionEnd.get());
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
