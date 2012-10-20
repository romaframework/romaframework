package org.romaframework.aspect.view.html.transformer.jsp.directive;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;

import org.romaframework.aspect.view.FormatHelper;
import org.romaframework.aspect.view.html.HtmlViewAspectHelper;
import org.romaframework.aspect.view.html.HtmlViewCodeBuffer;
import org.romaframework.aspect.view.html.area.HtmlViewRenderable;
import org.romaframework.aspect.view.html.component.HtmlViewAbstractContentComponent;
import org.romaframework.aspect.view.html.component.HtmlViewContentComponent;
import org.romaframework.aspect.view.html.css.StyleBuffer;
import org.romaframework.core.Roma;

public class JspTransformerHelper {

	public static void addCss(String selector, String property, String value) throws IOException {
		final StyleBuffer jsBuffer = HtmlViewAspectHelper.getCssBuffer();
		if (selector == null || property == null) {
			throw new IllegalArgumentException();
		}

		if (value == null || value.trim().length() == 0) {
			if (jsBuffer.containsRule(selector)) {
				jsBuffer.removeRule(selector, property);
			}

		} else {
			value = value.trim();
			if (!jsBuffer.containsRule(selector)) {
				jsBuffer.createRules(selector);
			}
			jsBuffer.addRule(selector, property, value);
		}
	}

	public static void addJs(String id, String script) {
		final HtmlViewCodeBuffer jsBuffer = HtmlViewAspectHelper.getJsBuffer();
		jsBuffer.setScript(id, script);
	}

	public static final void delegate(HtmlViewRenderable component, String part, Writer writer) throws IOException {
		if (part == null || part.toString().length() == 0) {
			component.render(writer);
		} else {
			component.renderPart(part.toString(), writer);
		}
	}

	public static String raw(HtmlViewRenderable component) {
		String toRender = "";

		Object content = null;
		if (component instanceof HtmlViewContentComponent) {
			if (((HtmlViewContentComponent) component).getContent() != null) {
				content = ((HtmlViewContentComponent) component).getContent();
			}
		} else if (component instanceof HtmlViewAbstractContentComponent) {
			if (((HtmlViewAbstractContentComponent) component).getContent() != null) {
				content = ((HtmlViewAbstractContentComponent) component).getContent();
			}
		}
		if (component instanceof HtmlViewContentComponent) {
			toRender = FormatHelper.format(content, ((HtmlViewContentComponent) component).getSchemaField());
		} else {
			if (content != null) {
				if (content instanceof Date) {
					toRender = Roma.i18n().getDateFormat().format(content);
				} else {
					toRender = content.toString();
				}
			}

		}

		return toRender;

	}
}
