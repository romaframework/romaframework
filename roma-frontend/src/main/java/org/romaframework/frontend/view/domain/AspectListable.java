package org.romaframework.frontend.view.domain;

import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.core.aspect.Aspect;

@CoreClass(orderFields = "name className definedInModule")
public class AspectListable {
  private Aspect aspect;

  public AspectListable(Aspect iModule) {
    aspect = iModule;
  }

  public String getName() {
    return aspect.aspectName();
  }

  public String getClassName() {
    return aspect.getClass().getName();
  }

  public String getDefinedInModule() {
    return "";
  }
}
