package kopo.poly.repository;

import kopo.poly.repository.entity.PostTagEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostTagRepository extends MongoRepository<PostTagEntity, String> {
    List<PostTagEntity> findByPostid(String postid);

    List<PostTagEntity> findAllByTagid(String tagid);
}
