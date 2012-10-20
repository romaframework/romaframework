package org.romaframework.aspect.view.html.transformer.plain;

import java.io.IOException;
import java.io.Writer;

import org.romaframework.aspect.view.html.area.HtmlViewBinder;
import org.romaframework.aspect.view.html.area.HtmlViewRenderable;
import org.romaframework.aspect.view.html.binder.NullBinder;
import org.romaframework.aspect.view.html.screen.HtmlViewScreen;
import org.romaframework.aspect.view.html.transformer.AbstractHtmlViewTransformer;
import org.romaframework.aspect.view.html.transformer.Transformer;

public class HtmlViewScreenTransformer extends AbstractHtmlViewTransformer implements Transformer {

	public HtmlViewBinder getBinder(HtmlViewRenderable renderable) {
		return NullBinder.getInstance();
	}

	@Override
	public void transform(final HtmlViewRenderable component, Writer writer) throws IOException {
		final HtmlViewScreen screen = (HtmlViewScreen) component;

		writer.write("<?xml version=\"1.0\"?>");
		writer.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
		writer.write("<head>");
		writer.write("<title></title>");
		writer.write("</head>");
		writer.write("<body>");
		screen.getRootArea().render(writer);
		writer.write("</body>");

	}

	public void transformPart(final HtmlViewRenderable component, final String part, Writer writer) throws IOException {
		transform(component, writer);
	}

	@Override
	public String toString() {
		return "screen";
	}

	public String getType() {
		return Transformer.PRIMITIVE;
	}
}
