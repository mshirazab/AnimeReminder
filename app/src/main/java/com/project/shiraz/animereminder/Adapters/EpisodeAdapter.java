package com.project.shiraz.animereminder.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.project.shiraz.animereminder.JsonStructures.EpisodeUnit;
import com.project.shiraz.animereminder.R;

import java.util.ArrayList;

import static com.project.shiraz.animereminder.Adapters.UsefulVisualChanges.makeMarquee;


public class EpisodeAdapter extends ArrayAdapter<EpisodeUnit> {
    public EpisodeAdapter(@NonNull Context context, ArrayList<EpisodeUnit> episodes) {
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
        EpisodeUnit episodeDetails = getItem(position);
        if (episodeDetails != null) {
            TextView nameTextView = (TextView) view.findViewById(R.id.item_episode_name);
            TextView numbertextView = (TextView) view.findViewById(R.id.item_episode_number);
            TextView datetextView = (TextView) view.findViewById(R.id.item_episode_date);

            nameTextView.setText(episodeDetails.Name);
            makeMarquee(nameTextView);
            numbertextView.setText("" +episodeDetails.Number);
            datetextView.setText(episodeDetails.Time);
        }
        return view;
    }
}
