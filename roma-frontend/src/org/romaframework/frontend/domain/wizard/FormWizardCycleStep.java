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

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.view.annotation.ViewField;

/**
 * @author luca.molino
 * 
 */
public abstract class FormWizardCycleStep extends FormWizardAbstractStep {

	@Override
	public boolean onNext() {
		if (isCycling()) {
			onNextCycle();
			return false;
		} else {
			return onNextStep();
		}
	}

	@Override
	public boolean onBack() {
		if (isCycling()) {
			onBackCycle();
			return false;
		} else {
			return onBackStep();
		}
	}

	@Override
	public boolean onFinish() {
		if (isCycling()) {
			return false;
		} else {
			return onNextStep();
		}
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public abstract boolean isCycling();

	protected abstract void onNextCycle();

	protected abstract void onBackCycle();

	protected abstract boolean onNextStep();

	protected abstract boolean onBackStep();

}
