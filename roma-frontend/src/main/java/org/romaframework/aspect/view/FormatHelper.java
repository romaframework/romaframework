package org.romaframework.aspect.view;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.i18n.I18NAspect;
import org.romaframework.aspect.i18n.I18NType;
import org.romaframework.aspect.view.feature.ViewFieldFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.util.parser.ObjectVariableResolver;

public class FormatHelper {

	public static Log	log	= LogFactory.getLog(FormatHelper.class);

	public static String decodeHtml(String iString) {
		if (iString == null) {
			return null;
		}
		iString = iString.replaceAll("&amp;", "&");
		iString = iString.replaceAll("&lt;", "<");
		iString = iString.replaceAll("&gt;", ">");
		iString = iString.replaceAll("&quot;", "\"");
		return iString;
	}

	public static String encodeHtml(String iString) {
		if (iString == null) {
			return null;
		}
		iString = iString.replaceAll("&", "&amp;");
		iString = iString.replaceAll("<", "&lt;");
		iString = iString.replaceAll(">", "&gt;");
		iString = iString.replaceAll("\"", "&quot;");
		return iString;
	}

	/**
	 * Parse a value of String type and convert to schemaField specified type, with schemaField specific format.
	 * 
	 * @param value
	 *          to parse.
	 * @param schemaField
	 *          with the parse settings.
	 * @return the value parsed.
	 */
	public static Object parse(Object value, SchemaField schemaField) {
		if (!(value instanceof String))
			return value;

		String stringValue = (String) value;
		Object result = value;
		SchemaClass fieldType = schemaField.getType().getSchemaClass();
		if (value != null) {
			if (fieldType.isAssignableAs(Roma.schema().getSchemaClass(Number.class))) {
				if (!stringValue.isEmpty()) {
					NumberFormat format = FormatHelper.getNumberFormat(schemaField);
					try {
						result = format.parse(stringValue);
					} catch (ParseException e) {
						throw new RuntimeException("Error on parse value of field:" + schemaField.getName() + " with format:" + format, e);
					}
				} else
					result = null;
			} else if (fieldType.isAssignableAs(Roma.schema().getSchemaClass(Date.class))) {
				if (!stringValue.isEmpty()) {
					DateFormat format = FormatHelper.getDateFormat(schemaField);
					try {
						result = format.parse(stringValue);
					} catch (ParseException e) {
						throw new RuntimeException("Error on parse value of field:" + schemaField.getName() + " with format:" + format, e);
					}
				} else
					result = null;
			} else {
				result = decodeHtml(stringValue);
			}
		}
		return result;

	}

	/**
	 * Format a value to string with the field specified format settings.
	 * 
	 * @param toFormat
	 *          value to format.
	 * @param schemaField
	 *          the SchemaField used to retrieve the format settings.
	 * @return formatted value.
	 */
	public static String format(Object toFormat, SchemaField schemaField) {
		return format(toFormat, schemaField, false);
	}

	/**
	 * Format a value to string with the field specified format settings.
	 * 
	 * @param toFormat
	 *          value to format.
	 * @param schemaField
	 *          the SchemaField used to retrieve the format settings.
	 * @param quoteHtml
	 *          if true execute the html quote of value to format.
	 * @return formatted value.
	 */
	public static String format(Object toFormat, SchemaField schemaField, boolean quoteHtml) {
		String formattedValue = null;
		if (toFormat == null) {
			formattedValue = "";
		} else if (toFormat instanceof Date) {
			try {
				formattedValue = getDateFormat(schemaField).format(toFormat);
			} catch (Throwable t) {
				formattedValue = toFormat.toString();
			}
		} else if (toFormat instanceof Number) {
			try {
				formattedValue = getNumberFormat(schemaField).format(toFormat);
			} catch (Throwable t) {
				formattedValue = toFormat.toString();
			}
		} else {
			String format = Roma.i18n().get(schemaField, I18NType.FORMAT, ViewFieldFeatures.FORMAT);
			if (format != null && !"".equals(format)) {
				formattedValue = new ObjectVariableResolver(format).resolveVariables(toFormat);
			} else {
				formattedValue = toFormat.toString();
			}
		}
		if (quoteHtml) {
			formattedValue = encodeHtml(formattedValue);
		}
		return formattedValue;
	}

	public static String format(Object toFormat, String format) {
		return format(toFormat, format, false);
	}

	public static String format(Object toFormat, String format, boolean quoteHtml) {
		String formattedValue = null;
		if (toFormat == null) {
			formattedValue = "";
		} else if (toFormat instanceof Date) {
			try {
				formattedValue = getDateFormat(format).format(toFormat);
			} catch (Throwable t) {
				formattedValue = toFormat.toString();
			}
		} else if (toFormat instanceof Number) {
			try {
				formattedValue = getNumberFormat(format).format(toFormat);
			} catch (Throwable t) {
				formattedValue = toFormat.toString();
			}
		} else {
			formattedValue = toFormat.toString();
		}
		if (quoteHtml) {
			formattedValue = encodeHtml(formattedValue);
		}
		return formattedValue;
	}

	public static DateFormat getDateFormat(SchemaField iField) {
		String message = null;
		String format = null;
		if (iField != null) {
			format = (String) iField.getFeature(ViewFieldFeatures.FORMAT);
			message = "Invalid date format for field'" + iField.getEntity().getName() + "." + iField.getName() + "' : " + format;
		} else {
			message = "Invalid default date format.";
		}

		return internalDateFormat(format, message);
	}

	public static DateFormat getDateFormat(String format) {
		return internalDateFormat(format, "Invalid date Format :" + format);
	}

	public static DateFormat internalDateFormat(String format, String errorMessage) {
		DateFormat dateFormat = null;
		format = Roma.component(I18NAspect.class).resolve(format);
		if (format != null) {
			try {
				dateFormat = new SimpleDateFormat(format, Roma.session().getActiveLocale());
			} catch (IllegalArgumentException ex) {
				log.warn(errorMessage);
			}
		}
		if (dateFormat == null) {
			dateFormat = Roma.component(I18NAspect.class).getDateFormat();
		}
		return dateFormat;
	}

	public static NumberFormat getNumberFormat(String format) {
		return internalNumberFormat(format, "Invalid number Format :" + format);
	}

	public static NumberFormat getNumberFormat(SchemaField iField) {
		String message = null;
		String format = null;
		if (iField != null) {
			format = (String) iField.getFeature(ViewFieldFeatures.FORMAT);
			message = "Invalid  number format for field'" + iField.getEntity().getName() + "." + iField.getName() + "' : " + format;
		} else {
			message = "Invalid default number format.";
		}
		return internalNumberFormat(format, message);
	}

	public static NumberFormat internalNumberFormat(String format, String errorMessage) {
		NumberFormat numberFormat = null;
		format = Roma.component(I18NAspect.class).resolve(format);
		if (format != null) {
			try {
				numberFormat = new DecimalFormat(format, new DecimalFormatSymbols(Roma.session().getActiveLocale()));
			} catch (IllegalArgumentException ex) {
				log.warn(errorMessage);
			}
		}
		if (numberFormat == null) {
			numberFormat = Roma.component(I18NAspect.class).getNumberFormat();
		}
		return numberFormat;
	}

}
