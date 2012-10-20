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
package org.romaframework.frontend.domain.searchengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.persistence.PersistenceAspect;
import org.romaframework.aspect.persistence.QueryByFilter;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.core.Roma;
import org.romaframework.core.domain.entity.ComposedEntity;
import org.romaframework.frontend.domain.crud.CRUDMain;

/**
 * @author molino
 * 
 */
public abstract class SearchEngineMain<T> extends CRUDMain<T> {

	public static final String	MAIN_FILTER_NAME	= "mainFilter";

	protected QueryOperation		filter						= new QueryOperation();

	protected List<String>			ids;

	protected Class<T>					searchClass;

	protected SearchEngineMain(Class<? extends ComposedEntity<?>> listClass, Class<? extends ComposedEntity<?>> createClass, Class<? extends ComposedEntity<?>> readClass,
			Class<? extends ComposedEntity<?>> editClass, Class<T> iSearchClass) {
		super(listClass, createClass, readClass, editClass);
		searchClass = iSearchClass;
	}

	@ViewField(render = ViewConstants.RENDER_OBJECTEMBEDDED, label = "", position = "form://filter")
	public QueryOperation getOperation() {
		return filter;
	}

	@Override
	@ViewField(visible = AnnotationConstants.FALSE)
	public ComposedEntity<?> getFilter() {
		return null;
	}

	@Override
	public void search() {
		PersistenceAspect db = getPersistenceAspect();
		Set<String> resultId = filter.executeQuery(db);
		if (resultId != null)
			ids = new ArrayList<String>(resultId);
		else
			ids = null;
		executePagingQuery();

		Roma.fieldChanged(this, "result");
	}

	@Override
	protected void executePagingQuery() {
		if (ids != null) {
			if (ids.size() > 0) {
				try {
					fillResult(loadResultFromIDs(ids.subList(0, getPageElements())));
				} catch (IndexOutOfBoundsException e) {
					fillResult(loadResultFromIDs(ids));
				}

				if (paging != null) {
					paging.setTotalItems(ids.size());
					paging.setCurrentPage(1);
					Roma.fieldChanged(this, "paging");
				}

				if (getResult().size() == 1) {
					setSelection(new Object[] { getResult().get(0) });
				} else {
					setSelection(null);
				}
			} else {
				fillResult(new ArrayList<T>());
			}
		} else {
			queryRequest = new QueryByFilter(searchClass);
			queryRequest.setStrategy(PersistenceAspect.STRATEGY_DETACHING);
			queryRequest.setMode(SEARCH_MODE_LOADING);
			super.executePagingQuery();
		}
	}

	@Override
	public void loadPage(int iFrom, int iTo) {
		if (queryRequest == null) {
			return;
		}
		fillResult(loadResultFromIDs(ids.subList(iFrom, iTo)));
		Roma.fieldChanged(this, "paging");
	}

	protected List<T> loadResultFromIDs(List<String> ids) {
		List<T> result = new ArrayList<T>();
		for (String id : ids) {
			result.add((T) Roma.context().persistence().loadObjectByOID(id, PersistenceAspect.FULL_MODE_LOADING));
		}
		return result;
	}

}
