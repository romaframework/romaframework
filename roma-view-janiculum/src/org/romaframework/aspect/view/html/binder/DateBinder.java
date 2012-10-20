package org.romaframework.aspect.view.html.binder;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.view.feature.ViewFieldFeatures;
import org.romaframework.aspect.view.form.ViewComponent;
import org.romaframework.aspect.view.html.area.HtmlViewBinder;
import org.romaframework.aspect.view.html.area.HtmlViewRenderable;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;

public class DateBinder implements HtmlViewBinder {

	private static Log	log	= LogFactory.getLog(TextBinder.class);

	public void bind(final HtmlViewRenderable renderable, final Map<String, Object> values) {
		String date = "";
		String time = "";
		for (String baseParam : values.keySet()) {
			if (baseParam.endsWith("_time"))
				time = (String) values.get(baseParam);
			else
				date = (String) values.get(baseParam);
		}

		if (log.isDebugEnabled())
			log.debug("binding " + renderable);
		final ViewComponent contentComponent = (ViewComponent) renderable;
		final SchemaField schemaField = contentComponent.getSchemaField();
		final Object enabled = schemaField.getFeature(ViewFieldFeatures.ENABLED);
		if (enabled != null && Boolean.FALSE.equals(enabled)) {
			return;
		}

		try {
			Date oldValue = (Date) SchemaHelper.getFieldValue(schemaField, contentComponent.getContainerComponent().getContent());
			Date result = getDate(date, time, oldValue);

			SchemaHelper.setFieldValue(schemaField, contentComponent.getContainerComponent().getContent(), result);
			contentComponent.setContent(SchemaHelper.getFieldValue(schemaField, contentComponent.getContainerComponent().getContent()));
		} catch (final Throwable e) {
			log.error("could not bind value: " + e);
		}
	}

	private Date getDate(String date, String time, Date oldValue) {
		Date result = null;
		final Calendar cal = Calendar.getInstance();
		if (oldValue != null) {
			cal.setTime(oldValue);
		}
		if (date != null && !date.equals("")) {
			final String[] dateSplitted = date.split("/");
			if (dateSplitted.length >= 3) {
				final String day = dateSplitted[0];
				cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
				final String month = dateSplitted[1];
				cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
				final String year = dateSplitted[2];
				cal.set(Calendar.YEAR, Integer.parseInt(year));
				result = cal.getTime();
			}
		}
		if (time != null && !time.equals("")) {
			final String[] timeSplitted = time.split(":");
			final String hour = timeSplitted[0];
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
			final String minute = timeSplitted[1];
			cal.set(Calendar.MINUTE, Integer.parseInt(minute));
			if (timeSplitted.length >= 3) {
				final String second = timeSplitted[2];
				cal.set(Calendar.SECOND, Integer.parseInt(second));
			}
			result = cal.getTime();
		} else if (result != null) {
			if (oldValue == null) {
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
			}
			result = cal.getTime();
		}
		return result;
	}
}
