package com.example.franciscofranco.marveltest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity  {

    private Button btnPrevious;
    private Button btnNext;
    private ListView listView;
    private CharactersJSONAdapter charactersJSONAdapter;

    public static final String NAME = "name";
    public static final String THUMBNAIL_URL = "thumbnailUrl";
    public static final String DESCRIPTION = "description";

    private int offset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPrevious = (Button) findViewById(R.id.btnPrevious);
        btnNext = (Button) findViewById(R.id.btnNext);
        listView = (ListView) findViewById(R.id.characterList);
        charactersJSONAdapter = new CharactersJSONAdapter(this, getLayoutInflater());

        listView.setAdapter(charactersJSONAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                triggerIntent(position);

            }
        });

        fetchData();
    }

    private void triggerIntent(int position) {

        Intent intent = new Intent(getApplicationContext(), CharacterActivity.class);

        String name = null;
        String thumbnailUrl = null;
        String description = null;

        JSONObject obj = (JSONObject) charactersJSONAdapter.getItem(position);

        try {

            name = obj.getString("name");

            JSONObject thumbnail = obj.getJSONObject("thumbnail");
            thumbnailUrl = thumbnail.getString("path")
                    + "."
                    + thumbnail.getString("extension") ;

            description = obj.getString("description");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        intent.putExtra(NAME, name);
        intent.putExtra(THUMBNAIL_URL, thumbnailUrl);
        intent.putExtra(DESCRIPTION, description);
        startActivity(intent);

    }

    public void previous(View view) {

        if (offset != 0) {

            offset = offset - 100;

            fetchData();

        }

    }

    public void next(View view) {

        if (offset != 1400) {

            offset = offset + 100;

            fetchData();
        }
    }

    public void fetchData() {
        CharactersRequestTask myTask = new CharactersRequestTask(this, charactersJSONAdapter);
        myTask.execute("timestamp", String.valueOf(offset));
    }
}
