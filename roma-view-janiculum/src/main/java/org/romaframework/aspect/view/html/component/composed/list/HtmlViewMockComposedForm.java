package org.romaframework.aspect.view.html.component.composed.list;

import java.util.ArrayList;
import java.util.List;

import org.romaframework.aspect.view.html.area.HtmlViewScreenArea;
import org.romaframework.aspect.view.html.component.HtmlViewContentComponent;
import org.romaframework.aspect.view.html.component.composed.HtmlViewAbstractComposedComponent;
import org.romaframework.core.schema.SchemaField;

public class HtmlViewMockComposedForm extends HtmlViewAbstractComposedComponent {

	private static final ArrayList<String>	EMPTY_HEADER	= new ArrayList<String>();

	public HtmlViewMockComposedForm(HtmlViewContentComponent containerComponent, SchemaField schemaField, Object content,
			HtmlViewScreenArea screenArea) {
		super(containerComponent, schemaField, content, screenArea);		
	}

	
	
	public List<String> getHeaders() {

		return EMPTY_HEADER;
	}



	public List<String> getHeadersRaw() {
		return EMPTY_HEADER;
	}

}
