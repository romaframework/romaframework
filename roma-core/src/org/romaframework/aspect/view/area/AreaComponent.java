package org.romaframework.aspect.view.area;

import org.romaframework.core.domain.type.TreeNode;

public interface AreaComponent extends TreeNode {

	public static final String	DEF_AREAMODE_NAME	= "placeholder";

	/**
	 * Returns the name of the area.
	 */
	public String getName();

	/**
	 * @return the areaSize
	 */
	public Integer getAreaSize();

	/**
	 * @return the areaAlign
	 */
	public String getAreaAlign();

	public String getStyle();

	public void clear();
}
