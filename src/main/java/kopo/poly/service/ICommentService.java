package kopo.poly.service;

import kopo.poly.dto.CommentDTO;
import kopo.poly.repository.entity.CommentEntity;

import java.util.List;

public interface ICommentService {
    List<CommentEntity> insert(CommentDTO cDTO);
    List<CommentEntity> delete(CommentDTO cDTO);
    List<CommentEntity> update(CommentDTO cDTO);
    List<CommentEntity> list(String title);
    List<CommentEntity> reList(String parents);
}
