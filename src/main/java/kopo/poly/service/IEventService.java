package kopo.poly.service;

import kopo.poly.dto.*;
import kopo.poly.repository.entity.EventEntity;
import kopo.poly.repository.entity.JoinEntity;
import kopo.poly.repository.entity.TagEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IEventService {
    int insertChannel(ChannelDTO pDTO) throws Exception;
    int updateChannel(ChannelDTO pDTO) throws Exception;
    int insertEvent(EventDTO pDTO) throws Exception;
    ChannelDTO channelFinder(String userid) throws Exception;
    EventDTO findEvent(String user, String title, int i) throws Exception;
    List<EventEntity> findAllEvent(Pageable pageable) throws Exception;
    List<EventEntity> findEvent2(String channel) throws Exception;
    List<TagEntity> tag(String title);
    void like(String user, String title, int i)throws Exception;

    List<EventEntity> searchTag(String tag);

    LikeDTO countLike(String title);
    List<JoinEntity> findEvent3(String userid) throws Exception;
    List<EventEntity> findEvent4(String category,Pageable pageable) throws Exception;
    List<EventEntity> searchEvent(SearchDTO sDTO) throws Exception;
    int join(JoinDTO jDTO)throws Exception;
    List<EventEntity> delete(String title);

    int countAll();
    int countAll2(String category);
    int countAll3(String category);

    List<JoinEntity> findTitle(String title);
}
