package com.example.intuitTwitter.domain.valueObject;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tweets")
public class TweetsMongoRecord {

    @Id
    private String id;
    private Tweet tweet;

    public TweetsMongoRecord(){

    }

    public TweetsMongoRecord(String id, Tweet tweet) {
        this.id = id;
        this.tweet = tweet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Tweet getTweet() {
        return tweet;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }
}
