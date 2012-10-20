package org.romaframework.frontend.domain.crud;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.feature.CoreFieldFeatures;
import org.romaframework.aspect.view.ViewCallback;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.core.Roma;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaHelper;

public class ListPaging<T> implements PagingListener, ViewCallback {

	@ViewField(label = "", render = ViewConstants.RENDER_OBJECTEMBEDDED, position = "form://paging")
	protected CRUDPaging	paging;

	protected List<T>			realElements;

	@ViewField(render = ViewConstants.RENDER_TABLE, label = "", selectionField = "selected", position = "form://elements", enabled = AnnotationConstants.FALSE)
	protected List<T>			elements;

	@ViewField(visible = AnnotationConstants.FALSE)
	protected List<T>			selected	= new ArrayList<T>();

	protected SchemaClass	listableClass;

	protected ListPaging() {
		this(10);
	}

	protected ListPaging(int pageElements) {
		paging = new CRUDPaging(this, pageElements);
		List<SchemaClass> generics = SchemaHelper.getSuperclassGenericTypes(Roma.schema().getSchemaClass(this));
		listableClass = generics.get(0);
	}

	public ListPaging(Class<T> listableClass) {
		this(listableClass, CRUDPaging.DEF_PAGE_ELEMENTS);
	}

	public ListPaging(Class<T> listableClass, int pageElements) {
		this(Roma.schema().getSchemaClass(listableClass), pageElements);
	}

	public ListPaging(SchemaClass listableClass, int pageElements) {
		paging = new CRUDPaging(this, pageElements);
		this.listableClass = listableClass;
	}

	public void loadAllPages() {
		elements = realElements;
		Roma.fieldChanged(this, "elements");
	}

	public void loadPage(int iFrom, int iTo) {
		if (realElements.size() < iTo) {
			iTo = realElements.size();
			if (iFrom >= iTo) {
				iFrom = iTo < paging.getPageElements() ? 0 : iTo - paging.getPageElements();
			}
		}

		elements = realElements.subList(iFrom, iTo);
		Roma.fieldChanged(this, "elements");
	}

	public void onShow() {
		Roma.setFeature(this, "elements", CoreFieldFeatures.EMBEDDED_TYPE, listableClass);
		Roma.setFeature(this, "selected", CoreFieldFeatures.EMBEDDED_TYPE, listableClass);
	}

	public void onDispose() {
	}

	public void loadList(Collection<T> collection) {
		realElements = new ArrayList<T>(collection);
		selected = new ArrayList<T>();
		paging.setTotalItems(realElements.size());
		paging.refreshCurrentPage();
		Roma.fieldChanged(this, "paging");
	}

	protected List<T> getRealElements() {
		return realElements;
	}

	public void selectAll() {
		setSelected(new ArrayList<T>(getElements()));
		Roma.fieldChanged(this, "elements");
	}

	public void deselectAll() {
		setSelected(null);
		Roma.fieldChanged(this, "elements");
	}

	public CRUDPaging getPaging() {
		return paging;
	}

	public void setPaging(CRUDPaging paging) {
		this.paging = paging;
	}

	public List<T> getElements() {
		return elements;
	}

	public void setElements(List<T> elements) {
		this.elements = elements;
	}

	public List<T> getSelected() {
		return selected;
	}

	public void setSelected(List<T> selected) {
		this.selected = selected;
	}

}
