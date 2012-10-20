package org.romaframework.frontend.domain.crud;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.feature.CoreFieldFeatures;
import org.romaframework.aspect.view.ViewCallback;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.aspect.view.form.SelectableInstance;
import org.romaframework.core.Roma;
import org.romaframework.core.domain.entity.ComposedEntity;
import org.romaframework.core.entity.EntityHelper;
import org.romaframework.core.factory.GenericFactory;
import org.romaframework.core.repository.GenericRepository;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaHelper;

public class SimpleCRUD<T, E extends ComposedEntity<T>> extends SelectableInstance implements ViewCallback {

	private List<E>									entities	= new ArrayList<E>();
	private List<E>									deleted		= new ArrayList<E>();

	protected GenericRepository<T>	repository;

	protected SchemaClass						entityClass;
	protected SchemaClass						listableClass;

	protected SimpleCRUD() {
		List<SchemaClass> generics = SchemaHelper.getSuperclassGenericTypes(Roma.schema().getSchemaClass(this));
		listableClass = generics.get(1);
		entityClass = generics.get(0);

		repository = Roma.repository(entityClass);
		initObjects();
	}

	private void initObjects() {
		List<T> all = repository.getAll();
		entities = new ArrayList<E>();
		for (T o : all) {
			entities.add(createListable(o));
		}
		Roma.fieldChanged(this, "objects");
	}

	@ViewField(render = ViewConstants.RENDER_TABLEEDIT, enabled = AnnotationConstants.FALSE, selectionField = "selection", label = "")
	public List<E> getObjects() {
		return entities;
	}

	@SuppressWarnings("unchecked")
	public void create() {
		T create = ((GenericFactory<T>) Roma.factory(entityClass)).create();
		E createListable = createListable(create);
		entities.add(createListable);
		Roma.fieldChanged(this, "objects");
	}

	@SuppressWarnings("unchecked")
	public void delete() {
		if (getSelection() == null)
			return;
		for (Object obj : getSelection()) {
			entities.remove(obj);
			deleted.add((E) obj);
		}
		Roma.fieldChanged(this, "objects");
	}

	public void back() {
		Roma.flow().back();
	}

	public void save() {
		for (E obj : deleted) {
			repository.delete(obj.getEntity());
		}
		for (E obj : entities) {
			repository.update(obj.getEntity());
		}

		reload();
	}

	public void reload() {
		deleted.clear();
		initObjects();
	}

	public void onShow() {
		Roma.setFeature(this, "objects", CoreFieldFeatures.EMBEDDED_TYPE, entityClass);
		Roma.fieldChanged(this, "objects");
	}

	public void onDispose() {

	}

	@SuppressWarnings("unchecked")
	protected E createListable(T instance) {
		try {
			return (E) EntityHelper.createObject(instance, listableClass);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

}
