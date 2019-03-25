package com.example.intuitTwitter.domain.valueObject;

public enum SystemEvent {

    GET_FOLLOWERS_FAILURE("1", "Get followers call failed for user"),
    ADD_FOLLOWING_USER_FAILURE("2", "failed to add user to following user list"),
    ADD_FOLLOWER_USER_FAILURE("3", "failed to add user to follower user list"),
    CLIENT_ID_MISSING("4", "client id missing error"),
    API_UNKNOWN_ERROR("5", "unknown exception in micro service"),
    API_AUTHORIZATION_DENIED("6", "Unknown client error"),
    ADD_TWEET_FAILURE("8", "failed to add a tweet"),
    INSUFFICIENT_PERMISSIONS("7", "Unauthorized url access");
    String id;
    String description;

    SystemEvent(String id, String description){
        this.id = id;
        this.description= description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
