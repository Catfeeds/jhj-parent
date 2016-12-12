package com.meijia.utils.log;

public class AccessLogVo {

	private String ip;
	
	private String dateTime;
	
	private String requestPath;
	
	private String responseStatus;
	
	private String byteSend;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getRequestPath() {
		return requestPath;
	}

	public void setRequestPath(String requestPath) {
		this.requestPath = requestPath;
	}

	public String getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}

	public String getByteSend() {
		return byteSend;
	}

	public void setByteSend(String byteSend) {
		this.byteSend = byteSend;
	}
	
	
}
