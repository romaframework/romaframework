package org.romaframework.frontend.domain.reporting;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewField;

public class IconButton {
	private String	localType;
	ReportGenerator	parent;

	public IconButton(ReportGenerator parent, String localType) {
		this.localType = localType;
		this.parent = parent;
	}

	@ViewField(label = "", render = ViewConstants.RENDER_LINK)
	public String getLocalType() {
		return "$" + localType + ".gif";
	}

	public void onLocalType() throws FileNotFoundException, IOException {
		parent.setType(localType);
		parent.report();
	}
}
