package org.romaframework.frontend.domain.page;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.core.annotation.CoreClass.LOADING_MODE;
import org.romaframework.aspect.validation.annotation.ValidationField;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewAction;
import org.romaframework.aspect.view.annotation.ViewClass;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.core.Roma;
import org.romaframework.core.config.Refreshable;

@CoreClass(loading = LOADING_MODE.EARLY)
@ViewClass(label = "")
public class ContainerPage<T> implements Refreshable {

	@ViewField(render = ViewConstants.RENDER_TAB, label = "", position = "form://innerPages", selectionField = "activePage")
	@ValidationField
	protected Map<String, T>	innerPages	= new LinkedHashMap<String, T>();

	@ViewField(visible = AnnotationConstants.FALSE)
	protected Object							activePage;

	public void changePage(String iPageName) {
		setActivePage(innerPages.get(iPageName));
		Roma.fieldChanged(this, "innerPages");
	}

	public void addPage(String iKey, T iValue) {
		innerPages.put(iKey, iValue);
		if (activePage == null)
			activePage = iValue;
	}

	public void removePage(String iKey) {
		innerPages.remove(iKey);
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public int countPages() {
		return innerPages.size();
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public void clearPages() {
		innerPages.clear();
	}

	public Map<String, T> getInnerPages() {
		return innerPages;
	}

	public Object getActivePage() {
		return activePage;
	}

	public void setActivePage(Object activePage) {
		this.activePage = activePage;
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public String getActivePageName() {
		for (String s : innerPages.keySet()) {
			Object o = innerPages.get(s);
			if (o.equals(activePage)) {
				return s;
			}
		}
		return null;
	}

	public Object getPage(String key) {
		return innerPages.get(key);
	}

	public void changePage(int iActivePageNum) {
		Iterator<T> it = innerPages.values().iterator();
		for (int i = 0; i <= iActivePageNum; ++i) {
			activePage = it.next();
		}
		Roma.fieldChanged(this, "innerPages");
	}

	public Object getPage(int iPageNum) {
		Iterator<T> it = innerPages.values().iterator();
		for (int i = 0; i < iPageNum; ++i) {
			it.next();
		}

		if (it.hasNext())
			return it.next();
		return null;
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public void refresh() {
		Roma.fieldChanged(this, "innerPages");
	}
}
