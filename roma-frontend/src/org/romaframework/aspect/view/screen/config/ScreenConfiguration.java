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

package org.romaframework.aspect.view.screen.config;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.romaframework.core.schema.xmlannotations.XmlAnnotationParser;
import org.romaframework.core.schema.xmlannotations.XmlFormAreaAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlScreenAnnotation;
import org.xml.sax.SAXException;

public class ScreenConfiguration {

	private InputStream						file;
	private XmlFormAreaAnnotation	type;
	private String								defaultArea;

	public ScreenConfiguration(InputStream iFile) throws IOException {
		file = iFile;
		load();
	}

	public ScreenConfiguration(InputStream iFile, XmlFormAreaAnnotation iType) {
		file = iFile;
		type = iType;
	}

	public void load() throws IOException {
		XmlScreenAnnotation doc;
		try {
			doc = XmlAnnotationParser.parseScreen(getFile());
			type = doc.getRootArea();
			defaultArea = doc.getDefaultArea();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	public InputStream getFile() {
		return file;
	}

	public String getDefaultArea() {
		return this.defaultArea;
	}

	public XmlFormAreaAnnotation getRootArea() {
		return type;
	}
}
