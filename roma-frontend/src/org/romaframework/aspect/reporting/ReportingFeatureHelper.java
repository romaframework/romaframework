/*
 * Copyright 2006 Giordano Maestro (giordano.maestro--at--assetdata.it)
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
package org.romaframework.aspect.reporting;

import org.romaframework.aspect.i18n.I18NType;
import org.romaframework.aspect.reporting.feature.ReportingClassFeatures;
import org.romaframework.aspect.reporting.feature.ReportingFieldFeatures;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.feature.ViewActionFeatures;
import org.romaframework.aspect.view.feature.ViewClassFeatures;
import org.romaframework.aspect.view.feature.ViewFieldFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassElement;
import org.romaframework.core.schema.SchemaFeatures;
import org.romaframework.core.schema.SchemaField;

public class ReportingFeatureHelper {

	public static Boolean isVisibleField(SchemaField iSchemaField) {
		Boolean result = (Boolean) iSchemaField.getFeature(ReportingFieldFeatures.VISIBLE);
		if (result != null) {
			return result;
		} else {
			result = (Boolean) iSchemaField.getFeature(ViewFieldFeatures.VISIBLE);
			if (result != null) {
				return result;
			}
		}
		return true;
	}

	public static Boolean isRenderImage(SchemaField iSchemaField) {
		String render = getRender(iSchemaField);
		if (ViewConstants.RENDER_IMAGE.equals(render)) {
			return true;
		} else {
			return false;
		}
	}

	public static Boolean isRenderHtml(SchemaFeatures iSchemaField) {
		String render = getRender(iSchemaField);
		if (ViewConstants.RENDER_HTML.equals(render)) {
			return true;
		} else {
			return false;
		}
	}

	public static Boolean isRenderRTF(SchemaFeatures iSchemaField) {
		String render = getRender(iSchemaField);
		if (ViewConstants.RENDER_RICHTEXT.equals(render)) {
			return true;
		} else {
			return false;
		}
	}

	public static Boolean isRenderChart(SchemaField iSchemaField) {
		String render = getRender(iSchemaField);
		if (ViewConstants.RENDER_CHART.equals(render)) {
			return true;
		} else {
			return false;
		}
	}

	public static String getLayout(SchemaFeatures iFeatures) {
		String fieldLayout = null;
		if (iFeatures instanceof SchemaClass) {
			fieldLayout = (String) iFeatures.getFeature(ReportingClassFeatures.LAYOUT);
		} else {
			fieldLayout = (String) iFeatures.getFeature(ReportingFieldFeatures.LAYOUT);
		}
		if (fieldLayout == null) {
			if (iFeatures instanceof SchemaField) {
				fieldLayout = (String) iFeatures.getFeature(ViewFieldFeatures.POSITION);
			} else if (iFeatures instanceof SchemaAction) {
				fieldLayout = (String) iFeatures.getFeature(ViewActionFeatures.POSITION);
			}
		}
		return fieldLayout;
	}

	public static String getRender(SchemaFeatures iFeatures) {
		String fieldRender = null;
		if (iFeatures instanceof SchemaClass) {
			fieldRender = (String) iFeatures.getFeature(ReportingClassFeatures.RENDER);
		} else {
			fieldRender = (String) iFeatures.getFeature(ReportingFieldFeatures.RENDER);
		}
		if (fieldRender == null) {
			if (iFeatures instanceof SchemaField) {
				fieldRender = (String) iFeatures.getFeature(ViewFieldFeatures.RENDER);
			} else if (iFeatures instanceof SchemaClass) {
				fieldRender = (String) iFeatures.getFeature(ViewClassFeatures.RENDER);
			}
		}
		return fieldRender;
	}

	public static String getLabel(SchemaFeatures iFeatures) {
		String fieldRender = null;
		if (iFeatures instanceof SchemaClass) {
			fieldRender = (String) iFeatures.getFeature(ReportingClassFeatures.LABEL);
		} else {
			fieldRender = (String) iFeatures.getFeature(ReportingFieldFeatures.LABEL);
		}
		if (fieldRender == null) {
			if (iFeatures instanceof SchemaField) {
				fieldRender = (String) iFeatures.getFeature(ViewFieldFeatures.LABEL);
			} else if (iFeatures instanceof SchemaClass) {
				fieldRender = (String) iFeatures.getFeature(ViewClassFeatures.LABEL);
			}
		}
		return fieldRender;
	}

	public static String getI18NLabel(SchemaClassElement schemaField) {
		return Roma.i18n().get(schemaField, I18NType.LABEL, ReportingFieldFeatures.LABEL);
	}

	public static String getI18NLabel(SchemaClassDefinition schemaClass) {
		return Roma.i18n().get(schemaClass, I18NType.LABEL, ReportingClassFeatures.LABEL);
	}

}
