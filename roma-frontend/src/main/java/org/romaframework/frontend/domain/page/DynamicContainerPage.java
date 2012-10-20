package org.romaframework.frontend.domain.page;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.i18n.I18NType;
import org.romaframework.aspect.view.annotation.ViewAction;
import org.romaframework.aspect.view.feature.ViewClassFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaClassResolver;
import org.romaframework.core.schema.SchemaHelper;

public class DynamicContainerPage<T> extends ContainerPage<T> {

	private SchemaClass		clazz;
	private Comparator<T>	comparator;

	protected DynamicContainerPage() {
		this.clazz = SchemaHelper.getSuperclassGenericType(this.getClass());
		populate();
	}

	protected DynamicContainerPage(Comparator<T> comparator) {
		this.clazz = SchemaHelper.getSuperclassGenericType(this.getClass());
		this.comparator = comparator;
		populate();
	}

	public DynamicContainerPage(Class<? extends T> clazz) {
		this(Roma.schema().getSchemaClass(clazz));
	}

	public DynamicContainerPage(Class<? extends T> clazz, Comparator<T> comparator) {
		this(Roma.schema().getSchemaClass(clazz), comparator);
	}

	public DynamicContainerPage(SchemaClass schemaClass) {
		this(schemaClass, null);
	}

	public DynamicContainerPage(SchemaClass schemaClass, Comparator<T> comparator) {
		this.clazz = schemaClass;
		this.comparator = comparator;
		populate();
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void populate() {
		List<Class<T>> moduleClasses;
		List<T> result = new ArrayList<T>();
		moduleClasses = (List) Roma.component(SchemaClassResolver.class).getLanguageClassByInheritance((Class<?>) clazz.getLanguageType());
		for (Class<T> class1 : moduleClasses) {
			if (!Modifier.isAbstract(class1.getModifiers()) && !Modifier.isInterface(class1.getModifiers())) {
				try {
					result.add((T) class1.newInstance());
				} catch (Exception e) {
					throw new RuntimeException("Error on creating pluggable class " + clazz, e);
				}
			}
		}
		if (comparator != null)
			Collections.sort((List) result, comparator);
		else if (clazz.isAssignableAs(Comparable.class))
			Collections.sort((List) result);
		for (T instanceTab : result) {
			String s = Roma.i18n().get(instanceTab, I18NType.LABEL,ViewClassFeatures.LABEL);
			addPage(s, instanceTab);
		}
	}

}
