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
package org.romaframework.module.users.domain.portal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that rapresents a list of <code>String</code>
 * 
 * @author l.molino
 * @see PortalPreferences
 */
public class PortletList implements Serializable {

	private static final long	serialVersionUID	= 8116638609205760042L;
	
	protected List<String> portlets = new ArrayList<String>();

  public List<String> getPortlets() {
    return portlets;
  }

  public void setPortlets(List<String> portlets) {
    this.portlets = portlets;
  }

  @Override
  public String toString() {
    return portlets.toString();
  }

}
