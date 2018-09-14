package pt.unl.fct.di.www.canicookit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

/**
 * Created by MendesPC on 25/11/2017.
 */

public class cameraActivity extends AppCompatActivity {

    Boolean vibrated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );

        vibrated = false;

        setContentView ( R.layout.cameralayout );
        final SurfaceView cameraView = (SurfaceView) findViewById ( R.id.camera_view );

        final BarcodeDetector barcodeDetector = new BarcodeDetector.Builder ( this )
                .setBarcodeFormats ( Barcode.QR_CODE )
                .build ();

        final CameraSource cameraSource = new CameraSource
                .Builder ( this, barcodeDetector )
                .setRequestedPreviewSize ( 640, 480 )
                .build ();

        cameraView.getHolder ().addCallback ( new SurfaceHolder.Callback () {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start ( cameraView.getHolder () );
                } catch (IOException ie) {
                    Log.e ( "CAMERA SOURCE", ie.getMessage () );
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop ();
            }
        } );

        barcodeDetector.setProcessor ( new Detector.Processor<Barcode> () {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems ();

                if (barcodes.size () != 0) {

                    if (!vibrated) {
                        Vibrator v = (Vibrator) getSystemService ( getApplicationContext ().VIBRATOR_SERVICE );
                        // Vibrate for 500 milliseconds
                        v.vibrate ( 150 );
                        vibrated = true;
                    }

                    // Process the info
                    Intent i = new Intent ( cameraActivity.this, MyInventoryActivity.class );
                    Barcode thisCode = barcodes.valueAt ( 0 );
                    i.putExtra ( "QR", thisCode.rawValue );
                    startActivity ( i );
                    finish ();
                }
            }
        } );


    }


}
