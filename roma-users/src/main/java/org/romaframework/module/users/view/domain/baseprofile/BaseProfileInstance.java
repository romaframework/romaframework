/*
 * Copyright 2006 Luca Garulli (luca.garulli--at--assetdata.it)
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

package org.romaframework.module.users.view.domain.baseprofile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.reporting.annotation.ReportingField;
import org.romaframework.aspect.session.SessionInfo;
import org.romaframework.aspect.validation.CustomValidation;
import org.romaframework.aspect.validation.ValidationException;
import org.romaframework.aspect.view.SelectionMode;
import org.romaframework.aspect.view.ViewCallback;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.core.Roma;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaManager;
import org.romaframework.frontend.domain.crud.CRUDInstance;
import org.romaframework.module.users.domain.BaseFunction;
import org.romaframework.module.users.domain.BaseProfile;
import org.romaframework.module.users.domain.BaseProfile.Mode;
import org.romaframework.module.users.view.domain.basefunction.BaseFunctionListable;

@CoreClass(orderFields = "entity availableClassNames availableMembers functions")
public class BaseProfileInstance extends CRUDInstance<BaseProfile> implements CustomValidation, ViewCallback {
	private static final String						DEF_HOME_PAGE	= "HomePage";

	private static final String						CLASS_ELEMENT	= "< Class >";

	@ViewField(selectionField = "selectedAvailableClassName", enabled = AnnotationConstants.FALSE)
	@ReportingField(visible = AnnotationConstants.FALSE)
	protected ArrayList<String>						availableClassNames;

	@ViewField(selectionField = "selectedAvailableMember", enabled = AnnotationConstants.FALSE)
	protected ArrayList<String>						availableMembers;

	@ViewField(visible = AnnotationConstants.FALSE)
	protected String											selectedAvailableMember;

	protected SchemaClass									selectedAvailableClass;

	@ViewField(visible = AnnotationConstants.FALSE)
	protected BaseFunction								selectedFunction;

	@ViewField(selectionField = "selectedFunction", enabled = AnnotationConstants.FALSE, render = ViewConstants.RENDER_TABLEEDIT)
	protected List<BaseFunctionListable>	functions			= new ArrayList<BaseFunctionListable>();

	@ViewField(label = "Mode", render = ViewConstants.RENDER_SELECT, selectionField = "entity.mode", selectionMode = SelectionMode.SELECTION_MODE_INDEX)
	@ReportingField(visible = AnnotationConstants.FALSE)
	public Mode[] getModes() {
		return BaseProfile.Mode.values();
	}

	public List<BaseFunctionListable> getFunctions() {
		functions.clear();

		for (BaseFunction f : entity.getFunctions().values())
			functions.add(new BaseFunctionListable(f));

		return functions;
	}

	@Override
	public void onCreate() {
		BaseProfile newProfile;
		newProfile = new BaseProfile();
		setEntity(newProfile);
		getEntity().setMode(BaseProfile.Mode.ALLOW_ALL_BUT);
		getEntity().setFunctions(new TreeMap<String, BaseFunction>());
		getEntity().setHomePage(DEF_HOME_PAGE);
	}

	@Override
	public void save() {
		// CHECK FOR FUNCTION UPDATING
		String key;

		boolean found = false;
		while (!found) {
			found = true;

			if (getEntity().getFunctions() != null) {
				for (Entry<String, BaseFunction> fun : getEntity().getFunctions().entrySet()) {
					key = fun.getKey();
					if (!getEntity().getFunctions().get(key).getName().equals(key)) {
						// FOUND INCHOERENCE: FIX IT
						getEntity().getFunctions().put(fun.getValue().getName(), fun.getValue());
						getEntity().getFunctions().remove(key);

						found = false;
						break;
					}
				}
			}
		}

		super.save();

		// UPDATE ALL IN-MEMORY USERS ACCOUNT
		Collection<SessionInfo> activeSessions = Roma.session().getSessionInfos();

		synchronized (activeSessions) {
			for (SessionInfo s : activeSessions) {
				if (s.getAccount() != null && s.getAccount().getProfile().equals(getEntity()))
					// REPLACE IT WITH CURRENT ENTITY
					s.getAccount().setProfile(getEntity());
			}
		}
	}

	@Override
	public void onShow() {
		SchemaManager schemaMgr = Roma.schema();
		Collection<SchemaClass> infos = schemaMgr.getAllClassesInfo();

		availableClassNames = new ArrayList<String>();
		for (SchemaClass cls : infos)
			availableClassNames.add(cls.getName());

		Collections.sort(availableClassNames);
		Roma.fieldChanged(this, "availableClassNames");
	}

	public void setSelectedAvailableClassName(String iSelection) {
		availableMembers = new ArrayList<String>();
		selectedAvailableClass = Roma.schema().getSchemaClass(iSelection);
		availableMembers.add(CLASS_ELEMENT);

		for (Iterator<SchemaAction> it = selectedAvailableClass.getActionIterator(); it.hasNext();)
			availableMembers.add(it.next().getName() + "()");

		for (Iterator<SchemaField> it = selectedAvailableClass.getFieldIterator(); it.hasNext();)
			availableMembers.add(it.next().getName());

		Roma.fieldChanged(this, "availableMembers");
	}

	public void add() {
		if (availableMembers == null)
			return;

		String funcName = selectedAvailableClass.getName();
		if (!selectedAvailableMember.equals(CLASS_ELEMENT)) {
			funcName += "." + selectedAvailableMember;
			if (funcName.endsWith("()"))
				funcName = funcName.substring(0, funcName.length() - 2);
		}

		boolean allowFunction = getEntity().getMode() == null || getEntity().getMode()== BaseProfile.Mode.DENY_ALL_BUT;

		getEntity().addFunction(funcName, allowFunction);

		Roma.fieldChanged(this, "functions");
	}

	public void remove() {
		if (selectedFunction == null)
			return;

		getEntity().removeFunction(selectedFunction.getName());

		Roma.fieldChanged(this, "functions");
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public String getSelectedAvailableClassName() {
		if (selectedAvailableClass != null)
			return selectedAvailableClass.getName();
		return null;
	}

	public ArrayList<String> getAvailableMembers() {
		return availableMembers;
	}

	public void setAvailableMembers(ArrayList<String> availableMembers) {
		this.availableMembers = availableMembers;
	}

	public String getSelectedAvailableMember() {
		return selectedAvailableMember;
	}

	public void setSelectedAvailableMember(String selectedAvailableMember) {
		this.selectedAvailableMember = selectedAvailableMember;
	}

	public void validate() throws ValidationException {
	}

	public ArrayList<String> getAvailableClassNames() {
		return availableClassNames;
	}

	public void setAvailableClassNames(ArrayList<String> availableClassNames) {
		this.availableClassNames = availableClassNames;
	}

	public BaseFunction getSelectedFunction() {
		return selectedFunction;
	}

	public void setSelectedFunction(BaseFunction selectedFunction) {
		this.selectedFunction = selectedFunction;
	}
	

}