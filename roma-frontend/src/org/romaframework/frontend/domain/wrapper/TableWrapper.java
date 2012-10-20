package org.romaframework.frontend.domain.wrapper;

import java.util.Collection;
import java.util.List;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.validation.ValidationException;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewClass;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.core.Roma;
import org.romaframework.core.domain.entity.ComposedEntity;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.frontend.domain.crud.CRUDHelper;

@ViewClass(render = ViewConstants.RENDER_OBJECTEMBEDDED)
public class TableWrapper<T> extends CollectionWrapper<T> {

	public TableWrapper(Class<T> iClass, Object iObject, String iSelectionField) {
		this(iClass, iObject, iSelectionField, false);
	}

	public TableWrapper(Class<T> iClass, Object iObject, String iSelectionField, boolean iLazy) {
		this(iClass, null);
		object = iObject;
		selectionFieldName = iSelectionField;
		lazy = iLazy;

		if (!lazy)
			load();
	}

	public TableWrapper(Class<T> iClass) {
		this(iClass, CRUDHelper.getCRUDListable(iClass), (Collection<T>) null);
	}

	public TableWrapper(Class<T> iClass, Collection<T> iElements) {
		this(iClass, CRUDHelper.getCRUDListable(iClass), iElements);
	}

	public TableWrapper(Class<T> iClass, Class<? extends ComposedEntity<?>> iListClass, Collection<T> iElements) {
		this(iClass, Roma.schema().getSchemaClass(iListClass), iElements);
	}

	public TableWrapper(Class<T> iClass, SchemaClass iListClass, Collection<T> iElements) {
		if (iClass == null)
			throw new IllegalArgumentException("Missed iClass parameter");

		if (iListClass == null)
			throw new IllegalArgumentException("Missed class to represent items. Assure you've generated the CRUD classes for the class '" + clazz + "'");

		clazz = iClass;
		listClass = iListClass;

		setDomainElements(iElements);
	}

	@ViewField(render = ViewConstants.RENDER_TABLEEDIT, selectionField = "selection", label = "", enabled = AnnotationConstants.FALSE)
	public List<? extends ComposedEntity<?>> getElements() {
		return elements;
	}

	public void validate() throws ValidationException {
	}
}
