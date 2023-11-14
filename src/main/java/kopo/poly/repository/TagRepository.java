package kopo.poly.repository;

import kopo.poly.repository.entity.TagEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends MongoRepository<TagEntity, String> {

    Optional<TagEntity> findByName(String name);

}
