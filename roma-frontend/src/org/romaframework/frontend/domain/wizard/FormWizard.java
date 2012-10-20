package org.romaframework.frontend.domain.wizard;

import java.util.HashMap;
import java.util.Map;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreField;
import org.romaframework.aspect.validation.annotation.ValidationAction;
import org.romaframework.aspect.view.ViewCallback;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.aspect.view.feature.ViewActionFeatures;
import org.romaframework.core.Roma;

public class FormWizard<T> implements ViewCallback {
	protected FormWizardStep[]		steps;

	protected int									currentStep		= 0;

	@ViewField(render = ViewConstants.RENDER_OBJECTEMBEDDED)
	@CoreField(useRuntimeType = AnnotationConstants.TRUE)
	protected FormWizardStep			content;

	protected Map<String, Object>	configuration	= new HashMap<String, Object>();

	public FormWizard(FormWizardStep[] steps) {
		this.steps = steps;
	}

	public void onShow() {
		showContent();
	}

	public void begin() {
		if (content.onBegin()) {
			beginAction();
		}
	}

	public void back() {
		if (currentStep > 0) {
			if (content.onBack()) {
				backAction();
			}
		}
	}

	@ValidationAction(validate = AnnotationConstants.TRUE)
	public void next() {
		if (currentStep < steps.length - 1) {

			if (content.onNext()) {
				nextAction();
			}
		}
	}

	@ValidationAction(validate = AnnotationConstants.TRUE)
	public void finish() {
		if (content.onFinish()) {

			finishAction();
		}
	}

	protected void finishAction() {
		currentStep = steps.length - 1;
		showContent();
	}

	public void cancel() {
		if (content.onCancel()) {
			cancelAction();
		}
	}

	public FormWizardStep getContent() {
		return content;
	}

	public FormWizardStep getStep(int iIndex) {
		return steps[iIndex];
	}

	@SuppressWarnings({ "unchecked", "hiding" })
	public <T> T getStep(Class<T> iClass) {
		for (FormWizardStep s : steps)
			if (s.getClass().equals(iClass))
				return (T) s;

		return null;
	}

	public void onDispose() {
		for (FormWizardStep step : steps) {
			step.onDispose();
		}
	}

	protected void beginAction() {
		currentStep = 0;
		showContent();
	}

	protected void backAction() {
		currentStep--;
		showContent();
	}

	protected void nextAction() {
		currentStep++;
		showContent();
	}

	protected void cancelAction() {
		Roma.flow().back();
	}

	protected void showContent() {
		onBeforeShowContent();

		changeContent();

		onAfterShowContent();
	}

	protected void changeContent() {
		content = steps[currentStep];

		content.setContainer(this);
		content.onShow();
		Roma.fieldChanged(this, "content");
	}

	protected void onBeforeShowContent() {
		if (content != null)
			content.onDispose();
	}

	protected void onAfterShowContent() {
		Roma.setFeature(this, "begin", ViewActionFeatures.ENABLED, currentStep > 0);
		Roma.setFeature(this, "back", ViewActionFeatures.ENABLED, currentStep > 0);
		Roma.setFeature(this, "next", ViewActionFeatures.ENABLED, currentStep < steps.length - 1);
		Roma.setFeature(this, "finish", ViewActionFeatures.ENABLED, currentStep == steps.length - 1);
	}

}
