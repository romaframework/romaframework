package org.romaframework.frontend.domain.crud;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreField;
import org.romaframework.frontend.domain.entity.ComposedEntityInstance;

public class GenericListable<T> extends ComposedEntityInstance<T> {

	@CoreField(useRuntimeType = AnnotationConstants.TRUE)
	public T getEntity() {
		return super.getEntity();
	}

}
