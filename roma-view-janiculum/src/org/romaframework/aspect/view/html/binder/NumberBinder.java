package org.romaframework.aspect.view.html.binder;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.view.FormatHelper;
import org.romaframework.aspect.view.feature.ViewFieldFeatures;
import org.romaframework.aspect.view.form.ViewComponent;
import org.romaframework.aspect.view.html.area.HtmlViewBinder;
import org.romaframework.aspect.view.html.area.HtmlViewRenderable;
import org.romaframework.aspect.view.html.component.HtmlViewAbstractContentComponent;
import org.romaframework.core.binding.BindingException;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;

public class NumberBinder implements HtmlViewBinder {
	private static Log	log	= LogFactory.getLog(NumberBinder.class);

	public void bind(final HtmlViewRenderable renderable, final Map<String, Object> values) throws BindingException {
		final String baseParam = values.keySet().iterator().next().split("_")[0];
		String value = (String) values.get(baseParam);
		if (log.isDebugEnabled())
			log.debug("binding " + renderable);
		final ViewComponent contentComponent = (ViewComponent) renderable;
		final SchemaField schemaField = contentComponent.getSchemaField();
		final Object enabled = schemaField.getFeature(ViewFieldFeatures.ENABLED);
		if (enabled != null && Boolean.FALSE.equals(enabled)) {
			return;
		}

		if (value != null) {
			try {
				Object val = FormatHelper.parse(value, schemaField);
				SchemaHelper.setFieldValue(schemaField, contentComponent.getContainerComponent().getContent(), val);
				contentComponent.setContent(SchemaHelper.getFieldValue(schemaField, contentComponent.getContainerComponent().getContent()));
			} catch (Exception e) {
				((HtmlViewAbstractContentComponent) contentComponent).setValid(false);
				((HtmlViewAbstractContentComponent) contentComponent).setDirty(true);
				log.info("invalid number inserted");
			}
		}

	}
}
