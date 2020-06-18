package echo.gui;

import echo.gui.EchoGUI;
import echo.gui.OnOffButton;
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
 * EchoGuiTest provides unit tests of the EchoGui class to ensure that the GUI 
 * is being correctly set up and displays the appropriate icons, as well as 
 * correctly handling undefined behavior. 
 * 
 * @author 640034510
 */
public class EchoGUITest {
    EchoGUI testGUI;
    
    
    public EchoGUITest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        testGUI = new EchoGUI();
        JLabel background = new JLabel();
        testGUI.background = background;
    }
    
    @After
    public void tearDown() {
        testGUI = null;
    }
    
    /**
     * Test of EchoGui, of class EchoGUI
     * Basic test on the EchoGUI no argument constructor to ensure that it is 
     * creating the correct new instance.
     * 
     * @author 640034510 
     */
    @Test
    public void testEchoGUI(){
        System.out.println("EchoGUI");
        EchoGUI newGUI = new EchoGUI();
        assertNull(newGUI.background);
        assertNotNull("ButtonPanel is null", newGUI.buttonPanel);
    }
    
    /**
     * Test of addButton method, of class EchoGUI
     * Test that addButton method adds a component (the button) to the buttonPanel
     * @author 640034510 
     */
    @Test
    public void testAddButton(){
        System.out.println("addButton");
        OnOffButton newBtn  = new OnOffButton(null,null,null);        
        testGUI.addButton(newBtn);
        assertTrue(testGUI.buttonPanel.getComponentCount() == 1);
    }
    
    /**
     * Test of addButton method, of class EchoGUI
     * Ensures that addButton method doesn't add a null component to the buttonPanel
     * @author 640034510 
    */    
    @Test
    public void testNullAddButton(){
        System.out.println("addButton -- null");
        testGUI.addButton(null);
        assertTrue(testGUI.buttonPanel.getComponentCount() == 0);
    }
    
    /**
     * Test of getInstance method, of class EchoGUI. 
     * Ensures that getInstance returns itself.
     * @author 640034510
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        EchoGUI result = testGUI.getInstance();
        assertTrue(testGUI.equals(result));
    }

    /**
     * Test of enterListeningMode method, of class EchoGUI. 
     * Test to see if background is 'CYAN'
     * @author 640034510
     */
    @Test
    public void testEnterListeningMode() {
        System.out.println("enterListeningMode");
        testGUI.enterListeningMode();
        Icon CYAN = new ImageIcon("./res/echocyan.png");
        assertEquals(CYAN.getIconHeight(), 
                testGUI.background.getIcon().getIconHeight()
        );
    }

    /**
     * Test of enterSpeakingMode method, of class EchoGUI. 
     * Test to see if background is 'BLUE'
     * @author 640034510
     */
    @Test
    public void testEnterSpeakingMode() {
        System.out.println("enterSpeakingMode");
        testGUI.enterSpeakingMode();
        Icon BLUE = new ImageIcon("./res/echoblue.png");
        assertEquals(BLUE.getIconHeight(), 
                testGUI.background.getIcon().getIconHeight()
        );
    }

    /**
     * Test of shutDown method, of class EchoGUI.
     * Test to see if background is 'BLACK'
     * @author 640034510
     */
    @Test
    public void testShutDown() {
        System.out.println("shutDown");
        testGUI.shutDown();
        Icon BLACK = new ImageIcon("./res/echooff.png");
        assertEquals(BLACK.getIconHeight(), 
                testGUI.background.getIcon().getIconHeight()
        );
    }
    
    
    /**
     * Test of addBackground, of class EchoGUI
     * Ensures a background is added correctly.
     * @author 640034510
     */
    @Test
    public void testAddBackground(){
        System.out.println("addBackground");
        JLabel background = new JLabel();
        testGUI.addBackground(background);
        assertTrue(background.equals(testGUI.background));
    } 

    /**
     * Test of buttonLightsOn method, of class EchoGUI.
     * Check the light up state of a button
     * @author 640033101
     */
    @Test
    public void testButtonLightsOn() {
        System.out.println("buttonLightsOn");
        OnOffButton newBtn  = new OnOffButton(null,null,null);    
        testGUI.addButton(newBtn);
        testGUI.buttonLightsOn();
        Icon on = new ImageIcon("./res/onoffLIT.png");
        assertEquals(on.getIconHeight(), 
                testGUI.onOffBtn.getIcon().getIconHeight()
        );
    }

    /**
     * Test of buttonLightsOff method, of class EchoGUI.
     * Check the light up state of a button
     * @author 640033101
     */
    @Test
    public void testButtonLightsOff() {
        System.out.println("buttonLightsOff");
        OnOffButton newBtn  = new OnOffButton(null,null,null);    
        testGUI.addButton(newBtn);
        testGUI.buttonLightsOff();
        Icon off = new ImageIcon("./res/onoff.png");
        assertEquals(off.getIconHeight(), 
                testGUI.onOffBtn.getIcon().getIconHeight()
        );
    }
}
