package com.example.instaclone;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends ParseObject {
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_COMMENTER = "commenter";
    public static final String KEY_POST = "post";
    public static final String KEY_POST_OWNER = "postOwner";

    public String getMessage(){
        return getString(KEY_MESSAGE);
    }
    public void setMessage(String message){
        put(KEY_MESSAGE, message);
    }

    public ParseUser getCommenter(){
        return getParseUser(KEY_COMMENTER);
    }
    public void setCommenter(ParseUser parseUser){
        put(KEY_COMMENTER, parseUser);
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


