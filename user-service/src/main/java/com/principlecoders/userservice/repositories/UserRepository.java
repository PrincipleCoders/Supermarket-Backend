package com.principlecoders.userservice.repositories;

import com.principlecoders.userservice.models.UserAdditionalData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserAdditionalData, String> {
}
