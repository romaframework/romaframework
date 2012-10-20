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
package org.romaframework.frontend.view.domain.activesession;

import java.util.HashMap;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.session.SessionInfo;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.frontend.domain.page.EntityPage;

@CoreClass(orderFields = "entity attributes")
public class ActiveSessionInstance extends EntityPage<SessionInfo> {

  @ViewField(render = ViewConstants.RENDER_TABLE, selectionField = "attributeSelected")
  protected HashMap<String, SessionAttributeInfo> attributes;

  @ViewField(visible = AnnotationConstants.FALSE)
  protected SessionAttributeInfo                  attributeSelected;

  public ActiveSessionInstance(SessionInfo iEntity) {
    super(iEntity);
  }

  public void cancel() {
    back();
  }

  public HashMap<String, SessionAttributeInfo> getAttributes() {
    return attributes;
  }

  public SessionAttributeInfo getAttributeSelected() {
    return attributeSelected;
  }

  public void setAttributeSelected(SessionAttributeInfo attributeSelected) {
    this.attributeSelected = attributeSelected;
  }
}
