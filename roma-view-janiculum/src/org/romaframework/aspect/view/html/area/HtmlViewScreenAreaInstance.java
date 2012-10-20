/*
 * Copyright 2006 Giordano Maestro (giordano.maestro--at--assetdata.it)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.romaframework.aspect.view.html.area;

import java.util.ArrayList;
import java.util.List;

import org.romaframework.aspect.view.ViewAspect;
import org.romaframework.aspect.view.ViewHelper;
import org.romaframework.aspect.view.area.AreaComponent;
import org.romaframework.aspect.view.html.HtmlViewAspect;
import org.romaframework.aspect.view.html.component.HtmlViewConfigurableEntityForm;
import org.romaframework.aspect.view.html.component.HtmlViewContentForm;
import org.romaframework.aspect.view.html.component.HtmlViewGenericComponent;
import org.romaframework.aspect.view.html.screen.HtmlViewScreen;
import org.romaframework.aspect.view.html.transformer.helper.TransformerHelper;
import org.romaframework.core.Roma;
import org.romaframework.core.domain.type.TreeNode;
import org.romaframework.core.schema.xmlannotations.XmlFormAreaAnnotation;

/**
 * 
 * The instance
 * 
 * @author Giordano Maestro (giordano.maestro--at--assetdata.it)
 * 
 */
public class HtmlViewScreenAreaInstance extends HtmlViewAbstractAreaInstance implements HtmlViewScreenArea {

	protected HtmlViewContentForm	form;
	protected String							type;

	public HtmlViewScreenAreaInstance(final HtmlViewScreenAreaInstance parent, final String name) {
		super(parent, name);
	}

	public HtmlViewScreenAreaInstance(final HtmlViewScreenAreaInstance iParentAreaInstance, final XmlFormAreaAnnotation iAreaTag) {
		super(iParentAreaInstance, iAreaTag.getName());

		// Set the size of the area
		areaSize = iAreaTag.getSize() != null ? iAreaTag.getSize() : 0;
		// Set the alignment of the area
		areaAlign = iAreaTag.getAlign() != null ? iAreaTag.getAlign() : null;
		// Set the style of the area
		areaStyle = iAreaTag.getStyle() != null ? iAreaTag.getStyle() : null;
		type = iAreaTag.getType();

		if (iParentAreaInstance == null) {
			addChild(new HtmlViewScreenAreaInstance(this, HtmlViewScreen.POPUPS));
		}

		// BROWSE RECURSIVELY ALL CHILDREN
		for (final XmlFormAreaAnnotation areaDef : iAreaTag.getChildren()) {
			// value = filterNodeContent(areaDef.xmlText());
			final HtmlViewScreenAreaInstance subArea = new HtmlViewScreenAreaInstance(this, areaDef);

			if (areaDef.getName() != null) {
				addChild(areaDef.getName(), subArea);
			}
		}

	}

	/**
	 * Bind the pojo in the screen area
	 * 
	 * @param iPojo
	 */
	public void bindPojo(final Object iPojo) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Bind the pojo in the screen area
	 * 
	 * @param iPojo
	 */
	public void bindForm(final HtmlViewContentForm form) {
		if (form != null && form.getContent() != null) {
			if (this.form != null && this.form.getContent() != form.getContent()) {
				disposeForm(this.form);
				((HtmlViewAspect) Roma.aspect(ViewAspect.class)).releaseForm(this.form);
			}
		}
		this.form = form;
	}

	private void disposeForm(HtmlViewGenericComponent root) {
		if (root.getChildren() != null) {
			for (HtmlViewGenericComponent child : root.getChildren()) {
				if (child != null) {
					disposeForm(child);
				}
			}
		}
		ViewHelper.invokeOnDispose(root.getContent());
	}

	public HtmlViewContentForm getComponentInArea() {
		return form;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.romaframework.aspect.view.html.area.HtmlViewRenderable#getHtmlId()
	 */
	public String getHtmlId() {
		if (parent == null) {
			return "screen_" + getName();
		} else {
			return ((HtmlViewRenderable) parent).getHtmlId() + TransformerHelper.SEPARATOR + getName();
		}
	}

	public boolean validate() {
		// TODO test it!!!
		boolean result = true;
		if (form != null && form instanceof HtmlViewRenderable) {
			if (!((HtmlViewRenderable) form).validate()) {
				result = false;
			}
		}
		if (childrenMap != null) {
			for (final Object child : childrenMap.values()) {
				if (child instanceof HtmlViewRenderable) {
					if (!((HtmlViewRenderable) child).validate()) {
						result = false;
					}
				}
			}
		}
		return result;
	}

	public void resetValidation() {
		if (form != null && form instanceof HtmlViewRenderable) {
			((HtmlViewRenderable) form).resetValidation();
		}
		if (childrenMap != null) {
			for (final Object child : childrenMap.values()) {
				if (child instanceof HtmlViewRenderable) {
					((HtmlViewRenderable) child).resetValidation();
				}
			}
		}
	}

	public HtmlViewContentForm getForm() {
		return form;
	}

	public String getType() {
		return type;
	}

	public boolean isDirty() {
		if (super.isDirty()) {
			return true;
		}
		if (form != null && form instanceof HtmlViewConfigurableEntityForm) {
			return ((HtmlViewConfigurableEntityForm) form).isDirty();
		}
		return false;
	}

	@Override
	public List<HtmlViewRenderable> getComponents() {
		if (form != null) {
			List<HtmlViewRenderable> list = new ArrayList<HtmlViewRenderable>();
			list.add(form);
			return list;
		}
		return super.getComponents();
	}

	public void clear() {
		if (childrenMap != null) {
			for (TreeNode tn : childrenMap.values()) {
				if (tn instanceof AreaComponent) {
					((AreaComponent) tn).clear();
				}
			}
			childrenMap.clear();
		}
		if (form != null) {
			disposeForm(form);
			((HtmlViewAspect) Roma.aspect(ViewAspect.class)).releaseForm(this.form);
			form = null;
		}
	}
}
