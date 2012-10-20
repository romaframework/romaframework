/*
 * Copyright 2006-2007 Luca Garulli (luca.garulli--at--assetdata.it)
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

package org.romaframework.aspect.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.core.feature.CoreFieldFeatures;
import org.romaframework.aspect.session.SessionAspect;
import org.romaframework.aspect.session.SessionInfo;
import org.romaframework.aspect.session.SessionListener;
import org.romaframework.aspect.view.command.impl.ChangeScreenViewCommand;
import org.romaframework.aspect.view.command.impl.RefreshViewCommand;
import org.romaframework.aspect.view.command.impl.ShowViewCommand;
import org.romaframework.aspect.view.event.SchemaEventAdd;
import org.romaframework.aspect.view.event.SchemaEventAddInline;
import org.romaframework.aspect.view.event.SchemaEventDown;
import org.romaframework.aspect.view.event.SchemaEventEdit;
import org.romaframework.aspect.view.event.SchemaEventOpen;
import org.romaframework.aspect.view.event.SchemaEventRemove;
import org.romaframework.aspect.view.event.SchemaEventReset;
import org.romaframework.aspect.view.event.SchemaEventSearch;
import org.romaframework.aspect.view.event.SchemaEventUp;
import org.romaframework.aspect.view.event.SchemaEventView;
import org.romaframework.aspect.view.feature.ViewActionFeatures;
import org.romaframework.aspect.view.feature.ViewClassFeatures;
import org.romaframework.aspect.view.feature.ViewFieldFeatures;
import org.romaframework.aspect.view.form.ContentForm;
import org.romaframework.aspect.view.form.FormViewer;
import org.romaframework.aspect.view.form.ViewComponent;
import org.romaframework.aspect.view.screen.Screen;
import org.romaframework.core.Roma;
import org.romaframework.core.Utility;
import org.romaframework.core.exception.UserException;
import org.romaframework.core.flow.Controller;
import org.romaframework.core.flow.ObjectRefreshListener;
import org.romaframework.core.module.SelfRegistrantConfigurableModule;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassResolver;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;
import org.romaframework.core.schema.SchemaObject;
import org.romaframework.core.schema.SchemaReloadListener;
import org.romaframework.core.schema.config.SchemaConfiguration;
import org.romaframework.core.schema.reflection.SchemaClassReflection;
import org.romaframework.core.schema.xmlannotations.XmlAspectAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlClassAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlFormAnnotation;

/**
 * View Aspect abstract implementation. It configures the ViewAspect from Java5 and XML annotations. This is a good starting point
 * for all View aspect implementations by extending this.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * 
 */
public abstract class ViewAspectAbstract extends SelfRegistrantConfigurableModule<String> implements ViewAspect, SessionListener, SchemaReloadListener, ObjectRefreshListener {
	protected Map<SessionInfo, Map<Object, ViewComponent>>	objectsForms;

	private static Log																			log	= LogFactory.getLog(ViewAspectAbstract.class);

	public ViewAspectAbstract() {
		Controller.getInstance().registerListener(SessionListener.class, this);
		Controller.getInstance().registerListener(SchemaReloadListener.class, this);

		objectsForms = Collections.synchronizedMap(new HashMap<SessionInfo, Map<Object, ViewComponent>>());
	}

	@Override
	public void startup() {
		// REGISTER THE VIEW DOMAIN TO SCHEMA CLASS RESOLVER
		Roma.component(SchemaClassResolver.class).addDomainPackage(Utility.getApplicationAspectPackage(aspectName()));
	}

	@Override
	public void shutdown() {
		objectsForms.clear();
	}

	public void beginConfigClass(SchemaClassDefinition iClass) {
	}

	public void endConfigClass(SchemaClassDefinition iClass) {
		updateFieldDependencies(iClass);
	}

