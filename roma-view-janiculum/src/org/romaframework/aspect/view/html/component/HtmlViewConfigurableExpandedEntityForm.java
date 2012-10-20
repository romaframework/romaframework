package org.romaframework.aspect.view.html.component;

import java.io.Writer;

import org.romaframework.aspect.view.area.AreaComponent;
import org.romaframework.aspect.view.form.ViewComponent;
import org.romaframework.aspect.view.html.area.HtmlViewFormArea;
import org.romaframework.aspect.view.html.area.HtmlViewScreenArea;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaObject;

public class HtmlViewConfigurableExpandedEntityForm extends HtmlViewConfigurableEntityForm {

	public HtmlViewConfigurableExpandedEntityForm(final HtmlViewContentForm containerComponent, final SchemaObject schemaObject,
			final SchemaField field, final Object fieldValue, final HtmlViewScreenArea iScreenArea) {
		super(containerComponent, schemaObject, field, iScreenArea, null, null, null);
	}

	@Override
	public HtmlViewFormArea getAreaForComponentPlacement() {
		if (containerComponent instanceof HtmlViewConfigurableExpandedEntityForm) {
			return ((HtmlViewConfigurableExpandedEntityForm) containerComponent).getAreaForComponentPlacement();
		} else {
			return ((HtmlViewConfigurableEntityForm) containerComponent).getRootArea();
		}
	}

	public void addChild(final String fieldName, final AreaComponent iAreaComponent, final ViewComponent iComponent) {
		if (containerComponent instanceof HtmlViewContentForm) {
			Object fieldComponent = containerComponent.getFieldComponent(fieldName);
			if (fieldComponent == null) {
				((HtmlViewContentForm) containerComponent).addChild(fieldName, iAreaComponent, (HtmlViewGenericComponent) iComponent);
			} else {
				if (containerComponent.getContainerComponent() instanceof HtmlViewConfigurableExpandedEntityForm) {
					// Bla bla bla
				}
			}
		}
	}

	@Override
	public HtmlViewContentComponent getFieldComponent(final String name) {
		try {
			return (HtmlViewContentComponent) containerComponent.getFieldComponent(name);
		} catch (final ClassCastException e) {
			log.error("The requested field " + name + " could not be a field.");
		}
		return null;
	}

	@Override
	public void render(Writer writer) {
	}

}
