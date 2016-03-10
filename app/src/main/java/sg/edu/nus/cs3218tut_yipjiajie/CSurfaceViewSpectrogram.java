package sg.edu.nus.cs3218tut_yipjiajie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;

/**
 * Created by ngtk on 4/2/16.
 */

public class CSurfaceViewSpectrogram extends SurfaceView implements SurfaceHolder.Callback {

    private Context drawContext;
    public  DrawThread       drawThread;
    private SurfaceHolder    drawSurfaceHolder;
    private Boolean          threadExists = false;
    public static volatile Boolean drawFlag = false;
    private static int rectPos = 0;


    private static final Handler handler = new Handler(){

        public void handleMessage(Message paramMessage)
        {
        }
    };

    public CSurfaceViewSpectrogram(Context ctx, AttributeSet attributeSet)
    {
        super(ctx, attributeSet);

        drawContext = ctx;

        init();

    }



    public void init()
    {

        if (!threadExists) {

            drawSurfaceHolder = getHolder();
            drawSurfaceHolder.addCallback(this);

            drawThread = new DrawThread(drawSurfaceHolder, drawContext, handler);

            drawThread.setName("" +System.currentTimeMillis());
            drawThread.start();
        }

        threadExists = Boolean.valueOf(true);

        drawFlag     = Boolean.valueOf(true);

        return;

    }


