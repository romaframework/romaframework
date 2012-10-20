package org.romaframework.aspect.view.html.transformer;

import java.io.IOException;
import java.io.Writer;

import org.romaframework.aspect.view.html.area.HtmlViewRenderable;
import org.romaframework.aspect.view.html.component.HtmlViewContentComponent;
import org.romaframework.aspect.view.html.constants.TransformerConstants;
import org.romaframework.aspect.view.html.transformer.helper.TransformerHelper;

public abstract class AbstractHtmlViewTransformer implements Transformer {

	protected static TransformerHelper	helper	= TransformerHelper.getInstance();

	public static String getComponentLabel(final HtmlViewContentComponent contentComponent, final String label) {
		if (label == null || label.length() == 0) {
			return "";// TODO NOBODY SHOULD ARRIVE HERE!!!
		}
		return "<label for=\"" + helper.getHtmlId(contentComponent, TransformerConstants.PART_CONTENT) + "\" id=\""
				+ helper.getHtmlId(contentComponent, TransformerConstants.PART_LABEL) + "\" class=\""
				+ helper.getHtmlClass(contentComponent.getTransformer().toString(), TransformerConstants.PART_LABEL, contentComponent) + "\" >" + label + "</label>\n";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.romaframework.aspect.view.html.transformer.Transformer#transform(org.romaframework.aspect.view.form.ViewComponent)
	 */
	public void transform(final HtmlViewRenderable component, Writer writer) throws IOException {
		transformPart(component, TransformerConstants.PART_ALL, writer);
	}

}