	protected void updateFieldDependencies(SchemaClassDefinition iClass) {
		Iterator<SchemaField> iterator = iClass.getFieldIterator();
		while (iterator.hasNext()) {
			SchemaField iField = iterator.next();
			String[] dependsOnList = iField.getFeature(ViewFieldFeatures.DEPENDS_ON);
			if (dependsOnList != null) {
				for (String fieldName : dependsOnList) {
					SchemaField dependsFieldSchema = iClass.getField(fieldName);
					if (dependsFieldSchema == null)
						continue;
					String[] fieldDepends = dependsFieldSchema.getFeature(ViewFieldFeatures.DEPENDS);
					if (fieldDepends == null)
						fieldDepends = new String[0];
					Set<String> fieldDependsList = new HashSet<String>(Arrays.asList(fieldDepends));
					fieldDependsList.add(iField.getName());
					dependsFieldSchema.setFeature(ViewFieldFeatures.DEPENDS, fieldDependsList.toArray(new String[] {}));
				}
			}
		}
	}

	public void configClass(SchemaClassDefinition iClass) {
		XmlClassAnnotation xmlNode = null;
		if (iClass instanceof SchemaClassReflection) {
			SchemaConfiguration conf = ((SchemaClassReflection) iClass).getDescriptor();
			if (conf != null)
				xmlNode = conf.getType();
		}
		if (xmlNode == null || xmlNode.aspect(ASPECT_NAME) == null)
			return;

		XmlAspectAnnotation featureDescriptor = xmlNode.aspect(ASPECT_NAME);

		if (featureDescriptor != null) {
			XmlFormAnnotation layout = featureDescriptor.getForm();
			if (layout != null && layout.getRootArea() != null)
				iClass.setFeature(ViewClassFeatures.FORM, layout.getRootArea());

		}
	}

	public void configField(SchemaField iField) {

		if (iField.getFeature(CoreFieldFeatures.EXPAND)) {
			iField.setFeature(ViewFieldFeatures.VISIBLE, false);
		}

		if (iField.getEntity().getFeature(ViewClassFeatures.EXPLICIT_ELEMENTS)) {
			if (!iField.isSettedFeature(ViewFieldFeatures.VISIBLE) && iField.getDescriptorInfo() == null) {
				iField.setFeature(ViewFieldFeatures.VISIBLE, false);
			}
		}

		if (iField.getFeature(CoreFieldFeatures.EMBEDDED)) {
			if (iField.getFeature(ViewFieldFeatures.RENDER) == null && !SchemaHelper.isMultiValueObject(iField))
				// IF THE FIELD IS EMBEDDED, THEN THE DEFAULT RENDER IS OBJECTEMBEDDED
				iField.setFeature(ViewFieldFeatures.RENDER, ViewConstants.RENDER_OBJECTEMBEDDED);
		}

		String classRender = iField.getEntity().getFeature(ViewClassFeatures.RENDER);
		if (classRender != null)
			if (classRender.equals(ViewConstants.RENDER_MENU)) {
				// INSIDE A MENU: FORCE MENU RENDERING AND LAYOUT
				iField.setFeature(ViewFieldFeatures.RENDER, ViewConstants.RENDER_MENU);
			} else if (classRender.equals(ViewConstants.RENDER_ACCORDION)) {
				// INSIDE AN ACCORDITION: FORCE ACCORDITION LAYOUT
				iField.setFeature(ViewFieldFeatures.RENDER, ViewConstants.RENDER_ACCORDION);
			}

		if (SchemaHelper.isMultiValueObject(iField)) {
			iField.setEvent(new SchemaEventAddInline(iField));
			iField.setEvent(new SchemaEventAdd(iField));
			iField.setEvent(new SchemaEventView(iField));
			iField.setEvent(new SchemaEventEdit(iField));
			iField.setEvent(new SchemaEventRemove(iField));
			iField.setEvent(new SchemaEventUp(iField));
			iField.setEvent(new SchemaEventDown(iField));
		} else if (iField.getType() != null && !SchemaHelper.isJavaType(iField.getType().getName())) {
			iField.setEvent(new SchemaEventOpen(iField));
			iField.setEvent(new SchemaEventReset(iField));
			iField.setEvent(new SchemaEventSearch(iField));
		}
	}

