package com.example.intuitTwitter.domain.repository;

import com.example.intuitTwitter.domain.valueObject.UserFollower;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserFollowerRepository extends MongoRepository<UserFollower, String> {
}
