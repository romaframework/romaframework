<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.transformer.jsp.directive.JspTransformerHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.Set"%>
<%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%>
<%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%>
<%@page import="java.util.Map"%>
<%@page import="java.io.Writer"%>
<%@page import="java.io.PrintWriter"%>
<%
	HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
%><div id="<%=JaniculumWrapper.id(component, null)%>" class="<%=JaniculumWrapper.cssClass(component, "colset", null)%>" style="display:table;<%=JaniculumWrapper.inlineStyle(component, null)%>">
	<div style="display:table-row;">
	<%
	int col = 0;
	for(Object child:JaniculumWrapper.getChildren(component)){ %>
		<div style="display:table-cell;" id="<%=JaniculumWrapper.id(component, null)%>_<%=col%>_td">
		<% JspTransformerHelper.delegate((HtmlViewRenderable)child, null,pageContext.getOut());%>
		</div>
	<%
		col++;
	} %>
	</div>
</div>