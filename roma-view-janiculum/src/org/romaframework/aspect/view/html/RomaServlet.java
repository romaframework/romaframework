package org.romaframework.aspect.view.html;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.view.ViewAspect;
import org.romaframework.aspect.view.form.FormViewer;
import org.romaframework.aspect.view.screen.Screen;
import org.romaframework.aspect.view.screen.config.ScreenManager;
import org.romaframework.core.Roma;
import org.romaframework.core.config.ApplicationConfiguration;
import org.romaframework.web.service.rest.RestServiceHelper;

public abstract class RomaServlet extends HttpServlet {

	private static final long	serialVersionUID	= -619678975158028470L;

	protected static Log	log	= LogFactory.getLog(RomaServlet.class);

	protected void startUserSession(final HttpServletRequest request, final HttpServletResponse response) {
		// During session creation it create a controller context and invoke the
		// application starter
		Screen screen = Roma.component(ScreenManager.class).getScreen("main-screen.xml");
		Roma.aspect(ViewAspect.class).setScreen(screen);

		final ApplicationConfiguration config = Roma.component(ApplicationConfiguration.class);

		if (RestServiceHelper.existsServiceToInvoke(request)) {
			RestServiceHelper.invokeRestService(request, response);
			RestServiceHelper.clearSession(request);
		} else {
			config.createUserSession();
		}
		FormViewer.getInstance().sync();
	}

}
