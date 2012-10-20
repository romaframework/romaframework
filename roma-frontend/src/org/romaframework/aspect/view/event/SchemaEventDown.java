package org.romaframework.aspect.view.event;

import java.lang.reflect.InvocationTargetException;

import org.romaframework.core.schema.SchemaField;

public class SchemaEventDown extends SchemaEventMove {

	private static final long	serialVersionUID	= 8779185509503371831L;

	public SchemaEventDown(SchemaField field) {
		super(field, "down");
	}

	@Override
	public Object invokeFinal(Object iContent, Object[] params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		move(iContent, 1);
		return true;
	}
}
