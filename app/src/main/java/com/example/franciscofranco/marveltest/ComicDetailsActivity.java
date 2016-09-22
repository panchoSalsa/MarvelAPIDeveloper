package com.example.franciscofranco.marveltest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ComicDetailsActivity extends AppCompatActivity {

    private TextView title;
    private TextView description;
    private TextView pagecount;
    private TextView characters;
    private ImageView thumbnail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_details);

        title = (TextView) findViewById(R.id.title);
        description = (TextView) findViewById(R.id.description);
        pagecount = (TextView) findViewById(R.id.pageCount);
        characters = (TextView) findViewById(R.id.characters);
        thumbnail = (ImageView) findViewById(R.id.thumbnail);

        processIntent();
    }

    private void processIntent() {

        Intent intent = getIntent();

        processTitle(intent);
        processPageCount(intent);
        processDescription(intent);
        processCharacters(intent);
        processThumbnail(intent);

    }

    void processTitle(Intent intent) {
        title.setText(intent.getStringExtra(ComicsActivity.TITLE));
    }

    void processPageCount(Intent intent) {

        String temp = String.valueOf(intent.getIntExtra(ComicsActivity.PAGECOUNT, 0));

        if (temp == null || temp.isEmpty() || temp.equals("0"))
            pagecount.setVisibility(View.GONE);
        else
            pagecount.setText("Page Count: " + temp);

    }

    void processDescription(Intent intent) {

        String temp = intent.getStringExtra(ComicsActivity.DESCRIPTION);

        if (temp == null || temp.isEmpty() || temp.equals("null"))
            description.setVisibility(View.GONE);
        else
            description.setText("Description: " + stripHtml(temp));

    }

    void processCharacters(Intent intent) {

        ArrayList<String> array = intent.getStringArrayListExtra(ComicsActivity.CHARACTERS);

        if (array == null || array.isEmpty())
            characters.setVisibility(View.GONE);
        else
            setCharacters(array);

    }

    void setCharacters(ArrayList<String> array) {

        StringBuilder tmp = new StringBuilder();
        tmp.append("Characters: ");;

        for (int i = 0; i < array.size() - 1; i++)
            tmp.append(array.get(i) + ", ");

        tmp.append(array.get(array.size() - 1)); // append last chacter

        characters.setText(tmp.toString());

    }

    void processThumbnail(Intent intent) {

        String url = intent.getStringExtra(ComicsActivity.THUMBNAIL_URL);

        Picasso.with(getApplicationContext())
                .load(url)
                .resize(500,500)
                .into(thumbnail);

    }

    private String stripHtml(String html) {

        return Html.fromHtml(html).toString();

    }

}
