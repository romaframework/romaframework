/*
 *
 * Copyright 2007 Luca Molino (luca.molino--AT--assetdata.it)
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
package org.romaframework.aspect.view.portal;

import org.romaframework.aspect.view.ViewCallback;

/**
 * Interface that rapresents a portlet
 * 
 * @author l.molino
 * 
 */
public interface PortalPage extends ViewCallback {
	/**
	 * Byte that indicates that the state of this portlet is minimized
	 */
	public static byte	MODE_MINIMIZED	= 0;

	/**
	 * Byte that indicates that the state of this portlet is maximized
	 */
	public static byte	MODE_MAXIMIZED	= 1;

	/**
	 * Method that returns the mode of PortalPage
	 * 
	 * @return the current mode of this PortalPage
	 */
	public byte getMode();

	/**
	 * Set current portlet mode.
	 */
	public void setMode(byte mode);

	/**
	 * Method that minimizes the portlet
	 * 
	 */
	public void minimize();

	/**
	 * Method that minimizes the portlet
	 * 
	 */
	public void maximize();

	/**
	 * User operations to do during minimizing
	 * 
	 */
	public void onMinimize();

	/**
	 * User operations to do during maximizing
	 * 
	 */
	public void onMaximize();

	public void onShow();

	public void onDispose();

	/**
	 * Method that sets a <code>String</code> containing the name of PortalPageContainer that contains this portlet
	 * 
	 * @param iContainerName
	 *          the name of the <code>PortalPageContaier</code>
	 * @see PortalPageContainer
	 */
	public void setContainerName(String iContainerName);

	/**
	 * Method that returns a <code>String</code> containing the name of PortalPageContainer that contains this portlet
	 * 
	 * @return the name of the container of this portlet
	 */
	public String getContainerName();

	/**
	 * Method that removes this portlet from the user configuration.
	 * 
	 * It uses the {@link #getContainerName()} method to know witch container has to be checked in preferences and (in case the
	 * className of this portlet is found) delete this portlet from its configuration.
	 * 
	 */
	public void removeFromPreferences();

	public String getMinimizedArea();

	public void setMinimizedArea(String minimizedArea);

	public String getMaximizedArea();

	public void setMaximizedArea(String maximizedArea);
}
