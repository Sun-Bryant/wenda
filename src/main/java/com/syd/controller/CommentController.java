package com.syd.controller;

import com.syd.async.EventProducer;
import com.syd.model.Comment;
import com.syd.model.EntityType;
import com.syd.model.HostHolder;
import com.syd.service.CommentService;
import com.syd.service.QuestionService;
import com.syd.service.SensitiveService;
import com.syd.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;


@Controller
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    SensitiveService sensitiveService;

    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content) {
        try {
            content = HtmlUtils.htmlEscape(content);
            content = sensitiveService.filter(content);
            // 过滤content
            Comment comment = new Comment();
            if (hostHolder.getUser() != null) {
                comment.setUserId(hostHolder.getUser().getId());
            } else {
                comment.setUserId(WendaUtil.ANONYMOUS_USERID);
            }
            comment.setContent(content);
            comment.setEntityId(questionId);
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);

            commentService.addComment(comment);

            // 更新题目里的评论数量
            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            questionService.updateCommentCount(comment.getEntityId(), count);
            // 怎么异步化
        } catch (Exception e) {
            logger.error("增加评论失败" + e.getMessage());
        }
        return "redirect:/question/" + String.valueOf(questionId);
    }

//    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
//    public String addComment(@RequestParam("questionId") int questionId,
//                             @RequestParam("content") String content) {
//        try {
//            Comment comment = new Comment();
//            comment.setContent(content);
//            if (hostHolder.getUser() != null) {
//                comment.setUserId(hostHolder.getUser().getId());
//            } else {
//                comment.setUserId(WendaUtil.ANONYMOUS_USERID);
//                // return "redirect:/reglogin";
//            }
//            comment.setCreatedDate(new Date());
//            comment.setEntityType(EntityType.ENTITY_QUESTION);
//            comment.setEntityId(questionId);
//            commentService.addComment(comment);
//
//            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
//            questionService.updateCommentCount(comment.getEntityId(), count);
//
//            eventProducer.fireEvent(new EventModel(EventType.COMMENT).setActorId(comment.getUserId())
//                    .setEntityId(questionId));
//
//        } catch (Exception e) {
//            logger.error("增加评论失败" + e.getMessage());
//        }
//        return "redirect:/question/" + questionId;
//    }
}
