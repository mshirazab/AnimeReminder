package com.project.shiraz.animereminder.Adapters;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.shiraz.animereminder.JsonStructures.EpisodeDetails;
import com.project.shiraz.animereminder.JsonStructures.EpisodeUnit;
import com.project.shiraz.animereminder.NotificationPublisher;
import com.project.shiraz.animereminder.R;

import java.util.ArrayList;


public class NewEpisodeAdapter extends RecyclerView.Adapter<NewEpisodeAdapter.ViewHolder> {
    private ArrayList<EpisodeUnit> episodes;
    private Context context;
    private String animeName;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView numberTextView;
        TextView dateTextView;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openEpisode(context, episodes.get(getAdapterPosition()));
                }
            });
            nameTextView = (TextView) itemView.findViewById(R.id.item_episode_name);
            numberTextView = (TextView) itemView.findViewById(R.id.item_episode_number);
            dateTextView = (TextView) itemView.findViewById(R.id.item_episode_date);
        }

    }

    public NewEpisodeAdapter(Context context, ArrayList<EpisodeUnit> episodes, String animeName) {
        this.episodes = episodes;
        this.context = context;
        this.animeName = animeName;
    }

    @Override
    public NewEpisodeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.episode_item, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return new NewEpisodeAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NewEpisodeAdapter.ViewHolder holder, int position) {
        EpisodeUnit episode = episodes.get(position);

        holder.nameTextView.setText(episode.Name);
        UsefulVisualChanges.makeMarquee(holder.nameTextView);
        holder.dateTextView.setText(episode.Time);
        String episodeNumber = "" + episode.Number;
        holder.numberTextView.setText(episodeNumber);
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    private void openEpisode(Context context, EpisodeUnit episode) {

        String url = episode.URL;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        long currentTime = System.currentTimeMillis();
        long episodeTime = EpisodeDetails.convertTime(episode.Time) * 1000;
        Log.d("Time", EpisodeDetails.convertTime(EpisodeDetails.convertTime(episode.Time)));
        if (currentTime > episodeTime)
            context.startActivity(intent);
        else
            scheduleNotification(
                    context,
                    getNotification(context, "New Episode",
                            animeName + " " + episode.Number + " : " + episode.Name,
                            R.mipmap.ic_launcher,
                            intent),
                    0);
    }

    private Notification getNotification(Context context, String title, String description, int resource, Intent intent) {
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(resource)
                        .setContentTitle(title)
                        .setContentText(description)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(description));
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(true);
        return mBuilder.build();
    }

    private void scheduleNotification(Context context, Notification notification, long delay) {

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }
}
