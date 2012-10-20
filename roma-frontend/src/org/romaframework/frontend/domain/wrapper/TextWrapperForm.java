package org.romaframework.frontend.domain.wrapper;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.view.ViewCallback;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.aspect.view.feature.ViewFieldFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.Utility;
import org.romaframework.core.binding.Bindable;
import org.romaframework.core.schema.SchemaField;

@CoreClass(orderFields = "classes userDefinedTypeName value")
public class TextWrapperForm extends TextWrapper implements Bindable, ViewCallback {
	private static final String		USER_DEFINED_TYPENAME	= "User defined";
	private static final String		NOT_HANDLED_TYPENAME	= "Not handled";

	public static final Class<?>[]		JAVA_BASE_CLASSES			= new Class[] { Boolean.class, Byte.class, Character.class, Double.class, Float.class, Integer.class, Long.class,
			Short.class, String.class											};

	public static final String[]	TYPE_NAMES						= new String[] { Boolean.class.getName(), Byte.class.getName(), Character.class.getName(), Double.class.getName(),
			Float.class.getName(), Integer.class.getName(), Long.class.getName(), Short.class.getName(), String.class.getName(), USER_DEFINED_TYPENAME, NOT_HANDLED_TYPENAME };

	private String								selectedType;
	private String								typeClassName;

	public TextWrapperForm() {
		super("");
	}

	public TextWrapperForm(Object iValue, Class<?> iTypeClass) {
		super(iValue, iTypeClass);
	}

	public TextWrapperForm(Object iValue) {
		super(iValue);
	}

	public void onShow() {
		selectedType = null;

		if (value == null)
			selectedType = Utility.getClassName(String.class);
		else {
			Class<?> cls = value.getClass();

			// SEARCH AMONG JAVA CLASSES
			for (Class<?> c : JAVA_BASE_CLASSES) {
				if (c.equals(cls)) {
					selectedType = Utility.getClassName(c);
					break;
				}
			}

			if (selectedType == null) {
				// TRY TO UNDERSTAND IF TYPE IS KNOWN OR LESS
				selectedType = Roma.schema().isAvailableSchemaClass(Utility.getClassName(cls)) ? USER_DEFINED_TYPENAME : NOT_HANDLED_TYPENAME;
			}
		}

		setSelectedType(selectedType);
		Roma.fieldChanged(this, "types");
	}

	public void onDispose() {
	}

	@ViewField(selectionField = "selectedType", render = ViewConstants.RENDER_SELECT)
	public String[] getTypes() {
		return TYPE_NAMES;
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public String getSelectedType() {
		return selectedType;
	}

	public void setSelectedType(String selectedBuiltinTypes) {
		this.selectedType = selectedBuiltinTypes;

		Roma.setFeature(this, "typeClassName", ViewFieldFeatures.VISIBLE,
				selectedType != null && (selectedType.equals(USER_DEFINED_TYPENAME) || selectedType.equals(NOT_HANDLED_TYPENAME)));
	}

	public String getTypeClassName() {
		return typeClassName;
	}

	public void setTypeClassName(String userDefinedTypeName) {
		this.typeClassName = userDefinedTypeName;
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public SchemaField getSourceField() {
		return null;
	}

	@ViewField(visible = AnnotationConstants.FALSE)
	public Object getSourceObject() {
		return null;
	}

	public void setSource(Object sourceObject, String sourceFieldName) {
	}
}
