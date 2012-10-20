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

import java.util.List;

import org.romaframework.aspect.persistence.QueryByFilter;
import org.romaframework.core.Roma;
import org.romaframework.module.users.domain.AbstractAccount;
import org.romaframework.module.users.domain.BaseAccount;

/**
 * Helper class for <code>PortalPreferences</code>
 * 
 * @author l.molino
 * 
 */
public class PortalPreferencesHelper {

  /**
   * Method that gets the account's portlet preferences
   * 
   * @param account
   *          the logged account to search preferences
   * @return this account's <code>PortalPreferences</code>
   * @see PortalPreferences
   * @see BaseAccount
   */
  public static PortalPreferences getUserPreferences(AbstractAccount account) {
    QueryByFilter query = new QueryByFilter(PortalPreferences.class);
    query.addItem("account", QueryByFilter.FIELD_EQUALS, account);
    List<PortalPreferences> result = Roma.context().persistence().query(query);
    if (result.size() < 0 || result.isEmpty())
      return null;
    else
      return result.get(0);
  }

}
