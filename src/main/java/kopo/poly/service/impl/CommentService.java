package kopo.poly.service.impl;

import kopo.poly.dto.CommentDTO;
import kopo.poly.repository.CommentRepository;
import kopo.poly.repository.entity.ChannelEntitiy;
import kopo.poly.repository.entity.CommentEntity;
import kopo.poly.repository.entity.UserInfoEntity;
import kopo.poly.service.ICommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService implements ICommentService {

    private final CommentRepository commentRepository;

    public List<CommentEntity> insert(CommentDTO cDTO){
        log.info(cDTO.getParents());
        CommentEntity pEntity = CommentEntity.builder()
                .userid(cDTO.getUserid()).comment(cDTO.getComment()).parents(cDTO.getParents()).posts(cDTO.getPosts())
                .build();


        commentRepository.save(pEntity);
        List<CommentEntity> rList = commentRepository.findAllByPosts(cDTO.getPosts());
        return rList;
    }
    public List<CommentEntity> delete(CommentDTO cDTO){
        CommentEntity rEntity = commentRepository.findById(cDTO.getId()).get();
        commentRepository.deleteById(rEntity.getId());
        List<CommentEntity> rList = commentRepository.findAllByPosts(cDTO.getPosts());
        return rList;
    }
    public List<CommentEntity> update(CommentDTO cDTO){
        log.info(cDTO.getId());
        CommentEntity rEntity = commentRepository.findById(cDTO.getId()).get();
        log.info(rEntity.getId());
        CommentEntity pEntity = CommentEntity.builder()
                .id(rEntity.getId()).userid(rEntity.getUserid()).comment(cDTO.getComment()).parents(rEntity.getParents()).posts(rEntity.getPosts())
                .build();
        commentRepository.save(pEntity);
        List<CommentEntity> rList = commentRepository.findAllByPosts(cDTO.getPosts());
        return rList;
    }
    public List<CommentEntity> list(String title){
        List<CommentEntity> rList = commentRepository.findAllByPosts(title);
        return rList;
    }
    public List<CommentEntity> reList(String parents){
        List<CommentEntity> rList = commentRepository.findAllByParents(parents);
        return rList;
    }
}
