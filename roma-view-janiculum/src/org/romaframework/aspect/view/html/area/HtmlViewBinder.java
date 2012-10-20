package org.romaframework.aspect.view.html.area;

import java.util.Map;

import org.romaframework.core.binding.BindingException;

public interface HtmlViewBinder {
	public void bind(HtmlViewRenderable renderable, Map<String, Object> values) throws BindingException;
}
