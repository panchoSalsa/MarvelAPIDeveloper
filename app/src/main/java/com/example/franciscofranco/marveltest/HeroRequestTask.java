package com.example.franciscofranco.marveltest;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


class HeroRequestTask extends AsyncTask<String, Void, JSONObject> {

    private Context context;
    private HeroJSONAdapter adapter;

    public HeroRequestTask(Context context, HeroJSONAdapter adapter) {

        Log.d("FRANCO_DEBUG", "HeroRequestTask()");

        this.context = context;
        this.adapter = adapter;

    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();

    }

    @Override
    protected JSONObject doInBackground(String... params) {

        Log.d("FRANCO_DEBUG", "doInBackground()");

        if (params.length == 0) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String privateKey = "6f7c49db00e026d7e0f5f9e3bfa4cdddd46c329b";
        String apiKey = "7ea2b0fd0ae54a8fa8ab0355470ddb47";

        JSONObject response = null;

        try {

            final String BASE_URL = "http://gateway.marvel.com/v1/public/characters?";
            final String TS_PARAM = "ts";
            final String API_KEY_PARAM = "apikey";
            final String HASH_PARAM = "hash";
            final String LIMIT = "limit";
            final String NAMESTARTSWITH = "nameStartsWith";

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(TS_PARAM, params[0])
                    .appendQueryParameter(API_KEY_PARAM, apiKey)
                    .appendQueryParameter(HASH_PARAM, md5.md5(params[0] + privateKey + apiKey))
                    .appendQueryParameter(LIMIT, "100")
                    .appendQueryParameter(NAMESTARTSWITH, params[1])
                    .build();

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            try {

                response = new JSONObject(buffer.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            Log.d("FRANCO_DEBUG", "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.d("FRANCO_DEBUG", "Error closing stream", e);
                }
            }
        }

        return response;
    }

    @Override
    protected void onPostExecute(JSONObject response) {

        Log.d("FRANCO_DEBUG", "onPostExecute()");

        super.onPostExecute(response);

        JSONArray results = null;

        try {

            results = response.getJSONObject("data").getJSONArray("results");

            if (results == null)

                return;

            else {

                adapter.updateData(results);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("FRANCO_DEBUG", "onPostExecute() ENDEND");

    }
}
