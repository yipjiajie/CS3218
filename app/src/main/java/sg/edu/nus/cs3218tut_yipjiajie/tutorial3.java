package sg.edu.nus.cs3218tut_yipjiajie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class tutorial3 extends Activity {


    public  static          CSurfaceViewSoundSampling   	surfaceView;
    private SoundSampler   	soundSampler;
    public  static short[]  buffer;
    public  static int      bufferSize;     // in bytes

    public void goToMainActivity(View view){

        // --- fill up codes here to end all drawings and sound sampling before returning to MainActivity
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);

        surfaceView.drawFlag = Boolean.valueOf(false);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial3);

        try {
            soundSampler = new SoundSampler(this);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Cannot instantiate SoundSampler", Toast.LENGTH_LONG).show();
        }

        try {
            soundSampler.init();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Cannot initialize SoundSampler.", Toast.LENGTH_LONG).show();
        }

        surfaceView = (CSurfaceViewSoundSampling)findViewById(R.id.surfaceView);
        surfaceView.drawThread.setBuffer(buffer);
        Toast.makeText(tutorial3.this, "Tutorial 3 Sound started", Toast.LENGTH_SHORT).show();
    }


    public void captureSound(View v) {
        if (surfaceView.drawThread.soundCapture) {
            surfaceView.drawThread.soundCapture = Boolean.valueOf(false);
            surfaceView.drawThread.segmentIndex = -1;
        }
        else {
            surfaceView.drawThread.soundCapture = Boolean.valueOf(true);

        }
    }



}
