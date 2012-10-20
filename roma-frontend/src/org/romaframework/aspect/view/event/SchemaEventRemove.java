package org.romaframework.aspect.view.event;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

import org.romaframework.aspect.view.feature.ViewFieldFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaParameter;

public class SchemaEventRemove extends SchemaEvent {

	private static final long	serialVersionUID	= 3961580751044485125L;

	public SchemaEventRemove(SchemaField field) {
		super(field, COLLECTION_REMOVE_EVENT, new ArrayList<SchemaParameter>());
	}

	@Override
	public Object invokeFinal(Object iContent, Object[] params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Object selectedValues = getSelectedValues(iContent);
		if (selectedValues == null) {
			return null;
		}
		if (!(selectedValues instanceof Collection<?> || selectedValues instanceof Object[])) {
			selectedValues = new Object[] { selectedValues };
		}

		final Collection<?> list = (Collection<?>) this.field.getValue(iContent);
		if (selectedValues instanceof Object[]) {
			final Object[] tmpSelValues = (Object[]) selectedValues;
			for (final Object o : tmpSelValues) {
				list.remove(o);
			}
		} else {
			list.remove(selectedValues);
		}

		getSelectionField(this.field).setValue(iContent, null);
		Roma.fieldChanged(iContent, this.field.getName());
		return null;
	}

	protected Object getSelectedValues(Object parent) {
		SchemaField listComponent = this.field;
		try {
			final String selectionFieldString = (String) listComponent.getFeature(ViewFieldFeatures.SELECTION_FIELD);
			if (selectionFieldString == null) {
				return null;
			}
			final SchemaField selectionField = listComponent.getEntity().getField(selectionFieldString);
			if (selectionField == null) {
				return null;
			}
			return selectionField.getValue(parent);
		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	protected static SchemaField getSelectionField(SchemaField schemaField) {
		final String selectionField = (String) schemaField.getFeature(ViewFieldFeatures.SELECTION_FIELD);
		if (selectionField == null) {
			return null;
		}
		return schemaField.getEntity().getField(selectionField);
	}
}
