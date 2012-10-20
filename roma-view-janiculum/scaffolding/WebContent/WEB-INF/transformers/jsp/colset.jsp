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
	
	String part = (String) request.getAttribute(RequestConstants.CURRENT_COMPONENT_PART_IN_TRANSFORMER);
	pageContext.setAttribute("part", part);
	String valign = JaniculumWrapper.areaVerticalAlignment(component);
	if("center".equals(valign)){
		valign = "middle";
	}
	String halign = JaniculumWrapper.areaHorizontalAlignment(component);
%>
<table id="<%=JaniculumWrapper.id(component, null)%>" class="<%=JaniculumWrapper.cssClass(component, "colset", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>">
	<tr>
	<%
	int col = 0;
	for(Object child:JaniculumWrapper.getChildren(component)){ %>
		<td id="<%=JaniculumWrapper.id(component, null)%>_<%=col%>_td">
		<% JspTransformerHelper.delegate((HtmlViewRenderable)child, null,pageContext.getOut());%>
		</td>
	<%
		JspTransformerHelper.addCss(JaniculumWrapper.id(component, null)+"_"+col+"_td", "vertical-align", valign);
		JspTransformerHelper.addCss(JaniculumWrapper.id(component, null)+"_"+col+"_td", "vertexttical-align", halign);
		col++;
	} %>
	</tr>
</table>