package org.romaframework.frontend.domain.wrapper;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.core.binding.Bindable;
import org.romaframework.core.domain.type.Pair;
import org.romaframework.core.schema.SchemaField;

@CoreClass(orderFields = "key value")
public class EntryWrapperForm extends Pair<TextWrapperForm, TextWrapperForm> implements Bindable {
	public EntryWrapperForm() {
	}

	public EntryWrapperForm(TextWrapperForm key, TextWrapperForm value) {
		super(key, value);
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public SchemaField getSourceField() {
		return null;
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public Object getSourceObject() {
		return null;
	}

	public void setSource(Object sourceObject, String sourceFieldName) {
	}
}
