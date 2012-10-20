package org.romaframework.frontend.domain.query;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.flow.FlowAspect;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.core.Roma;
import org.romaframework.core.config.Refreshable;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaField;

public class AttributeChooser implements Refreshable {

	@ViewField(render = ViewConstants.RENDER_TABLEEDIT, enabled = AnnotationConstants.FALSE, label = "")
	private List<StringChooser>	list;

	@ViewField(visible = AnnotationConstants.FALSE)
	private Object							sourceObjectzzz;
	@ViewField(visible = AnnotationConstants.FALSE)
	private String							sourceField;

	private Class<?>						clazz;

	public AttributeChooser(Class<?> clazz, Object iSourceObject, String iSourceFieldName) {
		setSource(iSourceObject, iSourceFieldName);
		this.clazz = clazz;
		list = new ArrayList<StringChooser>();
		list.add(new StringChooser(this, 0, getFields(this.clazz)));
	}

	public void fieldChanged(int position) {
		if (position < 0 || position >= list.size())
			return;
		while (list.size() > position + 1)
			list.remove(position + 1);
		if (list.get(position).getSelectedString() != null)
			list.add(new StringChooser(this, position + 1, getFields(getNextClass())));
		refresh();
	}

	private Class<?> getNextClass() {
		Class<?> tmpClass = this.clazz;
		try {
			for (StringChooser chooser : list) {
				if (chooser.getSelectedString() == null)
					return tmpClass;
				tmpClass = tmpClass.getDeclaredField(chooser.getSelectedString()).getType();
			}
		} catch (Exception e) {
			return null;
		}
		return tmpClass;
	}

	public void refresh() {
		Roma.fieldChanged(this, "list");
	}

	private List<String> getFields(Class<?> clazz) {
		List<String> result = new ArrayList<String>();
		if (clazz == null)
			return result;
		try {
			SchemaClass schema = Roma.schema().getSchemaClass(clazz);
			Map<String, SchemaField> fields = schema.getFields();
			if (fields != null)
				for (String field : fields.keySet()) {
					result.add(field);
				}
		} catch (Exception e) {
		}

		return result;
	}

	public void ok() {
		StringBuilder fieldName = new StringBuilder();
		for (StringChooser chooser : list) {
			if (chooser.getSelectedString() != null) {
				if (fieldName.length() > 0)
					fieldName.append(".");
				fieldName.append(chooser.getSelectedString());
			} else {
				break;
			}
		}

		try {
			String setterName = "set" + this.sourceField.substring(0, 1).toUpperCase() + this.sourceField.substring(1);
			this.sourceObjectzzz.getClass().getMethods();
			Method setter = sourceObjectzzz.getClass().getMethod(setterName, new Class<?>[] { String.class });
			setter.invoke(this.sourceObjectzzz, fieldName.toString());
		} catch (Exception e) {
			try {
				String setterName = "set" + this.sourceField.substring(0, 1).toUpperCase() + this.sourceField.substring(1);
				Method setter = sourceObjectzzz.getClass().getMethod(setterName, new Class<?>[] { Object.class });
				setter.invoke(this.sourceObjectzzz, fieldName.toString());
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		if (sourceObjectzzz instanceof Refreshable) {
			((Refreshable) sourceObjectzzz).refresh();
		}
		Roma.aspect(FlowAspect.class).back();
	}

	public List<StringChooser> getList() {
		return list;
	}

	public String getSourceField() {
		return this.sourceField;
	}

	public Object getSourceObjectzzz() {
		return this.sourceObjectzzz;
	}

	private void setSource(Object iSourceObject, String iSourceFieldName) {
		this.sourceObjectzzz = iSourceObject;
		this.sourceField = iSourceFieldName;
	}

}
