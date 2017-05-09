package com.project.shiraz.animereminder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.shiraz.animereminder.Adapters.NewAnimeAdapter;
import com.project.shiraz.animereminder.JsonStructures.Anime;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String SELECTED_EPISODE = "SELECTED_EPISODE";

    ProgressBar progressBar;
    ArrayList<Anime> animes;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference animeReference;
    ChildEventListener eventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        firebaseDatabase = FirebaseDatabase.getInstance();
        animeReference = firebaseDatabase.getReference().child("anime");

        progressBar = (ProgressBar) findViewById(R.id.main_progressbar);

        animes = new ArrayList<>();
        final NewAnimeAdapter adapter = new NewAnimeAdapter(this, animes);
        eventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                progressBar.setVisibility(View.GONE);
                Anime anime = dataSnapshot.getValue(Anime.class);
                Log.d("Got Data", anime.Name);
                animes.add(anime);
                adapter.notifyItemInserted(animes.size() - 1);
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
        animeReference.addChildEventListener(eventListener);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.episode_listview);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
