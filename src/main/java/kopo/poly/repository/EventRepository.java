package kopo.poly.repository;

import kopo.poly.repository.entity.ChannelEntitiy;
import kopo.poly.repository.entity.EventEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends MongoRepository<EventEntity, String> {
    Optional<EventEntity> findByTitle(String title);
    List<EventEntity> findByChannel(String channel);
    List<EventEntity> deleteByTitle(String title);
    List<EventEntity> findByCategory(String category, Pageable pageable);
    List<EventEntity> findAllByCategory(String category);

    Optional<EventEntity> findByEventSeq(int eventseq);

    List<EventEntity> findByTitleContaining(String title);
    List<EventEntity> findByAddrContaining(String addr);
    List<EventEntity> findByCategoryContaining(String category);
    List<EventEntity> findByTitleContainingAndCategoryContaining(String title,String category);
    List<EventEntity> findByTitleContainingAndAddrContaining(String title,String addr);
    List<EventEntity> findByAddrContainingAndCategoryContaining(String addr,String category);

    List<EventEntity> findByTitleContainingAndAddrContainingAndCategoryContaining(String title,String addr,String category);

}
