package org.romaframework.aspect.view.html.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.romaframework.aspect.validation.feature.ValidationFieldFeatures;
import org.romaframework.aspect.view.ViewAspect;
import org.romaframework.aspect.view.ViewHelper;
import org.romaframework.aspect.view.form.ViewComponent;
import org.romaframework.aspect.view.html.HtmlViewAspect;
import org.romaframework.aspect.view.html.HtmlViewAspectHelper;
import org.romaframework.aspect.view.html.area.HtmlViewRenderable;
import org.romaframework.aspect.view.html.area.HtmlViewScreenArea;
import org.romaframework.core.Roma;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;

public abstract class HtmlViewAbstractContentComponent extends HtmlViewAbstractComponent implements ViewComponent {

	protected Object				content;
	protected List<Object>	orderedContent;

	protected boolean				valid	= true;
	protected String				validationMessage;

	public HtmlViewAbstractContentComponent(final HtmlViewContentComponent containerComponent, final SchemaField schemaField, final Object content,
			final HtmlViewScreenArea screenArea) {
		super(containerComponent, screenArea, schemaField);
		this.content = content;
	}

	/**
	 * @return the content
	 */
	public Object getContent() {
		return content;
	}

	/**
	 * @return the schemaField
	 */
	public SchemaField getSchemaField() {
		return (SchemaField) getSchemaElement();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.romaframework.aspect.view.form.ViewComponent#setMetaDataField(org.romaframework.core.schema.SchemaField)
	 */
	public void setMetaDataField(final SchemaField schemaField) {
		schemaElement = schemaField;

	}

	/**
	 * returns true if the component contains a valid value (regarding validation rules given in the validate() method of its pojo)
	 * 
	 * @return true if the component contains a valid value, false otherwise
	 */
	public boolean isValid() {
		return valid;
	}

	public void setValid(final boolean valid) {
		this.valid = valid;
	}

	public boolean validate() {
		final boolean result = validateThroughAnnotations();
		setValid(result);
		return result;
	}

	protected boolean validateThroughAnnotations() {
		if (getSchemaField() == null) {
			return true;
		}
		final Integer min = (Integer) getSchemaField().getFeature(ValidationFieldFeatures.MIN);
		final Integer max = (Integer) getSchemaField().getFeature(ValidationFieldFeatures.MAX);
		if (min == null && max == null) {
			return true;
		}
		if (content != null) {
			if (content instanceof Number) {
				final Number num = (Number) content;
				if ((min != null && num.longValue() < min) || (max != null && num.longValue() > max)) {
					return false;
				}
			}
			if (content instanceof String) {
				if ((min != null && ((String) content).length() < min) || (max != null && ((String) content).length() > max)) {
					return false;
				}
			}
		}
		return true;
	}

	public String getValidationMessage() {
		return validationMessage;
	}

	public void setValidationMessage(final String validationMessage) {
		this.validationMessage = validationMessage;
	}

	public void setContent(final Object content) {
		this.content = content;
	}

	@Override
	public String toString() {
		final StringBuilder buffer = new StringBuilder();
		buffer.append("content: ");
		if (content != null) {
			buffer.append(content.toString());
			buffer.append(" ");
		}
		buffer.append(super.toString());
		buffer.append(", ");
		return buffer.toString();
	}

	public boolean hasSelection() {
		if (!isCollectionField()) {
			return false;
		}
		return getSelectionSchemaField() != null;
	}

	/**
	 * 
	 * @return true if this field is a collection or an array and its selection field is a single object
	 */
	public boolean isSingleSelection() {
		if (!isCollectionField()) {
			return false;
		}
		return !isMultiSelection();
	}

	/**
	 * 
	 * @return true if this field is a collection or an array and its selection field a collection or an array
	 */
	public boolean isMultiSelection() {
		if (isCollectionField()) {
			SchemaField selectionSchemaField = getSelectionSchemaField();
			if (selectionSchemaField != null && SchemaHelper.isMultiValueObject(selectionSchemaField)
					&& !selectionSchemaField.getType().getSchemaClass().equals(getSchemaField().getEmbeddedType())) {
				return true;
			}
		}
		return false;
	}

	public List<?> orderedContent() {
		List<Object> result = new ArrayList<Object>();
		for (HtmlViewGenericComponent obj : getChildren()) {
			if (obj instanceof HtmlViewContentComponent) {
				result.add(obj.getContent());
			}
		}
		return result;
	}

	public void clearComponents() {
		Collection<HtmlViewGenericComponent> components = getChildren();
		if (components == null) {
			return;
		}
		for (HtmlViewGenericComponent component : components) {
			component.clearComponents();
			if (Roma.session().getActiveSystemSession() != null)
				HtmlViewAspectHelper.getHtmlViewSession().removeRenderableBinding((HtmlViewRenderable) component);
			if (component.getContent() != null) {
				ViewHelper.invokeOnDispose(component.getContent());
				((HtmlViewAspect) Roma.aspect(ViewAspect.class)).removeObjectFormAssociation(component.getContent(), null);
			}
		}
		components.clear();
		clearChildren();
	}

	public void destroy() {

	}
}
