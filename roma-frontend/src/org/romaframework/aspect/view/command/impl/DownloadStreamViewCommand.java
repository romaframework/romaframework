/*
 * Copyright 2006-2009 Luca Garulli (luca.garulli--at--assetdata.it)
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
package org.romaframework.aspect.view.command.impl;

import java.io.InputStream;

import org.romaframework.aspect.view.command.ViewCommand;

public class DownloadStreamViewCommand implements ViewCommand {
	protected InputStream	in;
	protected String			fileName;
	protected String			contentType;

	public DownloadStreamViewCommand(InputStream in, String fileName, String contentType) {
		this.in = in;
		this.fileName = fileName;
		this.contentType = contentType;
	}

	public InputStream getIn() {
		return in;
	}

	public String getFileName() {
		return fileName;
	}

	public String getContentType() {
		return contentType;
	}

}
