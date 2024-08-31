package com.evilhydra.Notes.repository;

import com.evilhydra.Notes.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,String> {
    User findByUserId(String userId);
    void deleteByUserId(String userId);

}
