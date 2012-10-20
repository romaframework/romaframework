package org.romaframework.frontend.domain.wrapper;

import java.util.ArrayList;
import java.util.List;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewAction;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.core.Roma;
import org.romaframework.core.binding.Bindable;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;

public class EnumWrapper implements Bindable {

	private Object				parent;
	private String				fieldName;
	private SchemaField		field;

	@ViewField(render = ViewConstants.RENDER_SELECT, selectionField = "selectedElement", label = "")
	private List<Object>	list;

	public EnumWrapper() {

	}

	public EnumWrapper(Object parent, String fieldName) {
		setSource(parent, fieldName);
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<Object> list) {
		this.list = list;
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public Object getSelectedElement() {
		if (parent != null) {
			return (Object) SchemaHelper.getFieldValue(parent, fieldName);
		} else {
			return null;
		}
	}

	public void setSelectedElement(Object selectedElement) {
		if (parent != null) {
			SchemaHelper.setFieldValue(parent, fieldName, selectedElement);
		}
	}

	@Override
	public String toString() {
		return getSelectedElement() != null ? getSelectedElement().toString() : "";
	}

	public void setSource(Object parent, String fieldName) {
		SchemaClass schemaClass = Roma.schema().getSchemaClass(parent.getClass());
		this.parent = parent;
		this.fieldName = fieldName;
		this.field = schemaClass.getField(fieldName);
		if (!((Class<?>) this.field.getLanguageType()).isEnum()) {
			throw new IllegalArgumentException("trying to use an EnumWrapper on an object that is not an enum");
		}
		list = new ArrayList<Object>();
		for (Object elem : ((Class<?>) this.field.getLanguageType()).getEnumConstants()) {
			list.add(elem);
		}
		Roma.fieldChanged(this, "list");
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public Object getSourceObject() {
		return parent;
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public SchemaField getSourceField() {
		return this.field;
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public void refresh() {
		Roma.fieldChanged(this, "list");
	}

}
