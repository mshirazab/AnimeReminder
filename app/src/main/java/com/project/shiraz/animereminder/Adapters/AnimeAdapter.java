package com.project.shiraz.animereminder.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.project.shiraz.animereminder.JsonStructures.Anime;
import com.project.shiraz.animereminder.JsonStructures.EpisodeUnit;
import com.project.shiraz.animereminder.R;

import java.util.ArrayList;

import static com.project.shiraz.animereminder.Adapters.UsefulVisualChanges.makeMarquee;

/**
 * Created by shiraz on 9/5/17.
 */

public class AnimeAdapter  extends ArrayAdapter<Anime> {

    public AnimeAdapter(@NonNull Context context, ArrayList<Anime> animes) {
        super(context, R.layout.episode_item, animes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.episode_item, null);
        }
        Anime anime = getItem(position);
        if (anime != null) {
            TextView nameTextView = (TextView) view.findViewById(R.id.item_episode_name);
            TextView numbertextView = (TextView) view.findViewById(R.id.item_episode_number);
            TextView datetextView = (TextView) view.findViewById(R.id.item_episode_date);

            nameTextView.setText(anime.Name);
            makeMarquee(nameTextView);
            numbertextView.setVisibility(View.GONE);
            datetextView.setVisibility(View.GONE);
        }
        return view;
    }
}
