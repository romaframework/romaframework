package org.romaframework.frontend.domain.wrapper;

import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.core.annotation.CoreField;
import org.romaframework.aspect.core.feature.CoreFieldFeatures;
import org.romaframework.aspect.validation.CustomValidation;
import org.romaframework.aspect.view.ViewCallback;
import org.romaframework.aspect.view.annotation.ViewAction;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.aspect.view.feature.ViewFieldFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.domain.entity.ComposedEntity;
import org.romaframework.core.entity.EntityHelper;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaHelper;

@CoreClass(orderActions = "add remove")
public abstract class CollectionWrapper<T> implements ViewCallback, CustomValidation {
	protected Object														object;
	protected String														selectionFieldName;
	protected boolean														lazy	= false;

	@CoreField(useRuntimeType = AnnotationConstants.TRUE)
	protected List<? extends ComposedEntity<T>>	elements;
	protected Collection<T>											domainElements;
	@ViewField(visible = AnnotationConstants.FALSE)
	protected ComposedEntity<T>[]								selection;
	protected SchemaClass												listClass;
	protected Class<T>													clazz;

	protected static Log												log		= LogFactory.getLog(CollectionWrapper.class);

	public void onShow() {
		SchemaClass embType = (SchemaClass) Roma.getFeature(this, "elements", CoreFieldFeatures.EMBEDDED_TYPE);

		if (embType == null || embType.getLanguageType().equals(Object.class))
			Roma.setFeature(this, "elements", CoreFieldFeatures.EMBEDDED_TYPE, listClass);

		Roma.fieldChanged(this, "elements");

		if (object != null && selectionFieldName != null) {
			Roma.setFeature(object, selectionFieldName, ViewFieldFeatures.VISIBLE, Boolean.FALSE);
		}

		if (lazy)
			load();

		if (elements != null && elements.size() > 0)
			setSelection(elements.get(0));
	}

	public ComposedEntity<T>[] getSelection() {
		return selection;
	}

	@SuppressWarnings("unchecked")
	public void setSelection(ComposedEntity<T> iSelection) {
		setSelection(new ComposedEntity[] { iSelection });
	}

	public void setSelection(ComposedEntity<T>[] iSelection) {
		validate();

		this.selection = iSelection;
	}

	public void onDispose() {
	}

	/**
	 * Overwrite this method to use custom filters on search query.
	 */
	@SuppressWarnings("unchecked")
	@ViewAction(visible = AnnotationConstants.FALSE)
	public void load() {
		if (object != null && selectionFieldName != null) {
			setDomainElements((Collection<T>) SchemaHelper.getFieldValue(Roma.schema().getSchemaClass(object.getClass()), selectionFieldName, object));
		}
	}

	@SuppressWarnings("unchecked")
	public void setDomainElements(Collection<T> iElements) {
		domainElements = iElements;

		if (iElements == null)
			elements = null;
		else
			try {
				elements = (List<ComposedEntity<T>>) EntityHelper.createComposedEntityList(iElements, listClass);
			} catch (Exception e) {
				log.error("[CollectionWrapper.setDomainElements] Error on creating wrapper class for result. Class: " + listClass, e);
			}
		Roma.fieldChanged(this, "elements");
	}
}
