package org.romaframework.module.users.view.domain.customprofiling;

import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.frontend.domain.entity.ComposedEntityInstance;
import org.romaframework.module.users.domain.CustomFunction;

public class CustomFunctionListable extends ComposedEntityInstance<CustomFunction> {

	@ViewField (render=ViewConstants.RENDER_LABEL)
	private String	label;

	public CustomFunctionListable(String label, CustomFunction baseFunction) {
		super(baseFunction);
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
