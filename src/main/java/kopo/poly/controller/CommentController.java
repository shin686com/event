package kopo.poly.controller;

import kopo.poly.dto.*;
import kopo.poly.repository.entity.CommentEntity;
import kopo.poly.service.ICommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RequestMapping(value = "/comment")
@RequiredArgsConstructor
@Controller
public class CommentController {
    private final ICommentService commentService;

    @PostMapping("insert")
    @ResponseBody
    public List<CommentEntity> insert(@RequestParam("title")String title, HttpServletRequest request, HttpSession session){
        log.info("insert 시작");
        log.info(title);
        log.info(request.getParameter("parents"));
        log.info((String) session.getAttribute("SS_USER_ID"));
        CommentDTO cDTO = new CommentDTO();
        cDTO.setPosts(title);
        cDTO.setComment(request.getParameter("comment"));
        cDTO.setUserid((String) session.getAttribute("SS_USER_ID"));
        cDTO.setParents(request.getParameter("parents"));
        log.info(request.getParameter("comment"));
        List<CommentEntity> rList =commentService.insert(cDTO);
        log.info("insert 끝");
        return rList;
    }
    @GetMapping("list")
    @ResponseBody
    public List<CommentEntity> list(@RequestParam("title")String title){
        log.info("list 시작");
        List<CommentEntity> rList =commentService.list(title);
        log.info("list 끝");
        return rList;
    }
    @GetMapping("reList")
    @ResponseBody
    public List<CommentEntity> reList(@RequestParam("parents")String parents){
        log.info("list 시작");
        List<CommentEntity> rList =commentService.reList(parents);
        log.info("list 끝");
        return rList;
    }
    @PostMapping("delete")
    @ResponseBody
    public List<CommentEntity> delete(HttpServletRequest request){
        log.info("delete 시작");
        CommentDTO cDTO = new CommentDTO();
        cDTO.setId(request.getParameter("id"));
        log.info(request.getParameter("id"));
        List<CommentEntity> rList =commentService.delete(cDTO);
        log.info("delete 끝");
        return rList;
    }
    @PostMapping("update")
    @ResponseBody
    public List<CommentEntity> update(HttpServletRequest request, HttpSession session){
        log.info("update 시작");
        CommentDTO cDTO = new CommentDTO();
        log.info(request.getParameter("id"));
        log.info(request.getParameter("comment2"));
        cDTO.setId(request.getParameter("id"));
        cDTO.setComment(request.getParameter("comment2"));
        List<CommentEntity> rList =commentService.update(cDTO);
        log.info("update 끝");
        return rList;
    }
}
