package org.romaframework.module.mail.javamail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

public class PlainMail extends JavaMailAbstract {

	@Override
	public void setText(String text) {
		try {
			
			MimeBodyPart mbp = new MimeBodyPart();
			mbp.setText(text);
			mmp.addBodyPart(mbp);

		} catch (MessagingException mse) {

			mse.printStackTrace();
			System.out.println(mse.getMessage());

		}
	}

}
