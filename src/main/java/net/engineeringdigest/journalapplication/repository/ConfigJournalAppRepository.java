package net.engineeringdigest.journalapplication.repository;

import net.engineeringdigest.journalapplication.entity.ConfigJournalAppEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigJournalAppRepository extends MongoRepository<ConfigJournalAppEntry, ObjectId> {


}
