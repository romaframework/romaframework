package org.romaframework.frontend.domain.query;

import java.util.List;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewField;

public class StringChooser {
	private AttributeChooser	parent;
	private int								position;

	@ViewField(render = ViewConstants.RENDER_SELECT, selectionField = "selectedString", label = "")
	private List<String>			strings;
	@ViewField(visible = AnnotationConstants.FALSE)
	private String						selectedString;

	public StringChooser(AttributeChooser parent, int position, List<String> strings) {
		this.parent = parent;
		this.position = position;
		this.strings = strings;
	}

	public List<String> getStrings() {
		return strings;
	}

	public void setSelectedString(String selectedString) {
		this.selectedString = selectedString;
		if (parent != null) {
			parent.fieldChanged(position);
		}
	}

	public String getSelectedString() {
		return this.selectedString;
	}

}
