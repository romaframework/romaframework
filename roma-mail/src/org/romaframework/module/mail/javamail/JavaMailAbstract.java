/*
 * Copyright 2006 - 2009 Paolo Spizzirri (paolo.spizzirri--at--assetdata.it)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

/**
 * author: Paolo Spizzirri
 * 
 * Specific implementation of the Mail Aspect using the SUN Javamail package
 */

package org.romaframework.module.mail.javamail;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.romaframework.core.Roma;
import org.romaframework.module.mail.MailException;

public abstract class JavaMailAbstract {

	private static final String	SMTP_HOST						= "mail.smtp.host";
	private static final String	SMTP_AUTHORIZATION	= "mail.smtp.auth";
	private static final String	SMTP_HOST_UNKNOWN		= "Unknown SMTP host";
	private static final Object	SMTP_PORT						= "mail.smtp.port";
	private static final Object	POP3_PORT						= "mail.pop3.port";
	private static final Object	IMAP_PORT						= "mail.imap.port";

	protected MimeMessage				message							= null;
	protected MimeMultipart			mmp									= null;

	public void sendMail(String subject, String text, String from, Object to, List<String> cc, List<String> bcc, List<String> attachments, String authentication) {
		sendMail(subject, text, from, to, cc, bcc, attachments, authentication, true);
	}

