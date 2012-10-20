/*
 * Copyright 2006 Giordano Maestro (giordano.maestro--at--assetdata.it)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.romaframework.aspect.reporting;

import java.lang.annotation.Annotation;

import org.romaframework.aspect.core.feature.CoreFieldFeatures;
import org.romaframework.aspect.reporting.feature.ReportingFieldFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.Utility;
import org.romaframework.core.module.SelfRegistrantConfigurableModule;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassResolver;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.xmlannotations.XmlActionAnnotation;

/**
 * The abstract class for the reporting aspect
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public abstract class ReportingAspectAbstract extends SelfRegistrantConfigurableModule<String> implements ReportingAspect {

	public static final String	ASPECT_NAME	= "reporting";

	@Override
	public void startup() {
		super.startup();
		// REGISTER THE VIEW DOMAIN TO SCHEMA CLASS RESOLVER
		Roma.component(SchemaClassResolver.class).addDomainPackage(Utility.getApplicationAspectPackage(aspectName()));
		Roma.component(SchemaClassResolver.class).addDomainPackage(Utility.getRomaAspectPackage(aspectName()));
	}

	protected abstract void refresh(SchemaClassDefinition updatedClass);

	public void beginConfigClass(SchemaClassDefinition iClass) {
	}

	public void endConfigClass(SchemaClassDefinition iClass) {
	}

	public void configClass(SchemaClassDefinition iClass) {

		refresh(iClass);
	}

	public void configField(SchemaField iField) {

		setFieldDefaults(iField);
	}

	public void configAction(SchemaAction iAction) {
		// Reporting cannot be used on actions
	}

	public String aspectName() {
		return ASPECT_NAME;
	}

	public void setFieldDefaults(SchemaField iField) {
		if ((Boolean) iField.getFeature(CoreFieldFeatures.EMBEDDED)) {
			if (iField.getFeature(ReportingFieldFeatures.RENDER) == null)
				// IF THE FIELD IS EMBEDDED, THEN THE DEFAULT RENDER IS OBJECTEMBEDDED
				iField.setFeature(ReportingFieldFeatures.RENDER, ReportingConstants.RENDER_OBJECTEMBEDDED);
		}
	}

	public void configEvent(SchemaEvent iEvent, Annotation iAnnotation, XmlActionAnnotation iNode) {
		// TODO Auto-generated method stub

	}
}