package echo.gui;

import java.awt.Dimension;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

/**
 * EchoGUI class provides the visual GUI as seen by the end-user. 
 * 
 * @author 640033101
 * @author 640034510
 */
public class EchoGUI extends JFrame{
    
    //public static final EchoGUI INSTANCE = new EchoGUI();
    
    protected OnOffButton onOffBtn;
    protected JLabel background;
    JPanel buttonPanel = new JPanel();
    
    
    /**
     * A constructor for the GUI, initializes a new GUI.
     * 
     * @author 640033101
     */
    public EchoGUI() {
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setSize(300, 600);
        add(buttonPanel);
        
        buttonPanel.setLayout(new OverlayLayout(buttonPanel));
        buttonPanel.setPreferredSize(new Dimension(300,600));
        
    }    
    
    
    /**
     * addButton method to be used in Echo class to connect a button to the GUI
     * 
     * @author 640033101
     * @author 640034510
     */
    public void addButton(OnOffButton onOffBtn){
        if(onOffBtn != null){
            this.onOffBtn  = onOffBtn;
            buttonPanel.setLayout(null);
            buttonPanel.add(this.onOffBtn);
            this.onOffBtn.setBorder(null);
            this.onOffBtn.setBounds(120 ,400, 60, 60 );
        }
    }
    
    /**
     * addBackground method to be used in Echo class to connect a changing 
     * background to the GUI
     * 
     * @author 640033101
     */
    public void addBackground(JLabel background){
        if(background != null){
            this.background = background;
            this.background.setBounds( 0,0, 300, 600 );
            buttonPanel.add( this.background );
        }
    }
    
    /**
     * getInstance method to access the current object state of the GUI from a
     * different class
     * 
     * @return  the current object state of the GUI
     */
    public EchoGUI getInstance() {
        return this;
    }
    
    /**
     * enterSpeakingMode method sets the GUI lights to blue, for when the echo
     * speaks to the user
     * 
     * @author 640033101
     */
    protected void enterSpeakingMode() {
        Icon BLUE = new ImageIcon("./res/echoblue.png");
        background.setIcon(BLUE);
    }
    
    /**
     * enterListeningMode method sets the GUI lights to cyan, for when the echo
     * is listening to the user speak
     * 
     * @author 640033101
     */
    protected void enterListeningMode(){
        Icon CYAN = new ImageIcon("./res/echocyan.png");
        background.setIcon(CYAN);
    }
    
    /**
     * shutDown method sets the GUI lights to be off (black) for when the echo
     * is off
     * 
     * @author 640033101
     */
    protected void shutDown() {
        Icon OFF = new ImageIcon("./res/echooff.png");
        background.setIcon(OFF);
    }
    
    /**
     * Switches button lights on
     * 
     * @author 640033101
     */
    protected void buttonLightsOn() {
        Icon on = new ImageIcon("./res/onoffLIT.png");
        this.onOffBtn.setIcon(on);
    }
    
    /**
     * Switches button lights off
     * 
     * @author 640033101
     */
    protected void buttonLightsOff() {
        Icon off = new ImageIcon("./res/onoff.png");
        this.onOffBtn.setIcon(off);
    }
    
}