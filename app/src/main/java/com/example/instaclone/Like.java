package com.example.instaclone;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Like")
public class Like extends ParseObject {
    public static final String KEY_LIKER = "liker";
    public static final String KEY_POST = "post";
    public static final String KEY_POST_OWNER = "postOwner";


    public ParseUser getLiker(){
        return getParseUser(KEY_LIKER);
    }

    public void setLiker(ParseUser parseUser){
        put(KEY_LIKER, parseUser);

    }
    public ParseObject getPost(){
        return getParseObject(KEY_POST);
    }

    public void setPost(Post post){
        put(KEY_POST, post);
        put(KEY_POST_OWNER, post.getUser());
    }

    public ParseUser getPostOwner(){
        return getParseUser(KEY_POST_OWNER);
    }

}
