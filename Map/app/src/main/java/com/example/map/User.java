package com.example.map;

import java.util.ArrayList;

public class User
{
    private static ArrayList<User> userArrayList = new ArrayList<>();
    private static String currentUser = new String();

    private String id;
    private String name;

    public User(String id, String name)
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

    public static void initUsers()
    {
        User user1 = new User("Please select a Walk", "Please select a Walk");
        userArrayList.add(user1);

        User user2 = new User("Nature", "Nature");
        userArrayList.add(user2);

        User user3 = new User("City", "City");
        userArrayList.add(user3);
    }

    public int getImage()
    {
        switch (getId())
        {
            case "Please select a Walk":
                currentUser = "Please select a Walk";
                return R.drawable.selecticon;
            case "Nature":
                currentUser = "Nature";
                return R.drawable.treeicon;
            case "City":
                currentUser = "City";
                return R.drawable.cityicon;
        }
        return R.drawable.selecticon;
    }

    public static ArrayList<User> getUserArrayList()
    {
        return userArrayList;
    }
    public static String getCurrentUser()
    {
        return currentUser;
    }
}
