package com.example.franciscofranco.marveltest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

public class HeroByName extends AppCompatActivity {


    private EditText editText;

    public static ListView listView;

    private HeroJSONAdapter heroJSONAdapter;

    public static final String NAME = "name";
    public static final String THUMBNAIL_URL = "thumbnailUrl";
    public static final String DESCRIPTION = "description";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("FRANCO_DEBUG", "HeroByName onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_by_name);

        editText = (EditText) findViewById(R.id.editText);

        listView = (ListView) findViewById(R.id.heroList);

        heroJSONAdapter = new HeroJSONAdapter(this, getLayoutInflater());

        listView.setAdapter(heroJSONAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                triggerIntent(position);
            }
        });

    }

    private void triggerIntent(int position) {

        Intent intent = new Intent(getApplicationContext(), CharacterActivity.class);

        String name = null;
        String thumbnailUrl = null;
        String description = null;

        JSONObject obj = (JSONObject) heroJSONAdapter.getItem(position);

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

    public void fetchData(String hero) {

        HeroRequestTask myTask = new HeroRequestTask(this, heroJSONAdapter);
        myTask.execute("timestamp", hero);

    }

    public void Search(View view) {

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);

        String hero = editText.getText().toString();

        if (hero.isEmpty()) {
            return;
        } else {
            fetchData(hero);
        }
    }
}
