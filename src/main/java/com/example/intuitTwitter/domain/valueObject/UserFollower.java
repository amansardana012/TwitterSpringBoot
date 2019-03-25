package com.example.intuitTwitter.domain.valueObject;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.HashSet;

@Document(collection = "userFollower")
public class UserFollower implements Serializable {

    @Id
    private String userId;
    private HashSet<String> userFollowerSet= new HashSet<>();

    public UserFollower(){
    }
    public UserFollower(String userId, String userFollower) {
        this.userId = userId;
        this.userFollowerSet.add(userFollower);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public HashSet<String> getUserFollowerSet() {
        return userFollowerSet;
    }

    public void setUserFollowerSet(HashSet<String> userFollowerSet) {
        this.userFollowerSet = userFollowerSet;
    }

    public void addFollower(String followerUserId){
        getUserFollowerSet().add(followerUserId);
    }
}
