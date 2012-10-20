package org.romaframework.frontend.domain.crud;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreField;
import org.romaframework.aspect.core.feature.CoreFieldFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.domain.entity.ComposedEntity;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaHelper;
import org.romaframework.frontend.domain.crud.CRUDFilter;
import org.romaframework.frontend.domain.crud.CRUDHelper;
import org.romaframework.frontend.domain.crud.CRUDInstance;
import org.romaframework.frontend.domain.crud.CRUDMain;

public class GenericCRUD<T> extends CRUDMain<T> {

	@CoreField(useRuntimeType = AnnotationConstants.TRUE)
	private CRUDFilter<T>	filter;
	private List<T>				result;
	private Class<T>			entityType;

	private GenericCRUD(Class<? extends ComposedEntity<?>> iListClass, Class<? extends ComposedEntity<?>> iInstanceClass) {
		super(iListClass, iInstanceClass, iInstanceClass, iInstanceClass);
	}

	private static <T> Class<?> getListable(Class<T> clazz) {
		SchemaClass inst = CRUDHelper.getCRUDListable(clazz);
		if (inst == null)
			return GenericListable.class;
		return (Class<?>) inst.getLanguageType();
	}

	private static <T> Class<?> getInstance(Class<T> clazz) {
		SchemaClass inst = CRUDHelper.getCRUDInstance(clazz);
		if (inst == null)
			return GenericInstance.class;
		return (Class<?>) inst.getLanguageType();
	}

	public GenericCRUD(Class<T> entity) {
		this((Class) getListable(entity), (Class) getInstance(entity));
		this.entityType = entity;
		Roma.setFeature(this, "result", CoreFieldFeatures.EMBEDDED_TYPE, Roma.schema().getSchemaClass(getListable(entity)));
		SchemaClass inst = CRUDHelper.getCRUDFilter(entity);
		try {
			if (inst == null) {
				filter = new GenericFilter<T>((T) entity.newInstance());
			} else
				filter = (CRUDFilter<T>) inst.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Error on filter instantiation", e);
		}

	}

	protected Object createInstance(Object... iArgs) throws InstantiationException, IllegalAccessException, InvocationTargetException, IllegalArgumentException, SecurityException,
			NoSuchMethodException {
		SchemaClass entityClass = Roma.schema().getSchemaClass(entityType);

		Object entityObject = SchemaHelper.createObject(entityClass, iArgs);

		Object createInstance = CRUDHelper.getCRUDObject(createClass, entityObject);

		if (createInstance instanceof CRUDInstance<?>) {
			((CRUDInstance<T>) createInstance).setRepository(repository);
			((CRUDInstance<?>) createInstance).setMode(CRUDInstance.MODE_CREATE);
		}

		displayInstanceForm(createInstance);
		return createInstance;
	}

	@Override
	public ComposedEntity<?> getFilter() {
		return filter;
	}

	@Override
	public List<T> getResult() {
		return this.result;
	}

	@Override
	public void setResult(Object iValue) {
		this.result = (List<T>) iValue;
	}

}
