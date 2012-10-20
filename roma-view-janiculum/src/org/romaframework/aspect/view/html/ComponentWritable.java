package org.romaframework.aspect.view.html;

import java.io.IOException;
import java.io.Writer;

import org.json.JSONException;
import org.json.JSONWriteable;
import org.romaframework.aspect.view.html.area.HtmlViewRenderable;

public class ComponentWritable implements JSONWriteable {

	private HtmlViewRenderable	renderable;

	public ComponentWritable(HtmlViewRenderable renderable) {
		this.renderable = renderable;
	}

	public Writer write(Writer writer) throws JSONException {
		try {
			if (renderable != null) {
				renderable.render(writer);
			}
		} catch (IOException e) {
			throw new JSONException(e);
		}
		return writer;
	}

}
