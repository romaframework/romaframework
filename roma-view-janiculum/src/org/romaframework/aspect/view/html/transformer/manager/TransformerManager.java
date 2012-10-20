package org.romaframework.aspect.view.html.transformer.manager;

import org.romaframework.aspect.view.html.transformer.Transformer;

public interface TransformerManager {

	public Transformer getComponent(String key);

	public String getTypeByRender(String render);
	
}
