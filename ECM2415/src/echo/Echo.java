package echo;

import echo.gui.EchoFunction;
import echo.gui.EchoGUI;
import echo.gui.OnOffButton;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Echo class provides access to several classes through
 * a distinct user interface. Contains the main method through which the
 * entire application runs.
 * 
 * @author 640033101
 * @author 640034510
 */
public class Echo extends JFrame {
    OnOffButton onOffBtn;
    EchoFunction echoFun;
    EchoGUI frame;
    
    AtomicInteger functionProgress;
    AtomicBoolean functionEnd;
    

    /**
     * Null argument constructor for Echo. Included to prevent it from being 
     * overwritten.
     * @author 640034510
     */
    public Echo(){
        
    }
    
    /**
     * newEcho method Initializes the components of the echo. Additionally this
     * function displays the JFrame window once all the components have been 
     * added to the echo.
     * 
     * @author 640033101
     * @author 640034510
     */
    public void newEcho(){
        functionProgress = new AtomicInteger();
        functionProgress.set(0);
        
        functionEnd = new AtomicBoolean();
        functionEnd.set(false);
        
        /* Set up the button */
        Icon ONOFFIcon = new ImageIcon("./res/onoff.png");
        onOffBtn  = new OnOffButton(ONOFFIcon,functionProgress,functionEnd);
        onOffBtn.activateOnOffButton();
        
        onOffBtn.setBorderPainted(false);
        onOffBtn.setFocusPainted(false);
        onOffBtn.setContentAreaFilled(false);
        
        /* Set up echo functions */
        echoFun = new EchoFunction(onOffBtn, functionProgress, functionEnd);

        /* Set up frame */
        frame = new EchoGUI();
        
        /* Display the window */
        frame.setVisible( true );
        frame.setLocationRelativeTo( null );
        frame.setResizable( false );
        frame.setDefaultCloseOperation( Echo.EXIT_ON_CLOSE );  

        /* Add components to eachother */
        frame.addButton(onOffBtn);
        onOffBtn.addGui(frame);
        onOffBtn.addEchoFunctions(echoFun);
        
        JLabel background = new JLabel();
        frame.addBackground(background);
        Icon OFF = new ImageIcon("./res/echooff.png");
        background.setIcon(OFF);
    }
    
    
    /**
     * Main method through which the application runs.
     * @param args  Application ran with no arguments
     */
    public static void main(String[] args) { 
        Echo setUp = new Echo();
        setUp.newEcho();
    }
}

