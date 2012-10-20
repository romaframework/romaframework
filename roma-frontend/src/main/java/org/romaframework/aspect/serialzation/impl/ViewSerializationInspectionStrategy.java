/*
 * Copyright 2009 Emanuele Tagliaferri (emanuele.tagliaferri--at--assetdata.it)
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
package org.romaframework.aspect.serialzation.impl;

import java.util.Stack;

import org.romaframework.aspect.persistence.PersistenceException;
import org.romaframework.aspect.serialization.SerializationData;
import org.romaframework.aspect.serialization.SerializationElement;
import org.romaframework.aspect.serialization.exception.SerializationException;
import org.romaframework.aspect.serialization.impl.SchemaSerializationInspectionStrategy;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.feature.ViewFieldFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.schema.SchemaClassElement;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;

/**
 * Inspection same as a full inspection bat remove invisible fields and for render select inspect the selection field.
 * 
 * @author Emanuele Tagliaferri (emanuele.tagliaferri--at--assetdata.it)
 * 
 */
public class ViewSerializationInspectionStrategy extends SchemaSerializationInspectionStrategy {

	public static final String	NAME	= "ViewInspection";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected SerializationElement inspectField(Object fieldValue, SchemaField schemaField, Stack<Object> inspected) {
		Boolean visible = (Boolean) schemaField.getFeature(ViewFieldFeatures.VISIBLE);
		if (visible != null && !visible) {
			return null;
		}
		Object renderMode = schemaField.getFeature(ViewFieldFeatures.RENDER);
		if (ViewConstants.RENDER_SELECT.equals(renderMode)) {
			final String selectionField = (String) schemaField.getFeature(ViewFieldFeatures.SELECTION_FIELD);
			if (selectionField == null) {
				return null;
			}
			Object value = SchemaHelper.getFieldValue(inspected.peek(), selectionField);
			SchemaField selectionSchemaField = schemaField.getEntity().getField(selectionField);

			SerializationElement serializationElement = new SerializationElement();
			copyFeatures(schemaField, serializationElement, modeInspect);
			serializationElement.setName(schemaField.getName());
			serializationElement.setEvents(inspectSchemaElement(schemaField.getEventIterator()));
			try {
				Object oid = Roma.persistence().getOID(value);
				if (oid != null)
					value = oid;
			} catch (PersistenceException e) {
				// TODO Log.
			}
			SerializationData data = super.inspect(value, selectionSchemaField.getType(), selectionSchemaField.getEmbeddedType(), inspected);
			serializationElement.setData(data);
			return serializationElement;
		}

		if (ViewConstants.RENDER_OBJECTLINK.equals(renderMode)) {
			try {
				Object oid = Roma.persistence().getOID(fieldValue);
				if (oid != null)
					fieldValue = oid;
			} catch (PersistenceException e) {
				// TODO Log.
			}
		}

		return super.inspectField(fieldValue, schemaField, inspected);
	}

	@Override
	protected void fillField(Object toFill, SerializationElement serializationField, SchemaField schemaField, boolean copyFeatures) {
		Object renderMode = schemaField.getFeature(ViewFieldFeatures.RENDER);
		SerializationData fieldData = serializationField.getData();
		if (ViewConstants.RENDER_SELECT.equals(renderMode)) {
			if (copyFeatures) {
				copyFeatures(serializationField, schemaField, modeFill);
				for (SerializationElement serializationElement : serializationField.getEvents()) {
					SchemaClassElement destElement = schemaField.getEvent(serializationElement.getName());
					copyFeatures(serializationElement, destElement, modeFill);
				}
			}
			final String selectionField = (String) schemaField.getFeature(ViewFieldFeatures.SELECTION_FIELD);
			if (selectionField != null) {
				Object curValue = SchemaHelper.getFieldValue(toFill, selectionField);
				Object value = super.getFieldValue(fieldData, schemaField.getEntity().getField(selectionField), curValue, copyFeatures);
				try {
					Object object = Roma.persistence().loadObjectByOID((String) value, null);
					if (object != null)
						value = object;
				} catch (PersistenceException e) {
					// TODO Log.
				}
				try {
					SchemaHelper.setFieldValue(toFill, selectionField, value);
				} catch (Exception e) {
					throw new SerializationException("Error on field '" + schemaField.getName() + "' fill :" + e.getMessage(), e);
				}
				return;
			}
		}
		if (ViewConstants.RENDER_OBJECTLINK.equals(renderMode)) {
			Object curValue = schemaField.getValue(toFill);
			Object value = getFieldValue(fieldData, schemaField, curValue, copyFeatures);
			try {
				Object object = Roma.persistence().loadObjectByOID((String) value, null);
				if (object != null)
					value = object;
			} catch (PersistenceException e) {
				// TODO Log.
			}
			try {
				schemaField.setValue(toFill, value);
			} catch (Exception e) {
				throw new SerializationException("Error on field '" + schemaField.getName() + "' fill :" + e.getMessage(), e);
			}
		}
		super.fillField(toFill, serializationField, schemaField, copyFeatures);
	}

	@Override
	protected Object getFieldValue(SerializationData fieldData, SchemaField schemaField, Object currentValue, boolean copyFeatures) {
		Boolean visible = (Boolean) schemaField.getFeature(ViewFieldFeatures.VISIBLE);
		if (visible != null && !visible) {
			return null;
		}
		return super.getFieldValue(fieldData, schemaField, currentValue, copyFeatures);
	}

}
