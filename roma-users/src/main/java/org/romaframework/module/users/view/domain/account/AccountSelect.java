/*
 *
 * Copyright 2007 Luca Molino (luca.molino--AT--assetdata.it)
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
package org.romaframework.module.users.view.domain.account;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.flow.FlowAspect;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.core.Roma;
import org.romaframework.core.binding.Bindable;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.frontend.domain.crud.CRUDException;
import org.romaframework.frontend.domain.page.ContainerPage;
import org.romaframework.module.users.view.domain.baseaccount.BaseAccountSelect;
import org.romaframework.module.users.view.domain.basegroup.BaseGroupSelect;

/**
 * @author l.molino
 * 
 */
public class AccountSelect extends ContainerPage implements Bindable {

	protected Object			sourceObject;

	protected SchemaField	sourceField;

	public AccountSelect() {
		innerPages.put((Roma.i18n()).get("AccountSelect.baseaccount.label"), new BaseAccountSelect());
		innerPages.put((Roma.i18n()).get("AccountSelect.basegroup.label"), new BaseGroupSelect());
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public SchemaField getSourceField() {
		return sourceField;
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public Object getSourceObject() {
		return sourceObject;
	}

	public void setSource(Object iSourceObject, String iSourceFieldName) {
		BaseAccountSelect companies = (BaseAccountSelect) innerPages.get((Roma.i18n()).get("AccountSelect.baseaccount.label"));
		companies.setSource(iSourceObject, iSourceFieldName);
		BaseGroupSelect individuals = (BaseGroupSelect) innerPages.get((Roma.i18n()).get("AccountSelect.basegroup.label"));
		individuals.setSource(iSourceObject, iSourceFieldName);
		sourceObject = iSourceObject;

		// ASSIGN OBJECT SCHEMA FIELD
		SchemaClass cls = Roma.schema().getSchemaClass(iSourceObject.getClass());
		sourceField = cls.getField(iSourceFieldName);

		if (sourceField == null)
			throw new CRUDException("Cannot find field name " + iSourceObject.getClass().getSimpleName() + "." + iSourceFieldName + ". Check class definition");
	}

	public void close() {
		Roma.aspect(FlowAspect.class).back();
	}

}
