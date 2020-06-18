package echo.gui;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Icon;
import echo.sound.PlaySound;

/**
 * OnOffButton class provides functionality to the button, allowing the device
 * to be turned on and off, additionally calling the appropriate classes to
 * respond to questions
 * This class acts as the controller in the model-view design pattern.
 * 
 * @author 640033101
 * @author 640034510
 */
public class OnOffButton extends JButton{

    boolean onOff;    
    EchoFunction echoFun;
    EchoGUI gui;
    Activate activate;
    
    final AtomicInteger functionProgress;
    final AtomicBoolean functionEnd;
    final AtomicBoolean enableButton;
    
    final static String STARTUP = "./res/startup.wav";
    final static String SHUTDOWN = "./res/shutdown.wav";
    
    /**
     * OnOffButton constructor which controls the GUI updating. 
     * This button acts as the controller in the MVC design pattern, 
     * interfacing between the GUI and EchoFunction.
     * 
     * @param  onOffLabel       button label
     * @param functionProgress  atomic progress flag
     * @param functionEnd       atomic end flag
     * @author 640033101
     * @author 640034510
    */
    public OnOffButton( Icon onOffLabel, AtomicInteger functionProgress, AtomicBoolean functionEnd) {
        super(onOffLabel);
        this.onOff = false;
        this.functionProgress = functionProgress;
        this.functionEnd = functionEnd; 
        enableButton = new AtomicBoolean(true);
    }
    
    /**
     * activateOnOffButton method enables the on off button. This is separated 
     * from the constructor to ensure that the OnOffButton is initialised fully 
     * before it is added to the GUI.
     * 
     * @author 640034510
     */
    public void activateOnOffButton(){
        this.setEnabled(true);
        activate = new Activate();
        this.addActionListener(activate);         
    }
    
    /**
     * Activate nested class listens for the button to be pressed and controls the
     * echo state.
     */
    private class Activate implements ActionListener {
        /**
         * Overridden actionPerformed method. Controls the function  
         * of the echo and allows for it to update the GUI remotely. 
         * Additionally this controls the running of an instance of 
         * EchoFunction and either starts or stops it running in certain states.
         * 
         * @param  click  the action event object created by an action.
         * @author 640034510
         */
        @Override
        public void actionPerformed( ActionEvent click ) {
            Thread echoThread = null;
            /* Ensure that both echoFun and gui have been set to prevent
                unusual behavior. */
            if(enableButton.get() && echoFun != null && gui != null){
                if (!onOff) {
                    PlaySound.playFileWithSleep(STARTUP,400);                   
                    onButtonDelay(500);                   
                    onOff = true;
                    functionEnd.set(false);
                    functionProgress.set(0);
                    /* 
                    A new thread is made to launch the swingworker, echoFun,
                    and prevent it blocking the GUI when it is running.
                    It is less pretty than echoFun.execute but since we don't 
                    want to create a new EchoFunction object each time we do
                    something as it significantly impacts on the startup time
                    of the echo.                    
                    */
                    echoThread = new Thread(){
                        @Override
                        public void run(){                            
                            echoFun.doFunctions();
                        }
                    };
                    echoThread.start();
                    turnBtnLightsOn();
                    enterListeningMode();


                }else{
                    if(functionProgress.get() == 0){
                        /* Shut down the echo and end associated model threads. */
                        turnBtnLightsOff();
                        functionProgress.set(2);
                        functionEnd.set(true);
                        PlaySound.playFileWithSleep(SHUTDOWN,0);  
                        onOff = false;
                        enterOffMode();
                        offButtonDelay(200,echoThread);
                    }                    
                }
            }
        }
    }
    
