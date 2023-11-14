package kopo.poly.repository;

import kopo.poly.repository.entity.CommentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends MongoRepository<CommentEntity, String> {
    List<CommentEntity> findAllByPosts(String posts);
    List<CommentEntity> findAllByParents(String parents);

    void deleteAllByPosts(String posts);
}
