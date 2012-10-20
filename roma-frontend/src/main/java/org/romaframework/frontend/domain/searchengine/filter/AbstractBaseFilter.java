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

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.flow.FlowAspect;
import org.romaframework.aspect.persistence.PersistenceAspect;
import org.romaframework.aspect.persistence.Query;
import org.romaframework.aspect.persistence.QueryByExample;
import org.romaframework.aspect.view.annotation.ViewAction;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.core.Roma;
import org.romaframework.frontend.domain.entity.ComposedEntityInstance;
import org.romaframework.frontend.domain.searchengine.QueryCondition;

/**
 * @author lmolino
 * 
 */
public abstract class AbstractBaseFilter<T> extends ComposedEntityInstance<T> implements BaseFilter<T> {

	@ViewField(visible = AnnotationConstants.FALSE)
	protected byte	queryStrategy	= PersistenceAspect.STRATEGY_STANDARD;

	public AbstractBaseFilter(T iEntity) {
		super(iEntity);
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public Set<String> executeQuery(PersistenceAspect db, Class<?> context) {
		Set<String> result = new TreeSet<String>();
		Query query = getFilterQuery();
		query.setStrategy(queryStrategy);
		List<Object> employees = retainObjects(db, query);
		processResults(db, result, employees,context);
		return result;
	}

	protected List<Object> retainObjects(PersistenceAspect db, Query query) {
		List<Object> entities = db.query(query);
		return entities;
	}

	protected void processResults(PersistenceAspect db, Set<String> result, List<Object> entities, Class<?> context) {
		for (Object entity : entities) {
			result.add(db.getOID(entity).trim());
		}
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public Class<?> getEntityClass() {
		return entity.getClass();
	}

	public void ok() {
		Roma.aspect(FlowAspect.class).back();
	}

	public void cancel() {
		Roma.aspect(FlowAspect.class).back();
	}

	public byte getQueryStrategy() {
		return queryStrategy;
	}

	public void setQueryStrategy(byte queryStrategy) {
		this.queryStrategy = queryStrategy;
	}

	@Override
	public abstract String toString();

	@ViewAction(visible = AnnotationConstants.FALSE)
	public void back() {
	}

	@Override
	public boolean equals(Object arg0) {
		if (getClass().equals(arg0.getClass()))
			return entity.equals(((BaseFilter<?>) arg0).getEntity());
		return false;
	}

	@Override
	public int hashCode() {
		return entity.hashCode();
	}

	protected Query getFilterQuery() {
		QueryByExample query = new QueryByExample(entity);
		query.setStrategy(PersistenceAspect.STRATEGY_DEFAULT);
		return query;
	}

	public Class<? extends QueryCondition> getQueryConditionClass() {
		return QueryCondition.class;
	}

}
