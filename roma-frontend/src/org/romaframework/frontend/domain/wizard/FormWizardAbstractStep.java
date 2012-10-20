package org.romaframework.frontend.domain.wizard;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.view.annotation.ViewField;

public abstract class FormWizardAbstractStep implements FormWizardStep {
	@ViewField(visible = AnnotationConstants.FALSE)
	protected FormWizard<?>	container;

	public void onShow() {
	}

	public boolean onBegin() {
		return true;
	}

	public boolean onBack() {
		return true;
	}

	public boolean onNext() {
		return true;
	}

	public boolean onFinish() {
		return true;
	}

	public boolean onCancel() {
		return true;
	}

	public void onDispose() {
	}

	public FormWizard<?> getContainer() {
		return container;
	}

	public void setContainer(FormWizard<?> container) {
		this.container = container;
	}
}
