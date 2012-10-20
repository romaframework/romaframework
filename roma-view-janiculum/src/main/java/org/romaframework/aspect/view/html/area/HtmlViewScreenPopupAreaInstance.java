package org.romaframework.aspect.view.html.area;

import org.romaframework.aspect.view.html.screen.HtmlViewScreen;
import org.romaframework.aspect.view.html.transformer.Transformer;
import org.romaframework.aspect.view.html.transformer.manager.TransformerManager;
import org.romaframework.aspect.view.screen.Screen;
import org.romaframework.core.Roma;
import org.romaframework.core.schema.xmlannotations.XmlFormAreaAnnotation;

public class HtmlViewScreenPopupAreaInstance extends HtmlViewScreenAreaInstance {

	public HtmlViewScreenPopupAreaInstance(final HtmlViewScreenAreaInstance parent, final String name) {
		super(parent, changeName(name));
	}

	public HtmlViewScreenPopupAreaInstance(final HtmlViewScreenAreaInstance parentAreaInstance, final XmlFormAreaAnnotation areaTag) {
		super(parentAreaInstance, areaTag);
	}

	@Override
	public Transformer getTransformer() {
		return Roma.component(TransformerManager.class).getComponent(Screen.POPUP);
	}

	@Override
	public String getHtmlId() {
		return super.getHtmlId();
	}

	public static String changeName(final String name) {
		if (name.startsWith(HtmlViewScreen.SCREEN_DOUBLE_DOTS)) {
			final int index = name.indexOf(HtmlViewScreen.DOUBLE_DOTS);
			return name.substring(index + 1).replaceAll(":", "_").trim();
		} else {
			return name.replaceAll(":", "_").trim();
		}
	}

	@Override
	public void clear() {
		getParent().removeChild(this);
		((HtmlViewScreenAreaInstance) getParent()).setDirty(true);
		super.clear();
	}

}
