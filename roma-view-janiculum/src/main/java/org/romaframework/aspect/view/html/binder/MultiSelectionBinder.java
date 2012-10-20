package org.romaframework.aspect.view.html.binder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.romaframework.aspect.view.ViewHelper;
import org.romaframework.aspect.view.feature.ViewFieldFeatures;
import org.romaframework.aspect.view.html.area.HtmlViewBinder;
import org.romaframework.aspect.view.html.area.HtmlViewRenderable;
import org.romaframework.aspect.view.html.component.HtmlViewContentComponent;
import org.romaframework.aspect.view.html.component.HtmlViewGenericComponent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;

public class MultiSelectionBinder implements HtmlViewBinder {

	public void bind(final HtmlViewRenderable renderable, final Map<String, Object> values) {
		final HtmlViewContentComponent contentComponent = (HtmlViewContentComponent) renderable;
		final String selectionFieldName = (String) contentComponent.getSchemaField().getFeature(ViewFieldFeatures.SELECTION_FIELD);
		if (selectionFieldName == null || selectionFieldName.equals("")) {
			return;
		}
		final SchemaField selectionField = contentComponent.getContainerComponent().getSchemaObject().getField(selectionFieldName);
		if (selectionField == null) {
			return;
		}

		final Set<Integer> indexes = getSelectedIndexes(values);

		final List<Object> selectedValues = new ArrayList<Object>();
		List<Object> content = new ArrayList<Object>();

		for (HtmlViewGenericComponent obj : contentComponent.getChildren()) {
			if (obj instanceof HtmlViewContentComponent) {
				content.add(obj.getContent());
			}
		}

		final Object[] originalValues = SchemaHelper.getObjectArrayForMultiValueObject(content);
		for (final Integer index : indexes) {
			try {
				if (index > -1) {
					final Object value = originalValues[index];
					if (value != null) {
						selectedValues.add(value);
					}
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		ViewHelper.bindSelectionForField(contentComponent.getSchemaField(), contentComponent.getContainerComponent().getContent(),
				SchemaHelper.getObjectArrayForMultiValueObject(selectedValues));

	}

	private Set<Integer> getSelectedIndexes(final Map<String, Object> values) {
		final Set<Integer> indexes = new HashSet<Integer>();
		for (final String str : values.keySet()) {
			final String[] splitted = str.split("_");
			if (splitted.length > 1) {
				try {
					indexes.add(Integer.parseInt(splitted[1]));
				} catch (final NumberFormatException e) {
				}
			}
		}
		return indexes;
	}
}