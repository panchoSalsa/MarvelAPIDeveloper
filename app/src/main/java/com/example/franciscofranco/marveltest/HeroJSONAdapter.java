package com.example.franciscofranco.marveltest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HeroJSONAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private JSONArray mJsonArray;

    public HeroJSONAdapter(Context context, LayoutInflater inflater) {

        mContext = context;
        mInflater = inflater;
        mJsonArray = new JSONArray();

    }

    @Override
    public int getCount() {
        return mJsonArray.length();
    }

    @Override
    public Object getItem(int position) {
        return mJsonArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CharactersViewHolder holder;

        // check if the view already exists
        // if so, no need to inflate and findViewById again!
        if (convertView == null) {

            // Inflate the custom row layout from your XML.
            convertView = mInflater.inflate(R.layout.character_row, null);

            // create a new "Holder" with subviews
            holder = new CharactersViewHolder();
            holder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
            holder.name = (TextView) convertView.findViewById(R.id.name);

            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else {

            // skip all the expensive inflation/findViewById
            // and just get the holder you already made
            holder = (CharactersViewHolder) convertView.getTag();
        }

        JSONObject jsonObject = (JSONObject) getItem(position);

        parseJSONObject(holder, jsonObject);

        return convertView;
    }

    private void parseJSONObject(CharactersViewHolder holder, JSONObject obj) {

        String imageURL = null;
        String name = null;

        try {

            name = obj.getString("name");
            JSONObject thumbnail = obj.getJSONObject("thumbnail");

            holder.name.setText(name);

            imageURL = thumbnail.getString("path")
                    + "."
                    + thumbnail.getString("extension") ;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Picasso.with(mContext)
                .load(imageURL)
                .fit()
                .into(holder.thumbnail);
    }

    public void updateData(JSONArray jsonArray) {

            mJsonArray = jsonArray;
            notifyDataSetChanged();

    }
}
