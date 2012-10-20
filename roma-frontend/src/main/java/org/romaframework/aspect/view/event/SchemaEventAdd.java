package org.romaframework.aspect.view.event;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;

import org.romaframework.aspect.core.feature.CoreFieldFeatures;
import org.romaframework.aspect.flow.FlowAspect;
import org.romaframework.core.Roma;
import org.romaframework.core.binding.Bindable;
import org.romaframework.core.entity.EntityHelper;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;
import org.romaframework.core.schema.SchemaParameter;
import org.romaframework.core.schema.reflection.SchemaFieldReflection;
import org.romaframework.frontend.domain.binding.RuntimePair;
import org.romaframework.frontend.domain.crud.CRUDHelper;
import org.romaframework.frontend.domain.crud.CRUDInstance;
import org.romaframework.frontend.domain.crud.CRUDWorkingMode;
import org.romaframework.frontend.domain.wrapper.InstanceWrapper;
import org.romaframework.frontend.domain.wrapper.TextWrapperForm;

public class SchemaEventAdd extends SchemaEvent {

	private static final long	serialVersionUID	= 3961580751044485125L;

	public SchemaEventAdd(SchemaField field) {
		super(field, SchemaEvent.COLLECTION_ADD_EVENT, new ArrayList<SchemaParameter>());
	}

	@Override
	public Object invokeFinal(Object iContent, Object[] params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

		SchemaClass formClass = null;
		Object formInstance = null;
		if (SchemaHelper.isAssignableAs(field.getType(), Map.class)) {
			// TRY TO DETERMINE TYPES
			if (field instanceof SchemaFieldReflection) {
				SchemaFieldReflection fieldRef = (SchemaFieldReflection) field;
				SchemaClass[] genericClasses = fieldRef.getEmbeddedTypeGenerics();
				if (genericClasses != null && genericClasses.length == 2) {
					try {
						formInstance = new RuntimePair(genericClasses[0] != null ? genericClasses[0].newInstance() : null, genericClasses[1] != null ? genericClasses[1].newInstance() : null);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			} else
				formInstance = new RuntimePair(null, null);
		} else {
			SchemaClassDefinition iClass = field.getEmbeddedType();

			// if (iClass.getSchemaClass().isAssignableAs(ComposedEntity.class)) {
			// iClass = iClass.getField(ComposedEntity.NAME).getType();
			// }

			Boolean isEmbedded = field.getFeature(CoreFieldFeatures.EMBEDDED);
			if (!isEmbedded) {
				// TRY TO GET THE CRUD-SELECT CLASS, OTHERWISE THE CRUD-INSTANCE
				formClass = CRUDHelper.getCRUDSelect(iClass);
			}

			if (formClass == null)
				formClass = CRUDHelper.getCRUDInstance(iClass);

			try {
				if (formClass == null)
					// CRUD CREATE CLASS NOT FOUND: DISPLAY SIMPLE OBJECT
					formInstance = EntityHelper.createObject(null, iClass.getSchemaClass());
				else
					formInstance = SchemaHelper.createObject(formClass);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			if (formInstance instanceof Bindable)
				((Bindable) formInstance).setSource(iContent, field.getName());

			if (formInstance instanceof CRUDInstance)
				((CRUDInstance<?>) formInstance).setMode(CRUDWorkingMode.MODE_CREATE);
		}

		if (formClass == null) {
			Object instanceWrapper;

			if (formInstance instanceof String) {
				instanceWrapper = new TextWrapperForm(formInstance);
			} else {
				instanceWrapper = new InstanceWrapper(iContent, field, formInstance, InstanceWrapper.MODE_CREATE);
			}
			Roma.aspect(FlowAspect.class).popup(instanceWrapper);
			return instanceWrapper;
		}

		Roma.aspect(FlowAspect.class).popup(formInstance);
		return formInstance;
	}

}
