/*
 *
 * Copyright 2009 Luca Molino (luca.molino--AT--assetdata.it)
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
package org.romaframework.frontend.domain.searchengine.filter;

import java.util.Set;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.persistence.PersistenceAspect;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.core.domain.entity.ComposedEntity;
import org.romaframework.frontend.domain.searchengine.QueryCondition;

/**
 * @author lmolino
 * 
 */
public interface BaseFilter<T> extends ComposedEntity<T> {

	public static final String	FILTER_AND	= "Obbligatorio";

	public static final String	FILTER_OR		= "Facoltativo";

	@ViewField(visible = AnnotationConstants.FALSE)
	public byte getQueryStrategy();

	public void setQueryStrategy(byte queryStrategy);

	public void back();

	public Set<String> executeQuery(PersistenceAspect db, Class<?> context);

	@ViewField(visible = AnnotationConstants.FALSE)
	public Class<?> getEntityClass();

	@ViewField(visible = AnnotationConstants.FALSE)
	public Class<? extends QueryCondition> getQueryConditionClass();

	@Override
	public int hashCode();

	@Override
	public boolean equals(Object arg0);

}
