package sg.edu.nus.cs3218tut_yipjiajie;


import android.media.AudioRecord;
import android.util.Log;

/**
 * Created by ngtk on 22/1/15.
 */
public class SoundSamplerLiveFFT {


    private static final int  FS = 16000;     // sampling frequency
    public  AudioRecord       audioRecord;
    private int               audioEncoding = 2;
    private int               nChannels = 16;
    private tutorial5b  liveFFTActivity;
    private Thread            recordingThread;


    public SoundSamplerLiveFFT(tutorial5b mAct) throws Exception
    {

        liveFFTActivity = mAct;

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


        tutorial5b.bufferSize = AudioRecord.getMinBufferSize(FS, nChannels, audioEncoding);
        tutorial5b.buffer = new short[tutorial5b.bufferSize];

        audioRecord.startRecording();

        recordingThread = new Thread()
        {
            public void run()
            {
                while (true)
                {

                    audioRecord.read(tutorial5b.buffer, 0, tutorial5b.bufferSize);
                    liveFFTActivity.surfaceView.drawThread.setBuffer(tutorial5b.buffer);

                }
            }
        };
        recordingThread.start();

        return;

    }


}