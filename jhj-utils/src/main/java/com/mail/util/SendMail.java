package com.mail.util;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.mail.util.vo.MyEmail;

@Component("sendMail")
public class SendMail {
	
	@Autowired
	private JavaMailSenderImpl javaMailSenderImpl;
	
	public MimeMessage createMimeMessage(MyEmail myEmail) throws UnsupportedEncodingException, MessagingException{
		MimeMessage mimeMessage=javaMailSenderImpl.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
		mimeMessageHelper.setFrom(myEmail.getMailFrom(), myEmail.getMailFromName());
		mimeMessageHelper.setSubject(myEmail.getSubject());
		mimeMessageHelper.setTo(myEmail.getToEmail());
		mimeMessageHelper.setText(myEmail.getContext(),true);
		return mimeMessage;
	}
	
	public void sendMail(MyEmail myEmail) throws UnsupportedEncodingException, MessagingException{
		MimeMessage msg=createMimeMessage(myEmail);
		javaMailSenderImpl.send(msg);
	}
	
}
