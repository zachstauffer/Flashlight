package com.github.zachstauffer.flashlight;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends Activity {

    private View button;
    private static Camera cam;
    private boolean isCameraOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isCameraOn = false;

        if (!getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH)) {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }

        button = findViewById(R.id.background);
        if (button != null) {
            button.setOnTouchListener(touchListener);
            button.setOnClickListener(null);
        }
    }

    private final View.OnTouchListener touchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP){
                turnOffLED();
                return true;
            } else {
                turnOnLED();
            }
            return false;
        }
    };

    private final View.OnClickListener clickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (isCameraOn) {
                turnOffLED();
            } else {
                turnOnLED();
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.touchHold) {
            turnOffLED();
            button.setOnTouchListener(touchListener);
            button.setOnClickListener(null);
        } else if (id == R.id.onOff) {
            turnOffLED();
            button.setOnTouchListener(null);
            button.setOnClickListener(clickListener);
        }
        return super.onOptionsItemSelected(item);
    }

    public void turnOnLED() {
        try {
            cam = Camera.open();
            Camera.Parameters p = cam.getParameters();
            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            cam.setParameters(p);
            cam.startPreview();
            isCameraOn = true;
        } catch (Exception e) {
            // Do nothing
        }
    }

    public void turnOffLED() {
        try {
            cam.stopPreview();
            cam.release();
            cam = null;
            isCameraOn = false;
        } catch (Exception e) {
            // Do nothing
        }
    }
}
