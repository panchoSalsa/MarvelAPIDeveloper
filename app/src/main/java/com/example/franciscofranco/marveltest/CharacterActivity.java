package com.example.franciscofranco.marveltest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CharacterActivity extends AppCompatActivity {

    private TextView name;
    private ImageView thumbnail;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        name = (TextView) findViewById(R.id.name);
        thumbnail = (ImageView) findViewById(R.id.thumbnail);
        description = (TextView) findViewById(R.id.description);

        initializeView();

    }

    private void initializeView() {

        Intent intent = getIntent();

        name.setText(intent.getStringExtra(MainActivity.NAME));
        description.setText(intent.getStringExtra(MainActivity.DESCRIPTION));
        String thumbnail_url = intent.getStringExtra(MainActivity.THUMBNAIL_URL);

        Picasso.with(this)
                .load(thumbnail_url)
                .resize(800,800)
                .into(thumbnail);
    }

}
