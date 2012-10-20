package org.romaframework.module.users.view.domain.customprofiling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.i18n.I18NType;
import org.romaframework.aspect.persistence.PersistenceAspect;
import org.romaframework.aspect.view.ViewCallback;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.aspect.view.feature.ViewClassFeatures;
import org.romaframework.aspect.view.feature.ViewElementFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.config.Refreshable;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassElement;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.module.users.CustomProfiler;
import org.romaframework.module.users.domain.CustomFunction;
import org.romaframework.module.users.domain.CustomProfiling;

public class CustomProfilingSettings implements ViewCallback {

	private SchemaClassDefinition					classDefinition;
	private boolean												actions;
	private boolean												fields;
	@ViewField(label = "", enabled = AnnotationConstants.FALSE)
	private List<CustomFunctionListable>	enabledFunctions;
	private CustomProfiling								profiling;
	private Map<String, String>						labelAssoc	= new HashMap<String, String>();

	public CustomProfilingSettings(SchemaClassDefinition schemaClass) {
		this(schemaClass, new HashMap<String, String>());

	}

	public CustomProfilingSettings(SchemaClassDefinition schemaClass, Map<String, String> labelAssoc) {
		this(schemaClass, true, true);
		this.labelAssoc = labelAssoc;
	}

	public CustomProfilingSettings(SchemaClassDefinition schemaClass, boolean actions, boolean fields) {
		this.classDefinition = schemaClass;
		this.actions = actions;
		this.fields = fields;
	}

	public void onDispose() {
	}

	public void onShow() {
		profiling = Roma.component(CustomProfiler.class).getCustomProfiling();
		if (profiling == null)
			profiling = Roma.component(CustomProfiler.class).createCustomProfiling();
		if (enabledFunctions == null) {
			enabledFunctions = new ArrayList<CustomFunctionListable>();
			addSchemaClass(classDefinition);
			Roma.fieldChanged(this, "enabledFunctions");
		}

	}

	private void addSchemaClass(SchemaClassDefinition schemaClass) {
		if (fields) {
			Iterator<SchemaField> fieldItarator = schemaClass.getFieldIterator();
			while (fieldItarator.hasNext()) {
				addListable(fieldItarator.next());
			}
		}
		if (actions) {
			Iterator<SchemaAction> actionItarator = schemaClass.getActionIterator();
			while (actionItarator.hasNext()) {
				addListable(actionItarator.next());
			}
		}

	}

	private void addListable(SchemaClassElement element) {
		if ((Boolean) element.getFeature(ViewElementFeatures.VISIBLE)) {
			String layout = (String) element.getFeature(ViewElementFeatures.LAYOUT);
			if (layout != null && ViewConstants.LAYOUT_EXPAND.equals(layout)) {
				if (element instanceof SchemaField)
					addSchemaClass(((SchemaField) element).getType());
			} else {
				String label;
				if (labelAssoc.get(element.getName()) != null) {
					label = labelAssoc.get(element.getName());
				} else {
					label = Roma.i18n().get(element, I18NType.LABEL, ViewClassFeatures.LABEL);
				}

				String functionName = element.getEntity().getName() + "." + element.getName();
				CustomFunction function = profiling.getFunctions().get(functionName);
				if (function == null)
					function = new CustomFunction(functionName, true);
				enabledFunctions.add(new CustomFunctionListable(label, function));
			}
		}
	}

	public void cancel() {
		Roma.flow().back();
	}

	public void save() {
		for (CustomFunctionListable listable : enabledFunctions) {
			CustomFunction function = listable.getEntity();
			if (!function.isAllow()) {
				profiling.getFunctions().put(function.getName(), function);
			}
		}
		profiling = Roma.repository(CustomProfiling.class).update(profiling, PersistenceAspect.STRATEGY_DETACHING);
		Roma.component(CustomProfiler.class).setCustomProfiling(profiling);
		Object back = Roma.flow().back();
		if (back instanceof Refreshable)
			((Refreshable) back).refresh();
	}

	public void all() {
		for (CustomFunctionListable cur : enabledFunctions) {
			cur.getEntity().setAllow(true);
		}
		Roma.fieldChanged(this, "enabledFunctions");
	}

	public void none() {
		for (CustomFunctionListable cur : enabledFunctions) {
			cur.getEntity().setAllow(false);
		}
		Roma.fieldChanged(this, "enabledFunctions");
	}

	@ViewField(render = ViewConstants.RENDER_TABLEEDIT)
	public List<CustomFunctionListable> getEnabledFunctions() {
		return enabledFunctions;
	}

	public void setEnabledFunctions(List<CustomFunctionListable> enabledFunctions) {
		this.enabledFunctions = enabledFunctions;
	}

}
