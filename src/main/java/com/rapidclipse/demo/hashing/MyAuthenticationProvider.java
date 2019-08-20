
package com.rapidclipse.demo.hashing;

import com.rapidclipse.demo.hashing.domain.User;
import com.rapidclipse.framework.security.authentication.Authenticator;
import com.rapidclipse.framework.security.authentication.AuthenticatorProvider;
import com.rapidclipse.framework.security.authentication.CredentialsUsernamePassword;
import com.rapidclipse.framework.security.util.PasswordHasher;
import com.rapidclipse.framework.server.security.authentication.jpa.JPAAuthenticator;


public class MyAuthenticationProvider
	implements AuthenticatorProvider<CredentialsUsernamePassword, CredentialsUsernamePassword>
{
	private static MyAuthenticationProvider INSTANCE;

	public static MyAuthenticationProvider getInstance()
	{
		if(MyAuthenticationProvider.INSTANCE == null)
		{
			MyAuthenticationProvider.INSTANCE = new MyAuthenticationProvider();
		}

		return MyAuthenticationProvider.INSTANCE;
	}

	private JPAAuthenticator authenticator;

	private MyAuthenticationProvider()
	{
	}

	@Override
	public Authenticator<CredentialsUsernamePassword, CredentialsUsernamePassword> provideAuthenticator()
	{
		if(this.authenticator == null)
		{
			this.authenticator = new JPAAuthenticator(User.class);
			this.authenticator.setPasswordHasher(PasswordHasher.Sha2());
		}

		return this.authenticator;
	}
}