	public void configAction(SchemaAction iAction) {

		if (((SchemaAction) iAction).getParameterNumber() > 0)
			iAction.setFeature(ViewActionFeatures.VISIBLE, Boolean.FALSE);
		iAction.toString();

		if (iAction.getEntity().getFeature(ViewClassFeatures.EXPLICIT_ELEMENTS)) {
			if (!iAction.isSettedFeature(ViewActionFeatures.VISIBLE) && iAction.getDescriptorInfo() == null) {
				iAction.setFeature(ViewActionFeatures.VISIBLE, false);
			}
		}
		// CHECK RENDER AND LAYOUT MODES
		String classRender = iAction.getEntity().getFeature(ViewClassFeatures.RENDER);

		if (classRender != null)
			if (classRender.equals(ViewConstants.RENDER_MENU)) {
				// INSIDE A MENU: FORCE MENU RENDERING AND LAYOUT
				iAction.setFeature(ViewActionFeatures.RENDER, ViewConstants.RENDER_MENU);
			}
	}

	/**
	 * Display the form reading information from POJO received in the current desktop, in default position.
	 * 
	 * @param iContent
	 *          Object instance to display
	 */
	public void show(Object iContent) throws ViewException {
		show(iContent, null);
	}

	/**
	 * Display the form reading information from POJO received following the layout rules. Display the object on iWhere position in
	 * the current desktop.
	 * 
	 * @param iContent
	 * @param iPosition
	 * @throws ViewException
	 */
	public void show(Object iContent, String iPosition) throws ViewException {
		show(iContent, iPosition, null, null);
	}

	/**
	 * Display the form reading information from POJO received following the layout rules. Display the object on iWhere position in
	 * the current desktop.
	 * 
	 * @param iContent
	 * @param iPosition
	 * @throws ViewException
	 */
	public void show(Object iContent, String iPosition, Screen iScreen, SessionInfo iSession) throws ViewException {
		show(iContent, iPosition, iScreen, iSession, null);
	}

	/**
	 * Display the form reading information from POJO received following the layout rules. Display the object on iWhere position in
	 * the desktop received as the argument iDesktop.
	 * 
	 * @param iContent
	 *          Object instance to display
	 * @param iPosition
	 *          Desktop position where render the object
	 * @param iScreen
	 *          Desktop instance to use
	 * @throws Exception
	 */
	public void show(Object iContent, String iPosition, Screen iScreen, SessionInfo iSession, SchemaObject iSchema) throws ViewException {

		if (iScreen == null)
			// GET THE CURRENT ONE
			iScreen = getScreen();

		if (iContent == null) {
			if (iPosition == null)
				// GET CURRENT AREA FOR OBJECT
				iPosition = getScreen().getActiveArea();
			Roma.view().getScreen().getArea(iPosition).clear();
			return;
		}

		boolean currentSession = iSession == null || iSession.equals(Roma.session().getActiveSessionInfo());

		if (iSchema == null && iContent != null) {
			iSchema = Roma.session().getSchemaObject(iContent);
		}

		// SEARCH THE FORM TO VIEW BY ENTITY
		ContentForm form = (ContentForm) getFormByObject(iSession, iContent);
		if (form == null) {
			// CREATE IT
			form = ViewHelper.createForm(iSchema, null, iContent, iSession);
		} else {
			ViewHelper.invokeOnShow(iContent);
			iPosition = form.getScreenArea();
		}

		if (iPosition == null) {
			iPosition = getScreen().getActiveArea();
		}

		if (currentSession)
			// DISPLAY NOW
			showForm(form, iPosition, iScreen);
		else
			// PUSH CHANGES
			pushCommand(new ShowViewCommand(iSession, iScreen, form, iPosition));
	}

	/**
	 * Shows a form component
	 * 
	 * @param iForm
	 *          The form to be showed
	 * @param iWhere
	 *          The area where the form must be showed
	 * @param iDesktop
	 *          The Screen where to show the form
	 * @return
	 */
	public abstract String showForm(ContentForm iForm, String iWhere, Screen iDesktop);

