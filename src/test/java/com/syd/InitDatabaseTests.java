package com.syd;

import com.syd.dao.QuestionDAO;
import com.syd.dao.UserDAO;
import com.syd.model.EntityType;
import com.syd.model.User;
import com.syd.service.FollowService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

@Sql("/init-schema.sql")
@RunWith(SpringRunner.class)
@SpringBootTest
public class InitDatabaseTests {

	@Autowired
	UserDAO userDAO;
	@Autowired
	QuestionDAO questionDAO;
	@Autowired
	FollowService followService;

	@Test
	public void ininDatabase() {
		Random random = new Random();
		for (int i = 1; i < 11; ++i) {
			User user = new User();
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
			user.setName(String.format("USER%d", i));
			user.setSalt("");
			user.setPassword("");
			userDAO.addUser(user);

			//互相关注
			for (int j = 1; j < i; ++j) {
				followService.follow(j, EntityType.ENTITY_USER, i);
			}



//			//更新密码
//			user.setPassword("xx");
//			userDAO.updatePassword(user);

//			Question question = new Question();
//			question.setCommentCount(i);
//			Date date = new Date();
//			date.setTime(date.getTime() + 1000 * 3600 * i);
//			question.setCreatedDate(date);
//			question.setUserId(i + 1);
//			question.setTitle(String.format("title{%d}", i));
//			question.setContent(String.format("Alibaba %d", i));
//			questionDAO.addQuestion(question);


		}
//		Assert.assertEquals("xx", userDAO.selectById(1).getPassword());
//		userDAO.deleteById(1);
//		Assert.assertNull(userDAO.selectById(1));
//		System.out.println(questionDAO.selectLatestQuestions(0, 0, 10));
		//com syd dao是三个目录，不是一个目录，需要一个一个新建。

	}

	@Test
	public void contextLoads() {
		for (int i = 0; i < 10; ++i) {

			//互相关注
			for (int j = 1; j < i; ++j) {
				followService.follow(j, EntityType.ENTITY_USER, i);
			}
		}
	}


}
