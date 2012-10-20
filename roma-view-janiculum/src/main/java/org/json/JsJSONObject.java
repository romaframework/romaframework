package org.json;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

import org.romaframework.aspect.view.html.HtmlViewAspectHelper;
import org.romaframework.aspect.view.html.HtmlViewCodeBuffer;

public class JsJSONObject extends JSONObject {

	@Override
	public Writer write(Writer writer) throws JSONException {
		try {
			boolean b = false;
			Iterator<String> keys = keys();
			writer.write('{');

			while (keys.hasNext()) {
				if (b) {
					writer.write(',');
				}
				Object k = keys.next();
				writer.write(quote(k.toString()));
				writer.write(':');
				Object v = this.map.get(k);
				if (v instanceof JSONObject) {
					((JSONObject) v).write(writer);
				} else if (v instanceof JSONArray) {
					((JSONArray) v).write(writer);
				} else if (v instanceof JSONWriteable) {
					writer.write('"');
					((JSONWriteable) v).write(new QuotedWriter(writer));
					writer.write('"');
				} else {
					writer.write(valueToString(v));
				}
				b = true;
			}

			HtmlViewCodeBuffer codeBuffer = HtmlViewAspectHelper.getJsBuffer();
			if (codeBuffer != null) {
				if (b) {
					writer.write(',');
				}
				writer.write(quote("romajs"));
				writer.write(':');
				writer.write(valueToString(codeBuffer.getBufferContent()));
			}

			writer.write('}');
			return writer;
		} catch (IOException e) {
			throw new JSONException(e);
		}
	}
}