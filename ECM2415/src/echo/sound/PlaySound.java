package echo.sound;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * PlaySound class takes formatted sound in the form of a byte array and plays the
 * sound directly out to the speakers.
 * 
 * @author 620007467
 * @author 650033777
 * @author David Wakeling
 */
public class PlaySound {
    
    private static final int     SAMPLE_RATE     = 16000; /* MHz  */
    private static final int     SAMPLE_SIZE     = 16;    /* bits */
    private static final int     SAMPLE_CHANNELS = 1;     /* mono */
    
    
    /**
     * playWavSound method plays byte array in wav format, first removing
     * the header
     * 
     * @param byteArray the byteArray to be played
     * @author 650033777
     * @author 640034510 
     */
    public static void playWavSound(byte[] data){
        try {
            /*Remove WAV header */
            data = Arrays.copyOfRange(data, 44, data.length-44);
            /*Created a custom wav format as per Wolfram's specifications.*/
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 16000, 16, 1, 2, 8000, false);
            Clip clip = AudioSystem.getClip();
            clip.open(format, data, 0, data.length);
            
            /* Calculate duration of 'clip' in miliseconds */
            long frames = clip.getFrameLength();
            long miliDuration = (long) (((frames+0.0) / format.getFrameRate() * 1000)-250);
            clip.start();
            
            /* Sleep while 'clip' is playing. */
                Thread.sleep(miliDuration);

        } catch (LineUnavailableException ex) {
                System.err.println("Line Intterupted: Cannot get line");       
        } catch (InterruptedException ex) {
                System.err.println("OnOffButton: Sleep error interrupted");
        }    
    }
    
    /**
     * playByteArray method takes a byteArray and plays the sound content directly.
     * 
     * @param byteArray the byteArray to be played
     * 
     */
    static void playByteArray(byte[] byteArray) {
        AudioFormat af =
        new AudioFormat( SAMPLE_RATE
                       , SAMPLE_SIZE
                       , SAMPLE_CHANNELS
                       , true /* signed */
                       , false /* little-endian */
                       );
        DataLine.Info  info = new DataLine.Info( SourceDataLine.class, af );
        try {
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine( info );

            line.open( af );
            line.start();
            line.write( byteArray, 0, byteArray.length );
        } catch ( LineUnavailableException ex ) {
            System.out.println( ex ); System.exit( 1 );
        }
    }
    
    
  /**
   * setUpStream method sets up an auto stream and creates an AudioInputStream 
   * object
   * Catches an IO exception if unsuccessful
   * 
   * @param name The filename to play.
   * 
   * @return     The AudioInputStream object created from the file.
   */
  static AudioInputStream setupStream( String name ) {
    try {
      File             file = new File( name );
      AudioInputStream stm  = AudioSystem.getAudioInputStream( file );
      return stm;
    } catch ( IOException | UnsupportedAudioFileException ex ) {
      System.out.println( ex ); System.exit( 1 ); return null;
    }
  }
  
  /**
   * readStream method reads a stream and creates a buffer of a byte array in 
   * the format of a ByteArrayOutputStream.
   * @param stm The AudioInputStream to be read and converted
   * @return    The byte array in the format of ByteArrayOutputStream to be played
   */
  static ByteArrayOutputStream readStream( AudioInputStream stm ) {
    try {
      AudioFormat           af  = stm.getFormat();
      ByteArrayOutputStream bos = new ByteArrayOutputStream();

      int  bufferSize = (int) af.getSampleRate() * af.getFrameSize();
      byte buffer[]   = new byte[ bufferSize ];

      for (;;) {
        int n = stm.read( buffer, 0, buffer.length );
        if ( n > 0 ) {
           bos.write( buffer, 0, n );
        } else {
          break;
        }
      }

      return bos;
    } catch ( IOException ex ) {
      System.out.println( ex ); System.exit( 1 ); return null;
    }
  }
  
    /**
     * readByteArray method reads the data from file. 
     * This method is for purposes of creating a known byte array from a WAV 
     * file for use in testing.
     */
    public static byte[] readByteArray(String filename) {
        try {
            File file = new File(filename);
            FileInputStream fis = new FileInputStream(file);
            DataInputStream dis = new DataInputStream(fis);
            
            byte[] buffer = new byte[(int)file.length()];
            dis.readFully(buffer);
            dis.close();
            return buffer;
            
        } catch (IOException ex) {
            System.out.println(ex); System.exit(1); return null;
        }
    }
    
    /**
     * playFile method takes a filename, creates the corresponding AudioInputStream
     * and then ByteArrayOutputStream, then plays the sound directly, without
     * creating an intermediary audio file.
     * 
     * @param filename The file to be played
     */
    static void playFile(String filename) {
        AudioInputStream stm = setupStream(filename);
        ByteArrayOutputStream bos = readStream(stm);

        try {
            AudioFormat    af   = stm.getFormat();
            byte[]         ba   = bos.toByteArray();
            DataLine.Info  info = new DataLine.Info( SourceDataLine.class, af );
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine( info );

            line.open( af );
            line.start();
            line.write( ba, 0, ba.length );
        } catch ( LineUnavailableException ex ) {
            System.out.println( ex ); System.exit( 1 );
        }
    }
    
    /**
     * Plays audio file specified by the file path and blocks the thread for the
     * duration. Additional modifications to the delay can be set.
     * @author 650033777
     * @author 640034510
     * @param filePath  Path to sound file
     * @param ms        Additional pause
     */    
    public static void playFileWithSleep(String filePath, int ms){                
        try {
            
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            
            /* Calculate duration of 'clip' in miliseconds */
            AudioFormat format = audioInputStream.getFormat();
            long frames = audioInputStream.getFrameLength();
            long miliDuration = (long) (((frames+0.0) / format.getFrameRate() * 1000) + ms);
            clip.start();
            
            /* Sleep while 'clip' is playing. */
            Thread.sleep(miliDuration);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println(e.getMessage());
        }
        catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
