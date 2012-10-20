package org.romaframework.aspect.view.html.taglib;

import javax.servlet.jsp.tagext.TagSupport;

import org.romaframework.aspect.view.html.HtmlServlet;

public class RomaCssTag extends TagSupport {

	@Override
	public int doStartTag() {
		try {
			pageContext.getOut().flush();
			// TODO review css servlet path
			pageContext.getOut().print("<link rel=\"stylesheet\" type=\"text/css\" href=\"roma.css?");
			pageContext.getOut().print(HtmlServlet.PAGE_ID_PARAM);
			pageContext.getOut().print("=");
			pageContext.getOut().print(pageContext.getRequest().getAttribute(HtmlServlet.PAGE_ID_PARAM) + "\">");
		} catch (final Exception e) {
			e.printStackTrace();// TODO handle exception
		}

		return SKIP_BODY;
	}
}
