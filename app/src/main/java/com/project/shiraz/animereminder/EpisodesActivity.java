package com.project.shiraz.animereminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.shiraz.animereminder.Adapters.NewEpisodeAdapter;
import com.project.shiraz.animereminder.JsonStructures.EpisodeDetails;
import com.project.shiraz.animereminder.JsonStructures.EpisodeUnit;

import java.util.ArrayList;


public class EpisodesActivity extends AppCompatActivity {
    String animeName;

    RecyclerView recyclerView;
    ProgressBar progressBar;
    ArrayList<EpisodeUnit> episodeUnits;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference episodeReference;
    ChildEventListener eventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent intent = getIntent();
        animeName = intent.getStringExtra(MainActivity.SELECTED_EPISODE);

        firebaseDatabase = FirebaseDatabase.getInstance();
        episodeReference = firebaseDatabase.getReference().child(animeName);

        recyclerView = (RecyclerView) findViewById(R.id.episode_listview);
        progressBar = (ProgressBar) findViewById(R.id.main_progressbar);

        episodeUnits = new ArrayList<>();
        final NewEpisodeAdapter adapter = new NewEpisodeAdapter(this, episodeUnits, animeName);

        eventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                progressBar.setVisibility(View.GONE);
                EpisodeDetails episodeDetails = dataSnapshot.getValue(EpisodeDetails.class);
                episodeUnits.addAll(episodeDetails.Episodes);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        episodeReference.addChildEventListener(eventListener);

        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);
    }
}
