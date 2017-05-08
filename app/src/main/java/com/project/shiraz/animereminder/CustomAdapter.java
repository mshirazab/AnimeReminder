package com.project.shiraz.animereminder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by shiraz on 30/4/17.
 */

class CustomAdapter extends ArrayAdapter<Anime> {
    CustomAdapter(@NonNull Context context, ArrayList<Anime> episodes) {
        super(context, R.layout.episode_item, episodes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.episode_item, null);
        }
        Anime episodeDetails = getItem(position);
        if (episodeDetails != null) {
            TextView nameTextView = (TextView) view.findViewById(R.id.item_episode_name);
            TextView numbertextView = (TextView) view.findViewById(R.id.item_episode_number);
            TextView datetextView = (TextView) view.findViewById(R.id.item_episode_date);

            nameTextView.setText(episodeDetails.Name);
            nameTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            nameTextView.setSingleLine(true);
            nameTextView.setMarqueeRepeatLimit(5);
            nameTextView.setSelected(true);

            numbertextView.setText("1");
            datetextView.setText("");
        }
        return view;
    }
}
