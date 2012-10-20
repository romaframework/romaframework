package org.romaframework.aspect.view.html.component;

import java.util.List;
import java.util.Set;

/**
 * 
 * @author Giordano Maestro (giordano.maestro--at--assetdata.it)
 * 
 */
public interface HtmlViewContentComponent extends HtmlViewGenericComponent {

	boolean hasLabel();

	Set<Integer> selectedIndex();

	public boolean isSingleSelection();

	public boolean isMultiSelection();

	public boolean hasSelection();

	public List<?> orderedContent();
}
