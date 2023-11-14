package kopo.poly.repository;

import kopo.poly.repository.entity.ChannelEntitiy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChannelRepository extends MongoRepository<ChannelEntitiy, String> {


    Optional<ChannelEntitiy> findByUserId(String userId);


}
