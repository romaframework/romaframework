package org.romaframework.aspect.view.event;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.romaframework.aspect.flow.FlowAspect;
import org.romaframework.aspect.view.feature.ViewFieldFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.binding.Bindable;
import org.romaframework.core.entity.EntityHelper;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaParameter;
import org.romaframework.frontend.domain.crud.CRUDHelper;
import org.romaframework.frontend.domain.crud.CRUDInstance;
import org.romaframework.frontend.domain.crud.CRUDWorkingMode;
import org.romaframework.frontend.domain.wrapper.InstanceWrapper;

public class SchemaEventEdit extends SchemaEvent {

	private static final long	serialVersionUID	= 863239926207924259L;

	protected SchemaEventEdit(SchemaField iField, String iName, List<SchemaParameter> iOrderedParameters) {
		super(iField, iName, iOrderedParameters);
	}

	public SchemaEventEdit(SchemaField field) {
		super(field, SchemaEvent.COLLECTION_EDIT_EVENT, new ArrayList<SchemaParameter>());
	}

	@Override
	public Object invokeFinal(Object iContent, Object[] params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Object selectedValue = getSingleSelection(iContent);
		if (selectedValue != null) {
			SchemaClass clazz = Roma.schema().getSchemaClass(EntityHelper.getEntityObject(selectedValue).getClass());
			SchemaClass selectedValueClass = Roma.schema().getSchemaClass(selectedValue.getClass());

			// NO EVENT FOUND
			Object domainInstance;

			if (!selectedValueClass.equals(clazz)) {
				if (!selectedValueClass.isAssignableAs(clazz)) {
					domainInstance = EntityHelper.getEntityObject(selectedValue);
				} else
					domainInstance = selectedValue;

			} else {
				domainInstance = selectedValue;
			}

			// TRY TO DISPLAY CRUD CLASS IF ANY
			SchemaClass formClass = CRUDHelper.getCRUDInstance(EntityHelper.getEntityObject(selectedValue).getClass());

			Object formInstance;
			if (formClass == null)
				// CRUD CREATE CLASS NOT FOUND: DISPLAY SIMPLE OBJECT
				formInstance = domainInstance;
			else {
				try {
					formInstance = EntityHelper.createObject(domainInstance, formClass);
				} catch (InstantiationException e) {
					throw new RuntimeException(e);
				}
			}

			if (formInstance instanceof CRUDInstance) {
				((CRUDInstance<?>) formInstance).setMode(getOpenMode());
			}
			if (formInstance instanceof Bindable)
				((Bindable) formInstance).setSource(iContent, field.getName());

			if (formClass == null) {
				final InstanceWrapper instanceWrapper = new InstanceWrapper(iContent, field, formInstance, getOpenMode());
				Roma.aspect(FlowAspect.class).popup(instanceWrapper);
			} else
				Roma.aspect(FlowAspect.class).forward(formInstance);
		}
		return null;
	}

	protected int getOpenMode() {
		return CRUDWorkingMode.MODE_UPDATE;
	}

	protected Object getSingleSelection(Object parent) {
		Object selectedValue = null;
		Object selectedValues = getSelectedValues(parent);
		if (selectedValues instanceof Collection<?>) {
			if (((Collection<?>) selectedValues).size() == 1) {
				selectedValue = ((Collection<?>) selectedValues).iterator().next();
			} else {
				return null;
			}
		} else if (selectedValues instanceof Object[]) {
			if (((Object[]) selectedValues).length == 1) {
				selectedValue = ((Object[]) selectedValues)[0];
			} else {
				return null;
			}
		} else {
			selectedValue = selectedValues;
		}
		return selectedValue;
	}

	protected Object getSelectedValues(Object parent) {
		SchemaField listComponent = this.field;
		try {
			final String selectionFieldString = (String) listComponent.getFeature(

			ViewFieldFeatures.SELECTION_FIELD);
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
