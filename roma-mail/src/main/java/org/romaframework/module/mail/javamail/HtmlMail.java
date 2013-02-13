package org.romaframework.module.mail.javamail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

public class HtmlMail extends JavaMailAbstract {

	final static private String	TYPE	= "text/html";

	@Override
	public void setText(String text) throws MessagingException {
		MimeBodyPart mbp = new MimeBodyPart();
		mbp.setContent(text, TYPE);
		mmp.addBodyPart(mbp);

	}

}
