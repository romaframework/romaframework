package org.romaframework.frontend.domain.wizard;

import org.romaframework.aspect.view.ViewCallback;

public interface FormWizardStep extends ViewCallback {
	public FormWizard<?> getContainer();

	public void setContainer(FormWizard<?> iContainer);

	public String getInformation();

	public boolean onBegin();

	public boolean onBack();

	public boolean onNext();

	public boolean onFinish();

	public boolean onCancel();
}
