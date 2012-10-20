package org.romaframework.aspect.view.html.component;

import org.romaframework.aspect.view.area.AreaComponent;
import org.romaframework.aspect.view.form.ContentForm;
import org.romaframework.aspect.view.form.ViewComponent;
import org.romaframework.aspect.view.html.area.HtmlViewFormArea;
import org.romaframework.aspect.view.html.area.HtmlViewScreenArea;
import org.romaframework.core.schema.SchemaClassElement;

public interface HtmlViewContentForm extends ContentForm, HtmlViewContentComponent {

	public void clearAreas();

	public HtmlViewFormArea getAreaForComponentPlacement();

	public HtmlViewScreenArea getScreenAreaObject();

	public void addChild(String fieldName, AreaComponent iAreaComponent, ViewComponent iComponent);

	public void removeFieldComponent(String fieldName);

	public void placeComponents();

	public HtmlViewContentComponent getFieldComponent(String name);

	public AreaComponent searchAreaForRendering(String featureLayout, SchemaClassElement iField);
	
	public void addExpandedChild(HtmlViewConfigurableExpandedEntityForm iChildForm);
}
