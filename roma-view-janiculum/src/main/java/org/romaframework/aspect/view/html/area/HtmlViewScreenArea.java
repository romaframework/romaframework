package org.romaframework.aspect.view.html.area;

import org.romaframework.aspect.view.html.component.HtmlViewContentForm;

public interface HtmlViewScreenArea extends HtmlViewArea {

	public void bindPojo(Object iPojo);

	public void bindForm(HtmlViewContentForm iPojo);

	public HtmlViewContentForm getForm();

}
