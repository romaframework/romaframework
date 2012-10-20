/*
 *
 * Copyright 2009 Luca Molino (luca.molino--@--assetdata.it)
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
package org.romaframework.aspect.view.html.component;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.romaframework.aspect.view.html.area.HtmlViewBinder;
import org.romaframework.aspect.view.html.area.HtmlViewRenderable;
import org.romaframework.aspect.view.html.area.HtmlViewScreenArea;
import org.romaframework.aspect.view.html.constants.TransformerConstants;
import org.romaframework.aspect.view.html.transformer.Transformer;
import org.romaframework.core.schema.SchemaField;

/**
 * @author molino
 * 
 */
public class HtmlViewInvisibleContentComponent extends HtmlViewAbstractContentComponent implements HtmlViewContentComponent, Transformer {

	private static final HashSet<Integer>	EMPTY_INDEXES	= new HashSet<Integer>();
	private int														mapIndex			= 0;
	private String												label;

	public HtmlViewInvisibleContentComponent(HtmlViewContentComponent containerComponent, SchemaField schemaField, Object content, HtmlViewScreenArea screenArea) {
		super(containerComponent, schemaField, content, screenArea);
	}

	public HtmlViewInvisibleContentComponent(HtmlViewContentComponent containerComponent, int mapIndex, Object content, HtmlViewScreenArea screenArea, String label) {
		super(containerComponent, null, content, screenArea);
		this.mapIndex = mapIndex;
		this.label = label;
	}

	public Collection<HtmlViewGenericComponent> getChildren() {
		return null;
	}

	public void resetValidation() {
	}

	@Override
	public void render(Writer writer) throws IOException {
		writer.write("<div id=\"");
		writer.write(getHtmlId());
		writer.write("\" class=\"invisible_field\" ></div>");
	}

	public String getHtmlId() {
		if (this.getSchemaField() == null) {
			return ((HtmlViewRenderable) containerComponent).getHtmlId() + SEPARATOR + "MAPITEM" + SEPARATOR + mapIndex;
		}
		return ((HtmlViewRenderable) containerComponent).getHtmlId() + SEPARATOR + getSchemaField().getName();
	}

	@Override
	public Transformer getTransformer() {
		return this;
	}

	public boolean hasLabel() {
		return this.label != null;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Set<Integer> selectedIndex() {
		return EMPTY_INDEXES;
	}

	public void clearChildren() {
	}

	public void transform(final HtmlViewRenderable component, Writer writer) throws IOException {
		transformPart(component, TransformerConstants.PART_ALL, writer);
	}

	public HtmlViewBinder getBinder(HtmlViewRenderable renderable) {
		return null;
	}

	public String getType() {
		return "";
	}

	public void transformPart(HtmlViewRenderable component, String part, Writer writer) throws IOException {
		writer.write("<div id=\"");
		writer.write(component.getHtmlId());
		writer.write("\" class=\"invisible_field\" ></div>");
	}
}
