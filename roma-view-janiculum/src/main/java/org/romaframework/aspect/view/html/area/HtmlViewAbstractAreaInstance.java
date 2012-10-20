package org.romaframework.aspect.view.html.area;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.romaframework.aspect.view.area.AreaComponent;
import org.romaframework.aspect.view.html.HtmlViewAspectHelper;
import org.romaframework.aspect.view.html.HtmlViewSession;
import org.romaframework.aspect.view.html.transformer.Transformer;
import org.romaframework.aspect.view.html.transformer.manager.TransformerManager;
import org.romaframework.core.Roma;
import org.romaframework.core.domain.type.TreeNode;
import org.romaframework.core.domain.type.TreeNodeLinkedHashMap;

public abstract class HtmlViewAbstractAreaInstance extends TreeNodeLinkedHashMap implements HtmlViewArea {

	/**
	 * The previosly generated html
	 */
	protected String	htmlString;
	protected boolean	visible;
	protected int			areaSize;
	protected String	areaAlign;
	protected String	areaStyle;
	protected boolean	dirty	= true;
	protected Long		id;

	public HtmlViewAbstractAreaInstance(final TreeNodeLinkedHashMap parent, final String name) {
		super(parent, name);

	}

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible
	 *          the visible to set
	 */
	public void setVisible(final boolean visible) {
		this.visible = visible;
	}

	/**
	 * @return the areaSize
	 */
	public Integer getAreaSize() {
		return areaSize;
	}

	/**
	 * @param areaSize
	 *          the areaSize to set
	 */
	public void setAreaSize(final int areaSize) {
		this.areaSize = areaSize;
	}

	/**
	 * @return the areaAlign
	 */
	public String getAreaAlign() {
		return areaAlign;
	}

	/**
	 * @param areaAlign
	 *          the areaAlign to set
	 */
	public void setAreaAlign(final String areaAlign) {
		this.areaAlign = areaAlign;
	}

	/**
	 * @return the areaStyle
	 */
	public String getAreaStyle() {
		return areaStyle;
	}

	/**
	 * @param areaStyle
	 *          the areaAlign to set
	 */
	public void setAreaStyle(final String areaStyle) {
		this.areaStyle = areaStyle;
	}

	/**
	 * @return the htmlString
	 */
	public String getHtmlString() {
		return htmlString;
	}

	public AreaComponent searchArea(final String areaName) {
		return (AreaComponent) searchNode(areaName);
	}

	public long getId() {
		if (id == null) {
			final HtmlViewSession session = HtmlViewAspectHelper.getHtmlViewSession();
			id = session.addRenderableBinding((HtmlViewRenderable) this);
		}
		return id;
	}

	public Transformer getTransformer() {
		if (getType() == null) {
			return Roma.component(TransformerManager.class).getComponent(AreaComponent.DEF_AREAMODE_NAME);
		}
		return Roma.component(TransformerManager.class).getComponent(getType());

	}

	public void render(Writer writer) throws IOException {
		getTransformer().transform(this, writer);
	}

	@Override
	public void renderPart(String part, Writer writer) throws IOException {
		getTransformer().transformPart(this, part, writer);
	}

	public abstract String getType();

	@Override
	public String toString() {
		final StringBuilder buffer = new StringBuilder();
		buffer.append(getName());
		buffer.append(":");
		buffer.append(getType());

		if (childrenMap != null) {
			buffer.append("{");
			for (final TreeNode c : childrenMap.values()) {
				if (buffer.charAt(buffer.length() - 1) != '{') {
					buffer.append(",");
				}
				buffer.append(c.toString());
			}
			buffer.append("}");
		}
		return buffer.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<HtmlViewRenderable> getComponents() {
		if (getChildren() != null)
			return new ArrayList<HtmlViewRenderable>((Collection) getChildren());
		return new ArrayList<HtmlViewRenderable>();
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public String getStyle() {
		return areaStyle;
	}

	public void setStyle(String areaStyle) {
		this.areaStyle = areaStyle;
	}
}
