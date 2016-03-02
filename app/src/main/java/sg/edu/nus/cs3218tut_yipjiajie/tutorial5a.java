package sg.edu.nus.cs3218tut_yipjiajie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ngtk on 28/1/15.
 */

public class tutorial5a extends Activity {
    private String   trigo;
    private double   sineAmplitude;
    private double   cosineAmplitude;
    private double   sineFrequency;
    private double   cosineFrequency;
    private double   sinePhase;
    private double   cosinePhase;
    private TextView displayFunction;

    public  CSurfaceViewFFT   	surfaceView;
    public  static short[]  buffer;
    public  static int      bufferSize;     // in bytes

    public void goToMainActivity(View view) {

        try
        {
            CSurfaceViewFFT.drawFlag = Boolean.valueOf(false);
            surfaceView.drawThread.join();
            Intent myIntent = new Intent();
            setResult(RESULT_OK, myIntent);
        }
        catch (InterruptedException localInterruptedException)
        {
        }

        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial5a);
        displayFunction = (TextView)findViewById(R.id.displayFunction);
        trigo = "sine";
        sineAmplitude=0.0;
        cosineAmplitude=0.0;
        sineFrequency=0.0;
        cosineFrequency=0.0;
        sinePhase = 0.0;
        cosinePhase=0.0;
        bufferSize = 1024;
        buffer = new short[bufferSize];
        surfaceView = (CSurfaceViewFFT)findViewById(R.id.surfaceView);
        Toast.makeText(tutorial5a.this, "Tutorial 5A FFT started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void sinePressed(View view) {
        trigo = "sine";
    }

    public void cosinePressed(View view) {
        trigo = "cosine";
    }

    public void AplusPressed(View view){

        if (trigo.equalsIgnoreCase("sine")) {
            sineAmplitude += 1.0;

        } else if (trigo.equalsIgnoreCase("cosine")) {
            cosineAmplitude += 1.0;
        }

        composeSignal();

    }

    public void AminusPressed(View view){

        if (trigo.equalsIgnoreCase("sine")) {
            sineAmplitude -= 1.0;

        } else if (trigo.equalsIgnoreCase("cosine")) {
            cosineAmplitude -= 1.0;
        }

        composeSignal();

    }

    public void FplusPressed(View view){

        if (trigo.equalsIgnoreCase("sine")) {
            sineFrequency += 1.0;

        } else if (trigo.equalsIgnoreCase("cosine")) {
            cosineFrequency += 1.0;
        }

        composeSignal();

    }

    public void FminusPressed(View view){

        if (trigo.equalsIgnoreCase("sine")) {
            sineFrequency -= 1.0;

        } else if (trigo.equalsIgnoreCase("cosine")) {
            cosineFrequency -= 1.0;
        }

        composeSignal();

    }

    public void PplusPressed(View view){

        if (trigo.equalsIgnoreCase("sine")) {
            sinePhase += 1.0;

        } else if (trigo.equalsIgnoreCase("cosine")) {
            cosinePhase += 1.0;
        }

        composeSignal();

    }

    public void PminusPressed(View view){

        if (trigo.equalsIgnoreCase("sine")) {
            sinePhase -= 1.0;

        } else if (trigo.equalsIgnoreCase("cosine")) {
            cosinePhase -= 1.0;
        }

        composeSignal();

    }

    public void fftPressed(View view) {

        if (surfaceView.drawThread.FFTComputed) {
            surfaceView.drawThread.FFTComputed  = Boolean.valueOf(false);
        } else {
            surfaceView.drawThread.FFTComputed = Boolean.valueOf(true);
        }

    }


    public void composeSignal() {

        displayFunction.setText(String.valueOf(sineAmplitude) + " sin( 2π" +
                String.valueOf(sineFrequency) + " + " + String.valueOf(sinePhase) + " )" + " + " +
                String.valueOf(cosineAmplitude) + " cos( 2π" +
                String.valueOf(cosineFrequency) + " + " + String.valueOf(cosinePhase) + " )");

        // plot 1 second duration of signal
        double radStepSine    = 2*Math.PI*sineFrequency   / (double)bufferSize;
        double radStepCosine  = 2*Math.PI*cosineFrequency / (double)bufferSize;
        double freqSine       = 0.0;
        double freqCosine     = 0.0;
        double sinePhaseRad   = sinePhase*Math.PI/180;
        double cosinePhaseRad = cosinePhase*Math.PI/180;
        for (int i=0; i<bufferSize; i++) {
            buffer[i] = (short)(sineAmplitude*Math.sin(freqSine + sinePhaseRad) + cosineAmplitude*Math.cos(freqCosine + cosinePhaseRad));
            freqSine   += radStepSine;
            freqCosine += radStepCosine;
        }
        surfaceView.drawThread.setBuffer(buffer);
    }


}
