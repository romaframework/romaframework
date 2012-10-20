package org.romaframework.aspect.view.html;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.chart.ChartAspect;
import org.romaframework.aspect.view.form.ViewComponent;
import org.romaframework.aspect.view.html.area.HtmlViewRenderable;
import org.romaframework.core.Roma;

/**
 * this servlet creates a chart by a POJO. it requires a ChartAspect imported
 * 
 */
public class ChartServlet extends RomaServlet {

	private static final long		serialVersionUID			= 5212209627108153914L;

	public static final String	IMAGE_POJO_PARAMETER	= "imagePojo";

	protected static Log				log										= LogFactory.getLog(ChartServlet.class);

	protected ChartAspect				chartModule;
	protected boolean						inited								= false;

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		if (!inited) {
			inited = true;
			try {
				chartModule = Roma.aspect(ChartAspect.class);
			} catch (final Throwable t) {
				log.warn("no chart module defined");
				log.debug("Error retrieving ChartAspect cause: " + t, t);
			}
		}
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		response.setContentType("image/png");
		if (chartModule == null) {
			return;
		}

		final String pojoId = request.getParameter(IMAGE_POJO_PARAMETER);
		if (pojoId == null) {
			return;
		}
		try {
			final OutputStream writer = response.getOutputStream();
			final HtmlViewRenderable renderable = HtmlViewAspectHelper.getHtmlViewSession().getRenderableById(Long.parseLong(pojoId));
			final Object pojo = ((ViewComponent) renderable).getContent();
			chartModule.toChart(pojo, writer);
			writer.flush();
		} catch (final Throwable t) {
			log.error("could not render chart: " + t);
			log.debug(t);
		}
	}
}