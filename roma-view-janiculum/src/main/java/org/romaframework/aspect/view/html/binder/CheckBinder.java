package org.romaframework.aspect.view.html.binder;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.view.feature.ViewFieldFeatures;
import org.romaframework.aspect.view.form.ViewComponent;
import org.romaframework.aspect.view.html.area.HtmlViewBinder;
import org.romaframework.aspect.view.html.area.HtmlViewRenderable;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;

public class CheckBinder implements HtmlViewBinder {
	protected static Log	log	= LogFactory.getLog(CheckBinder.class);

	public void bind(final HtmlViewRenderable renderable, final Map<String, Object> values) {
		final ViewComponent contentComponent = (ViewComponent) renderable;
		try {
			final SchemaField schemaField = contentComponent.getSchemaField();
			final Object enabled = schemaField.getFeature( ViewFieldFeatures.ENABLED);
			if (Boolean.FALSE.equals(enabled)) {
				return;
			}

			if (checked(values)) {
				SchemaHelper.setFieldValue(schemaField, contentComponent.getContainerComponent().getContent(), true);
			} else {
				SchemaHelper.setFieldValue(schemaField, contentComponent.getContainerComponent().getContent(), false);
			}

			contentComponent.setContent(SchemaHelper.getFieldValue(schemaField, contentComponent.getContainerComponent().getContent()));
		} catch (final Throwable e) {
			log.error("could not bind checkbox value: " + e);
			log.debug("", e);
		}
	}

	protected boolean checked(final Map<String, Object> values) {
		for (final String key : values.keySet()) {
			final String[] splitted = key.split("_");
			if (splitted.length == 1) {
				return true;
			}
		}
		return false;
	}

}
