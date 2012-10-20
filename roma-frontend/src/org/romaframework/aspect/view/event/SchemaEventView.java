package org.romaframework.aspect.view.event;

import java.util.ArrayList;

import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaParameter;
import org.romaframework.frontend.domain.crud.CRUDWorkingMode;

public class SchemaEventView extends SchemaEventEdit {

	private static final long	serialVersionUID	= -735731798334727958L;

	public SchemaEventView(SchemaField field) {
		super(field, SchemaEvent.COLLECTION_VIEW_EVENT, new ArrayList<SchemaParameter>());
	}

	@Override
	protected int getOpenMode() {
		return CRUDWorkingMode.MODE_READ;
	}

}
