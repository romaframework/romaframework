package org.romaframework.aspect.view.event;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.romaframework.aspect.core.feature.CoreFieldFeatures;
import org.romaframework.aspect.flow.FlowAspect;
import org.romaframework.core.Roma;
import org.romaframework.core.binding.Bindable;
import org.romaframework.core.entity.EntityHelper;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaParameter;
import org.romaframework.frontend.domain.crud.CRUDHelper;
import org.romaframework.frontend.domain.crud.CRUDInstance;
import org.romaframework.frontend.domain.wrapper.InstanceWrapper;

public class SchemaEventOpen extends SchemaEvent {

	private static final long	serialVersionUID	= -1527990754566339469L;

	public SchemaEventOpen(SchemaField field) {
		super(field, "open", new ArrayList<SchemaParameter>());
	}

	@Override
	public Object invokeFinal(Object iContent, Object[] params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		SchemaEvent defaultEvent;
		if ((defaultEvent = field.getEvent(SchemaEvent.DEFAULT_EVENT_NAME)) != null) {
			return defaultEvent.invoke(iContent, params);
		}

		Boolean embedded = (Boolean) field.getFeature(CoreFieldFeatures.EMBEDDED);
		Object instance;
		if (embedded != null && embedded) {
			instance = createInstance(iContent, field);
		} else {
			instance = createSelect(iContent, field);
		}
		Roma.aspect(FlowAspect.class).popup(instance);
		return instance;
	}

	public Object createSelect(Object owner, SchemaField field) throws InvocationTargetException {
		SchemaClass selectClass = CRUDHelper.getCRUDSelect(field.getType().getSchemaClass());
		if (selectClass == null) {
			return createInstance(owner, field);
		}
		// TODO :BIND VALUE TO SELECT.
		// Object value = field.getValue(owner);
		try {

			Object select = EntityHelper.createObject(null, selectClass);
			if (select instanceof Bindable)
				((Bindable) select).setSource(owner, field.getName());
			return select;
		} catch (Exception e) {
			throw new InvocationTargetException(e);
		}
	}

	/**
	 * Create An instance from an field of an object if exist otherwise create the default instance wrapper.
	 * 
	 * @param owner
	 *          the owner of field.
	 * @param field
	 *          the field.
	 * @return the new instance of crud instance.
	 * @throws InvocationTargetException
	 */
	public Object createInstance(Object owner, SchemaField field) throws InvocationTargetException {
		Object value = field.getValue(owner);

		int mode = value == null ? CRUDInstance.MODE_CREATE : CRUDInstance.MODE_UPDATE;

		SchemaClass instanceClass = CRUDHelper.getCRUDInstance(field.getType().getSchemaClass());
		if (instanceClass == null) {
			return new InstanceWrapper(owner, field, value, mode);
		}

		try {
			Object instance = EntityHelper.createObject(value, instanceClass);
			if (instance instanceof Bindable)
				((Bindable) instance).setSource(owner, field.getName());
			if (instance instanceof CRUDInstance)
				((CRUDInstance<?>) instance).setMode(mode);
			return instance;
		} catch (Exception e) {
			throw new InvocationTargetException(e);
		}

	}
}
