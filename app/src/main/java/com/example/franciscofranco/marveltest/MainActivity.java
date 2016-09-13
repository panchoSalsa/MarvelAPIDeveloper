package com.example.franciscofranco.marveltest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private Button btnPrevious;
    private Button btnNext;
    private ListView listView;
    private CharactersJSONAdapter charactersJSONAdapter;

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

//        CharactersRequestTask myTask = new CharactersRequestTask(this, charactersJSONAdapter);
//        myTask.execute("timestamp", "0");

        fetchData();
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
