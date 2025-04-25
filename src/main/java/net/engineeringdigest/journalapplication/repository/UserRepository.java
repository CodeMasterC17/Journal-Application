package net.engineeringdigest.journalapplication.repository;

import org.bson.types.ObjectId;
import net.engineeringdigest.journalapplication.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUserName(String username);

    void deleteByUserName(String username);
}
