<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%>
<%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%>
<%@page import="java.util.Map"%>
<%@page import="org.romaframework.aspect.view.html.transformer.jsp.directive.JspTransformerHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%

HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);


JspTransformerHelper.addCss(JaniculumWrapper.id(component, null), "vertical-align", JaniculumWrapper.areaVerticalAlignment(component));
JspTransformerHelper.addCss(JaniculumWrapper.id(component, null), "text-align", JaniculumWrapper.areaHorizontalAlignment(component));
%>

<div id="<%=JaniculumWrapper.id(component, null) %>" class="<%=JaniculumWrapper.cssClass(component, "cell", null)%>" style="<%=JaniculumWrapper.inlineStyle( component, null)%>">
	<%
	for(Object child:JaniculumWrapper.getChildren(component)){
		JspTransformerHelper.delegate((HtmlViewRenderable)child, null,pageContext.getOut());
	}
	%>
</div>
