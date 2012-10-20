/*
 *
 * Copyright 2010 Luca Molino (luca.molino--AT--assetdata.it)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.romaframework.frontend.domain.wizard;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreField;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.aspect.view.feature.ViewFieldFeatures;
import org.romaframework.core.Roma;

/**
 * @author luca.molino
 * 
 */
public abstract class FormWizardSplitStep extends FormWizardAbstractStep {

	@ViewField(visible = AnnotationConstants.FALSE)
	protected String												stepsSelection;

	@ViewField(visible = AnnotationConstants.FALSE)
	protected Map<String, FormWizardStep[]>	subSteps;

	@ViewField(visible = AnnotationConstants.FALSE)
	protected int														currentStep	= -1;

	@CoreField(useRuntimeType = AnnotationConstants.TRUE)
	@ViewField(render = ViewConstants.RENDER_OBJECTEMBEDDED, label = "")
	protected FormWizardStep								subContent;

	public FormWizardSplitStep() {
		super();
		subSteps = new HashMap<String, FormWizardStep[]>();
	}

	@Override
	public void onShow() {
		super.onShow();
		if (stepsSelection == null || stepsSelection.equals("")) {
			Roma.setFeature(this, "stepsToSelect", ViewFieldFeatures.VISIBLE, Boolean.TRUE);
			Roma.setFeature(this, "subContent", ViewFieldFeatures.VISIBLE, Boolean.FALSE);
		} else {
			Roma.setFeature(this, "subContent", ViewFieldFeatures.VISIBLE, Boolean.TRUE);
			Roma.setFeature(this, "stepsToSelect", ViewFieldFeatures.VISIBLE, Boolean.FALSE);
		}
		updateContent();
	}

	@Override
	public void onDispose() {
		super.onDispose();
		if (stepsSelection != null && subSteps != null) {
			for (FormWizardStep step : subSteps.get(stepsSelection)) {
				step.onDispose();
			}
		}
		if (subSteps != null)
			subSteps.clear();
		subSteps = null;
		subContent = null;
		stepsSelection = null;
	}

	@Override
	public boolean onNext() {
		if (stepsSelection == null || !subSteps.keySet().contains(stepsSelection)) {
			return false;
		} else if (currentStep < subSteps.get(stepsSelection).length - 1) {
			if (subContent == null || subContent.onNext()) {
				currentStep++;
				updateContent();
			}
			return false;
		} else {
			if (subContent != null && subContent.onNext()) {
				return super.onNext();
			} else {
				return false;
			}
		}
	}

	@Override
	public boolean onBack() {
		if (currentStep >= 0) {
			if (subContent.onBack()) {
				currentStep--;
				subContent.onDispose();
				subContent = null;
				updateContent();
			}
			return false;
		} else {
			return super.onBack();
		}
	}

	@ViewField(render = ViewConstants.RENDER_SELECT, selectionField = "stepsSelection")
	public Set<String> getStepsToSelect() {
		if (subSteps != null)
			return subSteps.keySet();
		return null;
	}

	public String getStepsSelection() {
		return stepsSelection;
	}

	public void setStepsSelection(String selectedSteps) {
		this.stepsSelection = selectedSteps;
		if (selectedSteps != null && getStepsToSelect().contains(selectedSteps)) {
			Roma.setFeature(this, "stepsToSelect", ViewFieldFeatures.VISIBLE, Boolean.FALSE);
			Roma.setFeature(this, "subContent", ViewFieldFeatures.VISIBLE, Boolean.TRUE);
			getContainer().next();
		}
	}

	public FormWizardStep getSubContent() {
		return subContent;
	}

	public void setSubContent(FormWizardStep subContent) {
		this.subContent = subContent;
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public boolean isBegin() {
		return currentStep == 0;
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public boolean isEnd() {
		if (stepsSelection != null)
			return currentStep == subSteps.get(stepsSelection).length - 1;
		return false;
	}

	protected void updateContent() {
		if (subContent != null)
			subContent.onDispose();
		if (currentStep > -1) {
			Roma.setFeature(this, "stepsToSelect", ViewFieldFeatures.VISIBLE, Boolean.FALSE);
			Roma.setFeature(this, "subContent", ViewFieldFeatures.VISIBLE, Boolean.TRUE);
			subContent = subSteps.get(stepsSelection)[currentStep];
			subContent.setContainer(getContainer());
			Roma.fieldChanged(this, "subContent");
			subContent.onShow();
		} else {
			Roma.setFeature(this, "stepsToSelect", ViewFieldFeatures.VISIBLE, Boolean.TRUE);
			Roma.setFeature(this, "subContent", ViewFieldFeatures.VISIBLE, Boolean.FALSE);
		}
	}

}
