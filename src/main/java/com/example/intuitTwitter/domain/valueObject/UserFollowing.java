package com.example.intuitTwitter.domain.valueObject;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;

@Document(collection = "userFollowing")
public class UserFollowing {

    @Id
    private String userId;
    private HashSet<String> userFollowingSet= new HashSet<>();

    public UserFollowing(){}

    public UserFollowing(String userId, String followingUserId) {
        this.userId = userId;
        this.userFollowingSet.add(followingUserId);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public HashSet<String> getUserFollowingSet() {
        return userFollowingSet;
    }

    public void setUserFollowingSet(HashSet<String> userFollowingSet) {
        this.userFollowingSet = userFollowingSet;
    }

    public void addUserFollowing(String followingUserId){
        getUserFollowingSet().add(followingUserId);
    }
}
