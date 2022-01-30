package com.example.demo.obj;

import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class User {
    private int id;
    private String name;

    public User() {
    }
    public User(int id) {
        this.id = id;
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(JSONObject obj) {
        try {
            this.id = Integer.parseInt(obj.getString("user"));
        } catch (NumberFormatException invalidInt) {
            this.id = 0;
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean verifyUser(int id) {

        final ArrayList<User> userList = getUsersFromFile();
        final List<User> user = userList.stream().filter((c)->c.getId()==id).collect(Collectors.toList());
        if(user.size()==1)
            return true;
        return false;
    }

    public static ArrayList<User> getUsersFromFile() {
        final Resource resource = new ClassPathResource("database/Users.txt");
        final ArrayList<User> userList = new ArrayList<>();

        final BufferedReader br;
        try {
            File file = null;
            file = resource.getFile();
            br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null) {
//                System.out.println("ST: " + st);
                String[] delimitedUserValues = st.split(",");
                int id = 0;
                String name = "";
                for (int value = 0; value < delimitedUserValues.length; value++) {
                    //in case there are extra values
                    switch (value) {
                        case 0:
                            try {
                                id = Integer.parseInt(delimitedUserValues[value].trim());
                            } catch (NumberFormatException num) {
                            }
                            break;
                        case 1:
                            name = delimitedUserValues[value].trim();
                            break;
                        default:
                            break;
                    }
                }
                User newUser = new User(id, name);
//                System.out.println("new user: " + id + " " + name);
                userList.add(newUser);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userList;
    }


}
