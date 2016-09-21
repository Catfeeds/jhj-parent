package com.mail.util.vo;

public class MyEmail {
	
	//发送用户名
	private String mailFrom;
	//发送名称
	private String mailFromName="叮当到家";
	//接收人
	private String[] toEmail;
	//主题
	private String subject;
	//发送内容
	private String context;
	
	public String getMailFrom() {
		return mailFrom;
	}
	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}
	
	public String getMailFromName() {
		return mailFromName;
	}
	public void setMailFromName(String mailFromName) {
		this.mailFromName = mailFromName;
	}
	public String[] getToEmail() {
		return toEmail;
	}
	public void setToEmail(String[] toEmail) {
		this.toEmail = toEmail;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
}
