package echo.sound;

import echo.api.AnswerException;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
 * RecordSound class allows the recording of sound, returning a generic
 * byte string to be used by other sections of the application.
 * 
 * @author 650033852
 * @author 640034510
 * @author 650033777
 * @author 620007467
 * @author David Wakeling
 */
public class RecordSound {
    private static final int     SAMPLE_DIVISION = 2;
    private static final int     THRESHOLD_TIME  = 2;     /* secs */
    private static final int     MAX_LENGTH      = 10;    /* secs  */
    private static final int     SAMPLE_RATE     = 16000; /* MHz  */
    private static final int     SAMPLE_SIZE     = 16;    /* bits */
    private static final int     SAMPLE_CHANNELS = 1;     /* mono */
    
    private float threshold;
    private AudioInputStream stm;
    private TargetDataLine line;
    
    /**
     * RecordSound constructor combines the SetupStream and calculateThreshold 
     * methods so that when called, it records sound from the user's microphone.
     * 
     * @throws AnswerException if setupStream or calculateThreshold methods fail
     */
    public RecordSound() throws AnswerException {
        setupStream();
        calculateThreshold();
    }
    
    /**
     * isConnected method checks if a microphone is connected
     * This method checks if the threshold is above zero, which means some
     * audio was detected. Works as threshold is calculated on construction.
     * 
     * @return boolean      true if threshold is above zero, false if not
     * 
     * @author 650033777
     * 
     */
    public boolean isConnected() {
        return threshold > 0;
    }
    
    /**
     * resetStream method resets the stream so as to
     * handle multiple calls and queries. 
     *
     * @author 650033777
     */
    public void resetStream() {
        line.flush();
    }
    
    /**
     * closeStream method closes the stream so as to
     * prevent instances of the input stream for overlapping.
     * 
     * @author 650033777
     */
    public void closeStream(){
        line.close();
    }
    
    /**
     * setupStream method creates a new stream to use when recording sound in.
     * @throws AnswerException if stream cannot be setup successfully
     */
    private void setupStream() throws AnswerException {
        try {
            AudioFormat af =
              new AudioFormat(SAMPLE_RATE
                             , SAMPLE_SIZE
                             , SAMPLE_CHANNELS
                             , true /* signed */
                             , false /* little-endian */
                             );

            DataLine.Info info = new DataLine.Info(TargetDataLine.class, af);
            line = (TargetDataLine) AudioSystem.getLine(info);
            stm = new AudioInputStream(line);
            
            line.open(af);
            line.start();
            line.flush();
            
        } catch (LineUnavailableException ex) {
            throw new AnswerException("Couldn't setup stream");
        }
    }
    
    /**
     * calculateNoiseValue method checks how loud the input noise is,
     * used for knowing when to record a question and when a quiet gap in 
     * between a question is.
     * 
     * @param sampleList a byte array converted to a float type array (converted
     *                   in byteArrayToNoiseValue method)
     * 
     * @return a float numerical value of the noise 
     * 
     * @author 650033777
     */
    static float calculateNoiseValue(List<Float> sampleList){
        float noise = 0f;
        float last = 0f;
        for(float sample : sampleList){
            
            float absolute = Math.abs(sample);
            noise += Math.abs(last - absolute);
            last = absolute;
        }
        return noise;
    }
    
    /**
     * byteArrayToNoiseValue method converts a byte array representation of
     * recorded sound to a float array representation, and uses the calculateNoise
     * method to return the noise level.
     * 
     * @param byteArray recorded sound in byte array format
     * 
     * @return          float array representation of a noise value
     * 
     * @author 650033777
     */
    static float byteArrayToNoiseValue( byte[] byteArray ) {
        List<Float> sampleList = new ArrayList<>();
        int bIndex = 0;
        int n = byteArray.length;
        
        while(bIndex < n) {
            int sample = 0;
            sample |= byteArray[bIndex++] << 8;
            sample |= byteArray[bIndex++] & 0xFF;
            sampleList.add(sample / 32768f);
        } 
        return calculateNoiseValue(sampleList);         
    }
    
