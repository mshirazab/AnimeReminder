package com.project.shiraz.animereminder;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

class EpisodeDetails {
    String Name;
    EpisodeUnit[] Episodes;
    int Seasons;

    private long convertTime(String date) {
        String[] dateArray = date.split(" ");
        int currentDate;
        long unixTime = 0;
        currentDate = Integer.parseInt(dateArray[1].replaceAll("[^\\d]", ""));
        String dateString = "";
        if (currentDate < 10)
            dateString += "0";
        dateString += currentDate + dateArray[0].substring(0, 3) + dateArray[2];
        DateFormat dateFormat = new SimpleDateFormat("ddMMMyyyy");
        try {
            Date Date = dateFormat.parse(dateString);
            unixTime = (long) Date.getTime() / 1000;
        } catch (Exception ignored) {
        }
        return unixTime;
    }

    String convertTime(long unixSeconds) {
        Date date = new Date(unixSeconds * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(date);
    }
}
