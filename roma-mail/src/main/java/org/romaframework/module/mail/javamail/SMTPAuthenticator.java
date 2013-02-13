package org.romaframework.module.mail.javamail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SMTPAuthenticator extends Authenticator {

	private String userid;
	private String passwd;

	public SMTPAuthenticator(String userid, String passwd) {
		this.userid = userid;
		this.passwd = passwd;
	}

	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userid, passwd);
	}

}
