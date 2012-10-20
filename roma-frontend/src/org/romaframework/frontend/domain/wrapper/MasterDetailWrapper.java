package org.romaframework.frontend.domain.wrapper;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreField;
import org.romaframework.aspect.validation.CustomValidation;
import org.romaframework.aspect.validation.ValidationException;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewClass;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.aspect.view.feature.ViewFieldFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.domain.entity.ComposedEntity;
import org.romaframework.core.entity.EntityHelper;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.frontend.domain.crud.CRUDHelper;

@ViewClass(render = ViewConstants.RENDER_OBJECTEMBEDDED)
public class MasterDetailWrapper<T> extends CollectionWrapper<T> {
	@CoreField(useRuntimeType = AnnotationConstants.TRUE)
	@ViewField(render = ViewConstants.RENDER_OBJECTEMBEDDED)
	protected ComposedEntity<T>	detail;

	protected static Log				log	= LogFactory.getLog(MasterDetailWrapper.class);

	public MasterDetailWrapper(Class<T> iClass) {
		this(iClass, null);
	}

	public MasterDetailWrapper(Class<T> iClass, Object iObject, String iSelectionField) {
		this(iClass, iObject, iSelectionField, false);
	}

	public MasterDetailWrapper(Class<T> iClass, Object iObject, String iSelectionField, boolean iLazy) {
		this(iClass, null);
		object = iObject;
		selectionFieldName = iSelectionField;
		lazy = iLazy;

		if (!lazy)
			load();
	}

	public MasterDetailWrapper(Class<T> iClass, Collection<T> iElements) {
		this(iClass, CRUDHelper.getCRUDInstance(iClass), CRUDHelper.getCRUDListable(iClass), iElements);
	}

	public MasterDetailWrapper(Class<T> iClass, Class<? extends ComposedEntity<?>> iDetailClass, Class<? extends ComposedEntity<?>> iListClass, Collection<T> iElements) {
		this(iClass, Roma.schema().getSchemaClass(iDetailClass), Roma.schema().getSchemaClass(iListClass), iElements);
	}

	public MasterDetailWrapper(Class<T> iClass, SchemaClass iDetailClass, SchemaClass iListClass, Collection<T> iElements) {
		if (iClass == null)
			throw new IllegalArgumentException("Missed iClass parameter");

		if (iListClass == null)
			throw new IllegalArgumentException("Missed class to represent items. Assure you've generated the CRUD classes for the class '" + clazz + "'");

		clazz = iClass;
		listClass = iListClass;

		try {
			detail = (ComposedEntity<T>) iDetailClass.newInstance();
		} catch (Exception e) {
			log.error("[MasterDetail] Error on creating detail class of type: " + iDetailClass, e);
		}

		setDomainElements(iElements);
	}

	public void validate() throws ValidationException {
		if (detail instanceof CustomValidation)
			((CustomValidation) detail).validate();

		// Roma.fieldChanged(this, "elements");
	}

	public ComposedEntity<T>[] getSelection() {
		return super.getSelection();
	}

	@Override
	public void setSelection(ComposedEntity<T>[] iSelection) {
		super.setSelection(iSelection);

		if (iSelection != null && iSelection.length > 0)
			detail.setEntity(iSelection[0].getEntity());

		Roma.fieldChanged(this, "detail");
	}

	public MasterDetailWrapper<T> setInlineEdit(boolean iValue) {
		Roma.setFeature(this, "elements", ViewFieldFeatures.RENDER, iValue ? ViewConstants.RENDER_TABLEEDIT : ViewConstants.RENDER_TABLE);
		return this;
	}

	@ViewField(render = ViewConstants.RENDER_TABLE, selectionField = "selection", label = "", enabled = AnnotationConstants.FALSE)
	public List<? extends ComposedEntity<T>> getElements() {
		return elements;
	}

	public ComposedEntity<T> getDetail() {
		return detail;
	}

	@SuppressWarnings("unchecked")
	public void add(T iNewObject) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		ComposedEntity<T> wrappedObject = (ComposedEntity<T>) EntityHelper.createObject(iNewObject, listClass);
		((List<ComposedEntity<T>>) elements).add(wrappedObject);
		domainElements.add(iNewObject);
		setSelection(wrappedObject);
		Roma.fieldChanged(this, "elements");
	}
}
