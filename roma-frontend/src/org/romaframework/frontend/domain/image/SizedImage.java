package org.romaframework.frontend.domain.image;

import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.frontend.domain.misc.HtmlCssHelper;

public class SizedImage extends Image {

	protected int			width;
	protected boolean	selected;

	public SizedImage(String path, int width) {
		super(path);
		this.width = width;
		selected = false;
	}

	public void setSelected(boolean value) {
		this.selected = value;
	}

	@Override
	@ViewField(render = ViewConstants.RENDER_HTML, label = "")
	public String getPath() {
		return (selected) ? HtmlCssHelper.getHtmlImg(path, width + HtmlCssHelper.PX, HtmlCssHelper.AUTO, HtmlCssHelper.SEL_BORDER, HtmlCssHelper.SEL_PADDING,
				HtmlCssHelper.SEL_BACKGROUNDCOLOR) : HtmlCssHelper.getHtmlImg(path, width + HtmlCssHelper.PX, HtmlCssHelper.AUTO, HtmlCssHelper.STD_BORDER, HtmlCssHelper.STD_PADDING,
				HtmlCssHelper.STD_MARGIN, HtmlCssHelper.STD_BACKGROUNDCOLOR);
	}

}
