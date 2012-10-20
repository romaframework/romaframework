package org.romaframework.aspect.view.html.component;

import java.util.Collection;

import org.romaframework.aspect.view.form.ViewComponent;
import org.romaframework.aspect.view.html.area.HtmlViewRenderable;
import org.romaframework.aspect.view.html.area.HtmlViewScreenArea;
import org.romaframework.core.schema.SchemaClassElement;

public interface HtmlViewGenericComponent extends HtmlViewRenderable, ViewComponent {

	public void setScreenArea(HtmlViewScreenArea screenArea);

	public SchemaClassElement getSchemaElement();

	public HtmlViewScreenArea getScreenAreaObject();

	public Collection<HtmlViewGenericComponent> getChildren();

	public void clearChildren();

	public Collection<HtmlViewGenericComponent> getChildrenFilled();

	public void clearComponents();

	public boolean isDirty();

	public void setDirty(boolean dirty);

}
