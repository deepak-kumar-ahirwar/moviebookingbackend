package com.moviebookingapp.repository;


import java.util.Optional;



import com.moviebookingapp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

  Optional<User> findByUserName(String userName);
  Optional<User> findByEmail(String email);

}
