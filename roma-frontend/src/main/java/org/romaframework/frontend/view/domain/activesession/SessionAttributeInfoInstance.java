package org.romaframework.frontend.view.domain.activesession;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.flow.FlowAspect;
import org.romaframework.aspect.view.ViewCallback;
import org.romaframework.aspect.view.annotation.ViewAction;
import org.romaframework.aspect.view.feature.ViewFieldFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.exception.UserException;
import org.romaframework.core.schema.SchemaClassResolver;
import org.romaframework.core.schema.SchemaHelper;
import org.romaframework.frontend.domain.message.MessageOk;
import org.romaframework.frontend.domain.page.EntityPage;

@CoreClass(orderActions = "createValue resetValueToNull save")
public class SessionAttributeInfoInstance extends EntityPage<SessionAttributeInfo> implements ViewCallback {
	protected String	type;
	protected boolean	saved;

	public SessionAttributeInfoInstance() {
		super(null);
	}

	public SessionAttributeInfoInstance(SessionAttributeInfo iEntity) {
		super(iEntity);
	}

	public void onShow() {
		saved = entity.getName().length() > 0;

		type = entity.getType();
		Object value = entity.getValue();
		if (value != null) {
			Roma.setFeature(this, "entity.type", ViewFieldFeatures.ENABLED, Boolean.FALSE);
		}
	}

	public void onDispose() {
	}

	public void createValue() {
		if (entity.getValue() != null) {
			Roma.aspect(FlowAspect.class).popup(new MessageOk("alert", "Alert", null, "Cannot create a new object: object already existent!"));
			return;
		}

		if (type == null || type.length() == 0) {
			Roma.aspect(FlowAspect.class).forward(
					new MessageOk("alert", "Alert", null, "Please specify a 'Type' of object to create. 'Type' is the full class name with package."));
			return;
		}

		try {
			Class<?> cls = Roma.component(SchemaClassResolver.class).getLanguageClass(type);

			if (cls == null)
				cls = Class.forName(type);

			entity.setValue(SchemaHelper.createObject(null, cls));
		} catch (Exception e) {
			throw new UserException(entity, "Error on creating object", e);
		}

		Roma.fieldChanged(this, "entity");
	}

	public void resetValueToNull() {
		if (entity.getValue() == null) {
			Roma.aspect(FlowAspect.class).popup(new MessageOk("alert", "Alert", null, "Value is already null!"));
			return;
		}
		entity.setValue(null);

		Roma.fieldChanged(this, "entity");
	}

	@ViewAction(bind = AnnotationConstants.TRUE)
	public void save() {
		Roma.aspect(FlowAspect.class).back();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
