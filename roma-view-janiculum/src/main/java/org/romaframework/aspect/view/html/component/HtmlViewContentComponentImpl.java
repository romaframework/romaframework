package org.romaframework.aspect.view.html.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.romaframework.aspect.view.html.area.HtmlViewRenderable;
import org.romaframework.aspect.view.html.area.HtmlViewScreenArea;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;
import org.romaframework.core.schema.SchemaObject;

public class HtmlViewContentComponentImpl extends HtmlViewAbstractContentComponent implements HtmlViewContentComponent {

	private static final ArrayList<HtmlViewGenericComponent>	constant	= new ArrayList<HtmlViewGenericComponent>();
	private Object																						additionalInfo;

	public HtmlViewContentComponentImpl(final HtmlViewContentComponent containerComponent, final SchemaField schemaField,
			final Object content, final HtmlViewScreenArea screenArea) {
		super(containerComponent, schemaField, content, screenArea);
	}

	public void resetValidation() {
		if (!isValid())
			setDirty(true);
		setValid(true);
	}

	public boolean hasLabel() {
		return true;
	}

	public SchemaObject getSchemaInstance() {

		return null;
	}

	public void setMetaDataSchema(SchemaObject schemaObject) {

	}

	public Collection<HtmlViewGenericComponent> getChildren() {
		return constant;
	}

	public String getHtmlId() {
		return ((HtmlViewRenderable) containerComponent).getHtmlId() + SEPARATOR + getSchemaElement().getName();
	}

	public Set<Integer> selectedIndex() {
		Set<Integer> result = new HashSet<Integer>();
		Object parent = this.containerComponent.getContent();
		if (parent == null) {
			return result;
		}

		String selectionFieldName = getSelectionFieldName();
		if (selectionFieldName == null) {
			return result;
		}
		List<?> elements = orderedContent();
		Object selectionFieldValue = null;
		if (!("".equals(selectionFieldName))) {
			selectionFieldValue = SchemaHelper.getFieldValue(getSelectionSchemaClassDefinition(), selectionFieldName, parent);
		}
		if (selectionFieldValue != null) {
			Object[] selected = null;
			if (isSingleSelection()) {
				selected = new Object[] { selectionFieldValue };
			} else {
				selected = SchemaHelper.getObjectArrayForMultiValueObject(selectionFieldValue);
			}

			for (Object obj : selected) {
				if (elements.contains(obj)) {
					result.add(elements.indexOf(obj));
				}
			}

		}
		return result;
	}

	public Collection<HtmlViewGenericComponent> getChildrenFilled() {
		return getChildren();
	}

	public Object getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(Object additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public void clearChildren() {
		constant.clear();
	}

}
