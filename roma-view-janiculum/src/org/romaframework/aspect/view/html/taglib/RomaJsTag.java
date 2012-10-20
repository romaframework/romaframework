package org.romaframework.aspect.view.html.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.TagSupport;

import org.romaframework.aspect.view.html.HtmlServlet;

public class RomaJsTag extends TagSupport {
	@Override
	public int doStartTag() {

		try {
			// TODO review js servlet path
			String prefix = "";
			
			if(pageContext.getRequest() instanceof HttpServletRequest){
				prefix = ((HttpServletRequest)pageContext.getRequest()).getContextPath()+"/";
			}
			pageContext.getOut().print("<script id=\"romajs\" type=\"text/javascript\" src=\""+prefix+"roma.js?");
			pageContext.getOut().print(HtmlServlet.PAGE_ID_PARAM);
			pageContext.getOut().print("=");
			pageContext.getOut().print(pageContext.getRequest().getAttribute(HtmlServlet.PAGE_ID_PARAM) + "\"></script>");
		} catch (final Exception e) {
			e.printStackTrace();// TODO handle exception
		}

		return SKIP_BODY;
	}
}
