package com.example.truthball;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    LinearLayout ballLinearLayout;
    TextView answerTxtView;
    SensorManager mSensorManager;
    Sensor mSensor;
    private Animation ballAnimation;
    private boolean init;
    private boolean background ;
    private float x1, x2, x3 ;
    private static final float ERROR = (float) 10.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ballLinearLayout = findViewById(R.id.ballLinearLayout);
        answerTxtView = findViewById(R.id.answerTextView);
        TextView shake = findViewById(R.id.shakeTextView);
        shake.setText(getResources().getString(R.string.shakeBall));
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> listOfSensorsOnDevice = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        for (int i = 0; i < listOfSensorsOnDevice.size(); i++) {
            if (listOfSensorsOnDevice.get(i).getType() == Sensor.TYPE_ACCELEROMETER) {
                init = false;
                background = false;
                mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
                ballAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

       float x,y,z ;
       x = sensorEvent.values[0];
       y = sensorEvent.values[1];
       z = sensorEvent.values[2];
       if(shaked(x,y,z)) {
           showAnswer();
       }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private boolean shaked(float x, float y, float z){
        if(!init){
            x1 = x;
            x2 = y;
            x3 = z;
            init = true;
        } else {
            float diffX = abs(x1 - x);
            float diffY = abs(x2 - y);
            float diffZ = abs(x3 - z);

            if (diffX < ERROR) {

                diffX = (float) 0.0;
            }
            if (diffY < ERROR) {
                diffY = (float) 0.0;
            }
            if (diffZ < ERROR) {

                diffZ = (float) 0.0;
            }


            x1 = x;
            x2 = y;
            x3 = z;

            if (diffX > diffY) {
                return true;
            }
            else if(diffZ > diffX){
                return true;
            }
        }
        return false ;
        }



    private void showAnswer() {

        if(!background){
            ballLinearLayout.setBackground(getResources().getDrawable(R.drawable.hw3ball_empty));
            background = true ;
        }
        String[] answers = getResources().getStringArray(R.array.answers) ;
        int size = answers.length ;
        float temp = x1 + x2 + x3 ;
        int shake = (int) temp ;
        shake = abs(shake);
        shake = shake%size ;
        ballLinearLayout.startAnimation(ballAnimation);
        answerTxtView.setText(answers[shake]);

    }
}


