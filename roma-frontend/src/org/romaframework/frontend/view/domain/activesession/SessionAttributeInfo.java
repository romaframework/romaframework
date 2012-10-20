package org.romaframework.frontend.view.domain.activesession;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.validation.annotation.ValidationField;

@CoreClass(orderFields = "name type value")
public abstract class SessionAttributeInfo {
  @ValidationField(required = AnnotationConstants.TRUE)
  protected String name;

  public SessionAttributeInfo(String name) {
    this.name = name;
  }

  public abstract Object getValue();

  public abstract void setValue(Object value);

  @Override
  public String toString() {
    return getValue() != null ? getValue().toString() : "";
  }

  public String getName() {
    return name;
  }

  public String getType() {
    Object value = getValue();
    if (value == null)
      return "";
    return value.getClass().getName();
  }
}
