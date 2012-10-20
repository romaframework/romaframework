package org.romaframework.aspect.view.html.area;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.romaframework.aspect.view.form.ContentForm;
import org.romaframework.aspect.view.form.ViewComponent;
import org.romaframework.aspect.view.html.component.HtmlViewGenericComponent;
import org.romaframework.aspect.view.html.transformer.helper.TransformerHelper;
import org.romaframework.core.domain.type.TreeNodeLinkedHashMap;
import org.romaframework.core.schema.xmlannotations.XmlFormAreaAnnotation;

public class HtmlViewFormAreaInstance extends HtmlViewAbstractAreaInstance implements HtmlViewFormArea {

	protected List<HtmlViewGenericComponent>	components;
	protected HtmlViewScreenArea							screenArea;
	private final ContentForm									contentForm;
	protected String													type;

	public HtmlViewFormAreaInstance(final ContentForm form, final TreeNodeLinkedHashMap parent, final String name, final HtmlViewScreenArea screenArea) {
		super(parent, name);
		contentForm = form;
		this.screenArea = screenArea;
	}

	public HtmlViewFormAreaInstance(final ContentForm form, final HtmlViewFormAreaInstance iParentAreaInstance, final XmlFormAreaAnnotation iAreaTag,
			final HtmlViewScreenArea screenArea) {
		super(iParentAreaInstance, iAreaTag.getName());
		contentForm = form;
		this.screenArea = screenArea;
		// Set the size of the area
		areaSize = iAreaTag.getSize() != null ? iAreaTag.getSize() : 0;
		// Set the alignment of the area
		areaAlign = iAreaTag.getAlign() != null ? iAreaTag.getAlign() : null;
		// Set the style of the area
		areaStyle = iAreaTag.getStyle() != null ? iAreaTag.getStyle() : null;
		type = iAreaTag.getType();

		// BROWSE RECURSIVELY ALL CHILDREN
		for (final XmlFormAreaAnnotation areaDef : iAreaTag.getChildren()) {
			// value = filterNodeContent(areaDef.xmlText());
			final HtmlViewFormAreaInstance subArea = new HtmlViewFormAreaInstance(contentForm, this, areaDef, this.screenArea);

			if (areaDef.getName() != null) {
				addChild(areaDef.getName(), subArea);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.romaframework.aspect.view.html.area.HtmlViewFormArea#addComponent(org.romaframework.aspect.view.form.ViewComponent)
	 */
	public void addComponent(final HtmlViewGenericComponent component) {
		if (components == null) {
			components = new ArrayList<HtmlViewGenericComponent>();
		}
		components.add(component);
		setDirty(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.romaframework.aspect.view.html.area.HtmlViewRenderable#getHtmlId()
	 */
	public String getHtmlId() {
		if (parent == null) {
			return ((HtmlViewRenderable) contentForm).getHtmlId() + TransformerHelper.SEPARATOR + getName();
		} else {
			return ((HtmlViewRenderable) parent).getHtmlId() + TransformerHelper.SEPARATOR + getName();
		}
	}

	public HtmlViewScreenArea getScreenArea() {
		return screenArea;
	}

	public void setScreenArea(final HtmlViewScreenArea screenArea) {
		this.screenArea = screenArea;
		if (getChildren() != null) {
			for (final HtmlViewFormAreaInstance child : (Collection<HtmlViewFormAreaInstance>) getChildren()) {
				child.setScreenArea(screenArea);
			}
		}

	}

	public boolean removeComponent(final ViewComponent component) {
		setDirty(true);
		return components.remove(component);
	}

	public void replaceComponent(final ViewComponent oldComponent, final HtmlViewGenericComponent newComponent) {
		int pos = components.indexOf(oldComponent);
		components.remove(pos);
		components.add(pos, newComponent);
		setDirty(true);
	}

	public boolean validate() {
		boolean result = true;
		if (components != null) {
			for (final ViewComponent component : components) {
				if (component instanceof HtmlViewRenderable) {
					if (!((HtmlViewRenderable) component).validate()) {
						result = false;
					}
				}
			}
		}
		return result;
	}

	public List<HtmlViewRenderable> getComponents() {
		List<HtmlViewRenderable> renderables = super.getComponents();
		if (components != null)
			renderables.addAll(components);
		return renderables;
	}

	public void resetValidation() {
		if (components != null) {
			for (final ViewComponent component : components) {
				if (component instanceof HtmlViewRenderable) {
					((HtmlViewRenderable) component).resetValidation();
				}
			}
		}
	}

	public void setAreaSize(final Integer size) {
		areaSize = size;
	}

	public String getType() {
		return type;
	}

	public void clear() {
		if (components != null)
			components.clear();
	}

}
