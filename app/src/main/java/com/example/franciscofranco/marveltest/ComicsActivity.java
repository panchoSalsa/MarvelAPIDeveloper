package com.example.franciscofranco.marveltest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ComicsActivity extends AppCompatActivity {

    private int id = 0;

    // making listView public and static so that I can reference it in the ComicsRequestTask
    public static ListView listView;
    private View footerView;
    private ComicsJSONAdapter comicsJSONAdapter;

    public static int MAX;
    private final int OFFSET = 10;

    public static int offset;

    public static boolean loadingMore = false;

    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String PAGECOUNT = "pageCount";
    public static final String THUMBNAIL_URL = "thumbailURL";
    public static final String CHARACTERS = "characters";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comics);

        processIntent();

        Log.d("FRANCO_DEBUG", "Oncreate id is " + String.valueOf(id));

        listView = (ListView) findViewById(R.id.comicsList);

        comicsJSONAdapter = new ComicsJSONAdapter(this, getLayoutInflater());

        listView.setAdapter(comicsJSONAdapter);

        footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.loading_view, null, false);

        listView.addFooterView(footerView);

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

    @Override
    protected void onResume() {
        super.onResume();

        MAX = 0;
        offset = 0;

    }

    public void fetchData() {

        ComicsRequestTask myTask = new ComicsRequestTask(this, comicsJSONAdapter);
        myTask.execute("timestamp", String.valueOf(offset), String.valueOf(id));

    }

    private void hideFooter() {

        //footerView.setVisibility(View.GONE);
        // this did not work so I instead hide the TextView and ProgressBar

        TextView footerText = (TextView) footerView.findViewById(R.id.footerText);
        footerText.setVisibility(View.GONE);

        ProgressBar progressBar = (ProgressBar) footerView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

    }


    void processIntent() {

        Intent intent = getIntent();
        id = intent.getIntExtra(MainActivity.ID, 0);

    }

    private void triggerIntent(int position) {

        Log.d("FRANCO_DEBUG", "triggerIntent()");

        Intent intent = new Intent(getApplicationContext(), ComicDetailsActivity.class);

        String title = null;
        String description = null;
        int pageCount = 0;
        String thumbnailUrl = null;
        ArrayList<String> characters = new ArrayList<String>();

        JSONObject obj = (JSONObject) comicsJSONAdapter.getItem(position);

        try {

            Log.d("FRANCO_DEBUG", "*");

            title = obj.getString("title");
            description = obj.getString("description");
            pageCount = obj.getInt("pageCount");

            JSONObject thumbnail = obj.getJSONObject("thumbnail");
            thumbnailUrl = thumbnail.getString("path")
                    + "."
                    + thumbnail.getString("extension") ;

            Log.d("FRANCO_DEBUG", "**");

            JSONArray characterArray = obj.getJSONObject("characters").getJSONArray("items");

            for (int i = 0; i < characterArray.length(); ++i) {

                JSONObject characterObject = (JSONObject)characterArray.get(i);

                characters.add( characterObject.getString("name"));

            }

            Log.d("FRANCO_DEBUG", "***");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        intent.putExtra(TITLE, title);
        intent.putExtra(PAGECOUNT, pageCount);
        intent.putExtra(THUMBNAIL_URL, thumbnailUrl);
        intent.putExtra(DESCRIPTION, description);
        intent.putStringArrayListExtra(CHARACTERS, characters);

        Log.d("FRANCO_DEBUG", "befor startAct");

        startActivity(intent);

    }

}
