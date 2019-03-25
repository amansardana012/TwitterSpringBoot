package com.example.intuitTwitter.domain.repository;

import com.example.intuitTwitter.domain.valueObject.Tweet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.HashSet;
import java.util.List;

public interface TweetRepository extends MongoRepository<Tweet, String> {

    @Query(value = "{'userId' : {$in : ?0}}", sort = "{'timeStampInMillis' : -1}")
    List<Tweet> getRecentTweets(HashSet<String> userIds, int numberOfTweets);
}
