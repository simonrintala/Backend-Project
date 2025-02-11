package com.Java24GroupProject.AirBnBPlatform.repositories;

import com.Java24GroupProject.AirBnBPlatform.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

//NOTE: not finished, just made what needed to be there for Security implementation.
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNr(String phoneNr);

}
