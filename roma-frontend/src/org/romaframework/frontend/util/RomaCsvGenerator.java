/*
 *
 * Copyright 2010 Luca Molino (luca.molino--AT--assetdata.it)
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
package org.romaframework.frontend.util;

import java.io.ByteArrayInputStream;
import java.util.Iterator;
import java.util.List;

import org.romaframework.aspect.reporting.ReportingConstants;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.command.impl.DownloadStreamViewCommand;
import org.romaframework.aspect.view.feature.ViewFieldFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaField;

/**
 * CSV Generator from List or Object[] using SchemaClass to render columns and data
 * 
 * @author luca.molino
 * 
 */
public class RomaCsvGenerator {

	public static final String	TEXT_DELIMITER	= "\"";
	public static final String	FIELD_DELIMITER	= ";";

	public static void generateCsv(List<? extends Object> list, String fileName) {
		if (list.size() > 0) {
			SchemaClass schema = Roma.schema().getSchemaClass(list.get(0));
			generateCsv(list, schema, fileName);
		} else {
			generateCsv(list, null, fileName);
		}
	}

	public static void generateCsv(Object[] list, String fileName) {
		if (list.length > 0) {
			SchemaClass schema = Roma.schema().getSchemaClass(list[0]);
			generateCsv(list, schema, fileName);
		} else {
			generateCsv(list, null, fileName);
		}
	}

	public static void generateCsv(Object[] list, SchemaClass schema, String fileName) {
		StringBuilder csv = new StringBuilder();
		if (list.length > 0) {
			generateHeaderCsvObject(csv, schema);

			for (Object entity : list) {
				generateRowCsvObject(csv, schema, entity);
			}
		}

		Roma.view().pushCommand(
				new DownloadStreamViewCommand(new ByteArrayInputStream(csv.toString().getBytes()), fileName + "." + ReportingConstants.DOCUMENT_TYPE_CSV,
						ReportingConstants.DOCUMENT_TYPE_CSV));
	}

	public static void generateCsv(List<? extends Object> list, SchemaClass schema, String fileName) {
		StringBuilder csv = new StringBuilder();
		if (list.size() > 0) {
			generateHeaderCsvObject(csv, schema);

			for (Object entity : list) {
				generateRowCsvObject(csv, schema, entity);
			}
		}

		Roma.view().pushCommand(
				new DownloadStreamViewCommand(new ByteArrayInputStream(csv.toString().getBytes()), fileName + "." + ReportingConstants.DOCUMENT_TYPE_CSV,
						ReportingConstants.DOCUMENT_TYPE_CSV));
	}

	private static void generateHeaderCsvObject(StringBuilder csv, SchemaClass schema) {
		Iterator<SchemaField> iterator = schema.getFieldIterator();
		while (iterator.hasNext()) {
			SchemaField schemaField = iterator.next();
			if ((Boolean) schemaField.getFeature(ViewFieldFeatures.VISIBLE)) {
				if (schemaField.getFeature(ViewFieldFeatures.RENDER) != null && ((String) schemaField.getFeature(ViewFieldFeatures.RENDER)).equals(ViewConstants.RENDER_OBJECTEMBEDDED)) {
					generateHeaderCsvObjectEmbedded(csv, schemaField.getType());
				} else {
					csv.append(TEXT_DELIMITER);
					csv.append(schemaField.getName());
					csv.append(TEXT_DELIMITER);
					csv.append(FIELD_DELIMITER);
				}
			}
		}
		csv.append("\n");
	}

	private static void generateHeaderCsvObjectEmbedded(StringBuilder csv, SchemaClassDefinition schema) {
		Iterator<SchemaField> iterator = schema.getFieldIterator();
		while (iterator.hasNext()) {
			SchemaField schemaField = iterator.next();
			if ((Boolean) schemaField.getFeature(ViewFieldFeatures.VISIBLE)) {
				if (schemaField.getFeature(ViewFieldFeatures.RENDER) != null && ((String) schemaField.getFeature(ViewFieldFeatures.RENDER)).equals(ViewConstants.RENDER_OBJECTEMBEDDED)) {
					generateHeaderCsvObjectEmbedded(csv, schemaField.getType());
				} else {
					csv.append(TEXT_DELIMITER);
					csv.append(schemaField.getName());
					csv.append(TEXT_DELIMITER);
					csv.append(FIELD_DELIMITER);
				}
			}
		}
	}

	private static void generateRowCsvObject(StringBuilder csv, SchemaClass schema, Object entity) {
		if (entity == null) {
			csv.append(TEXT_DELIMITER);
			csv.append("");
			csv.append(TEXT_DELIMITER);
			csv.append(FIELD_DELIMITER);
		} else {
			Iterator<SchemaField> iterator = schema.getFieldIterator();
			while (iterator.hasNext()) {
				SchemaField schemaField = iterator.next();
				if ((Boolean) schemaField.getFeature(ViewFieldFeatures.VISIBLE)) {
					Object valueObject = schemaField.getValue(entity);
					String renderMode = (String) schemaField.getFeature(ViewFieldFeatures.RENDER);
					if (renderMode != null && renderMode.equals(ViewConstants.RENDER_OBJECTEMBEDDED)) {
						generateRowCsvObjectEmbedded(csv, schemaField.getType(), valueObject);
					} else {
						String value = schemaField.getValue(entity) != null ? schemaField.getValue(entity).toString() : "";
						csv.append(TEXT_DELIMITER);
						csv.append(String.valueOf(value));
						csv.append(TEXT_DELIMITER);
						csv.append(FIELD_DELIMITER);
					}
				}
			}
		}
		csv.append("\n");
	}

	private static void generateRowCsvObjectEmbedded(StringBuilder csv, SchemaClassDefinition schema, Object entity) {
		if (entity == null) {
			csv.append(TEXT_DELIMITER);
			csv.append("");
			csv.append(TEXT_DELIMITER);
			csv.append(FIELD_DELIMITER);
		} else {
			Iterator<SchemaField> iterator = schema.getFieldIterator();
			while (iterator.hasNext()) {
				SchemaField schemaField = iterator.next();
				if ((Boolean) schemaField.getFeature(ViewFieldFeatures.VISIBLE)) {
					Object valueObject = schemaField.getValue(entity);
					String renderMode = (String) schemaField.getFeature(ViewFieldFeatures.RENDER);
					if (renderMode != null && renderMode.equals(ViewConstants.RENDER_OBJECTEMBEDDED)) {
						generateRowCsvObjectEmbedded(csv, schemaField.getType(), valueObject);
					} else {
						String value = schemaField.getValue(entity) != null ? schemaField.getValue(entity).toString() : "";
						csv.append(TEXT_DELIMITER);
						csv.append(String.valueOf(value));
						csv.append(TEXT_DELIMITER);
						csv.append(FIELD_DELIMITER);
					}
				}
			}
		}
	}

}
