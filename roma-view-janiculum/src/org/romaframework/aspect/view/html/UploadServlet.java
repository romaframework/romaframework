package org.romaframework.aspect.view.html;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UploadServlet extends HtmlServlet{
	@Override
	protected void renderResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
		this.setResponseHeaders(response);
		PrintWriter writer = response.getWriter();
		writer.append("<p>upload completed</p>");
		writer.flush();
	}
}
