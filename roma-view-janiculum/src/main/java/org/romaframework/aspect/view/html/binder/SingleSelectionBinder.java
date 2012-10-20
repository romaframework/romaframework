package org.romaframework.aspect.view.html.binder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.view.SelectionMode;
import org.romaframework.aspect.view.ViewHelper;
import org.romaframework.aspect.view.feature.ViewFieldFeatures;
import org.romaframework.aspect.view.form.ViewComponent;
import org.romaframework.aspect.view.html.area.HtmlViewBinder;
import org.romaframework.aspect.view.html.area.HtmlViewRenderable;
import org.romaframework.aspect.view.html.component.HtmlViewContentComponent;
import org.romaframework.aspect.view.html.component.HtmlViewGenericComponent;
import org.romaframework.aspect.view.html.component.composed.list.HtmlViewCollectionComposedComponent;
import org.romaframework.core.Roma;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;

public class SingleSelectionBinder implements HtmlViewBinder {

	protected Log	log	= LogFactory.getLog(this.getClass());

	public void bind(final HtmlViewRenderable renderable, final Map<String, Object> values) {
		final HtmlViewContentComponent contentComponent = (HtmlViewContentComponent) renderable;
		// if (disabled(contentComponent)) {
		// return;
		// }
		log.debug("Select binder invoked");
		// Split the parameters name
		if (values == null || values.size() == 0) {
			log.error("invalid selection binding syntax");
			return;
		}
		final String[] splittedKey = values.keySet().iterator().next().split("_");
		// Retrieve the base parameter component
		final String baseParam = splittedKey[0];
		log.debug("Processing the select component with id " + baseParam);
		String[] splittedValue = ((String) values.get(baseParam)).split("_");
		if (splittedValue.length < 2) {
			log.error("invalid selection binding syntax");
			return;
		}
		final String valueIndex = splittedValue[1];
		final SchemaField collectionSchemaField = contentComponent.getSchemaField();
		final String selectionFieldName = (String) collectionSchemaField.getFeature(ViewFieldFeatures.SELECTION_FIELD);
		SelectionMode selectionMode = collectionSchemaField.getFeature(ViewFieldFeatures.SELECTION_MODE);

		if (SelectionMode.SELECTION_MODE_INDEX == selectionMode) {
			final int index = Integer.parseInt(valueIndex);
			ViewComponent container = (ViewComponent) contentComponent.getContainerComponent();
			ViewHelper.bindSelectionForField(collectionSchemaField, container.getContent(), new Object[] { index });
		} else {

			log.debug("Retrieved the selection field " + selectionFieldName);
			if (selectionFieldName != null && selectionFieldName.trim().length() > 0) {
				try {
					final int index = Integer.parseInt(valueIndex);
					Object value = null;
					if (index == -1) {
						// do nothing
						log.debug("Select value = none");
					} else {
						log.debug("Select value with name " + index);
						List<Object> content = new ArrayList<Object>();

						for (HtmlViewGenericComponent obj : contentComponent.getChildren()) {
							if (obj instanceof HtmlViewContentComponent) {
								content.add(obj.getContent());
							}
						}
						final Object[] collection = SchemaHelper.getObjectArrayForMultiValueObject(content);
						if (collection != null && index < collection.length) {
							value = collection[index];
						}
					}
					ViewComponent container = (ViewComponent) contentComponent.getContainerComponent();
					ViewHelper.bindSelectionForField(collectionSchemaField, container.getContent(), new Object[] { value });
					// if(container instanceof HtmlViewAbstractComponent){
					// ((HtmlViewAbstractComponent) container).setDirty(true);
					// }

					if (renderable instanceof HtmlViewCollectionComposedComponent) {
						HtmlViewCollectionComposedComponent comp = (HtmlViewCollectionComposedComponent) renderable;
						if (comp.isMap()) {
							comp.setSelectedMapIndex(index);
							Roma.fieldChanged(comp.getContainerComponent().getContent(), collectionSchemaField.getName());
						}
					}

				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected boolean disabled(final HtmlViewContentComponent contentComponent) {
		final Object enabled = contentComponent.getSchemaField().getFeature(ViewFieldFeatures.ENABLED);
		return Boolean.FALSE.equals(enabled);
	}

}
