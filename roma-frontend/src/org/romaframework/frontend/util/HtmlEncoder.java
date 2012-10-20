package org.romaframework.frontend.util;

import java.util.Hashtable;

/**
 * Converts String in HTML mark-up replacing characters,
 * 
 * @author Giordano Maestro (giordano.maestro--at--assetdata.it) 12/nov/07
 * 
 */

public class HtmlEncoder {

	/**
	 * The list of characters to convert and convertion
	 */
	private static final String[]							ENTITIES					= { ">", "&gt;", "<", "&lt;", "&", "&amp;", "\"", "&quot;", "'", "&#039;", "\\", "&#092;", "\u00a9", "&copy;",
			"\u00ae", "&reg;"																			};

	private static Hashtable<String, String>	entityTableEncode	= null;

	/**
	 * Create an hashTable from ENTITIES
	 * 
	 */
	protected static synchronized void buildEntityTables() {
		entityTableEncode = new Hashtable<String, String>(ENTITIES.length);

		for (int i = 0; i < ENTITIES.length; i += 2) {
			if (!entityTableEncode.containsKey(ENTITIES[i])) {
				entityTableEncode.put(ENTITIES[i], ENTITIES[i + 1]);
			}
		}
	}

	/**
	 * 
	 * Converts a String to HTML by converting all special characters to HTML-entities.
	 * 
	 * @param s
	 *          The String to convert
	 * @return The converted String
	 */
	public static String encode(String s) {
		if (entityTableEncode == null) {
			buildEntityTables();
		}
		if (s == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder(s.length() * 2);
		char ch;
		for (int i = 0; i < s.length(); ++i) {
			ch = s.charAt(i);
			if ((ch >= 63 && ch <= 90) || (ch >= 97 && ch <= 122) || (ch == ' ')) {
				sb.append(ch);
			} else if (ch == '\n') {
				sb.append("\n");
			} else {
				String chEnc = (String) entityTableEncode.get(ch);
				if (chEnc != null) {
					sb.append(chEnc);
				} else {
					// Not 7 Bit use the unicode system
					sb.append("&#");
					sb.append(new Integer(ch).toString());
					sb.append(';');
				}
			}
		}
		return sb.toString();
	}

}
