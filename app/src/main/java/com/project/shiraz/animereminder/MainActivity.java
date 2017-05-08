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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ProgressBar progressBar;
    ArrayList<Anime> episodes;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference animeReference;
    DatabaseReference episodeReference;
    ChildEventListener eventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        firebaseDatabase = FirebaseDatabase.getInstance();
        animeReference = firebaseDatabase.getReference().child("anime");
        episodeReference = firebaseDatabase.getReference().child("episodes");

        listView = (ListView) findViewById(R.id.episode_listview);
        progressBar = (ProgressBar) findViewById(R.id.main_progressbar);

        episodes = new ArrayList<>();
        final CustomAdapter adapter = new CustomAdapter(this, episodes);
        eventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                progressBar.setVisibility(View.GONE);
                Anime anime = dataSnapshot.getValue(Anime.class);
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
                sendNotification(position);
            }
        });
    }

    public void sendNotification(int episodeNumber) {

        //EpisodeDetails episodeDetails = episodes.get(episodeNumber);
        String url;
        url = "https://ww1.gogoanime.io/shingeki-no-kyojin-episode-1";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));

        long currentTime = System.currentTimeMillis();
        startActivity(i);
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
