package echo.gui;

import echo.sound.PlaySound;
import echo.sound.RecordSound;
import echo.api.SpeechToText;
import echo.api.TextToSpeech;
import echo.api.WebknoxAnswerer;
import echo.api.WolframAnswerer;
import echo.api.MicrosoftText;
import echo.api.MicrosoftSpeech;
import echo.api.KeyException;
import echo.api.AnswerException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * EchoFunction class controls the progress of the functions for the echo 
 in a new thread. The class extends SwingWorker and overwrites the
 * doInBackground method to run echo functions in parallel with a GUI and acts 
 * as the model in the model view controller pattern.
 * 
 * @author 640034510
 */
public class EchoFunction{
    RecordSound recorder;
    WolframAnswerer wolframAnswerer;
    WebknoxAnswerer webknoxAnswerer;
    SpeechToText speechToText;
    TextToSpeech textToSpeech;
    
    final OnOffButton button; // Controller
    final AtomicInteger functionProgress;
    final AtomicBoolean functionEnd;
    final static String FILEPATH = "./res/failure.wav";
    /**
     * Constructor for EchoFunctions. Initializes the parts for translating
     * speech in to an spoken answer.
     * @param button            Controller for EchoFunctions
     * @param functionProgress  Atomic progress tracker
     * @param functionEnd       Atomic control flag 
     * 
     * @author 640034510
     * @author 650033777
     */
    public EchoFunction(OnOffButton button, 
                         AtomicInteger functionProgress, 
                         AtomicBoolean functionEnd) {
        try{
            recorder = new RecordSound();      
            wolframAnswerer = new WolframAnswerer();
            webknoxAnswerer = new WebknoxAnswerer();
            speechToText = new MicrosoftSpeech();
            textToSpeech = new MicrosoftText();
        } catch (AnswerException | KeyException ex) {
            System.err.println(ex);
            button.remoteForceButtonOff();
        }
        
        if (!recorder.isConnected()) {
            System.err.println("microphone not connected");
            button.remoteForceButtonOff();
        }
        
        this.functionProgress = functionProgress;
        this.functionEnd = functionEnd;
        this.button = button;
    }
   
        /**
     * Test constructor for EchoFunctions. Initializes the parts for translating
     * speech in to an spoken answer. Only used when a dummy object object needs
     * to be made. Will throw exceptions if used as a real EchoFunction object.
     * @param button            Controller for EchoFunctions
     * @param functionProgress  Atomic progress tracker
     * @param functionEnd       Atomic control flag 
     * @param mockObject        Object to set as APIs and Microphones
     * 
     * @author 640034510
     * @author 650033777
     * 
     */
   protected EchoFunction(OnOffButton button,
                         AtomicInteger functionProgress, 
                         AtomicBoolean functionEnd, 
                         Object mockObject){
        this.functionProgress = functionProgress;
        this.functionEnd = functionEnd;
        this.button = button;
        
        recorder = (RecordSound) mockObject;      
        wolframAnswerer = (WolframAnswerer) mockObject;
        webknoxAnswerer = (WebknoxAnswerer) mockObject;
        speechToText = (MicrosoftSpeech) mockObject;
        textToSpeech = (MicrosoftText) mockObject;
    }
    
    
    
    
    
    
    
    /**
     * Method designed to run echo functions
     * 
     * @author 640034510 
     * @author 650033777
     */
    public void doFunctions() {
        String question;
        String answer;
        
        while(!functionEnd.get()) {   
            functionProgress.set(0);      
            question = null;
            
            try {           
                /* Record Speech */
                button.enterListeningMode();
                recorder.resetStream();
                final byte[] speech = recorder.getSoundIn(); 
                /* Translate speech */
                if(!functionEnd.get()){
                    functionProgress.set(1);                    
                    question = speechToText.convertSpeech(speech);
                }else{
                    break;
                }                
            } catch (AnswerException ex) {
                System.err.println(ex);
                if(!functionEnd.get()){                    
                    playError(); 
                }else{
                    break;
                }
                               
            }  
            
            if(question != null) { 
                button.enterSpeakingMode();
                try {           
                    /* Get answer, convert to speech and play */
                    answer = wolframAnswerer.ask(question);
                } catch (AnswerException ex) {
                    answer = webKnox(question);
                }
                if(answer != null){
                    byte[] byteArray = textToSpeech.convertToAudioBytes(answer);
                    if(functionEnd.get()){
                        break;
                    }
                   
                    PlaySound.playWavSound(byteArray);
                }
            }
        }
    }
    
    void playError(){
        button.enterSpeakingMode();
        PlaySound.playFileWithSleep(FILEPATH,-250);
    }

    
    /**
     * webKnox method wraps calling webknoxAnswerer as a second attempt
     * to retrieve an answer to the provided question
     * 
     * @param String the question to be asked
     * @author 650033777
     * @author 64003451
     */
    String webKnox(String question ){
        String answer;
        try {           
            /* Get answer, convert to speech and play */
            answer = webknoxAnswerer.ask(question);            
        } catch (AnswerException ex) {
            System.err.println(ex);
            if(!functionEnd.get()){
                playError();
            }
            return null;           
        }         
        return answer;
    }
    
}
