package org.romaframework.aspect.view.html.component.composed.list;

import org.romaframework.aspect.view.html.area.HtmlViewScreenArea;
import org.romaframework.aspect.view.html.component.HtmlViewComposedComponent;
import org.romaframework.aspect.view.html.component.HtmlViewContentComponent;
import org.romaframework.aspect.view.html.component.HtmlViewGenericComponent;
import org.romaframework.aspect.view.html.component.HtmlViewInvisibleContentComponent;
import org.romaframework.aspect.view.html.component.HtmlViewSimpleComponent;
import org.romaframework.core.schema.SchemaField;

public class HtmlViewLabelCollectionComponent extends HtmlViewCollectionComposedComponent{

	public HtmlViewLabelCollectionComponent(HtmlViewContentComponent containerComponent, SchemaField schemaField, Object content,
			HtmlViewScreenArea screenArea) {
		super(containerComponent, schemaField, content, screenArea);
		// TODO Auto-generated constructor stub
	}

	protected void createForm(Integer rowIndex, Integer colIndex, final Object obj, String label, HtmlViewComposedComponent component,boolean toShow) {
		HtmlViewGenericComponent form;
		if(toShow){
			form = new HtmlViewSimpleComponent(component, this.getScreenAreaObject());
			
			form.setContent(obj);
		}else{
			form = new HtmlViewInvisibleContentComponent(this, rowIndex, obj, this.getScreenAreaObject(), label);
		}

		component.addComponent(form,toShow);  //TODO
	}
}
