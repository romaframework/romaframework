package org.romaframework.aspect.view.html.transformer.freemarker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.feature.ViewActionFeatures;
import org.romaframework.aspect.view.feature.ViewFieldFeatures;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClassElement;
import org.romaframework.core.schema.SchemaElement;
import org.romaframework.core.schema.SchemaField;

public class LayoutExpandHelper {

	public static boolean isLayoutExpand(SchemaField field) {
		Object layout = field.getFeature(ViewFieldFeatures.POSITION);
		return ViewConstants.LAYOUT_EXPAND.equals(layout);
	}

	public static boolean isLayoutExpand(SchemaElement element) {
		return (element instanceof SchemaField) ? isLayoutExpand((SchemaField) element) : false;
	}

	public static boolean isVisible(SchemaClassElement element) {
		Boolean visible = false;
		if (element instanceof SchemaAction) {
			visible = element.getFeature(ViewActionFeatures.VISIBLE);
		} else if (element instanceof SchemaField) {
			visible = element.getFeature(ViewFieldFeatures.VISIBLE);
		}
		return Boolean.TRUE.equals(visible);
	}

	public static List<SchemaClassElement> expand(SchemaField field) {
		List<SchemaClassElement> elements = new ArrayList<SchemaClassElement>();
		Iterator<SchemaField> fieldIterator = field.getType().getFieldIterator();
		Iterator<SchemaAction> actionIterator = field.getType().getActionIterator();
		while (fieldIterator.hasNext()) {
			elements.add(fieldIterator.next());
		}
		while (actionIterator.hasNext()) {
			elements.add(actionIterator.next());
		}
		return elements;
	}

}
