package org.romaframework.frontend.domain.image;

import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.frontend.domain.misc.HtmlCssHelper;

public class Image {

	protected String	path;

	public Image(String path) {
		super();
		this.path = path;
	}

	@ViewField(render = ViewConstants.RENDER_HTML, label = "")
	public String getPath() {
		return HtmlCssHelper.getHtmlImg(path, HtmlCssHelper.AUTO, HtmlCssHelper.AUTO);
	}

}