    /**
     * calculateThreshold method reads the input stream and calculates 
     * what the background noise silence value is
     * 
     * @return The threshold noise value
     * 
     * @throws AnswerException if couldn't read stream 
     * 
     * @author 650033777
     */
    private void calculateThreshold() throws AnswerException {
        resetStream();
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        int  bufferSize = SAMPLE_RATE * stm.getFormat().getFrameSize() / SAMPLE_DIVISION;
        byte buffer[] = new byte[bufferSize];
            
        
        float noiseValue = 0;
        for(int i = 0; i < THRESHOLD_TIME * SAMPLE_DIVISION; i++) {
            try {
                int n = stm.read( buffer, 0, buffer.length ); 
            
                if ( n == 0 ) {
                    throw new AnswerException("Problem reading stream");
                }
                
                bos.write( buffer, 0, n );
                
            } catch (IOException ex) {
                throw new AnswerException("Problem reading stream");
            }
            noiseValue += byteArrayToNoiseValue(buffer);
        }
        threshold = noiseValue / (THRESHOLD_TIME * SAMPLE_DIVISION);
        threshold *= 1.3;
    }
    
    /**
     * ByteArrayOutputStream reads the input stream and converts it to a byte array.
     * The read will continue for a specified duration or when the noiseValue of
     * the last samples falls below a threshold indicating lack of speech.
     * 
     * @return The byte array of the input stream
     * 
     * @throws AnswerException if couldn't read stream
     * 
     * @author 650033777
     */
    private ByteArrayOutputStream readStream() throws AnswerException {
        boolean started = false;
        
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            int  bufferSize = SAMPLE_RATE * stm.getFormat().getFrameSize() / SAMPLE_DIVISION;
            byte buffer[] = new byte[bufferSize];

            
            int count = 0;
            int silence_count = 0;
            
            float noiseValue;
            do {
                int n = stm.read( buffer, 0, buffer.length ); 
                if ( n == 0 ) {
                    break;
                }
                
                noiseValue = byteArrayToNoiseValue(buffer);
                
                if(noiseValue < threshold) {
                    if(started) {
                        bos.write( buffer, 0, n );
                        count ++;
                    
                        silence_count ++;
                        
                        if(silence_count >= SAMPLE_DIVISION) {
                            break;
                        }
                    }
                } else {
                    if(silence_count >= SAMPLE_DIVISION) {
                        silence_count = 0;
                    }
                    
                    if(!started) {
                        started = true;
                    }
                    
                    bos.write( buffer, 0, n );
                    count ++;
                }
            } while ( noiseValue > 0 && count < MAX_LENGTH * SAMPLE_DIVISION);

            return bos;
        } catch (IOException ex) {
            throw new AnswerException("Problem reading stream");
        }
        
    }

    /**
     * getSoundIn method takes the microphone input and parses it to a bite
     * stream. To ensure that the bite stream is compatible with Microsoft 
     * cognitive services, a temporary wav file is created.
     * 
     * @return Byte array containing the audio stream
     * @throws AnswerException if problem recording sound
     */
    public byte[] getSoundIn() throws AnswerException {
        ByteArrayOutputStream bos = readStream();

        try {
            AudioFormat af =
              new AudioFormat( SAMPLE_RATE
                             , SAMPLE_SIZE
                             , SAMPLE_CHANNELS
                             , true /* signed */
                             , false
                             );
            
            byte[] ba = bos.toByteArray();
            InputStream is = new ByteArrayInputStream(ba);
            AudioInputStream ais  = new AudioInputStream(is, af, ba.length);
            
            File file = File.createTempFile("test", ".tmp"); 
            file.deleteOnExit();
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);
            
            FileInputStream fis = new FileInputStream(file);
            DataInputStream dis = new DataInputStream(fis);
            byte[] buffer = new byte[(int) file.length()];
            dis.readFully( buffer );
            dis.close();
            file.delete();
            
            return buffer;

        } catch ( IOException ex ) {
            throw new AnswerException("Problem recording sound");
        }
    }
    
}
