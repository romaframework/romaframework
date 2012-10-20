package org.romaframework.aspect.view.html.component.composed.tree;

import java.util.ArrayList;
import java.util.List;

import org.romaframework.aspect.view.html.area.HtmlViewScreenArea;
import org.romaframework.aspect.view.html.component.HtmlViewContentComponent;
import org.romaframework.aspect.view.html.component.HtmlViewGenericComponent;
import org.romaframework.core.schema.SchemaField;

public class HtmlViewTreeChildComponent extends HtmlViewTreeComposedComponent {

	

	public HtmlViewTreeChildComponent(HtmlViewContentComponent containerComponent, SchemaField schemaField, Object content,
			HtmlViewScreenArea screenArea, Integer level, Integer elemIndex) {
		super(containerComponent, schemaField, content, screenArea);
		this.level = level +1;
		this.elemIndex = elemIndex;
		
	}

	@Override
	public String getHtmlId() {
		return super.getHtmlId() + "_" + level +"_"+elemIndex;
	}
	
	
	public boolean isDirty() {
		return false;
	}
	
	public  List<?> orderedContent() {
		List<Object> result = new ArrayList<Object>();
		
		result.add(content);
		for (HtmlViewGenericComponent comp : getChildren()) {
			if (comp instanceof HtmlViewContentComponent) {
				result.addAll(((HtmlViewContentComponent) comp).orderedContent());
			}
		}
		
		return result;
	}
	
}
