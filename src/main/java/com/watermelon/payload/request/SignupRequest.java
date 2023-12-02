package com.watermelon.payload.request;

import java.util.Date;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SignupRequest {
	private String username;
	private String password;
	private String email;
	private String phone;
	@DateTimeFormat(pattern = "dd/mm/yyyy")
	private Date created = new Date();
	private boolean status = true;
	private Set<String> listRoles;
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public Set<String> getListRoles() {
		return listRoles;
	}
	public void setListRoles(Set<String> listRoles) {
		this.listRoles = listRoles;
	}
	
}
