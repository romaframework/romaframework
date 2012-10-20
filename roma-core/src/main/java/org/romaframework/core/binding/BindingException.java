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

package org.romaframework.core.binding;

import org.romaframework.core.exception.FieldErrorUserException;

public class BindingException extends FieldErrorUserException {
	private static final long	serialVersionUID	= -7495986118778177788L;

	public BindingException(Object iObject, String iFieldName, Throwable t) {
		super(iObject, iFieldName, "$validation.binding", t);
	}

	public BindingException(Object iObject, String iFieldName, String arg0) {
		super(iObject, iFieldName, arg0);
	}
}