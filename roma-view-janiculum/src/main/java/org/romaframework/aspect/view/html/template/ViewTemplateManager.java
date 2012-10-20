package org.romaframework.aspect.view.html.template;

import java.io.Writer;

import org.romaframework.aspect.view.html.area.HtmlViewRenderable;

public interface ViewTemplateManager {

	public void execute(String templateName, HtmlViewRenderable renderable, String part, Writer writer);
	
	public String getTemplatesPath();
	
	public void setTemplatesPath(String templatesPath);
	
	public boolean isCacheTemplates();
	
	public void setCacheTemplates(boolean cacheTemplates);


}