	/**
	 * Create a form instance
	 * 
	 * @param iSchemaClass
	 *          the schema class for the form creation
	 * @param iSchemaField
	 *          the schema field of the object, null if the form is not part of another form
	 * @param iParent
	 *          the parent form
	 * @return
	 */
	public abstract ContentForm createForm(SchemaObject iSchemaClass, SchemaField iSchemaField, ViewComponent iParent);

	/**
	 * Return the desktop for the current user.
	 * 
	 * @return Screen instance
	 */
	public Screen getScreen() {
		return FormViewer.getInstance().getScreen();
	}

	/**
	 * Return the screen for the user.
	 * 
	 * @param iUserSession
	 *          User session
	 * @return Screen instance
	 */
	public Screen getScreen(Object iUserSession) {
		return FormViewer.getInstance().getScreen(iUserSession);
	}

	/**
	 * Set the current screen.
	 * 
	 * @param iScreen
	 *          Screen instance to set for the current user.
	 */
	public void setScreen(Screen iScreen) {
		if (iScreen != null)
			FormViewer.getInstance().setScreen(iScreen);
	}

	/**
	 * Set the current screen.
	 * 
	 * @param iScreen
	 *          Screen instance to set for the current user.
	 */
	public void setScreen(Screen iScreen, SessionInfo iSession) {
		if (iScreen != null)
			pushCommand(new ChangeScreenViewCommand(iSession, iScreen));
	}

	/**
	 * Close the current form displayed. It applies only on Pop-up windows.
	 * 
	 * @param iUserObject
	 *          User Object to close
	 */
	public boolean close(Object iUserObject) {
		// TODO:delete
		return true;
	}

	public void onSessionCreating(SessionInfo iSession) {
		objectsForms.put(iSession, new IdentityHashMap<Object, ViewComponent>());
	}

	public void onSessionDestroying(SessionInfo iSession) {
		// REMOVE OBJECTS-AREA/COMPONENTS ASSOCIATION FOR CURRENT SESSION
		Map<Object, ViewComponent> forms = objectsForms.remove(iSession);
		if (forms != null) {
			if (log.isDebugEnabled())
				log.debug("[ObjectContext.onSessionDestroying] Removing components " + forms.values().size());

			for (ViewComponent c : forms.values()) {
				c.destroy();
			}

			if (log.isDebugEnabled())
				log.debug("[ObjectContext.onSessionDestroying] Removed " + forms.size() + " forms for session=" + iSession);
		}
	}

	/**
	 * Create an association between a User Object and a ContentForm. This association is useful to gather custom form information.
	 * 
	 * @param iUserObject
	 * @param iForm
	 */
	public void createObjectFormAssociation(Object iUserObject, ViewComponent iForm, SessionInfo iSession) {
		if (iSession == null)
			iSession = Roma.session().getActiveSessionInfo();

		if (iSession == null)
			throw new UserException(iForm.getContent(), "Cannot display the form since there is no active session");

		Map<Object, ViewComponent> userForms = objectsForms.get(iSession);
		userForms.put(iUserObject, iForm);
	}

	public void removeObjectFormAssociation(Object iUserObject, SessionInfo iSession) {
		if (iSession == null)
			if (iSession == null)
				iSession = Roma.session().getActiveSessionInfo();

		// REMOVE OBJECT-FORM ASSOCIATION
		Map<Object, ViewComponent> userForms = objectsForms.get(iSession);
		if (userForms != null) {
			if (log.isDebugEnabled())
				log.debug("[ViewAspectAbstract.removeObjectFormAssociation] Flushing form: " + iUserObject);
			userForms.remove(iUserObject);
		}
	}

	/**
	 * Return the form associated to a User Object.
	 * 
	 * @param iUserObject
	 * @return ContentComponent instance if any, otherwise null
	 */
	public ViewComponent getFormByObject(Object iUserObject) {
		return getFormByObject(null, iUserObject);
	}

