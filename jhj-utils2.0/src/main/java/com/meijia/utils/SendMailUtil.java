package com.meijia.utils;

import java.util.Properties;
import java.util.ResourceBundle;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.LoggerFactory;

public class SendMailUtil {

	private String from, smtp, username, password, port;

	public SendMailUtil() {

		ResourceBundle rb = ResourceBundle.getBundle("email_config");
		this.smtp = rb.getString("mail.smtp.host");
		this.port = rb.getString("mail.smtp.port");
		this.username = rb.getString("mail.smtp.username");
		this.password = rb.getString("mail.smtp.password");
		this.from = rb.getString("mail.smtp.from");

    }

	/**
	 * 发送文本类型的邮件
	 * @param to  					接受着邮箱地址，多个已;分割
	 * @param subject      		邮件主题
	 * @param content		邮件内容
	 */
	public void send(String to, String subject, String content) {

		// Get system properties
		Properties props = new Properties();

		// Setup mail server
		// properties.setProperty("mail.smtp.host", getSmtp());
		props.put("mail.smtp.host", getSmtp());
		props.put("mail.smtp.socketFactory.port", getPort());
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", getPort());

		// Get the default Session object.
		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(getUsername(),
								getPassword());
					}
				});
		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(getFrom()));

			String[] receivers = to.split(";");

			InternetAddress[] toAddress = new InternetAddress[receivers.length];
            for (int i = 0; i < receivers.length; i++) {
    			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
    					receivers[i]));
            }

			// Set Subject: header field
			message.setSubject(subject);

			// Now set the actual message
			message.setText(content);

			// Send message
			Transport.send(message);
			LoggerFactory.getLogger(getClass()).info(
					"Sent message successfully....");
		} catch (MessagingException mex) {
			LoggerFactory.getLogger(getClass()).error(mex.getMessage());
		}

	}

	/**
	 * 发送html类型的邮件
	 * @param to  					接受着邮箱地址，多个已;分割
	 * @param subject      		邮件主题
	 * @param content		邮件内容
	 */
	public void sendHtml(String to, String subject, String content) {

		// Get system properties
		Properties props = new Properties();

		// Setup mail server
		// properties.setProperty("mail.smtp.host", getSmtp());
		props.put("mail.smtp.host", getSmtp());
		props.put("mail.smtp.socketFactory.port", getPort());
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", getPort());

		// Get the default Session object.
		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(getUsername(),
								getPassword());
					}
				});
		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(getFrom()));

			String[] receivers = to.split(";");

			InternetAddress[] toAddress = new InternetAddress[receivers.length];
            for (int i = 0; i < receivers.length; i++) {
    			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
    					receivers[i]));
            }

			// Set Subject: header field
			message.setSubject(subject);

			// Now set the actual message
			Multipart mp = new MimeMultipart("related");// related意味着可以发送html格式的邮件
	        /** *************************************************** */
	        BodyPart bodyPart = new MimeBodyPart();// 正文
	        bodyPart.setDataHandler(new DataHandler(content,"text/html;charset=utf8"));// 网页格式
	        mp.addBodyPart(bodyPart);
	        message.setContent(mp);// 设置邮件内容对象

			// Send message
			Transport.send(message);
			LoggerFactory.getLogger(getClass()).info(
					"Sent message successfully....");
		} catch (MessagingException mex) {
			LoggerFactory.getLogger(getClass()).error(mex.getMessage());
		}

	}



	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSmtp() {
		return smtp;
	}

	public void setSmtp(String smtp) {
		this.smtp = smtp;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public static void main(String[] args) {
        // TODO Auto-generated method stub

//     String to = "402303480@qq.com;leon@zhirunjia.com";//给多个邮箱同时发送
		String to = "402303480@qq.com";//给多个邮箱同时发送
        String subject = "Java 测试邮件1";
        String content = "Java Mail 测试邮件1";
        SendMailUtil mail = new SendMailUtil();
//        mail.send(to, subject, content);

        String htmlContent = "<h1>表格</h1><br><table>" +
        								 "<tr><td>序号</td><td>订单号</td></tr>" +
        								 "<tr><td>1</td><td>20141218001</td></tr>" +
        								 "<tr><td>2</td><td>20141218002</td></tr>" +
        								 "</table>";
        mail.sendHtml(to, subject, htmlContent);
    }

}
