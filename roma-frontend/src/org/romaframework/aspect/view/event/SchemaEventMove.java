package org.romaframework.aspect.view.event;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

import org.romaframework.aspect.view.feature.ViewFieldFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;
import org.romaframework.core.schema.SchemaParameter;

public abstract class SchemaEventMove extends SchemaEvent {

	private static final long	serialVersionUID	= -6853938367107400294L;

	public SchemaEventMove(SchemaField field, String iName) {
		super(field, iName, new ArrayList<SchemaParameter>());
	}

	protected void move(Object parent, final int displacement) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Object selectedValues = getSelectedValues(parent, this.field);

		if (selectedValues == null) {
			return;
		}

		if (!(selectedValues instanceof Collection<?> || selectedValues instanceof Object[])) {
			selectedValues = new Object[] { selectedValues };
		}

		Object selectedValue;
		if (selectedValues instanceof Object[]) {
			if (((Object[]) selectedValues).length == 1) {
				selectedValue = ((Object[]) selectedValues)[0];
			} else {
				return;
			}
		} else {
			selectedValue = selectedValues;
		}
		if (selectedValue == null) {
			return;
		}

		Object fieldValue = this.field.getValue(parent);
		SchemaHelper.moveElement(fieldValue, selectedValue, displacement);
		Roma.fieldChanged(parent, this.field.getName());
	}

	protected static Object getSelectedValues(Object parent, SchemaField listComponent) {
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

}
