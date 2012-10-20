package org.romaframework.aspect.view.html;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.romaframework.aspect.session.html.helper.HtmlSessionHelper;
import org.romaframework.aspect.view.html.area.HtmlViewRenderable;
import org.romaframework.aspect.view.html.area.HtmlViewScreenAreaInstance;
import org.romaframework.aspect.view.html.component.HtmlViewAbstractComponent;
import org.romaframework.core.Roma;

public class EventServlet extends RomaServlet {

	private static final long		serialVersionUID	= 8215450790241653342L;

	protected static final Log	log								= LogFactory.getLog(RequestParserImpl.class);

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sess = request.getSession();
		if (sess != null && HtmlSessionHelper.isStarted(sess)) {
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			response.addHeader("Cache-Control", "must-revalidate");
			response.addHeader("Cache-Control", "no-cache");
			response.addHeader("Cache-Control", "no-store");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);

			JSONObject obj = new JSONObject();
			try {

				RequestParser rp = Roma.component(RequestParser.class);
				HtmlViewRenderable render = rp.invokeEvent(request);
				obj.put("bindingExecuted", true);
				obj.put("status", "ok");
				JSONObject changes = new JSONObject();
				if (render instanceof HtmlViewAbstractComponent) {
					changes.put(render.getHtmlId() + "_search", new ComponentWritable((HtmlViewAbstractComponent) render));
				} else {
					changes.put(render.getHtmlId() + "_search", new ComponentWritable((HtmlViewScreenAreaInstance) render));
				}
				obj.put("changes", changes);
				obj.write(response.getWriter());
			} catch (Throwable ex) {
				log.error("Error on event invoke", ex);
			}
		}
	}

}
