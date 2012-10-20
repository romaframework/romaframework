/*
 * Copyright 2006 Luca Garulli (luca.garulli--at--assetdata.it)
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

package org.romaframework.frontend;

import org.romaframework.core.Roma;
import org.romaframework.core.module.SelfRegistrantModule;
import org.romaframework.core.schema.SchemaClassResolver;

public class FrontendModule extends SelfRegistrantModule {

	public static final String	MODULE_NAME	= "Frontend";

	public void startup() {
		SchemaClassResolver classResolver = Roma.component(SchemaClassResolver.class);

		// REGISTER THE APPLICATION DOMAIN AS FIRST ONE PATH
		classResolver.addDomainPackage(FrontendModule.class.getPackage().getName());
		classResolver.addPackage(FrontendModule.class.getPackage().getName()+".view.domain");
	}

	public void shutdown() {
	}

	public String moduleName() {
		return MODULE_NAME;
	}
}
