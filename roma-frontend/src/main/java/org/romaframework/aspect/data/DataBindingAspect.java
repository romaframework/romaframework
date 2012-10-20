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
package org.romaframework.aspect.data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.data.feature.DataFieldFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.aspect.Aspect;
import org.romaframework.core.exception.ConfigurationNotFoundException;
import org.romaframework.core.flow.Controller;
import org.romaframework.core.module.SelfRegistrantConfigurableModule;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;
import org.romaframework.core.schema.SchemaObject;
import org.romaframework.core.schema.SchemaObjectListener;

/**
 * @author luca.molino
 * 
 */
public class DataBindingAspect extends SelfRegistrantConfigurableModule<String> implements Aspect, SchemaObjectListener {

	public static final String	ASPECT_NAME	= "data";

	private static Log					logger			= LogFactory.getLog(DataBindingAspect.class);

	public DataBindingAspect() {
	}

	public String aspectName() {
		return ASPECT_NAME;
	}

	@Override
	public void startup() throws RuntimeException {
		super.startup();
		Controller.getInstance().registerListener(SchemaObjectListener.class, this);
	}

	public void beginConfigClass(SchemaClassDefinition iClass) {
	}

	public void configClass(SchemaClassDefinition iClass) {
	}

	public void configField(SchemaField iField) {
	}

	public void configAction(SchemaAction iAction) {
	}

	public void configEvent(SchemaEvent iEvent) {
	}

	public void endConfigClass(SchemaClassDefinition iClass) {
	}

	public void onCreate(SchemaObject iObject) {
		if (iObject.getInstance() != null) {
			for (SchemaField schemaField : iObject.getFields().values()) {
				Class<?> repositoryClass = (Class<?>) schemaField.getFeature(DataFieldFeatures.REPOSITORY);
				if (repositoryClass == null)
					continue;
				String methodName = schemaField.getFeature(DataFieldFeatures.METHOD);
				String[] searchFields = schemaField.getFeature(DataFieldFeatures.SEARCH_FIELDS);
				int limit = schemaField.getFeature(DataFieldFeatures.LIMIT).intValue();
				Method methodToCall = getMethod(repositoryClass, methodName, searchFields);
				try {
					Object repository = repositoryClass.newInstance();
					setResult(iObject, schemaField, repositoryClass, methodName, limit, methodToCall, repository, searchFields);
				} catch (InstantiationException ie) {
					throw new ConfigurationNotFoundException("Error creating instance of class " + repositoryClass.getName(), ie);
				} catch (IllegalAccessException iae) {
					throw new ConfigurationNotFoundException("Error creating instance of class " + repositoryClass.getName(), iae);
				} catch (IllegalArgumentException iare) {
					throw new ConfigurationNotFoundException("Error creating instance of class " + repositoryClass.getName(), iare);
				}
			}
		}
	}

	public Object getUnderlyingComponent() {
		return null;
	}

	protected Class<?> getRepositoryClass(String repositoryClassName) {
		Class<?> repositoryClass = Roma.repository(repositoryClassName) != null ? Roma.repository(repositoryClassName).getClass() : null;
		if (repositoryClass == null) {
			repositoryClassName = configuration.get(repositoryClassName);
			if (repositoryClassName == null || repositoryClassName.equals("")) {
				throw new ConfigurationNotFoundException("Error loading repository class " + repositoryClassName + ": configuration not found.");
			} else {
				try {
					repositoryClass = Class.forName(repositoryClassName);
				} catch (ClassNotFoundException cnfe) {
					logger.error("Error loading repository class " + repositoryClassName);
					throw new ConfigurationNotFoundException("Error loading repository class " + repositoryClassName, cnfe);
				}
			}
		}
		return repositoryClass;
	}

	protected String getRepositoryClassName(SchemaField iField) {
		String repositoryClassName;
		if (SchemaHelper.isMultiValueObject(iField)) {
			if (((Class<?>) iField.getLanguageType()).isAssignableFrom(Map.class)) {
				throw new ConfigurationNotFoundException("DataField annotation is not assignable for Map (field " + iField.getName() + ")");
			}
			Class<?> embeddedClass = null;
			if (iField.getEmbeddedType() != null) {
				embeddedClass = (Class<?>) iField.getEmbeddedType().getLanguageType();
			} else {
				for (SchemaClass schema : iField.getEmbeddedTypeGenerics()) {
					if (schema != null)
						embeddedClass = (Class<?>) schema.getLanguageType();
				}
			}
			repositoryClassName = embeddedClass.getSimpleName() + "Repository";
		} else {
			repositoryClassName = ((Class<?>) iField.getLanguageType()).getSimpleName() + "Repository";
		}
		return repositoryClassName;
	}

	protected List<?> setResult(SchemaObject iObject, SchemaField schemaField, Class<?> repositoryClass, String methodName, int limit, Method methodToCall, Object repository,
			String[] searchFields) {
		Object[] args = new Object[searchFields.length];
		for (int i = 0; i < searchFields.length; i++) {
			String searchFieldName = searchFields[i];
			if (iObject.getField(searchFieldName) == null)
				throw new ConfigurationNotFoundException("Field '" + searchFieldName + "' in class " + iObject.getSchemaClass().getName() + " not found");
			args[i] = iObject.getField(searchFieldName).getValue(iObject.getInstance());
		}
		List<?> result = null;
		try {
			result = (List<?>) methodToCall.invoke(repository, args);
		} catch (InvocationTargetException ite) {
			throw new ConfigurationNotFoundException("Error invoking method " + methodName + " of instance " + repositoryClass.getName(), ite);
		} catch (IllegalAccessException iae) {
			throw new ConfigurationNotFoundException("Error invoking method " + methodName + " of instance " + repositoryClass.getName(), iae);
		}
		if (limit > 0 && result.size() > limit) {
			schemaField.setValue(iObject.getInstance(), result.subList(0, limit));
		} else {
			schemaField.setValue(iObject.getInstance(), result);
		}
		return result;
	}

	protected Method getMethod(Class<?> repositoryClass, String methodName, String[] searchFields) {
		Method methodToCall = null;
		for (Method method : repositoryClass.getMethods()) {
			if (method.getName().equals(methodName) && method.getParameterTypes().length == searchFields.length) {
				methodToCall = method;
				break;
			}
		}
		if (methodToCall == null) {
			throw new ConfigurationNotFoundException("Method '" + methodName + "' with " + searchFields.length + " parameters in repository '" + repositoryClass.getName()
					+ "' not found.");
		}
		return methodToCall;
	}

}