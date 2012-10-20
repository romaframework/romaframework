package org.romaframework.frontend.domain.wrapper;

import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.persistence.PersistenceAspect;
import org.romaframework.aspect.view.annotation.ViewAction;
import org.romaframework.aspect.view.annotation.ViewClass;
import org.romaframework.aspect.view.feature.ViewActionFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.binding.Bindable;
import org.romaframework.core.domain.entity.ComposedEntity;
import org.romaframework.core.entity.EntityHelper;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaHelper;
import org.romaframework.frontend.domain.crud.CRUDHelper;
import org.romaframework.frontend.domain.crud.CRUDInstance;
import org.romaframework.frontend.domain.crud.CRUDWorkingMode;

@CoreClass(orderActions = "reset refresh add")
@ViewClass(label = "")
public class SelectAddWrapper<T> extends SelectWrapper<T> {

	public SelectAddWrapper() {
		super();
	}

	public SelectAddWrapper(Class<T> iClass, Object iObject, String iSelectionField) {
		this(iClass, iObject, iSelectionField, false);
	}

	public SelectAddWrapper(Class<T> iClass, Object iObject, String iSelectionField, boolean iLazy) {
		super(iClass, iObject, iSelectionField, iLazy);
	}

	@Override
	public void setSelection(T iSelectedInstance) {
		if (repository == null) {
			super.setSelection(iSelectedInstance);
			Roma.fieldChanged(this, "list");
		} else {
			boolean needRefresh = false;

			if (iSelectedInstance != null && !list.contains(iSelectedInstance)) {
				iSelectedInstance = repository.create(iSelectedInstance, PersistenceAspect.STRATEGY_DETACHING);
				needRefresh = true;
			}

			super.setSelection(iSelectedInstance);

			if (needRefresh)
				refresh();
			else
				Roma.fieldChanged(this, "list");
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ViewAction(label = "")
	public void add() throws Exception {
		SchemaClass clazz = sourceField.getType().getSchemaClass();
		SchemaClass insatanceSchema = CRUDHelper.getCRUDInstance(clazz);
		Object newInstance;
		if (insatanceSchema == null)
			// CRUD CREATE CLASS NOT FOUND: DISPLAY SIMPLE OBJECT
			newInstance = EntityHelper.createObject(null, clazz);
		else
			newInstance = SchemaHelper.createObject(insatanceSchema);
		if (newInstance instanceof ComposedEntity<?>) {
			Object entity = EntityHelper.createObject(null, clazz);
			((ComposedEntity) newInstance).setEntity(entity);
		}
		if (newInstance instanceof Bindable)
			((Bindable) newInstance).setSource(this, "selection");

		if (newInstance instanceof CRUDInstance)
			((CRUDInstance) newInstance).setMode(CRUDWorkingMode.MODE_CREATE);
		Roma.flow().popup(newInstance);
	}

	public void onEnable() {
		super.onEnable();
		Roma.setFeature(this, "add", ViewActionFeatures.ENABLED, true);
	}

	public void onDisable() {
		super.onDisable();
		Roma.setFeature(this, "add", ViewActionFeatures.ENABLED, false);
	}

}
