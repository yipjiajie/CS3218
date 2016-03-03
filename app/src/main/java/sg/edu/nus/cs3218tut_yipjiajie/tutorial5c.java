package sg.edu.nus.cs3218tut_yipjiajie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class tutorial5c extends Activity {

	public  static CSurfaceViewSpectrogram   	surfaceView;
	private SoundSamplerSpectrogram   	soundSampler;
	public  static short[]  buffer;
    public  static int      bufferSize;     // in bytes

    public void goToMainActivity(View view){

        // --- fill up codes here to end all drawings and sound sampling before returning to MainActivity
    	Intent myIntent = new Intent();
    	setResult(RESULT_OK, myIntent);

    	surfaceView.drawFlag = Boolean.valueOf(false);
    	soundSampler.audioRecord.stop();
    	soundSampler.audioRecord.release();
    	finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial5c);

        try {
            soundSampler = new SoundSamplerSpectrogram(this);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Cannot instantiate SoundSampler", Toast.LENGTH_LONG).show();
        }

        try {
            soundSampler.init();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Cannot initialize SoundSampler.", Toast.LENGTH_LONG).show();
        }

        surfaceView = (CSurfaceViewSpectrogram)findViewById(R.id.surfaceView);
        surfaceView.drawThread.setBuffer(buffer);
        Toast.makeText(tutorial5c.this, "Tutorial 5C Spectrogram started", Toast.LENGTH_SHORT).show();
    }

       public void captureSoundSpectrogram(View v) {
        if (surfaceView.drawThread.captureSpectrogram) {
            surfaceView.drawThread.captureSpectrogram = Boolean.valueOf(false);
            surfaceView.drawThread.segmentIndex = -1;
        }
        else {
            surfaceView.drawThread.captureSpectrogram = Boolean.valueOf(true);

        }
    }


}
