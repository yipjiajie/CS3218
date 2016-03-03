package sg.edu.nus.cs3218tut_yipjiajie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick_Calculator(View view) {
        Intent myIntent;
        myIntent = new Intent(this, tutorial1.class);
        startActivity(myIntent);
    }

    public void onClick_Graphing(View view) {
        int requestCode = 2;
        Intent myIntent;
        myIntent = new Intent(this, tutorial2.class);
        startActivityForResult(myIntent, requestCode);
    }

    public void onClick_Sound(View view) {
        int requestCode = 3;
        Intent myIntent;
        myIntent = new Intent(this, tutorial3.class);
        startActivityForResult(myIntent, requestCode);
    }

    public void onClick_Calculus(View view) {
        int requestCode = 4;
        Intent myIntent;
        myIntent = new Intent(this, tutorial4.class);
        startActivityForResult(myIntent, requestCode);
    }

    public void onClick_FFTA(View view) {
        int requestCode = 51;
        Intent myIntent;
        myIntent = new Intent(this, tutorial5a.class);
        startActivityForResult(myIntent, requestCode);
    }

    public void onClick_FFTB(View view) {
        int requestCode = 52;
        Intent myIntent;
        myIntent = new Intent(this, tutorial5b.class);
        startActivityForResult(myIntent, requestCode);
    }

    public void onClick_FFTC(View view) {
        int requestCode = 53;
        Intent myIntent;
        myIntent = new Intent(this, tutorial5c.class);
        startActivityForResult(myIntent, requestCode);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Tutorial 2 Graphing
        if(requestCode==2) {
            if(resultCode==RESULT_OK) {
                Toast.makeText(this, "Exited from Tutorial 2 Graphing", Toast.LENGTH_LONG).show();
            }
        }

        // Tutorial 3 Sound
        if(requestCode==3) {
            if(resultCode==RESULT_OK) {
                Toast.makeText(this, "Exited from Tutorial 3 Sound", Toast.LENGTH_LONG).show();
            }
        }

        // Tutorial 4 Calculus
        if(requestCode==4) {
            if(resultCode==RESULT_OK) {
                Toast.makeText(this, "Exited from Tutorial 4 Calculus", Toast.LENGTH_LONG).show();
            }
        }

        // Tutorial 5 FFT
        if(requestCode==51) {
            if(resultCode==RESULT_OK) {
                Toast.makeText(this, "Exited from Tutorial 5A FFT", Toast.LENGTH_LONG).show();
            }
        }

        if(requestCode==52) {
            if(resultCode==RESULT_OK) {
                Toast.makeText(this, "Exited from Tutorial 5B Live FFT", Toast.LENGTH_LONG).show();
            }
        }

        if(requestCode==53) {
            if(resultCode==RESULT_OK) {
                Toast.makeText(this, "Exited from Tutorial 5C Spectrogram", Toast.LENGTH_LONG).show();
            }
        }
    }



}
