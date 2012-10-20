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
package org.romaframework.frontend.domain.binding;

import java.util.Map.Entry;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.core.annotation.CoreField;
import org.romaframework.core.domain.type.Pair;

/**
 * Utility class to handle a pair of values known at run-time.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * 
 * @param <K>
 *          key type
 * @param <V>
 *          value type
 */
@CoreClass(orderFields = "key value")
public class RuntimePair extends Pair<Object, Object> {
	@CoreField(useRuntimeType = AnnotationConstants.TRUE)
	protected Object	key;

	@CoreField(useRuntimeType = AnnotationConstants.TRUE)
	protected Object	value;

	public RuntimePair() {
	}

	public RuntimePair(Object iKey, Object iValue) {
		super(iKey, iValue);
	}

	public RuntimePair(Entry<Object, Object> iEntry) {
		super(iEntry);
	}
}
