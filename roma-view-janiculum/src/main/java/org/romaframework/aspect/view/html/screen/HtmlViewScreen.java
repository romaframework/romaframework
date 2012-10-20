package org.romaframework.aspect.view.html.screen;

import java.io.Writer;

import javax.servlet.ServletRequest;

import org.romaframework.aspect.view.area.AreaComponent;
import org.romaframework.aspect.view.html.area.HtmlViewRenderable;
import org.romaframework.aspect.view.html.area.HtmlViewScreenAreaInstance;
import org.romaframework.aspect.view.screen.Screen;

public interface HtmlViewScreen extends Screen, HtmlViewRenderable {

	public static final String	DOUBLE_DOTS					= ":";

	public static final String	SCREEN							= "screen";

	public static final String	SCREEN_DOUBLE_DOTS	= SCREEN + DOUBLE_DOTS;

	public static final String	SCREEN_POPUP				= SCREEN_DOUBLE_DOTS + POPUP;

	public static final String	MENU								= "menu";

	public static final String	POPUPS							= "popups";

	public static final String	MAIN								= "main";

	public static final String	DEFAULT_SCREEN_AREA	= "body";

	public HtmlViewScreenAreaInstance getRootArea();

	public void render(ServletRequest request, boolean css, boolean js, Writer writer);

	public AreaComponent getDefaultArea();

	public AreaComponent getPopupsScreenArea();

	public String getName();

	public String getRenderSet();

	public void setRenderSet(String renderSet);
}
