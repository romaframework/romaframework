package org.json;

import java.io.IOException;
import java.io.Writer;

public class QuotedWriter extends Writer {

	private Writer	writer;

	private char		b	= 0;

	public QuotedWriter(Writer wrapped) {
		this.writer = wrapped;
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {

		char c = 0;
		String t;
		for (int i = off; i < len; i += 1) {
			if (c != 0) {
				b = c;
			}
			c = cbuf[i];
			switch (c) {
			case '\\':
			case '"':
				writer.append('\\');
				writer.append(c);
				break;
			case '/':
				if (b == '<') {
					writer.append('\\');
				}
				writer.append(c);
				break;
			case '\b':
				writer.append("\\b");
				break;
			case '\t':
				writer.append("\\t");
				break;
			case '\n':
				writer.append("\\n");
				break;
			case '\f':
				writer.append("\\f");
				break;
			case '\r':
				writer.append("\\r");
				break;
			default:
				if (c < ' ' || (c >= '\u0080' && c < '\u00a0') || (c >= '\u2000' && c < '\u2100')) {
					t = "000" + Integer.toHexString(c);
					writer.append("\\u" + t.substring(t.length() - 4));
				} else {
					writer.append(c);
				}
			}

		}
	}

	@Override
	public void flush() throws IOException {
		writer.flush();
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}

}
