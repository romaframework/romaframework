package org.romaframework.module.mail.javamail;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailHelper {

	public static void setRecipients(MimeMessage iContainer, Object iRecipients, RecipientType iType) throws MessagingException {
		if (iRecipients != null) {
			iContainer.setRecipients(iType, setAddressesFromList(iRecipients));
		}
	}

	public static void setFromRecipient(MimeMessage iContainer, String iRecipient) throws MessagingException {
		iContainer.setFrom(new InternetAddress(iRecipient));
	}

	// private static InternetAddress[] setAddressesFromList(List<String> recipients) {
	// InternetAddress addresses[] = new InternetAddress[recipients.size()];
	// int i = 0;
	// for (String recipient : recipients) {
	// addresses[i] = new InternetAddress();
	// addresses[i].setAddress(recipient);
	// i++;
	// }
	// return addresses;
	// }

	private static InternetAddress[] setAddressesFromList(Object iRecipients) {

		InternetAddress addresses[] = null;

		if (iRecipients instanceof List<?>) {
			List<String> recipients = (ArrayList<String>) iRecipients;

			addresses = new InternetAddress[recipients.size()];
			int i = 0;
			for (String recipient : recipients) {
				addresses[i] = new InternetAddress();
				addresses[i].setAddress(recipient);
				i++;
			}
		} else {
			addresses = new InternetAddress[1];
			addresses[0] = new InternetAddress();
			addresses[0].setAddress(iRecipients.toString());
		}

		return addresses;
	}
}