	/**
	 * Return the form associated to a User Object.
	 * 
	 * @param iSession
	 *          User session, null to get the current active
	 * @param iUserObject
	 * @return ContentComponent instance if any, otherwise null
	 */
	public ViewComponent getFormByObject(Object iSession, Object iUserObject) {
		if (iSession == null)
			iSession = Roma.component(SessionAspect.class).getActiveSessionInfo();

		Map<Object, ViewComponent> userForms = objectsForms.get(iSession);

		if (userForms == null)
			return null;

		return userForms.get(iUserObject);
	}

	/**
	 * Return the first form of the declaring Class iClassOfObject.
	 * 
	 * @param iSession
	 *          User session, null to get the current active
	 * @param iClassOfObject
	 *          The Class of the object
	 * @return ContentComponent instance if any, otherwise null
	 */
	public List<ViewComponent> getFormsByClass(Object iSession, SchemaClass iClassOfObject) {
		if (iSession == null)
			iSession = Roma.component(SessionAspect.class).getActiveSessionInfo();

		Map<Object, ViewComponent> userForms = objectsForms.get(iSession);

		if (userForms == null)
			return null;

		List<ViewComponent> result = new ArrayList<ViewComponent>();
		for (Map.Entry<Object, ViewComponent> entry : userForms.entrySet()) {
			if (entry.getValue().getSchemaObject().getSchemaClass().equals(iClassOfObject))
				result.add(entry.getValue());
		}
		return result;
	}

	/**
	 * Return all the forms for all the active session that render POJOs of class iClass.
	 * 
	 * @param iClass
	 *          Class to search
	 * @return Map<SessionInfo, ContentComponent> with all entries that are handling POJOs of class iClass
	 */
	public Map<SessionInfo, ViewComponent> getFormsByClass(SchemaClass iClass) {
		Map<SessionInfo, ViewComponent> result = new HashMap<SessionInfo, ViewComponent>();

		Map<Object, ViewComponent> perSessionObjects;

		SchemaClass cls;
		for (Map.Entry<SessionInfo, Map<Object, ViewComponent>> entry : objectsForms.entrySet()) {
			perSessionObjects = entry.getValue();
			for (Map.Entry<Object, ViewComponent> formEntry : perSessionObjects.entrySet()) {
				if (formEntry.getKey() == null)
					continue;

				try {
					cls = Roma.schema().getSchemaClass(formEntry.getKey());

					if (cls.extendsClass(iClass))
						result.put(entry.getKey(), formEntry.getValue());
				} catch (Exception e) {
				}
			}
		}
		return result;
	}

	/**
	 * Refresh the changed objects.
	 */
	public void signalUpdatedClass(SchemaClass iSchemaClass, File iFile) {
		// OVERWRITE LIVING OBJECT SCHEMA OBJECTS BY COPYING NEW DEFINITION AND REFRESH ITS
		Map<SessionInfo, ViewComponent> forms = getFormsByClass(iSchemaClass);
		for (Map.Entry<SessionInfo, ViewComponent> entry : forms.entrySet()) {
			// entry.getValue().getSchemaObject().copyDefinition(iSchemaClass);

			Roma.aspect(ViewAspect.class).pushCommand(new RefreshViewCommand(entry.getKey(), (ViewComponent) entry.getValue()));
		}
	}

	@Override
	public void onObjectRefresh(SessionInfo iSession, Object iContent) {
		ViewComponent handler = getFormByObject(iContent);
		if (handler == null)
			return;

		if (handler.getContainerComponent() != null) {
			// OBJECT INSIDE ANOTHER ONE: REFRESH USING ITS CONTAINER
			Object parentObject = handler.getContainerComponent().getContent();
			String parentFieldName = handler.getSchemaField().getName();

			Roma.fieldChanged(parentObject, parentFieldName);
		} else {
			// OBJECT INSIDE ANOTHER ONE: REFRESH USING ITS CONTAINER
			Roma.fieldChanged(iContent);
		}
	}

	public String aspectName() {
		return ASPECT_NAME;
	}
}
