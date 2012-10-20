package org.romaframework.frontend.view.domain;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.core.module.Module;

@CoreClass(orderFields = "name className status implementsAspects")
public class ModuleListable {
  private Module module;

  public ModuleListable(Module iModule) {
    module = iModule;
  }

  public void startup() {
    module.startup();
  }

  public void shutdown() {
    module.shutdown();
  }

  public String getName() {
    return module.moduleName();
  }

  public String getClassName() {
    return module.getClass().getName();
  }

  public String getStatus() {
    return module.getStatus() != null ? module.getStatus() : "unknown";
  }

  public String getImplementsAspects() {
    return "";
  }

  @ViewField(visible = AnnotationConstants.FALSE)
  public Module getModule() {
    return module;
  }
}
