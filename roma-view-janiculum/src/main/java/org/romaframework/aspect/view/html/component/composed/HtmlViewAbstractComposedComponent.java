package org.romaframework.aspect.view.html.component.composed;

import java.util.ArrayList;
import java.util.Collection;

import org.romaframework.aspect.view.form.ViewComponent;
import org.romaframework.aspect.view.html.area.HtmlViewScreenArea;
import org.romaframework.aspect.view.html.component.HtmlViewComposedComponent;
import org.romaframework.aspect.view.html.component.HtmlViewContentComponent;
import org.romaframework.aspect.view.html.component.HtmlViewContentComponentImpl;
import org.romaframework.aspect.view.html.component.HtmlViewGenericComponent;
import org.romaframework.core.schema.SchemaField;

public abstract class HtmlViewAbstractComposedComponent extends HtmlViewContentComponentImpl implements HtmlViewComposedComponent {

	protected Collection<HtmlViewGenericComponent>	components;

	public HtmlViewAbstractComposedComponent(final HtmlViewContentComponent containerComponent, final SchemaField schemaField, final Object content,
			final HtmlViewScreenArea screenArea) {
		super(containerComponent, schemaField, content, screenArea);

	}

	public void addComponent(final HtmlViewGenericComponent component) {
		addComponent(component, true);
	}

	public void addComponent(final HtmlViewGenericComponent component, boolean invokeOnShow) {
		if (components == null) {
			components = new ArrayList<HtmlViewGenericComponent>();
		}
		components.add(component);
	}

	public Collection<HtmlViewGenericComponent> getComponents() {
		return components;
	}

	@Override
	public void setScreenArea(final HtmlViewScreenArea screenArea) {
		this.screenArea = screenArea;

		if (components != null) {
			for (final ViewComponent component : components) {
				((HtmlViewGenericComponent) component).setScreenArea(screenArea);
			}
		}
	}

	public Collection<HtmlViewGenericComponent> getChildren() {
		if (components == null) {
			return new ArrayList<HtmlViewGenericComponent>();
		}
		return components;
	}

	@Override
	public void clearChildren() {
		super.clearChildren();
		if (components != null)
			components.clear();
	}
}
