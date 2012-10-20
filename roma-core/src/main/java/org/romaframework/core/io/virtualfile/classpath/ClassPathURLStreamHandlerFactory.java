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
package org.romaframework.core.io.virtualfile.classpath;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

public class ClassPathURLStreamHandlerFactory implements URLStreamHandlerFactory {
	protected URLStreamHandler	handler	= new ClassPathURLStreamHandler();
	public static final String	PREFIX	= "classpath:";

	public URLStreamHandler createURLStreamHandler(String protocol) {
		if (protocol != null && protocol.startsWith(PREFIX))
			return handler;
		return null;
	}
}