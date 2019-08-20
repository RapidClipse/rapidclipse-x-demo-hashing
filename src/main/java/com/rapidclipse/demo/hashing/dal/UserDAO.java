package com.rapidclipse.demo.hashing.dal;

import com.rapidclipse.demo.hashing.domain.User;
import com.rapidclipse.framework.server.jpa.dal.JpaDataAccessObject.Implementation;

public class UserDAO extends Implementation<User, Integer>
{
	
	public final static UserDAO INSTANCE = new UserDAO();
	
	public UserDAO()
	{
		super(User.class);
	}
	
}
