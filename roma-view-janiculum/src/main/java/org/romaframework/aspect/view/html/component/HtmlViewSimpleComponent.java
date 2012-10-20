package org.romaframework.aspect.view.html.component;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.romaframework.aspect.view.form.ViewComponent;
import org.romaframework.aspect.view.html.area.HtmlViewRenderable;
import org.romaframework.aspect.view.html.area.HtmlViewScreenArea;
import org.romaframework.aspect.view.html.transformer.AbstractHtmlViewTransformer;
import org.romaframework.aspect.view.html.transformer.Transformer;
import org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper;
import org.romaframework.core.schema.SchemaHelper;
import org.romaframework.core.schema.SchemaObject;

public class HtmlViewSimpleComponent extends HtmlViewAbstractContentComponent implements HtmlViewContentComponent {

	private static final ArrayList<HtmlViewGenericComponent>	children	= new ArrayList<HtmlViewGenericComponent>();
	private final ViewComponent																contentComponent;

	public HtmlViewSimpleComponent(final ViewComponent iContentComponent, final HtmlViewScreenArea area) {
		super((HtmlViewContentComponent) iContentComponent.getContainerComponent(), iContentComponent.getSchemaField(), iContentComponent.getContent(), area);
		contentComponent = iContentComponent;
	}

	@Override
	public Transformer getTransformer() {
		return null;
	}

	@Override
	public void render(Writer writer) throws IOException {

		if (getSchemaElement() == null || contentComponent == null) {
			return;
		}

		if (!((HtmlViewContentComponent) contentComponent).hasLabel()) {
			return;
		}


		String label = JaniculumWrapper.i18NLabel(this);
		label = AbstractHtmlViewTransformer.getComponentLabel((HtmlViewContentComponent) contentComponent, label);

		writer.write(label);

	}

	public void resetValidation() {
	}


	@Override
	public void renderPart(final String part, Writer writer) {
		// Makes nothing
	}

	@Override
	public String getHtmlId() {
		return ((HtmlViewRenderable) containerComponent).getHtmlId() + SEPARATOR + getSchemaElement().getName();
	}

	public SchemaObject getSchemaInstance() {
		return null;
	}

	public void setMetaDataSchema(SchemaObject schemaObject) {
	}

	public Collection<HtmlViewGenericComponent> getChildren() {
		return children;
	}

	public boolean hasLabel() {
		return true;
	}

	public Set<Integer> selectedIndex() {
		Set<Integer> result = new HashSet<Integer>();
		Object parent = this.containerComponent.getContent();
		if (parent == null) {
			return result;
		}

		if (getSelectionFieldName() == null) {
			return result;
		}
		List<?> elements = orderedContent();
		Object selectionFieldValue = SchemaHelper.getFieldValue(getSelectionSchemaClassDefinition(), getSelectionFieldName(), parent);
		if (selectionFieldValue != null) {
			Object[] selected = null;
			if (isSingleSelection()) {
				selected = new Object[] { selectionFieldValue };
			} else {
				selected = SchemaHelper.getObjectArrayForMultiValueObject(selectionFieldValue);
			}

			for (Object obj : selected) {
				if (elements.contains(obj)) {
					result.add(elements.indexOf(obj));
				}
			}

		}
		return result;
	}

	public void clearChildren() {
		children.clear();
	}

}
