package org.romaframework.aspect.view.html.component.composed.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.romaframework.aspect.view.feature.ViewBaseFeatures;
import org.romaframework.aspect.view.html.area.HtmlViewScreenArea;
import org.romaframework.aspect.view.html.component.HtmlViewContentComponent;
import org.romaframework.aspect.view.html.component.HtmlViewGenericComponent;
import org.romaframework.aspect.view.html.component.composed.HtmlViewAbstractComposedComponent;
import org.romaframework.core.domain.type.TreeNode;
import org.romaframework.core.schema.SchemaField;

public class HtmlViewTreeComposedComponent extends HtmlViewAbstractComposedComponent {

	private static final ArrayList<String>	EMPTY_HEADER	= new ArrayList<String>();
	protected Integer												elemIndex;
	protected Integer												level;

	public HtmlViewTreeComposedComponent(HtmlViewContentComponent containerComponent, SchemaField schemaField, Object content, HtmlViewScreenArea screenArea) {
		super(containerComponent, schemaField, content, screenArea);
		elemIndex = 0;
		level = 0;
	}

	public List<String> getHeaders() {
		return EMPTY_HEADER;
	}

	public List<String> getHeadersRaw() {
		return EMPTY_HEADER;
	}

	protected void placeComponents() {
		clearComponents();
		if (content == null) {
			return;
		}

		if (!(content instanceof TreeNode)) {
			log.error("Render " + getSchemaElement().getFeature(ViewBaseFeatures.RENDER) + "  supported only for List and Map elements");
		}

		TreeNode node = (TreeNode) content;

		Integer index = 0;
		if (node.getChildren() != null) {
			for (TreeNode child : node.getChildren()) {
				HtmlViewTreeChildComponent newChild = new HtmlViewTreeChildComponent(this.containerComponent, getSchemaField(), child, screenArea, level, index);
				newChild.setContent(child);
				addComponent(newChild);
				index = index + 1;
			}

		}
	}

	@Override
	public Collection<HtmlViewGenericComponent> getChildren() {
		final Collection<HtmlViewGenericComponent> children = super.getChildren();

		if (children != null) {
			return children;
		} else {
			return new ArrayList<HtmlViewGenericComponent>();
		}
	}

	@Override
	public String getHtmlId() {
		return super.getHtmlId();
	}

	@Override
	public void setContent(final Object content) {
		super.setContent(content);
		placeComponents();
	}

	public List<?> orderedContent() {
		List<Object> result = new ArrayList<Object>();

		result.add(content);
		for (HtmlViewGenericComponent comp : getChildren()) {
			if (comp instanceof HtmlViewContentComponent) {
				result.addAll(((HtmlViewContentComponent) comp).orderedContent());
			}
		}

		return result;
	}

	public boolean isDirty() {
		return super.isDirty();
	}

}
