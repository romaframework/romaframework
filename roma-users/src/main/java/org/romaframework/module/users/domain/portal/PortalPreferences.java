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

import java.util.HashMap;
import java.util.Map;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.module.users.domain.BaseAccount;

/**
 * Class that rapresents the portlet configuration
 * 
 * @author l.molino
 * 
 */
public class PortalPreferences {

  @ViewField(visible = AnnotationConstants.FALSE)
  protected BaseAccount              account;

  @ViewField(render = ViewConstants.RENDER_TABLE)
  protected Map<String, PortletList> portletsInfos = new HashMap<String, PortletList>();

  
  public PortalPreferences() {
  	
  }
  /**
   * It creates a new instance of PortlaPreferences for specified account
   * 
   * @param iAccount
   *          the account associated with this preferences
   */
  public PortalPreferences(BaseAccount iAccount) {
    account = iAccount;
  }

  /**
   * Method that returns the <code>BaseAccount</code> of this preferences
   * 
   * @return the account of this preferences
   * @see BaseAccount
   */
  public BaseAccount getAccount() {
    return account;
  }

  /**
   * Method that sets the <code>BaseAccount</code> of this preferences
   * 
   * @param account
   *          the account of this preferences
   * @see BaseAccount
   */
  public void setAccount(BaseAccount account) {
    this.account = account;
  }

  /**
   * Method that returns a map that contains portlet configuration
   * 
   * @return the map of portlet configuration, mapped by the name of the contaier
   */
  public Map<String, PortletList> getPortletsInfos() {
    return portletsInfos;
  }

  /**
   * 
   * Method that sets the map containing portlet configurations
   * 
   * @param portlets
   *          the map containing portlet configurations
   */
  public void setPortletsInfos(Map<String, PortletList> portlets) {
    portletsInfos = portlets;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof PortalPreferences))
      return false;
    else {
      PortalPreferences other = (PortalPreferences) o;
      if (account != null)
        return account.equals(other.account);
      else
        return other.account == null;
    }
  }

  @Override
  public int hashCode() {
    if (account != null)
      return account.hashCode();
    else
      return 0;
  }

}
