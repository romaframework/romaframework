package org.romaframework.aspect.view.html.binder;

import java.util.Map;

import org.romaframework.aspect.view.html.area.HtmlViewBinder;
import org.romaframework.aspect.view.html.area.HtmlViewRenderable;
import org.romaframework.aspect.view.html.component.HtmlViewContentComponentImpl;
import org.romaframework.core.Roma;
import org.romaframework.core.binding.BindingException;
import org.romaframework.core.schema.SchemaHelper;

public class SearchResultBinder implements HtmlViewBinder {

	public void bind(HtmlViewRenderable renderable, Map<String, Object> values) throws BindingException {
		if (renderable instanceof HtmlViewContentComponentImpl) {
			HtmlViewContentComponentImpl cc = ((HtmlViewContentComponentImpl) renderable);
			Object additionalInfo = cc.getAdditionalInfo();
			if (SchemaHelper.isMultiValueObject(additionalInfo)) {
				Object[] vals = SchemaHelper.getObjectArrayForMultiValueObject(additionalInfo);
				String s = (String) values.get(String.valueOf(renderable.getId()));
				cc.getSchemaField().setValue(cc.getContainerComponent().getContent(), vals[Integer.valueOf(s)]);
				Roma.fieldChanged(cc.getContainerComponent().getContent(),cc.getSchemaField().getName());
				//cc.setDirty(true);
			}
		}
	}

}
