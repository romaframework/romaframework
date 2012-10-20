package org.romaframework.frontend.domain.wrapper;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.core.config.GenericEventListener;
import org.romaframework.frontend.domain.entity.ComposedEntityInstance;

@CoreClass(orderFields = "selected entity")
public class SelectableItem<T> extends ComposedEntityInstance<T> {
	@ViewField(label = "")
	protected boolean								selected	= false;

	@ViewField(visible = AnnotationConstants.FALSE)
	protected GenericEventListener	listener;

	public SelectableItem() {
	}

	public SelectableItem(GenericEventListener iListener, T iEntity) {
		this(iEntity);
		listener = iListener;
	}

	public SelectableItem(T iEntity) {
		super(iEntity);
	}

	public boolean isSelected() {
		return selected;
	}

	/**
	 * Notify the listener about the selection
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
		if (listener != null)
			listener.callback(this);
	}

	public GenericEventListener getListener() {
		return listener;
	}

	public void setListener(GenericEventListener listener) {
		this.listener = listener;
	}
}
