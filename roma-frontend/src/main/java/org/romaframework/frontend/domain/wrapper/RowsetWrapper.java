package org.romaframework.frontend.domain.wrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.validation.ValidationException;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewClass;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.core.Roma;
import org.romaframework.core.config.GenericEventListener;
import org.romaframework.core.domain.entity.ComposedEntity;
import org.romaframework.core.entity.EntityHelper;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.frontend.domain.crud.CRUDHelper;

@ViewClass(render = ViewConstants.RENDER_OBJECTEMBEDDED)
public class RowsetWrapper<T> extends CollectionWrapper<T> implements GenericEventListener {
	protected static Log	log	= LogFactory.getLog(RowsetWrapper.class);

	public RowsetWrapper(Class<T> iClass, Object iObject, String iSelectionField) {
		this(iClass, iObject, iSelectionField, false);
	}

	public RowsetWrapper(Class<T> iClass, Object iObject, String iSelectionField, boolean iLazy) {
		this(iClass, null);
		object = iObject;
		selectionFieldName = iSelectionField;
		lazy = iLazy;

		if (!lazy)
			load();
	}

	public RowsetWrapper(Class<T> iClass) {
		this(iClass, CRUDHelper.getCRUDListable(iClass), (Collection<T>) null);
	}

	public RowsetWrapper(Class<T> iClass, Collection<T> iElements) {
		this(iClass, CRUDHelper.getCRUDListable(iClass), iElements);
	}

	public RowsetWrapper(Class<T> iClass, Class<? extends ComposedEntity<?>> iListClass, Collection<T> iElements) {
		this(iClass, Roma.schema().getSchemaClass(iListClass), iElements);
	}

	public RowsetWrapper(Class<T> iClass, SchemaClass iListClass, Collection<T> iElements) {
		if (iClass == null)
			throw new IllegalArgumentException("Missed iClass parameter");

		if (iListClass == null)
			throw new IllegalArgumentException("Missed class to represent items. Assure you've generated the CRUD classes for the class '" + clazz + "'");

		clazz = iClass;
		listClass = iListClass;

		setDomainElements(iElements);
	}

	@ViewField(render = ViewConstants.RENDER_ROWSET, selectionField = "selection", label = "", enabled = AnnotationConstants.FALSE)
	public List<? extends ComposedEntity<T>> getElements() {
		return elements;
	}

	public void validate() throws ValidationException {
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setDomainElements(Collection<T> iElements) {
		domainElements = iElements;

		if (iElements == null)
			elements = null;
		else
			try {
				List<? extends ComposedEntity<?>> tempElements = EntityHelper.createComposedEntityList(iElements, listClass);
				elements = new ArrayList<ComposedEntity<T>>();
				for (ComposedEntity<?> item : tempElements) {
					((List) elements).add(new SelectableItem<ComposedEntity>(this, item));
				}
			} catch (Exception e) {
				log.error("[CollectionWrapper.setDomainElements] Error on creating wrapper class for result. Class: " + listClass, e);
			}
		Roma.fieldChanged(this, "elements");
	}

	@SuppressWarnings("unchecked")
	public void callback(Object iObject) {
		if (iObject == null)
			return;

		ArrayList<ComposedEntity<?>> sel = new ArrayList<ComposedEntity<?>>();
		for (ComposedEntity<?> e : elements) {
			if (((SelectableItem<ComposedEntity<T>>) e).isSelected())
				sel.add((ComposedEntity<?>) e);
		}

		selection = new ComposedEntity[sel.size()];
		sel.toArray(selection);
	}

	@Override
	public void setSelection(ComposedEntity<T>[] iSelection) {
		super.setSelection(iSelection);

		boolean found;
		for (ComposedEntity<T> item : elements) {
			found = false;
			for (ComposedEntity<T> selected : selection) {
				if (item.equals(selected)) {
					found = true;
					break;
				}
			}
			((SelectableItem<?>) item).setSelected(found);
			Roma.fieldChanged(item, "selected");
		}
	}
}
