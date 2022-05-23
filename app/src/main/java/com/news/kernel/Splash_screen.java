package com.news.kernel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.news.kernel.R;

public class Splash_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread thread = new Thread(){
            public void run(){
                try{
                    // to run the given code
                    sleep(2000);
                }catch(Exception e){
                    // to check if any exceptions are there and report it
                    e.printStackTrace();
                }finally{
                    // code is written inside finally because even if there is an exception
                    // the catch block will report it and the code further written in this finally block will be executed after it

                    // Here we want to move to the next screen after the splash screen is shown using intent
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);

                }
            }
        };thread.start();

    }





}