package org.romaframework.aspect.view.event;

import java.lang.reflect.InvocationTargetException;

import org.romaframework.core.schema.SchemaField;

public class SchemaEventUp extends SchemaEventMove {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7831692491545374957L;

	public SchemaEventUp(SchemaField field) {
		super(field, "up");
	}

	@Override
	public Object invokeFinal(Object iContent, Object[] params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		move(iContent, -1);
		return true;
	}
}
