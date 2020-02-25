package com.eurowings.subscriber.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

public class Response {
	
	private HttpStatus code;
	private String message;
	List<User> userList=new ArrayList<User>();
	


	public HttpStatus getCode() {
		return code;
	}
	public void setCode(HttpStatus code) {
		this.code = code;
	}
	public List<User> getUserList() {
		return userList;
	}
	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "Response [code=" + code + ", message=" + message + ", userList=" + userList + "]";
	}

	

}
