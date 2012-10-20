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
package org.romaframework.frontend.domain.crud;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.view.annotation.ViewField;

/**
 * Allow to know the working mode.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * 
 */
public interface CRUDWorkingMode {

	public static final int	MODE_EMBEDDED	= 0;

	public static final int	MODE_CREATE		= 1;
	public static final int	MODE_READ			= 2;
	public static final int	MODE_UPDATE		= 3;

	public static final int	MODE_CUSTOM		= 9;

	@ViewField(visible = AnnotationConstants.FALSE)
	public int getMode();
}
