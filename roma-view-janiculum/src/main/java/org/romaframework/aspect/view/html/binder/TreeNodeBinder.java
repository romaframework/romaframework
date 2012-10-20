package org.romaframework.aspect.view.html.binder;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.view.feature.ViewFieldFeatures;
import org.romaframework.aspect.view.html.area.HtmlViewBinder;
import org.romaframework.aspect.view.html.area.HtmlViewRenderable;
import org.romaframework.aspect.view.html.component.HtmlViewAbstractContentComponent;
import org.romaframework.aspect.view.html.component.HtmlViewContentComponent;
import org.romaframework.core.binding.BindingException;
import org.romaframework.core.domain.type.TreeNode;
import org.romaframework.core.domain.type.TreeNodeHelper;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;

public class TreeNodeBinder implements HtmlViewBinder {

	private static Log	LOG	= LogFactory.getLog(TreeNodeBinder.class);

	public void bind(HtmlViewRenderable renderable, Map<String, Object> values) throws BindingException {
		final String baseParam = values.keySet().iterator().next().split("_")[0];
		String valueAsString = (String) values.get(baseParam);
		Integer value = null;
		if (LOG.isDebugEnabled())
			LOG.debug("binding " + renderable);
		final HtmlViewContentComponent contentComponent = (HtmlViewContentComponent) renderable;
		if (contentComponent instanceof HtmlViewAbstractContentComponent) {
			((HtmlViewAbstractContentComponent) contentComponent).setDirty(true);
		}

		final SchemaField schemaField = contentComponent.getSchemaField();

		try {
			String selectionFieldName = (String) schemaField.getFeature(ViewFieldFeatures.SELECTION_FIELD);
			if (selectionFieldName != null) {
				// UPDATE SELECTION
				SchemaField selectionField = schemaField.getEntity().getField(selectionFieldName);

				Object toSet = SchemaHelper.getFieldValue(schemaField, contentComponent.getContainerComponent().getContent());
				if (valueAsString != null) {
					value = Integer.valueOf(valueAsString.trim());
				}
				TreeNode findChildAllTree = TreeNodeHelper.findChildByNumber((TreeNode) toSet, value);
				SchemaHelper.setFieldValue(selectionField, contentComponent.getContainerComponent().getContent(), findChildAllTree);
				contentComponent.setContent(SchemaHelper.getFieldValue(schemaField, contentComponent.getContainerComponent().getContent()));
			}
		} catch (final Exception e) {
			LOG.error("could not bind value: " + e);
		}

	}

}
