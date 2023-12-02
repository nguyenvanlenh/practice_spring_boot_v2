package com.watermelon.security;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.watermelon.model.entity.User;

@Service
public class CustomUserDetails implements UserDetails ,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String username;
	@JsonIgnore
	private String password;
	private String email;
	private String phone;
	private boolean status;
	private Collection<? extends GrantedAuthority> authorities;

	public CustomUserDetails() {
		// TODO Auto-generated constructor stub
	}

	public CustomUserDetails(long id, String username, String password, String email, String phone, boolean status,
			Collection<? extends GrantedAuthority> authorities) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.phone = phone;
		this.status = status;
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return this.authorities;
	}

	/**
	 * mapper from infor user to customeUsesDetails
	 * 
	 */
	public CustomUserDetails mapUserToUserDetail(User user) {
		// lay cac quyen tu user
		List<GrantedAuthority> lisAuthorities = user.getListRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getNameRole())).collect(Collectors.toList());

//		List<GrantedAuthority> list = new ArrayList<>();
//		for(Role r : user.getListRoles()) {
//			SimpleGrantedAuthority s = new SimpleGrantedAuthority(r.getNameRole());
//			list.add(s);
//		}
//		lisAuthorities = list;
		// tra ve customUserDetail cung ds quyen
		CustomUserDetails rs = new CustomUserDetails(
				user.getId(),
				user.getUsername(),
				user.getPassword(),
				user.getEmail(),
				user.getPhone(),
				user.isStatus(),
				lisAuthorities);

		return rs;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
