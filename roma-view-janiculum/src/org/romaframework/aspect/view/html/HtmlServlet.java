/*
 * Copyright 2006 Giordano Maestro (giordano.maestro--at--assetdata.it)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.romaframework.aspect.view.html;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.ValidationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.session.html.helper.HtmlSessionHelper;
import org.romaframework.aspect.view.ViewAspect;
import org.romaframework.aspect.view.command.impl.DownloadReaderViewCommand;
import org.romaframework.aspect.view.command.impl.DownloadStreamViewCommand;
import org.romaframework.aspect.view.command.impl.OpenWindowViewCommand;
import org.romaframework.aspect.view.command.impl.RedirectViewCommand;
import org.romaframework.aspect.view.command.impl.ReportingDownloadViewCommand;
import org.romaframework.aspect.view.html.css.StyleBuffer;
import org.romaframework.aspect.view.html.screen.HtmlViewScreen;
import org.romaframework.core.Roma;
import org.romaframework.core.binding.BindingException;
import org.romaframework.core.exception.ExceptionHelper;
import org.romaframework.frontend.domain.message.ErrorMessageTextDetail;
import org.romaframework.web.service.rest.RestServiceHelper;

/**
 * The main Servlet of the html view aspect
 * 
 * @author Giordano Maestro (giordano.maestro--at--assetdata.it)
 * 
 */
public class HtmlServlet extends RomaServlet {

	private static final long		serialVersionUID	= 1742514931340296814L;

	public static final String	ROMA_JS						= "###__ROMA_JS__###";

	public static final String	ROMA_CSS					= "###__ROMA_CSS__###";

	public static final String	PAGE_ID_PARAM			= "pageId";

	protected static Log				log								= LogFactory.getLog(HtmlServlet.class);

	/**
	 * Given a request it must understand the action to be performed and bind the pojos, finally it call the display to write the
	 * result html on the response
	 */
	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		try {
			HttpSession httpSession = request.getSession(true);

			if (!isStarted(httpSession)) {
				httpSession = request.getSession(true);
				startUserSession(request, response);

				setStarted(httpSession);
			} else if (RestServiceHelper.existsServiceToInvoke(request)) {
				RestServiceHelper.invokeRestService(request, response);
				RestServiceHelper.clearSession(request);
			}

			removePreviousPushCommands(request);

			final RequestParser requestParser = Roma.component(RequestParser.class);

			try {
				requestParser.parseRequest(request);
			} catch (final ValidationException e) {
				ErrorMessageTextDetail toShow = new ErrorMessageTextDetail("application.error", "Application Error", e);
				toShow.setMessage(e.getMessage());
				toShow.setDetail(ExceptionHelper.toString(e));
				Roma.flow().forward(toShow, "popup", null, Roma.session().getActiveSessionInfo());
			} catch (final BindingException e) {
				ErrorMessageTextDetail toShow = new ErrorMessageTextDetail("application.error", "Application Error", e);
				toShow.setMessage(e.getMessage());
				toShow.setDetail(ExceptionHelper.toString(e));
				Roma.flow().forward(toShow, "popup", null, Roma.session().getActiveSessionInfo());
			} catch (final Throwable e) {
				ErrorMessageTextDetail toShow = new ErrorMessageTextDetail("application.error", "Application Error", e);
				toShow.setMessage(e.getMessage());
				toShow.setDetail(ExceptionHelper.toString(e));
				Roma.flow().forward(toShow, "popup", null, Roma.session().getActiveSessionInfo());
				// throw new ServletException(e);
			}

			Boolean redirected = (Boolean) request.getAttribute(HtmlViewAspectHelper.REDIRECTED);
			if (redirected != null && redirected) {
				request.setAttribute(HtmlViewAspectHelper.REDIRECTED, Boolean.FALSE);
				return;
			}

			renderResponse(request, response);
		} finally {

			HtmlViewAspectHelper.removeCssBuffer();
			HtmlViewAspectHelper.removeJsBuffer();
		}
	}

	protected void renderResponse(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		setResponseHeaders(response);
		final String pageId = HtmlViewAspectHelper.newPageId();
		request.setAttribute(PAGE_ID_PARAM, pageId);
		request.getSession().setAttribute(PAGE_ID_PARAM, pageId);

		final HtmlViewScreen screen = (HtmlViewScreen) Roma.aspect(ViewAspect.class).getScreen();

		synchronized (screen) {
			OutputStream out = new ByteArrayOutputStream();
			Writer w = new OutputStreamWriter(out);
			screen.render(request, true, true, w);
			w.flush();
			String buff = out.toString();
			buff = flushBuffers(buff);
			response.getWriter().append(buff);
			((HtmlViewAspect) Roma.view()).cleanDirtyComponents();
		}
	}

	protected void setResponseHeaders(final HttpServletResponse response) {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		// response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
		response.addHeader("Cache-Control", "must-revalidate");
		response.addHeader("Cache-Control", "no-cache");
		response.addHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setDateHeader("Expires", 0); // prevents caching at the proxy server
	}

	protected void removePreviousPushCommands(final HttpServletRequest request) {
		request.getSession().setAttribute(DownloadReaderViewCommand.class.getSimpleName(), null);
		request.getSession().setAttribute(DownloadStreamViewCommand.class.getSimpleName(), null);
		request.getSession().setAttribute(ReportingDownloadViewCommand.class.getSimpleName(), null);
		request.getSession().setAttribute(OpenWindowViewCommand.class.getSimpleName(), null);
		request.getSession().setAttribute(RedirectViewCommand.class.getSimpleName(), null);
	}

	protected void setStarted(HttpSession httpSession) {
		HtmlSessionHelper.setStarted(httpSession);
	}

	protected boolean isStarted(final HttpSession httpSession) {
		if (httpSession == null) {
			return false;
		}
		return HtmlSessionHelper.isStarted(httpSession);
	}

	/**
	 * Flush the buffers of a page
	 * 
	 * @param pageId
	 *          the Id of the page
	 */
	protected String flushBuffers(String buff) {
		final StyleBuffer cssBuffer = HtmlViewAspectHelper.getCssBuffer();
		String cssContent = "";
		if (cssBuffer != null) {
			if (!cssBuffer.isClosed()) {
				cssBuffer.close();
			}
			cssContent = cssBuffer.getStyleBuffer().toString();
		}
		buff = buff.replace(ROMA_CSS, cssContent);

		final HtmlViewCodeBuffer jsBuffer = HtmlViewAspectHelper.getJsBuffer();
		String jsContent = "";
		if (jsBuffer != null) {
			if (!jsBuffer.isClosed()) {
				jsBuffer.close();
			}
			jsContent = jsBuffer.getBufferContent();
		}
		buff = buff.replace(ROMA_JS, jsContent);
		return buff;
	}

}
