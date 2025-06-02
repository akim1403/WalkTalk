package com.example.map;

import java.util.ArrayList;

public class Time
{
    private static ArrayList<Time> timeArrayList = new ArrayList<>();
    private static String currentTime = new String();

    private String id;
    private String name;

    public Time(String id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void initTime()
    {
        Time user1 = new Time("Any Length", "Any Length");
        timeArrayList.add(user1);

        Time user2 = new Time("15 Min", "15 Min");
        timeArrayList.add(user2);

        Time user3 = new Time("30 Min", "30 Min");
        timeArrayList.add(user3);

        Time user4 = new Time("45 Min", "45 Min");
        timeArrayList.add(user4);

        Time user5 = new Time("60 Min", "60 Min");
        timeArrayList.add(user5);
    }

    public int getImage()
    {
        switch (getId())
        {
            case "Any Length":
                currentTime = "Any Length";
                return R.drawable.anytime;
            case "15 Min":
                currentTime = "15 Min";
                return R.drawable.ftmin;
            case "30 Min":
                currentTime = "30 Min";
                return R.drawable.tmin;
            case "45 Min":
                currentTime = "45 Min";
                return R.drawable.ffmin;
            case "60 Min":
                currentTime = "60 Min";
                return R.drawable.smin;
        }
        return R.drawable.anytime;
    }

    public static ArrayList<Time> getTimeArrayList()
    {
        return timeArrayList;
    }
    public static String getCurrentTime()
    {
        return currentTime;
    }
}
