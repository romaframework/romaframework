/*
 * Copyright 2006 Giordano Maestro (giordano.maestro--at--assetdata.it)
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
package org.romaframework.aspect.view.html.transformer;

import java.io.IOException;
import java.io.Writer;

import org.romaframework.aspect.view.html.area.HtmlViewBinder;
import org.romaframework.aspect.view.html.area.HtmlViewRenderable;

/**
 * 
 * @author Optimus Prime (optimus.prime--at--assetdata.it)
 * 
 */
public interface Transformer {

	public final String PRIMITIVE = "primitive";
	public final String LIST = "list";
	public final String GRID = "grid";
	
	public void transform(HtmlViewRenderable iComponent, Writer writer) throws IOException;

	public void transformPart(HtmlViewRenderable iComponent, String part, Writer writer) throws IOException;

	public HtmlViewBinder getBinder(HtmlViewRenderable renderable);
	
	public String getType(); 

}