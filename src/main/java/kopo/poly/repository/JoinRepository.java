package kopo.poly.repository;

import kopo.poly.repository.entity.JoinEntity;
import kopo.poly.repository.entity.UserInfoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JoinRepository extends MongoRepository<JoinEntity, String> {
    Optional<JoinEntity> findByUseridAndTitle(String userid,String title);
    List<JoinEntity> findByTitle(String title);
    List<JoinEntity>findByUserid(String userid);
}
