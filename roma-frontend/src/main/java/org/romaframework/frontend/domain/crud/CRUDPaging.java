/*
 * Copyright 2006-2007 Luca Garulli (luca.garulli--at--assetdata.it)
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

package org.romaframework.frontend.domain.crud;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.flow.annotation.FlowAction;
import org.romaframework.aspect.flow.feature.FlowActionFeatures;
import org.romaframework.aspect.i18n.I18NType;
import org.romaframework.aspect.persistence.PersistenceConstants;
import org.romaframework.aspect.persistence.annotation.Persistence;
import org.romaframework.aspect.view.ViewCallback;
import org.romaframework.aspect.view.annotation.ViewAction;
import org.romaframework.aspect.view.annotation.ViewClass;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.aspect.view.feature.ViewActionFeatures;
import org.romaframework.aspect.view.feature.ViewFieldFeatures;
import org.romaframework.core.Roma;

/**
 * Handles the paging in CRUD Main.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
@CoreClass(orderFields = "totalItems pageLabel pages", orderActions = "first prev next last queryAll csv")
@ViewClass(label = "")
public class CRUDPaging implements ViewCallback {

	public static final int				DEF_PAGE_ELEMENTS	= 15;

	@ViewField(label = "", render = "text", enabled = AnnotationConstants.FALSE)
	protected int									totalItems;

	@ViewField(visible = AnnotationConstants.FALSE)
	protected int									currentPage;

	@ViewField(label = "", visible = AnnotationConstants.TRUE, selectionField = "currentPage", render = "select")
	protected Integer[]						pages;

	protected PagingListener			listener;

	protected int									pageElements;

	@ViewField(visible = AnnotationConstants.FALSE)
	protected boolean							pagingEnabled			= true;

	public static final String		QUERY_ALL_MESSAGE	= "queryAll";
	protected static final String	PAGE_LABEL				= "$page";
	protected static final String	TOTAL_ITEMS_LABEL	= "$totalItems";

	public CRUDPaging(PagingListener iListener, int iPageElements) {
		currentPage = 1;
		listener = iListener;
		pages = new Integer[] {};
		pageElements = iPageElements;
	}

	public CRUDPaging(PagingListener iListener) {
		this(iListener, DEF_PAGE_ELEMENTS);
	}

	public void onShow() {
		Roma.setFeature(this, "first", ViewActionFeatures.ENABLED, currentPage > 1);
		Roma.setFeature(this, "prev", ViewActionFeatures.ENABLED, currentPage > 1);
		Roma.setFeature(this, "next", ViewActionFeatures.ENABLED, currentPage < pages.length);
		Roma.setFeature(this, "last", ViewActionFeatures.ENABLED, currentPage < pages.length);
		Roma.setFeature(this, "pages", ViewFieldFeatures.ENABLED, pages.length > 1);
	}

	@Persistence(mode = PersistenceConstants.MODE_NOTX)
	@ViewAction(label = "")
	public void first() {
		setCurrentPage(1);
	}

	@Persistence(mode = PersistenceConstants.MODE_NOTX)
	@ViewAction(label = "")
	public void prev() {
		if (currentPage > 1)
			setCurrentPage(currentPage - 1);
	}

	@Persistence(mode = PersistenceConstants.MODE_NOTX)
	@ViewAction(label = "")
	public void next() {
		if (currentPage < pages.length)
			setCurrentPage(currentPage + 1);
	}

	@Persistence(mode = PersistenceConstants.MODE_NOTX)
	@ViewAction(label = "")
	public void last() {
		setCurrentPage(pages.length);
	}

	@Persistence(mode = PersistenceConstants.MODE_NOTX)
	@ViewAction(label = "")
	@FlowAction(confirmRequired = AnnotationConstants.TRUE, confirmMessage = "$CRUDMain.queryAll.confirm")
	public void queryAll() {
		if (pagingEnabled) {
			listener.loadAllPages();
			pagingEnabled(false);
		} else {
			first();
		}
	}

	private void pagingEnabled(boolean enabled) {
		Roma.setFeature(this, "pageLabel", ViewFieldFeatures.VISIBLE, enabled);
		Roma.setFeature(this, "pages", ViewFieldFeatures.VISIBLE, enabled);
		Roma.setFeature(this, "next", ViewActionFeatures.VISIBLE, enabled);
		Roma.setFeature(this, "prev", ViewActionFeatures.VISIBLE, enabled);
		Roma.setFeature(this, "first", ViewActionFeatures.VISIBLE, enabled);
		Roma.setFeature(this, "last", ViewActionFeatures.VISIBLE, enabled);
		Roma.setFeature(this, "queryAll", FlowActionFeatures.CONFIRM_REQUIRED, enabled);
		pagingEnabled = enabled;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	@Persistence(mode = PersistenceConstants.MODE_NOTX)
	public void setCurrentPage(int iCurrentPage) {
		if (pages.length == 0)
			// NO PAGES LOADED YET: TAKES NO EFFECT
			return;

		if (iCurrentPage == currentPage && pagingEnabled)
			return;

		if (!pagingEnabled)
			pagingEnabled(true);

		this.currentPage = iCurrentPage;
		refreshCurrentPage();
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public void refreshCurrentPage() {
		if (pagingEnabled) {
			int rangeFrom = (currentPage - 1) * getPageElements();
			listener.loadPage(rangeFrom, rangeFrom + getPageElements());
			Roma.fieldChanged(this, "pages");
			onShow();
		} else {
			listener.loadAllPages();
		}
	}

	@ViewField(label = "", render = "label")
	public String getPageLabel() {
		return Roma.i18n().resolve(listener, PAGE_LABEL, I18NType.LABEL);
	}

	public Integer[] getPages() {
		return pages;
	}

	/**
	 * Fill the select containing all available pages
	 * 
	 * @param iPageTotal
	 *          Total pages
	 */
	public void setPagesCount(int iPageTotal) {
		pages = new Integer[iPageTotal];
		for (int i = 0; i < iPageTotal; ++i)
			pages[i] = i + 1;

		Roma.fieldChanged(this, "pages");
	}

	public int getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
		int pagesOffset = totalItems % pageElements;
		setPagesCount(totalItems / pageElements + (pagesOffset != 0 ? 1 : 0));
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public int getPageElements() {
		return pageElements;
	}

	public void setPageElements(int pageElements) {
		this.pageElements = pageElements;
	}

	public boolean isPagingEnabled() {
		return pagingEnabled;
	}

	public void onDispose() {
	}
}