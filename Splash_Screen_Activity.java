package com.sp.android_studio_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Splash_Screen_Activity extends AppCompatActivity {

    Timer timer;

    private TextToSpeech tts;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_2);

        auth = FirebaseAuth.getInstance();

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.ENGLISH);
                    tts.speak("Welcome to Recycling Buddy!", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                /*Intent intent = new Intent(Splash_Screen_Activity.this, LoginActivity.class);
                startActivity(intent);
                finish();*/

                if(auth.getCurrentUser() != null) {
                    Intent intentNav_HeaderP2 = new Intent(Splash_Screen_Activity.this, nav_headerP2.class);
                    startActivity(intentNav_HeaderP2);
                    finish();
                } else {
                    Intent intentLogin = new Intent(Splash_Screen_Activity.this, LoginActivity.class);
                    startActivity(intentLogin);
                    finish();
                }
            }
        }, 2000);

    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}