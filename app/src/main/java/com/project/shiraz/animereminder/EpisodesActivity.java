package com.project.shiraz.animereminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import com.project.shiraz.animereminder.Adapters.EpisodeAdapter;
import com.project.shiraz.animereminder.JsonStructures.Anime;
import com.project.shiraz.animereminder.JsonStructures.EpisodeDetails;
import com.project.shiraz.animereminder.JsonStructures.EpisodeUnit;

import java.util.ArrayList;


public class EpisodesActivity extends AppCompatActivity {
    String animeName;

    ListView listView;
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

        listView = (ListView) findViewById(R.id.episode_listview);
        progressBar = (ProgressBar) findViewById(R.id.main_progressbar);

        episodeUnits = new ArrayList<>();
        final EpisodeAdapter adapter = new EpisodeAdapter(this, episodeUnits);

        eventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                progressBar.setVisibility(View.GONE);
                EpisodeDetails episodeDetails = dataSnapshot.getValue(EpisodeDetails.class);
                adapter.addAll(episodeDetails.Episodes);
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

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openEpisode(episodeUnits.get(position));
            }
        });
    }

    public void openEpisode(EpisodeUnit episode) {

        String url = episode.URL;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        long currentTime = System.currentTimeMillis();
        long episodeTime = EpisodeDetails.convertTime(episode.Time) * 1000;
        Log.d("Time", EpisodeDetails.convertTime(EpisodeDetails.convertTime(episode.Time)));
        if (currentTime > episodeTime)
            startActivity(intent);
        else
            scheduleNotification(
                    getNotification("New Episode",
                            animeName + " " + episode.Number + " : " + episode.Name,
                            R.mipmap.ic_launcher,
                            intent),
                    0);
    }

    private Notification getNotification(String title, String description, int resource, Intent intent) {
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(resource)
                        .setContentTitle(title)
                        .setContentText(description)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(description));
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(true);
        return mBuilder.build();
    }

    private void scheduleNotification(Notification notification, long delay) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }
}
