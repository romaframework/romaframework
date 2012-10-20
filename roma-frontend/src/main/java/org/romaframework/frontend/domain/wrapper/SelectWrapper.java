package org.romaframework.frontend.domain.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.persistence.PersistenceAspect;
import org.romaframework.aspect.view.ViewCallback;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewAction;
import org.romaframework.aspect.view.annotation.ViewClass;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.aspect.view.feature.ViewFieldFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.Utility;
import org.romaframework.core.binding.Bindable;
import org.romaframework.core.repository.PersistenceAspectRepository;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;
import org.romaframework.frontend.domain.crud.CRUDException;

@CoreClass(orderActions = "reset refresh")
@ViewClass(label = "")
public class SelectWrapper<T> implements ViewCallback, ObjectWrapper, Bindable {
	@ViewField(render = ViewConstants.RENDER_SELECT, label = "", selectionField = "selection")
	protected List<T>													list;

	protected SchemaClass											clazz;
	protected PersistenceAspectRepository<T>	repository;

	protected T																selection;
	protected Object													object;
	protected String													selectionFieldName;

	@ViewField(visible = AnnotationConstants.FALSE)
	protected SchemaField											sourceField;

	public SelectWrapper() {
	}

	public SelectWrapper(Class<T> iClass) {
		this(iClass, null, null, false);
	}

	public SelectWrapper(Class<T> iClass, Object iObject, String iSelectionField) {
		this(iClass, iObject, iSelectionField, false);
	}

	public SelectWrapper(Class<T> iClass, Object iObject, String iSelectionField, boolean iLazy) {
		this(Utility.getClassName(iClass), iObject, iSelectionField, iLazy);
	}

	public SelectWrapper(String iClassName, Object iObject, String iSelectionField, boolean iLazy) {
		clazz = Roma.schema().getSchemaClass(iClassName);
		object = iObject;
		selectionFieldName = iSelectionField;
		sourceField = clazz.getField(iSelectionField);
		repository = (PersistenceAspectRepository) Roma.repository((Class) sourceField.getLanguageType());

		if (!iLazy)
			load();
	}

	public SelectWrapper(Set<T> iSetValues, Object iObject, String iSelectionField) {
		this(iSetValues, iObject, iSelectionField, false);
	}

	public SelectWrapper(Set<T> iSetValues, Object iObject, String iSelectionField, boolean iAutoSelection) {
		list = new ArrayList<T>(iSetValues);
		init(iObject, iSelectionField, iAutoSelection);
	}

	public SelectWrapper(T[] iArrayValues, Object iObject, String iSelectionField) {
		this(iArrayValues, iObject, iSelectionField, false);
	}

	public SelectWrapper(T[] iArrayValues, Object iObject, String iSelectionField, boolean iAutoSelection) {
		list = new ArrayList<T>();

		if (list != null)
			for (T item : iArrayValues)
				list.add(item);

		init(iObject, iSelectionField, iAutoSelection);
	}

	public void onShow() {
		boolean value = Roma.getFeature(object, sourceField.getName(), ViewFieldFeatures.ENABLED);
		Roma.setFeature(this, "list", ViewFieldFeatures.ENABLED, value);
	}

	protected void init(Object iObject, String iSelectionField, boolean iAutoSelection) {
		object = iObject;
		selectionFieldName = iSelectionField;
		SchemaClass cls = Roma.schema().getSchemaClass(iObject.getClass());
		sourceField = cls.getField(iSelectionField);

		if (sourceField == null)
			throw new CRUDException("Cannot find field name " + iObject.getClass().getSimpleName() + "." + iSelectionField + ". Check class definition");

		if (iAutoSelection && list.size() == 1)
			setSelection(list.get(0));
	}

	/**
	 * Overwrite this method to use custom filters on search query.
	 */
	protected void load() {
		if (repository != null)
			list = repository.getAll(Roma.context().persistence(), PersistenceAspect.STRATEGY_DETACHING, null);
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public void clear() {
		if (list != null && list.size() > 0)
			list.clear();
		reset();
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public void reset() {
		setSelection(null);
		Roma.fieldChanged(this, "list");
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public void refresh() {
		load();
		Roma.fieldChanged(this, "list");
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public T getSelection() {
		if (object != null) {
			return (T) SchemaHelper.getFieldValue(object, selectionFieldName);
		} else {
			return selection;
		}
	}

	public void setSelection(T iSelectedInstance) {
		if (object != null) {
			SchemaHelper.setFieldValue(object, selectionFieldName, iSelectedInstance);
		}
		selection = iSelectedInstance;
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public Object getFinalValue() throws Exception {
		return getSelection();
	}

	public List<T> getList() {
		return list;
	}

	public void onDispose() {
	}

	@Override
	public String toString() {
		return list != null ? list.toString() : "null";
	}

	public void setList(List<T> list) {
		this.list = list;
		Roma.fieldChanged(this, "list");
	}

	public void onEnable() {
		Roma.setFeature(this, "list", ViewFieldFeatures.ENABLED, true);
	}

	public void onDisable() {
		Roma.setFeature(this, "list", ViewFieldFeatures.ENABLED, false);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setSource(Object source, String fieldName) {
		this.object = source;
		this.selectionFieldName = fieldName;
		SchemaClass cls = Roma.schema().getSchemaClass(this.object.getClass());
		sourceField = cls.getField(this.selectionFieldName);

		if (sourceField == null)
			throw new CRUDException("Cannot find field name " + this.object.getClass().getSimpleName() + "." + this.selectionFieldName + ". Check class definition");

		clazz = Roma.schema().getSchemaClass(source);
		repository = (PersistenceAspectRepository) Roma.repository((Class) sourceField.getLanguageType());
		load();
		if (list != null && list.size() == 1) {
			setSelection(list.get(0));
			Roma.fieldChanged(this, "list");
		}
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public Object getSourceObject() {
		return object;
	}

	public SchemaField getSourceField() {
		return sourceField;
	}

}
