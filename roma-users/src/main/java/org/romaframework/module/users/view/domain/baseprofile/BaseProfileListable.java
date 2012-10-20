/*
 * Copyright 2006 Luca Garulli (luca.garulli--at--assetdata.it)
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

package org.romaframework.module.users.view.domain.baseprofile;

import org.romaframework.frontend.domain.entity.ComposedEntityInstance;
import org.romaframework.module.users.domain.BaseProfile;

public class BaseProfileListable extends ComposedEntityInstance<BaseProfile> {
  public BaseProfileListable(BaseProfile iEntity) {
    super(iEntity);
  }

  // INSERT HERE GETTER METHOD TO VIEW ADDITIONAL FIELDS
  public String getParent() {
    if (getEntity().getParent() != null)
      return getEntity().getParent().getName();
    return "";
  }
}
