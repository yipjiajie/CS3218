package sg.edu.nus.cs3218tut_yipjiajie;

import android.media.AudioRecord;
import android.util.Log;

/**
 * Created by ngtk on 4/2/16.
 */

public class SoundSampler {
    private static final int  FS = 16000;     // sampling frequency
    public AudioRecord audioRecord;
    private int               audioEncoding = 2;
    private int               nChannels = 16;
    private Thread            recordingThread;

    public SoundSampler(tutorial3 mAct) throws Exception
    {
        try {
            if (audioRecord != null) {
                audioRecord.stop();
                audioRecord.release();
            }
            audioRecord = new AudioRecord(1, FS, nChannels, audioEncoding, AudioRecord.getMinBufferSize(FS, nChannels, audioEncoding));
        }
        catch (Exception e) {
            Log.d("Error in SoundSampler ", e.getMessage());
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

        tutorial3.bufferSize = AudioRecord.getMinBufferSize(FS, nChannels, audioEncoding);
        tutorial3.buffer = new short[tutorial3.bufferSize];

        audioRecord.startRecording();

        recordingThread = new Thread()
        {
            public void run()
            {
                while (true)
                {

                    audioRecord.read(tutorial3.buffer, 0, tutorial3.bufferSize);
                    tutorial3.surfaceView.drawThread.setBuffer(tutorial3.buffer);

                }
            }
        };
        recordingThread.start();

        return;



    }



}

