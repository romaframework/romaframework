package org.romaframework.frontend.domain.crud;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreField;
import org.romaframework.frontend.domain.crud.CRUDInstance;

public class GenericInstance<T> extends CRUDInstance<T> {

	@CoreField(useRuntimeType = AnnotationConstants.TRUE)
	public T getEntity() {
		return super.getEntity();
	}

}
