<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%><%@ page language="java" contentType="text/html; charset=UTF-8"pageEncoding="UTF-8"%><%@page import="java.util.Map"%><%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%><%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="org.romaframework.aspect.view.form.ViewComponent"%><%@page import="org.romaframework.aspect.view.html.HtmlViewAspectHelper"%>
<%@page import="org.romaframework.aspect.view.html.component.HtmlViewConfigurableEntityForm" %><%
	HtmlViewRenderable component = (HtmlViewRenderable) request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
	if (component instanceof HtmlViewConfigurableEntityForm) {
		%><div class="<%=JaniculumWrapper.cssClass(component, "objectembedded", null)%>" id="<%=JaniculumWrapper.id(component, null)%>"><%
				HtmlViewAspectHelper.renderByJsp((ViewComponent)component, request, pageContext.getOut());
		%></div><%
	} else {
		component.render(pageContext.getOut());
	}
%>