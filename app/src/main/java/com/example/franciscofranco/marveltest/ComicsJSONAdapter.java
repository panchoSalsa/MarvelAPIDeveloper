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

/**
 * Created by FranciscoFranco on 9/20/16.
 */
public class ComicsJSONAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
    private JSONArray mJsonArray;

    public ComicsJSONAdapter(Context context, LayoutInflater inflater) {

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

        ComicsViewHolder holder;

        // check if the view already exists
        // if so, no need to inflate and findViewById again!
        if (convertView == null) {

            // Inflate the custom row layout from your XML.
            convertView = mInflater.inflate(R.layout.comics_row, null);

            // create a new "Holder" with subviews
            holder = new ComicsViewHolder();
            holder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
            holder.title = (TextView) convertView.findViewById(R.id.title);

            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else {

            // skip all the expensive inflation/findViewById
            // and just get the holder you already made
            holder = (ComicsViewHolder) convertView.getTag();
        }

        JSONObject jsonObject = (JSONObject) getItem(position);

        parseJSONObject(holder, jsonObject);

        return convertView;
    }

    private void parseJSONObject(ComicsViewHolder holder, JSONObject obj) {

        String imageURL = null;
        String title = null;

        try {

            title = obj.getString("title");

            JSONObject thumbnail = obj.getJSONObject("thumbnail");

            holder.title.setText(title);

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

        try {
            mJsonArray = concatArray(mJsonArray,jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        notifyDataSetChanged();

    }

    private JSONArray concatArray(JSONArray... arrs) throws JSONException {

        JSONArray result = new JSONArray();

        for (JSONArray arr: arrs) {
            for (int i = 0; i < arr.length(); ++i) {
                result.put(arr.get(i));
            }
        }

        return result;
    }

    public void clearData() {

        mJsonArray = new JSONArray();

    }

}