    public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1, int paramInt2, int paramInt3)
    {
        drawThread.setSurfaceSize(paramInt2, paramInt3);
    }

    public void surfaceCreated(SurfaceHolder paramSurfaceHolder)
    {

        init();

    }

    public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder)
    {
        while (true)
        {
            if (!drawFlag)
                return;
            try
            {
                drawFlag = Boolean.valueOf(false);
                drawThread.join();

            }
            catch (InterruptedException localInterruptedException)
            {
            }
        }

    }


    class DrawThread extends Thread
    {
        private Bitmap soundBackgroundImage;
        private short[]        soundBuffer;
        private int[]          soundSegmented;
        public  Boolean        captureSpectrogram = Boolean.valueOf(false);
        public  int            FFT_Len      = 1024;
        public  int            segmentIndex = -1;
        private int            soundCanvasHeight = 0;
        private int			   soundCanvasWidth  = 0;
        private Paint soundLinePaint;
        private Paint		   soundLinePaint2;
        private Paint          soundLinePaint3;
        private Paint          spectrogramPaint;
        private SurfaceHolder  soundSurfaceHolder;
        private int            drawScale   = 8;

        //
        private double[]       soundFFT;
        private double[]       soundFFTMag;
        private double[]       soundFFTTemp;
        private double         maxIntensity;
        private double         minIntensity;

        public DrawThread(SurfaceHolder paramContext, Context paramHandler, Handler arg4)
        {
            soundSurfaceHolder = paramContext;

            soundLinePaint     = new Paint();
            soundLinePaint.setARGB(255, 0, 0, 255);
            soundLinePaint.setStrokeWidth(3);

            soundLinePaint2     = new Paint();
            soundLinePaint2.setAntiAlias(true);
            soundLinePaint2.setARGB(255, 255, 0, 0);
            soundLinePaint2.setStrokeWidth(4);

            soundLinePaint3     = new Paint();
            soundLinePaint3.setAntiAlias(true);
            soundLinePaint3.setARGB(255, 0, 255, 255);
            soundLinePaint3.setStrokeWidth(3);

            spectrogramPaint 	= new Paint();
            spectrogramPaint.setAntiAlias(true);
            spectrogramPaint.setStrokeWidth(4);

            soundBuffer        = new short[2048];

            soundBackgroundImage = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);


            soundSegmented     = new int[FFT_Len];

            //
            soundFFT           = new double[FFT_Len*2];
            soundFFTMag        = new double[FFT_Len];
            soundFFTTemp       = new double[FFT_Len*2];

        }



        public void doDraw(Canvas canvas)
        {

            soundCanvasHeight  = canvas.getHeight();
            soundCanvasWidth   = canvas.getWidth();

            int height         = soundCanvasHeight;
            int width          = soundCanvasWidth;

            int intensityFFT   = 0;		// used to determine the intensities of the fft

            Paint paint = new Paint();
            paint.setColor(Color.YELLOW);
            paint.setStyle(Style.FILL);
            canvas.drawPaint(paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(20);
            canvas.drawText("'Capture Sound' to see Spectrogram", 250, 20, paint);

            int xStart = 0;
            while (xStart <  soundSegmented.length-1) {

                int yStart = soundBuffer[xStart] / height * drawScale;
                int yStop  = soundBuffer[xStart+1] / height * drawScale;

                int yStart1 = yStart + height/4;
                int yStop1  = yStop  + height/4;

                canvas.drawLine(xStart, yStart1, xStart +1, yStop1, soundLinePaint2);

                if (xStart %100 == 0) {
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(20);
                    canvas.drawText(Integer.toString(xStart), xStart, height/2, paint);
                    canvas.drawText(Integer.toString(yStop),  xStart, yStop1, paint);
                }

                xStart++;

            }

            if (captureSpectrogram) {
                segmentIndex = 0;
                while (segmentIndex < FFT_Len) {
                    soundSegmented[segmentIndex] = soundBuffer[segmentIndex];
                    soundFFT[2 * segmentIndex] = (double) soundSegmented[segmentIndex];
                    soundFFT[2 * segmentIndex + 1] = 0.0;
                    segmentIndex++;
                }

                // fft
                DoubleFFT_1D fft = new DoubleFFT_1D(FFT_Len);
                fft.complexForward(soundFFT);
                captureSpectrogram = Boolean.valueOf(true);

                // perform fftshift here
                for (int i = 0; i < FFT_Len; i++) {
                    soundFFTTemp[i] = soundFFT[i + FFT_Len];
                    soundFFTTemp[i + FFT_Len] = soundFFT[i];
                }
                for (int i = 0; i < FFT_Len * 2; i++) {
                    soundFFT[i] = soundFFTTemp[i];
                }

                double mx = -99999;
                for (int i = 0; i < FFT_Len; i++) {
                    double re = soundFFT[2 * i];
                    double im = soundFFT[2 * i + 1];
                    soundFFTMag[i] = Math.log(re * re + im * im + 0.001);
                    if (soundFFTMag[i] > mx) mx = soundFFTMag[i];
                }

                // normalize
                for (int i = 0; i < FFT_Len; i++) {
                    soundFFTMag[i] = height * 4 / 5 - soundFFTMag[i] / mx * 500;
                }
                maxIntensity = mx;

                // display the signal in temporal domain
                xStart = 0;
                while (xStart < FFT_Len - 1) {
                    int yStart = soundSegmented[xStart] / height * drawScale;
                    int yStop = soundSegmented[xStart + 1] / height * drawScale;

                    int yStart1 = yStart + height / 4;
                    int yStop1 = yStop + height / 4;

                    canvas.drawLine(xStart, yStart1, xStart + 1, yStop1, soundLinePaint2);

                    if (xStart % 100 == 0) {
                        paint.setColor(Color.BLACK);
                        paint.setTextSize(20);
                    }
                    xStart++;
                }

                // mapping values of soundFFT mag into smaller array size of 256 (to display the colors correctly)
                // Essentially it is to normalize the values to fit in the heatmap
                double[] mappedSoundFFT;
                int size = FFT_Len/4;
                mappedSoundFFT = new double[size];
                int count = 0;
                
                for(int i=0;i<FFT_Len;i++){
                    mappedSoundFFT[count] += soundFFTMag[i];
                    if((i+1)%4==0){
                        mappedSoundFFT[count] = mappedSoundFFT[count]/4; // compute average values within every 4 slots
                        count++;
                    }
                }
                
                // find new values of min and max intensity based on normalized values
                minIntensity = maxIntensity = mappedSoundFFT[0];
                for(int i=1;i<size;i++){
                    if(mappedSoundFFT[i] > maxIntensity)
                        maxIntensity = mappedSoundFFT[i];
                    if(mappedSoundFFT[i] < minIntensity)
                        minIntensity = mappedSoundFFT[i];
                }
                
                int value = 0;
                // plot the spectrogram heat map
                for (int i=0;i<size-1;i++){
                    value = (int)((mappedSoundFFT[i]-minIntensity)/(maxIntensity-minIntensity)*510);
                    if(value <= 255){
                        spectrogramPaint.setARGB(255, 255, value, 0);
                    }
                    else{
                        spectrogramPaint.setARGB(255, 510-value,255 , 0);
                    }
                    canvas.drawLine(rectPos,height-100-i,rectPos,height-99-i, spectrogramPaint);
                }
                
            } // end if

        }


        public void setBuffer(short[] paramArrayOfShort)
        {
            synchronized (soundBuffer)
            {
                soundBuffer = paramArrayOfShort;
                return;
            }
        }


        public void setSurfaceSize(int canvasWidth, int canvasHeight)
        {
            synchronized (soundSurfaceHolder)
            {
                soundBackgroundImage = Bitmap.createScaledBitmap(soundBackgroundImage, canvasWidth, canvasHeight, true);
                return;
            }
        }


        public void run()
        {

            while (drawFlag)
            {

                Canvas localCanvas = null;
                try
                {
                    localCanvas = soundSurfaceHolder.lockCanvas(new Rect(rectPos, 0, rectPos+1, 1150));
                    synchronized (soundSurfaceHolder)
                    {
                        if (localCanvas != null)
                            doDraw(localCanvas);

                        rectPos++;
                        rectPos = rectPos % 1151;

                    }
                }
                finally
                {
                    if (localCanvas != null)
                        soundSurfaceHolder.unlockCanvasAndPost(localCanvas);
                }
            }
        }


    }


}
