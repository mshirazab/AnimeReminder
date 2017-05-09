package com.project.shiraz.animereminder.JsonStructures;

import com.project.shiraz.animereminder.JsonStructures.EpisodeUnit;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EpisodeDetails {
    public String Name;
    public ArrayList<EpisodeUnit> Episodes;
    public long Seasons;

    public static long convertTime(String date) {
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
            unixTime = Date.getTime() / 1000;
        } catch (Exception ignored) {
        }
        return unixTime + 10*60*60;
    }

    public static String convertTime(long unixSeconds) {
        Date date = new Date(unixSeconds * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        return sdf.format(date);
    }
}
