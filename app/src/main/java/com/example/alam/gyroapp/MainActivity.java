package com.example.alam.gyroapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private float gravity[];
    // Magnetic rotational data
    private float magnetic[]; //for magnetic rotational data
    private float accels[] = new float[3];
    private float mags[] = new float[3];
    private float[] values = new float[3];

    TextView textView,textView2;
    Button button,button2,button3,next;
    float value,value2;
    // azimuth, pitch and roll
    private float azimuth;
    private float pitch;
    private float roll;
    TextView label,again;

    LinearLayout linearLayout,result,motionPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.gr);
        button = (Button) findViewById(R.id.btn1);
        button2 = (Button) findViewById(R.id.btn2);
        textView2 = (TextView) findViewById(R.id.show);
        button3 = (Button) findViewById(R.id.disp);
        linearLayout = (LinearLayout) findViewById(R.id.startPage);
        next = (Button) findViewById(R.id.hideStartPage);
        label =(TextView) findViewById(R.id.label);
        result = (LinearLayout) findViewById(R.id.resultpage);
        motionPage = (LinearLayout) findViewById(R.id.motion);
        again = (TextView) findViewById(R.id.again);

        setSensor();

    }

    public void setSensor() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);


        SensorManager sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        SensorEventListener mySensorEventListener = new SensorEventListener() {
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
            Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            int rotation = display.getRotation();

            public void onSensorChanged(SensorEvent event) {
                switch (event.sensor.getType()) {
                    case Sensor.TYPE_MAGNETIC_FIELD:
                        mags = event.values.clone();
                        break;
                    case Sensor.TYPE_ACCELEROMETER:
                        accels = event.values.clone();
                        break;
                }

                if (mags != null && accels != null) {
                    gravity = new float[9];
                    magnetic = new float[9];
                    SensorManager.getRotationMatrix(gravity, magnetic, accels, mags);
                    float[] outGravity = new float[9];
                    SensorManager.remapCoordinateSystem(gravity, SensorManager.AXIS_X,SensorManager.AXIS_Y, outGravity);
                    SensorManager.getOrientation(outGravity, values);

                    azimuth = values[0] * 57f;
                    pitch =values[1] *57f;
                    roll = values[2] * 57f;
                    mags = null;
                    accels = null;
                    if(roll<0){
                        roll = Math.abs(roll-90);
                        if(roll>90)
                        {
                            textView.setText("Reached Limit");

                        }
                        else {
                            textView.setText(String.valueOf(roll).substring(0, 3));
                        }

                    }
                    else {
                        if(roll>90){
                            textView.setText("Reached Limit");

                        }
                        else {
                            textView.setText(String.valueOf(roll).substring(0, 2));
                        }
                    }

                    textView.setTextSize(40);
                }

            }
        };

        sManager.registerListener(mySensorEventListener, sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
        sManager.registerListener(mySensorEventListener, sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_NORMAL);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setVisibility(View.VISIBLE);
                if(textView.getText().toString()!="Reached Limit")
                value = Float.valueOf( textView.getText().toString());
                button.setVisibility(View.GONE);
                button2.setVisibility(View.VISIBLE);
                label.setText("Extend your right knee. Then click stop");

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    value2 = Float.valueOf(textView.getText().toString());
                }
                catch (NumberFormatException ne){
                    ne.printStackTrace();
                }
                textView.setVisibility(View.INVISIBLE);
                button2.setVisibility(View.GONE);
                button3.setVisibility(View.VISIBLE);
                label.setVisibility(View.INVISIBLE);

            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setVisibility(View.INVISIBLE);
                motionPage.setVisibility(View.GONE);
                result.setVisibility(View.VISIBLE);
                again.setVisibility(View.VISIBLE);
                alertDialogBuilder.setTitle("Your Knee Rotation");
                alertDialogBuilder.setMessage(String.valueOf(value-value2) + (char) 0x00B0);

                alertDialogBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.show().getWindow();

            }
        });


        SensorEventListener rotationalVectorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
//                float[] rotationMatrix = new float[16];
//                SensorManager.getRotationMatrixFromVector(
//                        rotationMatrix, sensorEvent.values);
//                // Remap coordinate system
//                float[] remappedRotationMatrix = new float[16];
//                SensorManager.remapCoordinateSystem(rotationMatrix,
//                        SensorManager.AXIS_X,
//                        SensorManager.AXIS_Z,
//                        remappedRotationMatrix);
//
//// Convert to orientations
//                float[] orientations = new float[3];
//                for(int i = 0; i < 3; i++) {
//                    orientations[i] = (float)(Math.toDegrees(orientations[i]));
//                }
//                SensorManager.getOrientation(remappedRotationMatrix, orientations);
//                if(orientations[2] > 45) {
//                    getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
//                } else if(orientations[2] < -45) {
//                    getWindow().getDecorView().setBackgroundColor(Color.BLUE);
//                } else if(Math.abs(orientations[2]) < 10) {
//                    getWindow().getDecorView().setBackgroundColor(Color.WHITE);
//                }


            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                label.setVisibility(View.VISIBLE);
                button3.setVisibility(View.GONE);
                motionPage.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                label.setText("Place your device on your Right Knee \n Click Button to Continue ");
                textView.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
            }
        });
        //sensorManager.registerListener(rotationalVectorListener,rotationSensor,SensorManager.SENSOR_DELAY_NORMAL);
        //sensorManager.registerListener(gyroscopeListener,gyroscopeSensor,SensorManager.SENSOR_DELAY_NORMAL);
    again.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            result.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }
    });


    }



}
