package com.example.frsamuel;

import java.sql.Time;
import java.sql.Timestamp;

public class Posts {
    private String post;
    private String user_id;
   // private Timestamp time;

    public Posts()
    {

    }

    public Posts(String user_id, String post)
    {
        this.post = post;
        this.user_id = user_id;
        //this.time = time;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

   /* public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }*/
}
