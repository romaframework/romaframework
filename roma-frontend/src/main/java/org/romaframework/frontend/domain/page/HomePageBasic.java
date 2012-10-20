/*
 * Copyright 2006-2010 Luca Garulli (luca.garulli--at--assetdata.it)
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
package org.romaframework.frontend.domain.page;

import java.util.List;

import org.romaframework.aspect.i18n.I18NType;
import org.romaframework.aspect.view.ViewCallback;
import org.romaframework.core.Roma;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaHelper;
import org.romaframework.frontend.domain.crud.CRUDConstants;
import org.romaframework.frontend.domain.crud.CRUDMain;

public class HomePageBasic extends ContainerPage<CRUDMain<?>> implements ViewCallback {

	public HomePageBasic() {
		fillPages();
	}

	public void onShow() {
	}

	/**
	 * Fill the home page with all pages crud.
	 */
	protected void fillPages() {
		try {
			List<SchemaClass> mains = Roma.schema().getSchemaClassesByPackage(this.getClass().getPackage().getName());
			SchemaClass mainSchema = Roma.schema().getSchemaClass(CRUDMain.class);
			for (SchemaClass schemaClass : mains) {
				if (schemaClass.isAssignableAs(mainSchema) && schemaClass.getName().endsWith(CRUDConstants.MAIN_EXTENSION)) {
					addPage(Roma.i18n().get(SchemaHelper.getSuperclassGenericType(schemaClass), I18NType.LABEL), (CRUDMain<?>) schemaClass.newInstance());
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Error on Page filling", e);
		}
	}

	public void onDispose() {
	}
}
