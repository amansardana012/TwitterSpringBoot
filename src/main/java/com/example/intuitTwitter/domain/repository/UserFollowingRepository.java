package com.example.intuitTwitter.domain.repository;

import com.example.intuitTwitter.domain.valueObject.UserFollowing;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserFollowingRepository extends MongoRepository<UserFollowing, String> {
}
