package org.romaframework.aspect.view.html.component;

import java.util.Collection;
import java.util.List;


public interface HtmlViewComposedComponent extends HtmlViewContentComponent {

	public void addComponent(HtmlViewGenericComponent iComponent);

	public void clearComponents();

	public Collection<HtmlViewGenericComponent> getComponents();

  public List<String> getHeaders();
  
  public List<String> getHeadersRaw();

  public void addComponent(final HtmlViewGenericComponent component, boolean invokeOnShow);
	
}
