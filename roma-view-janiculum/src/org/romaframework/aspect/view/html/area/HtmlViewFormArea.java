package org.romaframework.aspect.view.html.area;

import org.romaframework.aspect.view.area.AreaComponent;
import org.romaframework.aspect.view.form.ViewComponent;
import org.romaframework.aspect.view.html.component.HtmlViewGenericComponent;

public interface HtmlViewFormArea extends HtmlViewArea {

	/**
	 * 
	 * @param component
	 */
	public void addComponent(HtmlViewGenericComponent component);

	/**
	 * 
	 * @param screenArea
	 */
	public void setScreenArea(HtmlViewScreenArea screenArea);

	/**
	 * 
	 * @return
	 */
	public HtmlViewScreenArea getScreenArea();

	/**
	 * 
	 * @param component
	 * @return
	 */
	public boolean removeComponent(ViewComponent component);
	
	
	/**
	 * 
	 * @param oldComponent
	 * @param newComponent
	 */
	public void replaceComponent(final ViewComponent oldComponent, final HtmlViewGenericComponent newComponent);
	
	public AreaComponent searchArea(String iAreaName);

}
