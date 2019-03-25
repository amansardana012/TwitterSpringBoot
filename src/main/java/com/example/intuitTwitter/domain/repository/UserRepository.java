package com.example.intuitTwitter.domain.repository;

import com.example.intuitTwitter.domain.valueObject.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, String> {

}
