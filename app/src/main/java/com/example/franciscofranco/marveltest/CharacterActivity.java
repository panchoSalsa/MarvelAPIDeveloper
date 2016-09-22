package com.example.franciscofranco.marveltest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CharacterActivity extends AppCompatActivity {

    private TextView name;
    private ImageView thumbnail;
    private TextView description;

    private int id = 0;

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

        id = intent.getIntExtra(MainActivity.ID, 0);

        Picasso.with(this)
                .load(thumbnail_url)
                .resize(800,800)
                .into(thumbnail);

    }

    public void Comics(View view) {

        Log.d("FRANCO_DEBUG", "Comics clicked");

        Intent intent = new Intent(getApplicationContext(), ComicsActivity.class);
        intent.putExtra(MainActivity.ID, id);

        startActivity(intent);

    }
}
