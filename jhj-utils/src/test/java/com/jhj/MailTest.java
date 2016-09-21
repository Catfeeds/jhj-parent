package com.jhj;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mail.util.SendMail;
import com.mail.util.vo.MyEmail;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试  
@ContextConfiguration({"/spring-mail.xml"})
public class MailTest {
	
	@Autowired 
	private SendMail sendMail;
	
	@Test
	public void sendMial() throws UnsupportedEncodingException, MessagingException {
//		ApplicationContext ac=new ClassPathXmlApplicationContext("spring-mail.xml");
//		SendMail sendMail=(SendMail) ac.getBean("sendMail");
		MyEmail myEmail=new MyEmail();
		myEmail.setMailFrom("zhangzhiqiang@jia-he-jia.com");  
//		myEmail.setMailFromName("家和家");
		myEmail.setSubject("你好");  
		myEmail.setToEmail(new String[]{"zha_zq@163.com"});  
		myEmail.setContext("<a href='www.baidu.com'><font color='red'>fdsfdsf</font></a>"); 
		sendMail.sendMail(myEmail);
		
	}
}
