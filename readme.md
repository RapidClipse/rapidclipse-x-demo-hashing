About Hashing
-------------

Rapidclipse provides the ability to add **Authentication** to a project.

A very popular auth method is to authenticate users against a database. The RapidClipse uses this method when a provider of type **Database (JPA)** is created.
The common way to store a password is to convert a password into a cryptographically protected form - usually a **hash**. 
A **hash** is a object generated from a string of text by a cryptographic hash algorithm. The result has a fixed length, and the same input will always produce the same hash, but the result will vary widely with small variations in input. 

Be aware that **hashing** and **encrypting** are two words that are often used interchangeably, but are actually not the same! 
Hash functions are very difficult/slow to reverse - so much so that it's practically impossible, while encryption is always reversible, a encrypted object must be able to be decrypted. Well-known hash functions/algorithms are for example **SHA** and **MD5**. 
The Rapidclipse wizard provides four common hash strategies, but MD5 just for compatibility reasons for existing credentials.
Of course, it is possible to store the user's password in plaintext, but this is quite a security nightmare.

To add this Authentication aspect to a project, Rapidclipse provides an [*"New Authentication Provider"*-wizard][1] that asks for all the necessary information and, at the end, generates an **AuthenticationProvider** and other necessary files.
So when the wizard has finished, a (JPA) **AuthenticationProvider** is available, a **user** entity and the corresponding database table exist, a user can be authenticated against the database. 

*But stop! There are no users yet!*

At this point a common problem arises: 
**How to store a plaintext-String-password** in a entity field of type byte[] that expects the already hashed password?

There are multiple ways to hash a password, or - in general - to hash a string. 
Most of them needs a **byte[]** as parameter, so there are two steps to perform to get a Hash:

### 1. Get the bytes
First the password string must be converted into a bytearray.
This is quite simple, the String class has a ```getBytes()``` method:
```java
	byte[] passwordAsByteArray = "myPlaintextPassword".getBytes(StandardCharsets.UTF_8);
```
or 
```java
	String mypassword = "myPlaintextPassword";
	byte[] passwordAsByteArray = mypassword.getBytes(StandardCharsets.UTF_8);
```


##### But there is a pitfall: 
The ```getBytes()``` method can be used without the **Charset** parameter. But this converting uses a certain **Charset** internally. If not set, the system default is used. 
RapidClipse preview uses ```UTF-8``` as the default charset. But if a app is deployed and runs on another system, the system default can differ, common other charsets are for example ```US-ASCII```, ```UTF-16```, ```ISO-8859-1``` or ```windows-1252```. 

For example for the first 128 symbols that contain most latin letters it doesn't matter if the charset is ```UTF-8``` or ```windows-1252```.
But if there are signs like ```§``` or the german ```ß``` the resulting bytearrays differ and, going further, also the generated Hash! 
If for example a password was hashed with ```windows-1252``` at creating a user and this hash was saved to database, but the current app uses ```UTF-8``` the hashes may differ, so the user cannot log in with the correct password!

*In this demo mostly the short ```getBytes()``` variant is used, as it is not intended to be deployed.*

There are some useful methods regarding Charsets, for example:
- To check the current charset:
```java
	Charset.defaultCharset();
```
- To get a charset:
```java
	Charset myCharset = Charset.forName("ISO-8859-1");
```
- Some of the Charsets are available in the "StandardCharsets" class:
```java
	Charset myCharset = StandardCharsets.UTF_8;
```

### 2. Hash the password

Now the password is available as a bytearray and must be hashed
This bytearray can now be hashed. In the following three ways are described: 
- using the RapidClipse framework's Hashstrategy, 
- using the the MessageDigest class and
- with the Shiro framework. 

#### 1A. Rapidclipse Hashstrategy (I):

The Rapidclipse AuthenticationProvider already contains all the necessary information, e.g. which hash algorithm to use. 
Here, the AuthenticationProvider is named "MyAuthenticationProvider". 
```java
	byte[] passwordAsByteArray = this.password.getBytes();

	// Get an instance of the AuthenticationProvider
	MyAuthenticationProvider myAuthProvider = MyAuthenticationProvider.getInstance();

	// Get the provider's authenticator
	JPAAuthenticator authenticator = (JPAAuthenticator) myAuthProvider.provideAuthenticator();

	// Get the HashStrategy
	HashStrategy hashStrategy = authenticator.getHashStrategy();

	// hash the password
	byte[] myHashedPassword = hashStrategy.hashPassword(passwordAsByteArray);
```

