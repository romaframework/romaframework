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
package org.romaframework.frontend.domain.wrapper;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.romaframework.aspect.core.annotation.CoreClass;

@CoreClass(orderActions = "up down")
public class ModifiableOrderMemberWrapper<T> extends OrderMemberWrapper<T> {

	public ModifiableOrderMemberWrapper(Collection<T> iToOrder) {
		super(iToOrder);
	}

	/**
	 * Security Aspect implementation should override it or to hook it in order to provide a form to bind any Principal instance
	 */
	public void onToOrderAdd() {
	}

	@Override
	public Object getFinalValue() throws Exception {
		List<String> result = new LinkedList<String>();
		for (OrderMemberWrapperElement element : toOrder) {
			result.add(element.getValue());
		}

		if (result.size() == 0)
			return null;

		StringBuilder buffer = new StringBuilder();
		for (Object o : result) {
			if (buffer.length() > 0)
				buffer.append(" ");

			buffer.append(o.toString());
		}

		return buffer.toString();
	}
}
