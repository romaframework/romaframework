package org.romaframework.aspect.view.html.taglib;

import static org.romaframework.aspect.view.html.HtmlServlet.ROMA_JS;

import javax.servlet.jsp.tagext.TagSupport;


public class RomaInlineJsTag extends TagSupport {
	@Override
	public int doStartTag() {

		try {
			pageContext.getOut().print("<script type=\"text/javascript\" >\n"+ROMA_JS+"</script>\n");
		} catch (final Exception e) {
			e.printStackTrace();// TODO handle exception
		}
		return SKIP_BODY;
	}
}