Of course this can be condensed: 
```java
	// All-in-one
	byte[] myHashedPassword = ((JPAAuthenticator)MyAuthenticationProvider.getInstance().provideAuthenticator())
		.getHashStrategy().hashPassword(this.password.getBytes());
```

#### 1B. Rapidclipse Hashstrategy (II):

It is not necessary to obtain the HashStrategy from the provider.
Instead, a new HashStrategy can be created and used, but in this case the hash algorithm must be chosen manually.
The following code gets the Hashstrategy from this provider and uses it's ```hashPassword()``` method.

```java
	HashStrategy hashStrategy = new HashStrategy.MD5();
	HashStrategy hashStrategy = new HashStrategy.SHA1();
	HashStrategy hashStrategy = new HashStrategy.SHA2();
	HashStrategy hashStrategy = new HashStrategy.PBKDF2WithHmacSHA1()
```
The code to hash the password is the same as above:
```java
	byte[] myHashedPassword = new HashStrategy.SHA2().hashPassword(this.password.getBytes());
```

#### 2. MessageDigest

The Rapidclipse Hashstrategy is based on the "MessageDigest" class from the Java Cryptography Architecture(JCA), a part of the java platform.
This MessageDigest class itself can also be used very easily, but the algorithm must be set as a string parameter:
```java
	// Get a MessageDigest instance that implements the SHA-256 algorithm
	MessageDigest mySha256Object = MessageDigest.getInstance("SHA-256");

	// Set the password bytearray
	mySha256Object.update(this.password.getBytes());

	// Get the hash 
	byte[] myHashedPassword = mySha256Object.digest();
```
And the short way:
```java
	byte[] myHashedPassword = MessageDigest.getInstance("SHA-256").digest(this.password.getBytes());
```
The "MessageDigest" is a engine class that searches different provider for a certain service according to the applied parameter. 
The available provider (not only for the MessageDigest class) can be obtained with 
```java
	Security.getProviders();
```
The "Show provider and services" - Button in the "CommonView.java"-view of the demo project lists the available provider and it's services.

For further information about the MessageDigest class and the Provider and it's services have a look on the [JCA website][2].

#### 3. The Shiro framework 

The shiro framework provides the "SimpleHash" class and explicit classes for the most common hash algorithms (MD2, MD5 and some SHA variants).
The password can be given to this classes directly as a string, so no ```getBytes``` is necessary. Which on the other hand means that the Charset can not be set.
The class for SHA-256 can be used really easy:
```java
	Sha256Hash sha256Hash = new Sha256Hash(this.password);
```
These classes have some other functionality that can be easily accessed. 
For example adding a salt to the password:
```java
	RandomNumberGenerator rng = new SecureRandomNumberGenerator();
	ByteSource salt = rng.nextBytes();

	Sha256Hash sha256HashWithSalt = new Sha256Hash(this.password, salt);
```
Or print a hex representation of the hash (see next paragraph):
```java
	String hexEncoded = sha256HashWithSalt.toHex();
```
For further information have a look an the [shiro website][3].


### 3. Some words to displaying a hash

A hash itself is just a bunch of bits and bytes without any relation to characters. 
The hash can be converted to a string like this:
```java
	String hashAsString = new String(myHashedPassword);
	System.out.println(hashAsString);
```
But writing that string to console or to the UI displays it in a really strange and unreadable form.
		
The most used way to display a Hash is to get it's hexadecimal representation. That's just a way the bytes of a hash are "encrypted" to get a more readable format.
There are some ways to get that format, one easy way ist to use the ```Hex``` class of the "shiro" framework:
```java
	String hashAsHex = Hex.encodeToString(myHashedPassword);
```
or the ```Hex``` class of the ["apache commons" framework][4]
```java
	String hashAsHex = Hex.encodeHexString(myHashedPassword);
```
The shiro framework is available in RapidClipse projects. But the Apache commons framework must be added project (add a maven dependency to the pom.xml file of the project)




  [1]: https://rapidclipse.atlassian.net/wiki/pages/viewpage.action?pageId=37290032
  [2]: http://docs.oracle.com/javase/8/docs/technotes/guides/security/crypto/CryptoSpec.html
  [3]: https://shiro.apache.org/
  [4]: https://commons.apache.org/proper/commons-codec/