    /**
     * playOn method plays a static on-sound. The file is located in
     * "./res/startup.wav" and pauses thread execution while the sound is 
     * playing. 
     * 
     * @author 640033101
     * @author 640034510
     */
    public void playOn() {
        try {
            String startup = "./res/startup.wav";
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                                        new File(startup).getAbsoluteFile());
            
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            
            /* Calculate duration of 'clip' in miliseconds */
            AudioFormat format = audioInputStream.getFormat();
            long frames = audioInputStream.getFrameLength();
            long miliDuration = 
                (long) (400 + ((frames+0.0) / format.getFrameRate() * 1000)) ;
            clip.start();
            
            /* Sleep while 'clip' is playing. */
            try {
                Thread.sleep(miliDuration);
            } catch (InterruptedException ex) {
                System.out.println("OnOffButton: Sleep 1 interrupted");
            }
        } catch (UnsupportedAudioFileException | 
                 IOException                   | 
                 LineUnavailableException e) {
            System.err.println(e.getMessage());
        }
    }    
    
    /**
     * addGui method sets the view which the button controls. Must be the
     * GUI that the button is connected to otherwise a different gui will 
     * update. This is the view in MVC.
     * 
     * @param gui GUI instance to be controlled
     * @author 640034510
     */
    public void addGui(EchoGUI gui){
        this.gui = gui;
    }
    
    /**
     * addEchoFunctions methods sets the instance of EchoFunction that is 
     * controlled by the button. This  is model in the MVC pattern.
     * 
     * @param echoFun EchoFunction instance to be run.
     * 
     * @author 640034510
     */
    public void addEchoFunctions(EchoFunction echoFun){
        this.echoFun = echoFun;
    }
    
    /**
     * enterListeningMode provides a binding to make the GUI enter listening 
     * mode remotely.
     * 
     * @author 640034510
     */
    public void enterListeningMode(){
        gui = gui.getInstance();
        gui.enterListeningMode();
    }
    
    /**
     * onButtonDelay disables the button for a specified time to prevent the
     * a button from being spammed with clicks. It creates a new thread
     * to ensure that the button's parent is not blocked by the wait 
     * @param ms        Time that the button is disabled for
     * @author 640034510
     */
    protected void onButtonDelay(int ms){      
        new Thread(){              
            @Override
            public void run(){
                enableButton.set(false);
                try {
                    Thread.sleep(ms);
                } catch (InterruptedException ex) {
                   System.err.println(ex.getMessage());
                }finally{
                    enableButton.set(true);
                }
                
            }
        }.start();
    }
    
    /**
     * onButtonDelay disables the button for a specified time to prevent the
     * a button from being overwhelmed with clicks. It creates a new thread
     * to ensure that the buttons parent is not blocked by the wait
     * 
     * @param ms        Time that the button is disabled for
     * @param thread    Thread to check if the thread has ended
     * 
     * @author 640034510
     */
    protected void offButtonDelay(int ms, Thread thread){            
        new Thread(){              
            @Override
            public void run(){
                enableButton.set(false);
                try {
                    Thread.sleep(ms);
                } catch (InterruptedException ex) {
                   System.err.println(ex.getMessage());
                }finally{
                    if(thread!=null){
                        while(thread.isAlive()){
                            //Wait for thread to die
                        }
                    }
                    enableButton.set(true);
                }
            }
        }.start();
    }   
    
    /**
     * Binding to make the GUI enter speaking mode remotely.
     * enterSpeakingMode provides a binding to make the GUI enter speaking mode 
     * remotely.
     * 
     * @author 640034510
     */
    public void enterSpeakingMode(){
        gui = gui.getInstance();
        gui.enterSpeakingMode();
    }
    
    /**
     * enterOffMode provides a binding to make the GUI enter off mode remotely.
     * 
     * @author 640034510
     */
    public void enterOffMode(){
        gui = gui.getInstance();
        gui.shutDown();
    }
    
    /**
     * Binding to make the on/off button light up remotely.
     * 
     * @author 640033101
     */
    public void turnBtnLightsOn(){
        gui = gui.getInstance();
        gui.buttonLightsOn();
    }
    
    /**
     * Binding to make the on/off button light go off remotely.
     * 
     * @author 640033101
     */
    public void turnBtnLightsOff(){
        gui = gui.getInstance();
        gui.buttonLightsOff();
    }

    /**
     * Override current state of the button to immediately toggle the button
     * to turn the echo off. Can create some undesirable processes so should
     * only be used for testing or under dire circumstances.
     * 
     * @author 640033101
     */
    void remoteForceButtonOff(){
        if(activate != null){
            functionEnd.set(true);
            onOff = true;
            enableButton.set(true);            
            if(echoFun == null){
                echoFun = new EchoFunction(this,functionProgress,functionEnd,null);
            }
            activate.actionPerformed(null);
        }
    }
}