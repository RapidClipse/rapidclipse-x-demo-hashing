
package com.rapidclipse.demo.hashing.domain;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.rapidclipse.demo.hashing.dal.UserDAO;
import com.rapidclipse.framework.security.authentication.CredentialsUsernamePassword;
import com.rapidclipse.framework.server.data.DAO;


@Entity
@DAO(UserDAO.class)
@Cacheable(true)
@Table(name = "`User`")
public class User implements Serializable, CredentialsUsernamePassword
{

	private byte[] password;
	private String username;
	
	public User()
	{
		super();
	}
	
	@Id
	@Column(name = "USERNAME", unique = true, nullable = false)
	public String getUsername()
	{
		return this.username;
	}
	
	public void setUsername(final String username)
	{
		this.username = username;
	}
	
	@Column(name = "`PASSWORD`", nullable = false)
	public byte[] getPassword()
	{
		return this.password;
	}
	
	public void setPassword(final byte[] password)
	{
		this.password = password;
	}
	
	@Override
	public String username()
	{
		return this.getUsername();
	}
	
	@Override
	public byte[] password()
	{
		return this.getPassword();
	}

}
