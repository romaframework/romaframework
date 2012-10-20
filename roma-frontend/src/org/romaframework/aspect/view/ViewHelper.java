/*
 * Copyright 2006-2007 Luca Garulli (luca.garulli--at--assetdata.it)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.romaframework.aspect.view;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.session.SessionInfo;
import org.romaframework.aspect.view.feature.ViewFieldFeatures;
import org.romaframework.aspect.view.form.ContentForm;
import org.romaframework.core.Roma;
import org.romaframework.core.exception.ConfigurationException;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;
import org.romaframework.core.schema.SchemaObject;
import org.romaframework.core.schema.virtual.VirtualObject;

/**
 * Helper class to resolve common tasks about View Aspect.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * 
 */
public class ViewHelper {

	public static final String	SHOW_EVENT_NAME			= "show";
	public static final String	DISPOSE_EVENT_NAME	= "dispose";

	private static Log					log									= LogFactory.getLog(ViewHelper.class);

	public static ContentForm createForm(Object iUserObject, SessionInfo iSession) {
		return createForm(Roma.session().getSchemaObject(iUserObject), null, iUserObject, iSession);
	}

	public static ContentForm createForm(SchemaObject iSchema, SchemaField iField, Object iUserObject) {
		return createForm(iSchema, iField, iUserObject, Roma.session().getActiveSessionInfo());
	}

	public static ContentForm createForm(SchemaObject iSchema, SchemaField iField, Object iUserObject, SessionInfo iSession) {
		try {
			ContentForm form = ((ViewAspectAbstract) Roma.aspect(ViewAspect.ASPECT_NAME)).createForm(iSchema, iField, null);

			form.setContent(iUserObject, iSession);
			return form;
		} catch (Exception e) {
			throw new ViewException(e);
		}
	}

	/**
	 * Disable all fields of the user object required. It acts recursively browsing all own fields.
	 * 
	 * @param iUserObject
	 *          User object to introspect
	 */
	public static void enableFields(Object iUserObject, boolean iValue) {
		if (iUserObject == null)
			return;

		SchemaObject schemaObject = Roma.session().getSchemaObject(iUserObject);
		if (schemaObject != null)
			enableFields(iUserObject, schemaObject, iValue);
	}

	/**
	 * Disable all fields of the user object required. It acts recursively browsing all own fields.
	 * 
	 * @param iUserObject
	 *          User object to introspect
	 * @param iSchema
	 *          Schema definition of object
	 */
	public static void enableFields(Object iUserObject, SchemaClassDefinition iSchema, boolean iValue) {
		SchemaField field;
		for (Iterator<SchemaField> itField = iSchema.getFieldIterator(); itField.hasNext();) {
			field = itField.next();
			if (!field.getFeature(ViewFieldFeatures.VISIBLE))
				continue;

			Roma.setFeature(iUserObject, field.getName(), ViewFieldFeatures.ENABLED, iValue);

			if (SchemaHelper.isAssignableAs(field.getType(), java.util.Collection.class)) {
				// DISABLE ALL SUB-OBJECT IF THEY ARE FORMS
				Object subObj = SchemaHelper.getFieldValue(field, iUserObject);
				if (subObj != null) {
					Collection<?> coll = (Collection<?>) subObj;
					for (Object o : coll) {
						enableFields(o, iValue);
					}
				}
			} else if (SchemaHelper.isAssignableAs(field.getType(), java.util.Map.class)) {
				// DISABLE ALL SUB-OBJECT IF THEY ARE FORMS
				Object subObj = SchemaHelper.getFieldValue(field, iUserObject);
				if (subObj != null) {
					Map<?, ?> map = (Map<?, ?>) subObj;
					for (Object o : map.values()) {
						enableFields(o, iValue);
					}
				}
			} else if (field.getType() != null) {
				Object subObj = SchemaHelper.getFieldValue(field, iUserObject);
				String fieldRender = (String) field.getFeature(ViewFieldFeatures.RENDER);
				if (subObj != null && (fieldRender != null && fieldRender.equals(ViewConstants.RENDER_OBJECTEMBEDDED)))
					enableFields(subObj, field.getType(), iValue);
			}
		}
	}

	/**
	 * Invoke the onShow() event (if any) against the object passed.
	 * 
	 * @param content
	 *          POJO where to invoke the onShow() method
	 */
	public static void invokeOnShow(Object content) {
		if (content == null)
			return;

		try {
			if (content instanceof ViewCallback || content instanceof VirtualObject)
				SchemaHelper.invokeEvent(content, SHOW_EVENT_NAME);
		} catch (Exception e) {
			log.error("[ViewHelper.invokeOnShow] Error on invoking onShow() method on object: " + content, e);
		}
	}

	// public static void invokeOnShow(Object content, String log) {
	// invokeOnShow(content);
	// System.out.println(log + content);
	// }

	/**
	 * Invoke the onDispose() event (if any) against the object passed.
	 * 
	 * @param content
	 *          POJO where to invoke the onDispose() method
	 */
	public static void invokeOnDispose(Object content) {
		if (content == null)
			return;

		try {
			if (content instanceof ViewCallback || content instanceof VirtualObject)
				SchemaHelper.invokeEvent(content, DISPOSE_EVENT_NAME);
		} catch (Exception e) {
			log.error("[ViewHelper.invokeOnDispose] Error on invoking onDispose() method on object: " + content, e);
		}
	}

	/**
	 * Extract the selectedField feature and bind that field with the selection content.
	 * 
	 * @param iField
	 * @param iContent
	 * @param iSelection
	 */
	public static void bindSelectionForField(SchemaField iField, Object iContent, Object[] iSelection) {
		String selectionFieldName = (String) iField.getFeature(ViewFieldFeatures.SELECTION_FIELD);
		if (selectionFieldName != null) {
			// UPDATE SELECTION
			SchemaField selectionField = iField.getEntity().getField(selectionFieldName);
			Object selectedObject = SchemaHelper.getFieldObject(iContent, selectionFieldName);

			SchemaHelper.insertElements(selectionField, selectedObject, iSelection, true);
		}
	}

	public static Type getEmbeddedType(SchemaField iField) throws ConfigurationException {
		Type embeddedType = SchemaHelper.getEmbeddedType(iField);

		if (embeddedType.equals(Object.class)) {
			String selectionFieldName = (String) iField.getFeature(ViewFieldFeatures.SELECTION_FIELD);
			// NO EMBEDDED TYPE SETTED: TRY TO DETERMINE IT BY SELECTION FIELD
			if (selectionFieldName != null) {
				SchemaField selectionField = SchemaHelper.getFieldName(iField.getEntity(), selectionFieldName);

				if (selectionField == null) {
					throw new ConfigurationException("Cannot find the selection field called " + selectionFieldName + " defined in the correlated field "
							+ iField.getEntity().getSchemaClass().getName() + "." + iField.getName());
				}

				embeddedType = (Class<?>) selectionField.getLanguageType();
			}

			if (embeddedType == null) {
				throw new ConfigurationException("Cannot find embedded type definition for the field " + iField.getEntity().getSchemaClass().getName() + "." + iField.getName());
			}
		}

		return embeddedType;
	}

}
