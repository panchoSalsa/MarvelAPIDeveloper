package com.example.franciscofranco.marveltest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity  {

    public static ListView listView;
    private View footerView;
    private CharactersJSONAdapter charactersJSONAdapter;

    public static final String NAME = "name";
    public static final String THUMBNAIL_URL = "thumbnailUrl";
    public static final String DESCRIPTION = "description";

    private final int MAX = 1400;
    private final int OFFSET = 100;

    public static int offset = 0;

    public static boolean loadingMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.characterList);
        charactersJSONAdapter = new CharactersJSONAdapter(this, getLayoutInflater());

        footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.loading_view, null, false);

        listView.addFooterView(footerView);

        listView.setAdapter(charactersJSONAdapter);

        //setting  listener on scroll event of the list
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                //what is the bottom item that is visible
                int lastInScreen = firstVisibleItem + visibleItemCount;

                //is the bottom item visible & not loading more already? Load more!
                if ((lastInScreen == totalItemCount) && !(loadingMore)) {

                    if (offset < MAX) {

                        offset = offset + OFFSET;
                        fetchData();

                    } else

                        hideFooter();

                }
            }
        });

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

    public void fetchData() {

        CharactersRequestTask myTask = new CharactersRequestTask(this, charactersJSONAdapter);
        myTask.execute("timestamp", String.valueOf(offset));

    }

    private void hideFooter() {

        //footerView.setVisibility(View.GONE);
        // this did not work so I instead hide the TextView and ProgressBar

        TextView footerText = (TextView) footerView.findViewById(R.id.footerText);
        footerText.setVisibility(View.GONE);

        ProgressBar progressBar = (ProgressBar) footerView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

    }

}