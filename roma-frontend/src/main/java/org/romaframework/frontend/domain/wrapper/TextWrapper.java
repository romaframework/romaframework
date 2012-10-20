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

import java.lang.reflect.InvocationTargetException;
import java.text.Format;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.view.annotation.ViewField;

/**
 * It is a wrapper for a textArea to insert into an editable table
 * 
 * @author Giordano Maestro (giordano.maestro--at--assetdata.it) 16/ott/07
 */
public class TextWrapper implements ObjectWrapper, Comparable<TextWrapper> {
	/**
	 * The wrapper object
	 */
	@ViewField(label = "")
	protected String		value;
	/**
	 * The class of the wrapped object
	 */
	protected Class<?>	typeClass;

	/**
	 * The formatter to be used for the parsing
	 */
	protected Format		formatter;

	public TextWrapper(Object iValue) {
		this(iValue, iValue != null ? iValue.getClass() : String.class);
	}

	/**
	 * Creates the wrapper, the Class must have a a constructor with a string arg
	 * 
	 * @param iValue
	 * @param iNewInstanceClass
	 */
	public TextWrapper(Object iValue, Class<?> iNewInstanceClass) {
		typeClass = iNewInstanceClass;
		if (iValue == null) {
			value = "";
		} else {
			value = iValue.toString();
		}
	}

	/**
	 * Return The Wrapped Object
	 * 
	 * @return The Wrapped Object
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Set the wrapped Object
	 * 
	 * @param value
	 *          The wrapped Object
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * 
	 * Return the final value of the wrapper
	 * 
	 * @throws Exception
	 *           , IllegalAccessException, InvocationTargetException
	 */
	@ViewField(visible = AnnotationConstants.FALSE)
	public Object getFinalValue() throws Exception, IllegalAccessException, InvocationTargetException {
		if (value != null && "".equals(value.trim())) {
			// TODO change the exception throwed
			if (formatter == null) {
				return typeClass.getConstructor(new Class[] { String.class }).newInstance(new Object[] { value });
			} else {
				return formatter.parseObject(value);
			}
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		return value;
	}

	public int compareTo(TextWrapper o) {
		if (o == null)
			return 1;
		if (this.value == null && o.value != null)
			return -1;
		return this.value.compareTo(o.value);
	}
}
