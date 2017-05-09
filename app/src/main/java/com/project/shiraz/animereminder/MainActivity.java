package com.project.shiraz.animereminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.shiraz.animereminder.Adapters.AnimeAdapter;
import com.project.shiraz.animereminder.Adapters.EpisodeAdapter;
import com.project.shiraz.animereminder.JsonStructures.Anime;
import com.project.shiraz.animereminder.JsonStructures.EpisodeDetails;
import com.project.shiraz.animereminder.JsonStructures.EpisodeUnit;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static final String SELECTED_EPISODE = "SELECTED_EPISODE";

    ListView listView;
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

        listView = (ListView) findViewById(R.id.episode_listview);
        progressBar = (ProgressBar) findViewById(R.id.main_progressbar);

        animes = new ArrayList<>();
        final AnimeAdapter adapter = new AnimeAdapter(this, animes);
        eventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                progressBar.setVisibility(View.GONE);
                Anime anime = dataSnapshot.getValue(Anime.class);
                Log.d("Got Data", anime.Name);
                adapter.add(anime);
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

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), EpisodesActivity.class);
                intent.putExtra(SELECTED_EPISODE, animes.get(position).Name);
                startActivity(intent);
            }
        });
    }
}
