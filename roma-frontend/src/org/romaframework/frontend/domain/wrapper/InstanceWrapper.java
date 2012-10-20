package org.romaframework.frontend.domain.wrapper;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreField;
import org.romaframework.aspect.flow.FlowAspect;
import org.romaframework.aspect.view.ViewCallback;
import org.romaframework.aspect.view.annotation.ViewAction;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.aspect.view.feature.ViewActionFeatures;
import org.romaframework.aspect.view.feature.ViewFieldFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.config.Refreshable;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;
import org.romaframework.frontend.domain.crud.CRUDWorkingMode;

public class InstanceWrapper implements CRUDWorkingMode, ViewCallback {

	protected Object			sourceObject;
	protected SchemaField	sourceField;

	@ViewField(visible = AnnotationConstants.FALSE)
	protected int					mode;

	@CoreField(expand = AnnotationConstants.TRUE, useRuntimeType = AnnotationConstants.TRUE)
	protected Object			wrapped;

	public InstanceWrapper(final Object sourceObject, final SchemaField sourceField, final Object wrapped, int mode) {
		this.sourceObject = sourceObject;
		this.sourceField = sourceField;
		this.wrapped = wrapped;
		this.mode = mode;
	}

	public Object getWrapped() {
		return wrapped;
	}

	public void setWrapped(final Object wrapped) {
		this.wrapped = wrapped;
	}

	public void save() {
		if (mode == MODE_CREATE) {
			final Object[] result = { wrapped };
			SchemaHelper.insertElements(sourceField, sourceObject, result);
		} else if (mode == MODE_UPDATE && this.sourceObject instanceof Refreshable) {
			((Refreshable) this.sourceObject).refresh();
		}
		Roma.fieldChanged(sourceObject, sourceField.getName());
		Roma.component(FlowAspect.class).back();
	}

	@ViewAction(bind = AnnotationConstants.FALSE)
	public void cancel() {
		Roma.component(FlowAspect.class).back();
	}

	public void onDispose() {

	}

	public void onShow() {
		if (this.mode == MODE_READ) {
			Roma.setFeature(this, "save", ViewActionFeatures.VISIBLE, false);
			Roma.setFeature(this, "wrapped", ViewFieldFeatures.ENABLED, false);
		}
	}

	public int getMode() {
		return mode;
	}
}