	/**
	 * 
	 * @param subject
	 * @param text
	 * @param from
	 * @param to
	 * @param cc
	 * @param bcc
	 * @param attachments
	 * @param defaultAuthentication
	 */
	public void sendMail(String subject, String text, String from, Object to, List<String> cc, List<String> bcc, List<String> attachments, String authentication, boolean silent) {
		try {
			inizializeContainer(authentication);

			MailHelper.setFromRecipient(message, from);
			MailHelper.setRecipients(message, to, RecipientType.TO);
			MailHelper.setRecipients(message, cc, RecipientType.CC);
			MailHelper.setRecipients(message, bcc, RecipientType.BCC);

			message.setSubject(subject);
			setText(text);

			setAttachments(attachments);

			message.setContent(mmp);
			Transport.send(message);

		} catch (MessagingException mse) {
			if (!silent) {
				throw new MailException(mse);
			}
			if (mse.getMessage().contains(SMTP_HOST_UNKNOWN)) {
				throw new MailException(SMTP_HOST_UNKNOWN);
			} else {
				mse.printStackTrace();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

	}

	public void sendMail(String subject, String text, String from, Object to, List<String> cc, List<String> bcc, String authentication, List<? extends AttachmentElement> attachments) {
		sendMail(subject, text, from, to, cc, bcc, authentication, attachments, true);
	}

	@SuppressWarnings("rawtypes")
	public void sendMail(String subject, String text, String from, Object to, List<String> cc, List<String> bcc, String authentication,
			List<? extends AttachmentElement> attachments, boolean silent) {
		try {
			inizializeContainer(authentication);

			MailHelper.setFromRecipient(message, from);
			MailHelper.setRecipients(message, to, RecipientType.TO);
			MailHelper.setRecipients(message, cc, RecipientType.CC);
			MailHelper.setRecipients(message, bcc, RecipientType.BCC);

			message.setSubject(subject);
			setText(text);

			setAttachmentsElements(attachments);

			message.setContent(mmp);
			Transport.send(message);

		} catch (MessagingException mse) {
			if (!silent) {
				throw new MailException(mse);
			}
			if (mse.getMessage().contains(SMTP_HOST_UNKNOWN)) {
				throw new MailException(SMTP_HOST_UNKNOWN);
			} else {
				mse.printStackTrace();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

	}

	/**
	 * 
	 * @param subject
	 * @param text
	 * @param from
	 * @param to
	 * @param defaultAuthentication
	 */
	public void sendMail(String subject, String text, String from, Object to, String authentication) {
		sendMail(subject, text, from, to, null, null, null, authentication, true);
	}

	
	public void sendMail(String subject, String text, String from, Object to, String authentication, boolean silent) {
		sendMail(subject, text, from, to, null, null, null, authentication, silent);
	}

	
	/**
	 * 
	 * @param subject
	 * @param text
	 * @param from
	 * @param to
	 * @param attachements
	 * @param defaultAuthentication
	 */
	public void sendMail(String subject, String text, String from, List<String> to, List<String> attachments, String authentication) {
		sendMail(subject, text, from, to, null, null, attachments, authentication, true);
	}

	/**
	 * 
	 * @param subject
	 * @param text
	 * @param from
	 * @param to
	 * @param attachments
	 * @param authentication
	 * @param silent
	 *          if true does not throw exception
	 */
	public void sendMail(String subject, String text, String from, List<String> to, List<String> attachments, String authentication, boolean silent) {
		sendMail(subject, text, from, to, null, null, attachments, authentication, silent);
	}

	@SuppressWarnings("rawtypes")
	public void sendMail(String subject, String text, String from, List<String> to, String authentication, List<? extends AttachmentElement> attachments) {
		sendMail(subject, text, from, to, null, null, authentication, attachments);
	}
	

	@SuppressWarnings("rawtypes")
	public void sendMail(String subject, String text, String from, List<String> to, String authentication, List<? extends AttachmentElement> attachments, boolean silent) {
		sendMail(subject, text, from, to, null, null, authentication, attachments, silent);
	}

	/**
	 * 
	 * @param subject
	 * @param text
	 * @param from
	 * @param to
	 * @param cc
	 * @param attachements
	 * @param defaultAuthentication
	 */
	public void sendMail(String subject, String text, String from, List<String> to, List<String> cc, List<String> attachements, String authentication) {
		sendMail(subject, text, from, to, cc, null, attachements, authentication);
	}


	public void sendMail(String subject, String text, String from, List<String> to, List<String> cc, List<String> attachements, String authentication, boolean silent) {
		sendMail(subject, text, from, to, cc, null, attachements, authentication, silent);
	}
	
	
	/**
	 * 
	 * @param msg
	 * @throws MailException
	 */
	public void sendMail(MimeMessage msg) {
		try {
			Transport.send(msg);

		} catch (MessagingException mse) {

		}
	}

	/**
	 * 
	 * @param defaultAuthentication
	 */
	protected void inizializeContainer(String authenticationKey) {

		ServerConfiguration configuration = Roma.component("ServerConfiguration");
		Session session = null;
		Properties props = new Properties();
		props.put(SMTP_HOST, configuration.getSmtp());
		if (configuration.getSmtpPort() != null) {
			props.put(SMTP_PORT, configuration.getSmtpPort());
		}
		if (configuration.getPop3Port() != null) {
			props.put(POP3_PORT, configuration.getPop3Port());
		}
		if (configuration.getImapPort() != null) {
			props.put(IMAP_PORT, configuration.getImapPort());
		}
		if (authenticationKey != null) {
			Authentication authentication = configuration.getAuthentications().get(authenticationKey);
			SMTPAuthenticator authenticator = new SMTPAuthenticator(authentication.getUserid(), authentication.getPassword());
			props.put(SMTP_AUTHORIZATION, true);
			session = Session.getDefaultInstance(props, authenticator);
		} else {
			session = Session.getDefaultInstance(props);
		}
		session.setDebug(configuration.getDebug());

		message = new MimeMessage(session);
		mmp = new MimeMultipart();

	}

	/**
	 * This method is responsible to add file attachments, if exist, to the email container
	 * 
	 * @param attachments
	 *          List of File
	 * @throws MessagingException
	 * @throws IOException
	 */

	protected void setAttachments(List<String> attachments) throws MessagingException, IOException {

		MimeBodyPart mbp = null;
		File file = null;

		if (attachments != null) {

			for (String attachment : attachments) {

				file = new File(attachment);

				DataSource source = new FileDataSource(file);

				mbp = new MimeBodyPart();
				mbp.setDataHandler(new DataHandler(source));
				mbp.setFileName(file.getName());

				mmp.addBodyPart(mbp);
			}

		}

	}

	@SuppressWarnings("rawtypes")
	protected void setAttachmentsElements(List<? extends AttachmentElement> attachments) throws MessagingException, IOException {
		if (attachments != null) {
			for (AttachmentElement element : attachments)
				if (element instanceof FileAttachmentElement) {
					setFileAttachment((FileAttachmentElement) element);
				} else if (element instanceof ByteArrayAttachmentElement) {
					setByteArrayAttachment((ByteArrayAttachmentElement) element);
				}
		}
	}

	protected void setByteArrayAttachment(ByteArrayAttachmentElement attachment) throws MessagingException, IOException {
		MimeBodyPart mbp = null;
		DataSource source = new ByteArrayDataSource(attachment.getFile(), attachment.getMimeType());
		mbp = new MimeBodyPart();
		mbp.setDataHandler(new DataHandler(source));
		mbp.setFileName(attachment.getFileName());
		mmp.addBodyPart(mbp);

	}

	protected void setFileAttachment(FileAttachmentElement attachment) throws MessagingException, IOException {

		MimeBodyPart mbp = null;

		DataSource source = new FileDataSource(attachment.getFile());

		mbp = new MimeBodyPart();
		mbp.setDataHandler(new DataHandler(source));
		mbp.setFileName(attachment.getFileName());

		mmp.addBodyPart(mbp);

	}

	protected abstract void setText(String text) throws MessagingException;

}
