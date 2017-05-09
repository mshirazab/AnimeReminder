package com.project.shiraz.animereminder.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.shiraz.animereminder.EpisodesActivity;
import com.project.shiraz.animereminder.JsonStructures.Anime;
import com.project.shiraz.animereminder.MainActivity;
import com.project.shiraz.animereminder.R;

import java.util.ArrayList;

public class NewAnimeAdapter extends RecyclerView.Adapter<NewAnimeAdapter.ViewHolder> {
    private ArrayList<Anime> animes;
    private Context context;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView numberTextView;
        TextView dateTextView;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EpisodesActivity.class);
                    intent.putExtra(MainActivity.SELECTED_EPISODE, animes.get(getAdapterPosition()).Name);
                    context.startActivity(intent);
                }
            });
            nameTextView = (TextView) itemView.findViewById(R.id.item_episode_name);
            numberTextView = (TextView) itemView.findViewById(R.id.item_episode_number);
            dateTextView = (TextView) itemView.findViewById(R.id.item_episode_date);
        }
    }

    public NewAnimeAdapter(Context context, ArrayList<Anime> animes) {
        this.animes = animes;
        this.context = context;
    }

    @Override
    public NewAnimeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.episode_item, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NewAnimeAdapter.ViewHolder holder, int position) {
        Anime anime = animes.get(position);

        holder.nameTextView.setText(anime.Name);
        holder.dateTextView.setVisibility(View.GONE);
        holder.numberTextView.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return animes.size();
    }
}
