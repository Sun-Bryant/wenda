package com.syd.service;

import com.syd.dao.CommentDAO;
import com.syd.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;


@Service
public class CommentService {
    @Autowired
    private CommentDAO commentDAO;
    @Autowired
    SensitiveService sensitiveService;


    public List<Comment> getCommentsByEntity(int entityId, int entityType) {
        return commentDAO.selectByEntity(entityId, entityType);
    }

    public int addComment(Comment comment) {
        //html过滤
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        //敏感词过滤
        comment.setContent(sensitiveService.filter(comment.getContent()));
        return commentDAO.addComment(comment);
    }

    public int getCommentCount(int entityId, int entityType) {
        return commentDAO.getCommentCount(entityId, entityType);
    }

    public Comment getCommentById(int id) {
        return commentDAO.getCommentById(id);
    }

    public void deleteComment(int entityId, int entityType) {
        commentDAO.updateStatus(entityId, entityType, 1);
    }

    public int getUserCommentCount(int userId) {
        return commentDAO.getUserCommentCount(userId);
    }
}
