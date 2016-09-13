package com.example.franciscofranco.marveltest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private CharactersJSONAdapter charactersJSONAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.characterList);
        charactersJSONAdapter = new CharactersJSONAdapter(this, getLayoutInflater());

        listView.setAdapter(charactersJSONAdapter);

        CharactersRequestTask myTask = new CharactersRequestTask(this, charactersJSONAdapter);
        myTask.execute("timestamp");
    }
}
