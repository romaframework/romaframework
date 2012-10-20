package org.romaframework.frontend.domain.page;

import java.util.LinkedHashMap;
import java.util.Map;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewClass;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.frontend.domain.entity.ComposedEntityInstance;

@ViewClass(label = "")
public class ComposedEntityTabbedInstance<T> extends ComposedEntityInstance<T> {

	@ViewField(render = ViewConstants.RENDER_TAB, label = "", position = "form://innerPages", selectionField = "activePage")
	protected Map<String, Object>	innerPages	= new LinkedHashMap<String, Object>();

	@ViewField(visible = AnnotationConstants.FALSE)
	protected Object							activePage;

	public ComposedEntityTabbedInstance(T entity) {
		super(entity);
	}

	public Map<String, Object> getInnerPages() {
		return innerPages;
	}

	public Object getActivePage() {
		return activePage;
	}

	public void setActivePage(Object activePage) {
		this.activePage = activePage;
	}

}
