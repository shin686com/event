package kopo.poly.repository;

import kopo.poly.repository.entity.LikeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends MongoRepository<LikeEntity, String> {
    Optional<LikeEntity> findByUserAndTitle(String user,String title);
    int countByTitleAndLikes(String title, int like);




}
