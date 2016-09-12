package com.example.franciscofranco.marveltest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView thumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        thumbnail = (ImageView) findViewById(R.id.thumbnail);

        RequestTask myTask = new RequestTask(this, thumbnail);
        myTask.execute("timestamp");
    }
}
