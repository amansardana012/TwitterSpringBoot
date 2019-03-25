package com.example.intuitTwitter.controller;


import com.example.intuitTwitter.domain.repository.TweetRepository;
import com.example.intuitTwitter.domain.repository.UserFollowerRepository;
import com.example.intuitTwitter.domain.repository.UserFollowingRepository;
import com.example.intuitTwitter.domain.repository.UserRepository;
import com.example.intuitTwitter.domain.valueObject.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TwitterController {

    private final UserFollowingRepository userFollowingRepository;
    private final UserFollowerRepository userFollowerRepository;
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;
    private final Logger logger =  LoggerFactory.getLogger(TwitterController.class);

    @Autowired
    public TwitterController(UserFollowingRepository userFollowingRepository, UserFollowerRepository userFollowerRepository, UserRepository userRepository, TweetRepository tweetRepository){
        this.userFollowingRepository= userFollowingRepository;
        this.userFollowerRepository = userFollowerRepository;
        this.userRepository = userRepository;
        this.tweetRepository  = tweetRepository;
    }

    @RequestMapping(value = "/tweets/", method = RequestMethod.POST)
    public ResponseEntity addTweet(@RequestBody Tweet tweet){
        try {
            Optional<User> user = userRepository.findById(tweet.getUserId());
            if(user.isPresent()){
                Tweet addedTweet = tweetRepository.insert(new Tweet(tweet.getUserId(), tweet.getContent()));
                return new ResponseEntity(addedTweet, HttpStatus.OK);
            }
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        catch(Exception ex){
            logger.error(SystemEvent.ADD_TWEET_FAILURE.getId(), SystemEvent.ADD_TWEET_FAILURE.getDescription(), ex);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "users/{userId}/tweets/{tweetId}", method = RequestMethod.DELETE)
    public ResponseEntity addTweet(@PathVariable("userId") String userId, @PathVariable("tweetId") String tweetId){
        try {
            Optional<User> user = userRepository.findById(userId);
            if(user.isPresent()){
                tweetRepository.deleteById(tweetId);
                return new ResponseEntity(HttpStatus.OK);
            }
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        catch(Exception ex){
            logger.error(SystemEvent.ADD_TWEET_FAILURE.getId(), SystemEvent.ADD_TWEET_FAILURE.getDescription(), ex);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "users/{userId}/timeline", method = RequestMethod.GET)
    public ResponseEntity addTweet(@PathVariable("userId") String userId){
        try {
            Optional<User> user = userRepository.findById(userId);
            if(user.isPresent()){
                HashSet<String> userToFindTweets= new HashSet<>();
                userToFindTweets.add(userId);
                UserFollowing userFollowing = userFollowingRepository.findById(userId).orElse(new UserFollowing());
                userToFindTweets.addAll(userFollowing.getUserFollowingSet());
                List<Tweet> tweets = tweetRepository.getRecentTweets(userToFindTweets, 5);
                return new ResponseEntity(tweets,HttpStatus.OK);
            }
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        catch(Exception ex){
            logger.error(SystemEvent.ADD_TWEET_FAILURE.getId(), SystemEvent.ADD_TWEET_FAILURE.getDescription(), ex);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Get followers for this user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of followers"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 500, message = "Something went wrong")
    })
    @RequestMapping(value = "/users/{userId}/followers", method = RequestMethod.GET)
    public ResponseEntity getFollowers(@PathVariable("userId") String userId){
        try {
            Optional<User> user = userRepository.findById(userId);
            if(user.isPresent()){
                UserFollower userFollower = userFollowerRepository.findById(userId).orElse(new UserFollower());
                return new ResponseEntity(userFollower.getUserFollowerSet(), HttpStatus.OK);
            }
            else{
                logger.error(SystemEvent.GET_FOLLOWERS_FAILURE.getId(), SystemEvent.GET_FOLLOWERS_FAILURE.getDescription() + " "+ userId);
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }
        catch(Exception ex){
            logger.error(SystemEvent.GET_FOLLOWERS_FAILURE.getId(), SystemEvent.GET_FOLLOWERS_FAILURE.getDescription() + " "+ userId, ex);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Follow the new user")
    @RequestMapping(value = "/users/{userId}/follow/{followingUserId}", method = RequestMethod.PUT)
    public ResponseEntity follow(@PathVariable("userId") String userId, @PathVariable("followingUserId") String followingUserId){
        try {
            if(addUsertoFollowingUserList(userId, followingUserId) && addUsertoFollowerUserList(followingUserId, userId)) {
                return new ResponseEntity (HttpStatus.OK);
            }
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(Exception ex){
            logger.error(SystemEvent.GET_FOLLOWERS_FAILURE.getId(), SystemEvent.GET_FOLLOWERS_FAILURE.getDescription());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean addUsertoFollowingUserList(String userId, String followingUserId){
        try {
            UserFollowing userFollowing = userFollowingRepository.findById(userId).orElse(new UserFollowing(userId, followingUserId));
            userFollowing.addUserFollowing(followingUserId);
            userFollowingRepository.save(userFollowing);
            return true;
        }
        catch (Exception ex){
            logger.error(SystemEvent.ADD_FOLLOWING_USER_FAILURE.getId(), SystemEvent.ADD_FOLLOWING_USER_FAILURE.getDescription());
            return false;
        }
    }

    private boolean addUsertoFollowerUserList(String userId, String followerUserId){
        try {
            UserFollower userFollower = userFollowerRepository.findById(userId).orElse(new UserFollower(userId, followerUserId));
            userFollower.addFollower(followerUserId);
            userFollowerRepository.save(userFollower);
            return true;
        }
        catch (Exception ex){
            logger.error(SystemEvent.ADD_FOLLOWER_USER_FAILURE.getId(), SystemEvent.ADD_FOLLOWER_USER_FAILURE.getDescription() + " "+ userId, ex);
            return false;
        }
    }


    @RequestMapping(value = "/users/", method = RequestMethod.POST)
    public ResponseEntity addUser(@RequestBody User user){
        try {
            userRepository.save(user);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch(Exception ex){
            logger.error(SystemEvent.GET_FOLLOWERS_FAILURE.getId(), SystemEvent.GET_FOLLOWERS_FAILURE.getDescription(), ex);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





}
