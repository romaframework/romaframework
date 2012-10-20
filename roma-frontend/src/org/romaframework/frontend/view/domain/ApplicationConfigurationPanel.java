package org.romaframework.frontend.view.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.flow.annotation.FlowAction;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.core.Roma;
import org.romaframework.core.aspect.Aspect;
import org.romaframework.core.config.ApplicationConfiguration;
import org.romaframework.core.module.Module;
import org.romaframework.core.module.ModuleManager;

public class ApplicationConfigurationPanel implements RomaControlPanelTab {
	@ViewField(render = ViewConstants.RENDER_HTML, label = "")
	public String getHelp() {
		return "To make persistent changes please modify the META-INF/component/applicationContext.xml file.";
	}

	@ViewField(render = ViewConstants.RENDER_LABEL)
	public String getApplicationName() {
		return Roma.component(ApplicationConfiguration.class).getApplicationName();
	}

	@ViewField(render = ViewConstants.RENDER_LABEL)
	public String getApplicationPackage() {
		return Roma.component(ApplicationConfiguration.class).getApplicationPackage();
	}

	@ViewField(render = ViewConstants.RENDER_LABEL)
	public String getApplicationVersion() {
		return Roma.component(ApplicationConfiguration.class).getApplicationVersion();
	}

	public boolean isApplicationDevelopment() {
		return Roma.component(ApplicationConfiguration.class).isApplicationDevelopment();
	}

	public void setApplicationDevelopment(boolean iValue) {
		Roma.component(ApplicationConfiguration.class).setApplicationDevelopment(iValue);
	}

	@ViewField(render = ViewConstants.RENDER_LABEL)
	public String getStatus() {
		return Roma.component(ApplicationConfiguration.class).getStatus();
	}

	@FlowAction(back = AnnotationConstants.TRUE)
	public void apply() {
	}

	@ViewField(render = ViewConstants.RENDER_TABLE, enabled = AnnotationConstants.FALSE, label = "Aspects")
	public List<AspectListable> getAspectNames() {
		List<AspectListable> aspectNames = new ArrayList<AspectListable>();

		Collection<Aspect> aspects = Roma.aspects();
		for (Aspect a : aspects)
			aspectNames.add(new AspectListable(a));

		return aspectNames;
	}

	@ViewField(render = ViewConstants.RENDER_TABLE, enabled = AnnotationConstants.FALSE, label = "Modules")
	public List<ModuleListable> getModuleNames() {
		List<ModuleListable> moduleNames = new ArrayList<ModuleListable>();

		Collection<Module> modules = ModuleManager.getInstance().getConfigurationValues();
		for (Module m : modules)
			moduleNames.add(new ModuleListable(m));

		return moduleNames;
	}

}
