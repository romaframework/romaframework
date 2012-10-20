/*
 * Copyright 2011 Luigi Dell'Aquila (luigi.dellaquila--at--assetdata.it)
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
package org.romaframework.aspect.view.html.transformer.jsp;

import java.io.IOException;
import java.io.Writer;

import org.romaframework.aspect.view.html.area.HtmlViewBinder;
import org.romaframework.aspect.view.html.area.HtmlViewRenderable;
import org.romaframework.aspect.view.html.area.ViewHtmlBinderFactory;
import org.romaframework.aspect.view.html.template.ViewTemplateManager;
import org.romaframework.aspect.view.html.transformer.Transformer;
import org.romaframework.core.Roma;

public class JspTransformer implements Transformer {

	public static final String	FILE_SUFFIX		= ".jsp";
	public static final String	JANICULUM			= "janiculum";
	public static final String	PART_TO_PRINT	= "part";
	public static final String	CODE_TO_PRINT	= "codeToPrint";
	private String							renderName;
	private String							type;
	private String							set;

	public JspTransformer(String fileName) {
		renderName = fileName;
	}

	public HtmlViewBinder getBinder(HtmlViewRenderable renderable) {
		return Roma.component(ViewHtmlBinderFactory.class).getBinder(renderable);

	}

	public void transform(HtmlViewRenderable component, Writer writer) throws IOException {
		transformPart(component, null, writer);
	}

	public void transformPart(HtmlViewRenderable component, String part, Writer writer) throws IOException {
		ViewTemplateManager mgr = Roma.component(ViewTemplateManager.class);
		String templateName;
		if (getSet() != null) {
			templateName = getSet() + "/" + getTemplateName();
		} else {
			templateName = getTemplateName();
		}
		mgr.execute(templateName, component, part, writer);
	}

	/**
	 * Retrieve the template based on the style
	 * 
	 * @param style
	 * @return
	 */
	private String getTemplateName() {
		return renderName + FILE_SUFFIX;
	}

	@Override
	public String toString() {
		return renderName;
	}

	public String getRenderName() {
		return renderName;
	}

	public void setRenderName(String renderName) {
		this.renderName = renderName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSet() {
		return set;
	}

	public void setSet(String set) {
		this.set = set;
	}

}
