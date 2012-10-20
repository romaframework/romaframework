/*
 * Copyright 2008 Luigi Dell'Aquila (luigi.dellaquila@assetdata.it)
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
package org.romaframework.aspect.view.html.taglib;

import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class provides basic functionality for the Roma tag library
 * 
 * @author Luigi Dell'Aquila
 * 
 */
public abstract class RomaAbstractTab extends TagSupport {

	protected Log			log	= LogFactory.getLog(getClass());

	protected String	style;

	public String getStyle() {
		return style;
	}

	public void setStyle(final String style) {
		this.style = style;
	}
}
