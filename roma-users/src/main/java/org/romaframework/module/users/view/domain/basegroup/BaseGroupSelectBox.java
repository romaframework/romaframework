/*
 *
 * Copyright 2010 Luca Molino (luca.molino--AT--assetdata.it)
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
package org.romaframework.module.users.view.domain.basegroup;

import org.romaframework.frontend.domain.wrapper.SelectionBoxElement;
import org.romaframework.module.users.domain.BaseGroup;

/**
 * @author luca.molino
 * 
 */
public class BaseGroupSelectBox extends SelectionBoxElement<BaseGroup> {

	public BaseGroupSelectBox(Object iInstance, String iFieldName) throws IllegalArgumentException {
		super(iInstance, iFieldName);
	}

}