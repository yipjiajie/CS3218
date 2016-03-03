package sg.edu.nus.cs3218tut_yipjiajie;


import android.media.AudioRecord;
import android.util.Log;

/**
 * Created by ngtk on 22/1/15.
 */
public class SoundSamplerSpectrogram {


    private static final int  FS = 16000;     // sampling frequency
    public  AudioRecord       audioRecord;
    private int               audioEncoding = 2;
    private int               nChannels = 16;
    private tutorial5c  spectrogram;
    private Thread            recordingThread;


    public SoundSamplerSpectrogram(tutorial5c mAct) throws Exception
    {

        spectrogram = mAct;

        try {
            if (audioRecord != null) {
                audioRecord.stop();
                audioRecord.release();
            }
            audioRecord = new AudioRecord(1, FS, nChannels, audioEncoding, AudioRecord.getMinBufferSize(FS, nChannels, audioEncoding));

        }
        catch (Exception e) {
            Log.d("Error in Init() ", e.getMessage());
            throw new Exception();
        }

        return;

    }

    public void init() throws Exception
    {
        try {
            if (audioRecord != null) {
                audioRecord.stop();
                audioRecord.release();
            }
            audioRecord = new AudioRecord(1, FS, nChannels, audioEncoding, AudioRecord.getMinBufferSize(FS, nChannels, audioEncoding));

        }
        catch (Exception e) {
            Log.d("Error in Init() ", e.getMessage());
            throw new Exception();
        }

        tutorial5c.bufferSize = AudioRecord.getMinBufferSize(FS, nChannels, audioEncoding);
        tutorial5c.buffer = new short[tutorial5c.bufferSize];

        audioRecord.startRecording();

        recordingThread = new Thread()
        {
            public void run()
            {
                while (true)
                {

                    audioRecord.read(tutorial5c.buffer, 0, tutorial5c.bufferSize);
                    spectrogram.surfaceView.drawThread.setBuffer(tutorial5c.buffer);

                }
            }
        };
        recordingThread.start();

        return;

    }


}